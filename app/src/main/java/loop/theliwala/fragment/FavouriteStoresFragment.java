package loop.theliwala.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import loop.theliwala.R;
import loop.theliwala.activity.DashboardActivity;
import loop.theliwala.adapter.FavouriteStoresAdapter;
import loop.theliwala.ballsynccustomprogress.BallTriangleSyncDialog;
import loop.theliwala.database.DbHelper;
import loop.theliwala.framework.IAsyncWorkCompletedCallback;
import loop.theliwala.framework.ServiceCaller;
import loop.theliwala.models.Data;
import loop.theliwala.utilities.CompatibilityUtility;
import loop.theliwala.utilities.FontManager;
import loop.theliwala.utilities.Utility;

public class FavouriteStoresFragment extends Fragment {


    // TODO: Rename and change types of parameters
    private String mParam2;
    private String mParam1;

    public FavouriteStoresFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FavouriteStoresFragment newInstance(String param1, String param2) {
        FavouriteStoresFragment fragment = new FavouriteStoresFragment();
        Bundle args = new Bundle();
        args.putString("ARG_PARAM1", param1);
        args.putString("ARG_PARAM2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("ARG_PARAM1");
            mParam2 = getArguments().getString("ARG_PARAM2");
        }
    }

    private Context context;
    private RecyclerView recyclerView;
    private View view;
    private LinearLayout favourite_layout;
    Typeface materialdesignicons_font, regular;
    FavouriteStoresAdapter itemAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_favourite_stores, container, false);
        if (CompatibilityUtility.isTablet(context)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        init();
        getFavouriteStoreList();
        return view;
    }


    // initlization...........
    private void init() {
        regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreencart(true);
        rootActivity.setScreenSave(false);
        rootActivity.setScreenFavourite(false);
        rootActivity.setScreenLocation(true);
        rootActivity.setItemCart();
        rootActivity.setScreenTitle("Favourite Store");
        favourite_layout = (LinearLayout) view.findViewById(R.id.favourite_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.favouriteStore_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        Typeface bold = FontManager.getFontTypeface(context, "fonts/roboto.bold.ttf");
    }

    private void setValue() {
        DbHelper dbHelper = new DbHelper(context);
        List<Data> favouriteStoresList = dbHelper.getAllFavouriteStoresData();
        if (favouriteStoresList != null && favouriteStoresList.size() > 0) {
            itemAdapter = new FavouriteStoresAdapter(context, favouriteStoresList);
            recyclerView.setAdapter(itemAdapter);
        } else {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.no_data_found, null);
            TextView nodataIcon = (TextView) view.findViewById(R.id.nodataIcon);
            TextView nodata = (TextView) view.findViewById(R.id.nodata);
            nodata.setTypeface(regular);
            nodataIcon.setTypeface(materialdesignicons_font);
            nodataIcon.setText(Html.fromHtml("&#xf187;"));
            nodata.setText("Any Store Favourite not found");
            favourite_layout.setGravity(Gravity.CENTER);
            favourite_layout.removeAllViews();
            favourite_layout.addView(view);
        }
    }

    @Override
    public void onResume() {
        getActivity().registerReceiver(mReceiverLocation, new IntentFilter("data"));
        super.onResume();
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(mReceiverLocation);
        super.onPause();
    }

    //come from favorite adapter
    private BroadcastReceiver mReceiverLocation = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getAction().equalsIgnoreCase("data")) {
                    Boolean flag = intent.getBooleanExtra("flag", false);
                    if (flag) {
                        setValue();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    //get all Favourite store recyclerView.........
    public void getFavouriteStoreList() {
        if (Utility.isOnline(getActivity())) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
            int id = sharedPreferences.getInt("userId", 0);
            if (id != 0) {
                final BallTriangleSyncDialog dotDialog = new BallTriangleSyncDialog(context);
                dotDialog.show();
                ServiceCaller serviceCaller = new ServiceCaller(context);
                serviceCaller.callGetFavouriteStoreByUserService(id, new IAsyncWorkCompletedCallback() {
                    @Override
                    public void onDone(String workName, boolean isComplete) {
                        setValue();
                        if (dotDialog.isShowing()) {
                            dotDialog.dismiss();
                        }
                    }
                });
            }
        } else {
            noInternet();
        }

    }

    private void noInternet() {
        // Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, getActivity());//off line msg....
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.no_data_found, null);
        TextView nodata = (TextView) view.findViewById(R.id.nodata);
        TextView nodataIcon = (TextView) view.findViewById(R.id.nodataIcon);
        nodataIcon.setTypeface(materialdesignicons_font);
        nodataIcon.setText(Html.fromHtml("&#xf5aa;"));
        nodata.setText("No internet Connection found");
        favourite_layout.setGravity(Gravity.CENTER);
        favourite_layout.removeAllViews();
        favourite_layout.addView(view);
    }

}

