package loop.theliwala.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;
import loop.theliwala.R;
import loop.theliwala.activity.DashboardActivity;
import loop.theliwala.adapter.BestSellAdapter;
import loop.theliwala.adapter.NewArriveAdapter;
import loop.theliwala.adapter.SelectCatagoryAdapter;
import loop.theliwala.adapter.SelectStoreAdapter;
import loop.theliwala.adapter.SlidingImage_Adapter;
import loop.theliwala.ballsynccustomprogress.BallTriangleSyncDialog;
import loop.theliwala.database.DbHelper;
import loop.theliwala.framework.IAsyncWorkCompletedCallback;
import loop.theliwala.framework.ServiceCaller;
import loop.theliwala.framworkretrofit.ApiClient;
import loop.theliwala.framworkretrofit.ApiInterface;
import loop.theliwala.models.ContentData;
import loop.theliwala.models.ContentDataAsArray;
import loop.theliwala.models.Data;
import loop.theliwala.models.Result;
import loop.theliwala.utilities.CompatibilityUtility;
import loop.theliwala.utilities.Contants;
import loop.theliwala.utilities.FontManager;
import loop.theliwala.utilities.Utility;
import loop.theliwala.viewpagerindicator.CirclePageIndicator;
import retrofit2.Call;
import retrofit2.Callback;


public class HomeFragment extends Fragment {

