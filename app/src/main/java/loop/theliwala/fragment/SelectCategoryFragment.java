package loop.theliwala.fragment;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
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

import loop.theliwala.R;
import loop.theliwala.activity.DashboardActivity;
import loop.theliwala.adapter.SelectCatagoryAdapter;
import loop.theliwala.ballsynccustomprogress.BallTriangleSyncDialog;
import loop.theliwala.database.DbHelper;
import loop.theliwala.framework.IAsyncWorkCompletedCallback;
import loop.theliwala.framework.ServiceCaller;
import loop.theliwala.models.ContentDataAsArray;
import loop.theliwala.models.Data;
import loop.theliwala.utilities.CompatibilityUtility;
import loop.theliwala.utilities.FontManager;
import loop.theliwala.utilities.Utility;

/**
 * Created by LALIT on 8/10/2017.
 */

public class SelectCategoryFragment extends Fragment {

    private int menuId;
    private int storeId;


    // TODO: Rename and change types and number of parameters
    public static SelectCategoryFragment newInstance(int menuId, int storeId) {
        SelectCategoryFragment fragment = new SelectCategoryFragment();
        Bundle args = new Bundle();
        args.putInt("MenuId", menuId);
        args.putInt("StoreId", storeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            menuId = getArguments().getInt("MenuId");
            storeId = getArguments().getInt("StoreId");
        }
    }

    private SelectCatagoryAdapter adapter;
    private List<Data> categoryList;
    RecyclerView recyclerView;
    StaggeredGridLayoutManager gaggeredGridLayoutManager;
    private Context context;
    private LinearLayout linearLayout;
    View view;
    Typeface materialDesignIcons, regular;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        if (CompatibilityUtility.isTablet(context)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        view = inflater.inflate(R.layout.fragment_select_catagory, container, false);
        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreencart(true);
        rootActivity.setScreenSave(false);
        rootActivity.setScreenLocation(false);
        rootActivity.setScreenFavourite(false);
        rootActivity.setScreenTitle("Categories");
        regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        materialDesignIcons = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        init();
        getAllCategoryList();
        return view;
    }

    //set data in reycle view
    private void init() {
        categoryList = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
        // TextView title = (TextView) view.findViewById(R.id.tv_title);
        //title.setText("Choose Category");
        Typeface bold = FontManager.getFontTypeface(context, "fonts/roboto.bold.ttf");
        //title.setTypeface(bold);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        recyclerView.setHasFixedSize(false);
//        if (CompatibilityUtility.isTablet(context)) {
//            gaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
//        } else {
//            gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
//        }
        // recyclerView.setLayoutManager(gaggeredGridLayoutManager);

    }

    private void noDataFound() {
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.no_data_found, null);
        TextView nodataIcon = (TextView) view.findViewById(R.id.nodataIcon);
        TextView nodata = (TextView) view.findViewById(R.id.nodata);
        nodata.setTypeface(regular);
        nodataIcon.setTypeface(materialDesignIcons);
        nodataIcon.setText(Html.fromHtml("&#xf187;"));
        nodata.setText("Any Category not found");
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.removeAllViews();
        linearLayout.addView(view);
    }

    //get all category list data
    private void getAllCategoryList() {

        if (Utility.isOnline(context)) {
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
                                categoryList.addAll(Arrays.asList(data));
                            }
                            if (categoryList != null && categoryList.size() != 0) {
                                recyclerView.setAdapter(new SelectCatagoryAdapter(context, categoryList));
                            } else {
                                noDataFound();
                            }
                        }
                        if (dotDialog.isShowing()) {
                            dotDialog.dismiss();
                        }

                    } else {
                        noDataFound();
                    }
                }
            });
        } else {
            //   Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, context);//off line msg....
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.no_data_found, null);
            TextView nodata = (TextView) view.findViewById(R.id.nodata);
            TextView nodataIcon = (TextView) view.findViewById(R.id.nodataIcon);
            nodataIcon.setTypeface(materialDesignIcons);
            nodataIcon.setText(Html.fromHtml("&#xf5aa;"));
            nodata.setText("No internet Connection found");
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.removeAllViews();
            linearLayout.addView(view);
        }
    }
}
