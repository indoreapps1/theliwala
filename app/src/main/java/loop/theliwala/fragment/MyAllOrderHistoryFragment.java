package loop.theliwala.fragment;

import android.content.Context;
import android.content.Intent;
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
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import loop.theliwala.R;
import loop.theliwala.activity.DashboardActivity;
import loop.theliwala.activity.LoginActivity;
import loop.theliwala.adapter.MyAllOrderHistoryAdapter;
import loop.theliwala.ballsynccustomprogress.BallTriangleSyncDialog;
import loop.theliwala.database.DbHelper;
import loop.theliwala.framework.IAsyncWorkCompletedCallback;
import loop.theliwala.framework.ServiceCaller;
import loop.theliwala.models.Data;
import loop.theliwala.utilities.CompatibilityUtility;
import loop.theliwala.utilities.FontManager;
import loop.theliwala.utilities.Utility;


public class MyAllOrderHistoryFragment extends Fragment {

    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;

    public static MyAllOrderHistoryFragment newInstance(int param1, String param2) {
        MyAllOrderHistoryFragment fragment = new MyAllOrderHistoryFragment();
        Bundle args = new Bundle();
        args.putInt("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt("param1");
            mParam2 = getArguments().getString("param2");
        }
    }

    Context context;
    View view;
    RecyclerView my_order_recycler_view;
    LinearLayout my_order_history_layout;
    Typeface materialdesignicons_font, regular;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        if (CompatibilityUtility.isTablet(context)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.fragment_my_all_order_history, container, false);
        init();
        getMyAllOrderHistoryData();
        return view;
    }

    private void init() {
        regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        my_order_history_layout = (LinearLayout) view.findViewById(R.id.my_order_history_layout);
        my_order_recycler_view = (RecyclerView) view.findViewById(R.id.my_order_recycler_view);
        my_order_recycler_view.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        my_order_recycler_view.setLayoutManager(layoutManager);
        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        if (rootActivity != null) {
            rootActivity.setScreencart(true);
            rootActivity.setScreenSave(false);
            rootActivity.setScreenFavourite(false);
            rootActivity.setScreenLocation(false);
            rootActivity.setScreenCartDot(false);
            //rootActivity.setScreenTitle("All Order History");
        }
    }

    private void setHistoryData() {
        DbHelper dbHelper = new DbHelper(context);
        List<Data> orderlist = dbHelper.GetMyAllOrderHistoryData();
        if (orderlist != null && orderlist.size() > 0) {
            MyAllOrderHistoryAdapter myAllOrderHistoryAdapter = new MyAllOrderHistoryAdapter(context, orderlist);
            my_order_recycler_view.setAdapter(myAllOrderHistoryAdapter);
            // do not add address messag
        } else {
            // for empty data .............
            noDataFound();
        }
    }

    private void noDataFound() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.no_data_found, null);
        TextView nodataIcon = (TextView) view.findViewById(R.id.nodataIcon);
        TextView nodata = (TextView) view.findViewById(R.id.nodata);
        nodata.setTypeface(regular);
        nodataIcon.setTypeface(materialdesignicons_font);
        nodataIcon.setText(Html.fromHtml("&#xf187;"));
        nodata.setText("Any Order History Not found");
        my_order_history_layout.setGravity(Gravity.CENTER);
        my_order_history_layout.removeAllViews();
        my_order_history_layout.addView(view);
    }

    // my order hisatory............
    public void getMyAllOrderHistoryData() {
        if (Utility.isOnline(context)) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
            int id = sharedPreferences.getInt("userId", 0);
            if (id != 0) {
                final BallTriangleSyncDialog dotDialog = new BallTriangleSyncDialog(context);
                dotDialog.show();
                ServiceCaller serviceCaller = new ServiceCaller(context);
                serviceCaller.callMyAllOrderHistoryService(id, new IAsyncWorkCompletedCallback() {
                    @Override
                    public void onDone(String workName, boolean isComplete) {
                        if (isComplete) {
                            setHistoryData();
                        } else {
                            noDataFound();
                        }
                        if (dotDialog.isShowing()) {
                            dotDialog.dismiss();
                        }
                    }

                });
            } else {
                Intent intent = new Intent(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } else {
            // Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, context);//offline check..........
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.no_data_found, null);
            TextView nodata = (TextView) view.findViewById(R.id.nodata);
            TextView nodataIcon = (TextView) view.findViewById(R.id.nodataIcon);
            nodataIcon.setTypeface(materialdesignicons_font);
            nodataIcon.setText(Html.fromHtml("&#xf5aa;"));
            nodata.setText("No internet Connection found");
            my_order_history_layout.setGravity(Gravity.CENTER);
            my_order_history_layout.removeAllViews();
            my_order_history_layout.addView(view);
        }
    }
}