    // TODO: Rename and change types of parameters
    private int localityId;
    //  private int LoginId;
    private String param;


    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(int localityId, String param) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt("LocalityId", localityId);
        args.putString("param", param);
//        args.putInt("LoginId", LoginId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            localityId = getArguments().getInt("LocalityId");
            param = getArguments().getString("param");
//            LoginId = getArguments().getInt("LoginId");
        }
    }

    private Context context;
    private View view;
    private RecyclerView categooryRecyclerView, bestSellingRecyclerView, newArriveRecyclerView;
    private LinearLayout linearLayout;
    Typeface materialDesignIcons, regular, bold;
    DbHelper dbHelper;
    Data data;
    List<Data> dataList, newArriveList, bestSellList;
    CardView card_viewCategory;
    CirclePageIndicator circlePageIndicator;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<String> ImagesArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_home, container, false);
        if (CompatibilityUtility.isTablet(context)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        materialDesignIcons = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        init();// initlization...........
        getPagerData();
        return view;
    }

    private void viewPagerSetUp() {
        //------viwepagerss settings...........
        mPager = (ViewPager) view.findViewById(R.id.pager);
        circlePageIndicator = view.findViewById(R.id.indicator);
        NUM_PAGES = ImagesArray.size();
        if (ImagesArray != null && ImagesArray.size() > 0) {
            mPager.setAdapter(new SlidingImage_Adapter(context, ImagesArray));
            circlePageIndicator.setViewPager(mPager);
        }
        final float density = context.getResources().getDisplayMetrics().density;

//Set circle indicator radius
        circlePageIndicator.setRadius(5 * density);
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new

                                    TimerTask() {
                                        @Override
                                        public void run() {
                                            handler.post(Update);
                                        }
                                    }, 1000, 1000);

        // Pager listener over indicator
        circlePageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
    }

    private void getPagerData() {
        ImagesArray.clear();
        ApiInterface apiInterface = ApiClient.getAPIClient().create(ApiInterface.class);

        apiInterface.getPagerData().enqueue(new Callback<ContentDataAsArray>() {
            @Override
            public void onResponse(Call<ContentDataAsArray> call, retrofit2.Response<ContentDataAsArray> response) {
                ContentDataAsArray contentData = response.body();
                if (contentData != null) {
                    for (Data result : contentData.getData()) {
                        ImagesArray.addAll(Arrays.asList(result.getImage()));
                    }
                }
                if (ImagesArray != null && ImagesArray.size() != 0) {
                    viewPagerSetUp();
                } else {
//                    Toasty.error(context, "No data Found").show();
                }
            }

            @Override
            public void onFailure(Call<ContentDataAsArray> call, Throwable t) {
                Toasty.error(context, "Your Connection Slow ! Time Out").show();
            }
        });
    }

    // initlization...........
    private void init() {
        ImagesArray = new ArrayList<String>();
        dataList = new ArrayList<>();
        newArriveList = new ArrayList<>();
        bestSellList = new ArrayList<>();
        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreencart(true);
        rootActivity.setItemCart();
        rootActivity.setScreenLocation(false);
        rootActivity.setScreenFavourite(true);
        rootActivity.setScreenSave(false);
        rootActivity.setScreenTitle("Theliwala");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
//        recyclerView.setLayoutManager(linearLayoutManager);
        categooryRecyclerView = view.findViewById(R.id.categooryRecyclerView);
        bestSellingRecyclerView = view.findViewById(R.id.bestSellingRecyclerView);
        newArriveRecyclerView = view.findViewById(R.id.newArriveRecyclerView);
        categooryRecyclerView.setLayoutManager(linearLayoutManager);
        bestSellingRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        newArriveRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        linearLayout = view.findViewById(R.id.linearLayout);
        card_viewCategory = view.findViewById(R.id.card_viewCategory);
        bold = FontManager.getFontTypeface(context, "fonts/roboto.bold.ttf");
        regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        // title.setTypeface(bold);
        getCategoryData();
        getBestSellData();
        getNewArriveData();
        card_viewCategory.setOnClickListener(v -> {
            SelectCategoryFragment selectCategoryFragment = SelectCategoryFragment.newInstance(0, 0);
            moveFragment(selectCategoryFragment);
        });
    }

    private void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void getCategoryData() {
        dataList.clear();
        final BallTriangleSyncDialog dotDialog = new BallTriangleSyncDialog(context);
        dotDialog.show();
        ServiceCaller serviceCaller = new ServiceCaller(context);
        serviceCaller.callAllCategoryListService(new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
                if (isComplete) {
                    ContentDataAsArray contentData = new Gson().fromJson(workName, ContentDataAsArray.class);
                    if (contentData != null) {
                        for (Data data : contentData.getData()) {
                            dataList.addAll(Arrays.asList(data));
                        }
                        if (dataList != null && dataList.size() != 0) {
                            categooryRecyclerView.setAdapter(new SelectCatagoryAdapter(context, dataList));
                        }
                    }
                }
                if (dotDialog.isShowing()) {
                    dotDialog.dismiss();
                }
            }
        });
    }

    private void getBestSellData() {
        bestSellList.clear();
        ServiceCaller serviceCaller = new ServiceCaller(context);
        serviceCaller.callAllBestSellListService(new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
                if (isComplete) {
                    ContentDataAsArray contentData = new Gson().fromJson(workName, ContentDataAsArray.class);
                    if (contentData != null) {
                        for (Data data : contentData.getData()) {
                            bestSellList.addAll(Arrays.asList(data));
                        }
                        if (bestSellList != null && bestSellList.size() != 0) {
                            bestSellingRecyclerView.setAdapter(new BestSellAdapter(context, bestSellList));
                        }
                    }
                }
            }
        });
    }

    private void getNewArriveData() {
        newArriveList.clear();
        ServiceCaller serviceCaller = new ServiceCaller(context);
        serviceCaller.callGetAllNewArriveListService(new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
                if (isComplete) {
                    ContentDataAsArray contentData = new Gson().fromJson(workName, ContentDataAsArray.class);
                    if (contentData != null) {
                        for (Data data : contentData.getData()) {
                            newArriveList.addAll(Arrays.asList(data));
                        }
                        if (newArriveList != null && newArriveList.size() != 0) {
                            newArriveRecyclerView.setAdapter(new NewArriveAdapter(context, newArriveList));
                        }
                    }
                }
            }
        });
    }
}
