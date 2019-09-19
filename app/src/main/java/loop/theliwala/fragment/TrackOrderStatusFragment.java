package loop.theliwala.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import loop.theliwala.R;
import loop.theliwala.activity.DashboardActivity;
import loop.theliwala.adapter.TrackOrderStatusAdapter;
import loop.theliwala.ballsynccustomprogress.BallTriangleSyncDialog;
import loop.theliwala.database.DbHelper;
import loop.theliwala.framework.IAsyncWorkCompletedCallback;
import loop.theliwala.framework.ServiceCaller;
import loop.theliwala.models.Data;
import loop.theliwala.myalert.SweetAlertDialog;
import loop.theliwala.utilities.CompatibilityUtility;
import loop.theliwala.utilities.FontManager;
import loop.theliwala.utilities.Utility;


public class TrackOrderStatusFragment extends Fragment {


    private String OrderNumber, StoreName;
    private int storeId, orderId, LoginId;

    public static TrackOrderStatusFragment newInstance(String OrderNumber, int storeId, int orderId, String StoreName, int LoginId) {
        TrackOrderStatusFragment fragment = new TrackOrderStatusFragment();
        Bundle args = new Bundle();
        args.putString("OrderNumber", OrderNumber);
        args.putInt("storeId", storeId);
        args.putInt("orderId", orderId);
        args.putInt("LoginId", LoginId);
        args.putString("StoreName", StoreName);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            OrderNumber = getArguments().getString("OrderNumber");
            storeId = getArguments().getInt("storeId");
            orderId = getArguments().getInt("orderId");
            LoginId = getArguments().getInt("LoginId");
            StoreName = getArguments().getString("StoreName");

        }
    }

    View view;
    TextView tv_storeName, tv_placed, tv_status;
    Typeface materialDesignIcons, regular;
    private Context context;
    LinearLayout layout_orderDetails, liner_layout;
    List<Data> data;

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        if (CompatibilityUtility.isTablet(context)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        view = inflater.inflate(R.layout.fragment_track_order_status, container, false);
        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreencart(false);
        rootActivity.setScreenSave(false);
        rootActivity.setScreenFavourite(false);
        rootActivity.setScreenLocation(false);
        rootActivity.setScreenCartDot(false);
        rootActivity.setScreenTitle("Order Status");
        regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        materialDesignIcons = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        init();
        setStatus();
        return view;
    }

    private void init() {
        tv_storeName = (TextView) view.findViewById(R.id.tv_storeName);
        tv_placed = (TextView) view.findViewById(R.id.tv_placed);
        tv_status = (TextView) view.findViewById(R.id.tv_status);
        layout_orderDetails = (LinearLayout) view.findViewById(R.id.layout_orderDetails);
        liner_layout = (LinearLayout) view.findViewById(R.id.liner_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    //set status in  progress bar
    private void setStatus() {
        if (Utility.isOnline(context)) {
            final BallTriangleSyncDialog dotDialog = new BallTriangleSyncDialog(context);
            dotDialog.show();
            SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
            LoginId = sharedPreferences.getInt("userId", 0);
            if (LoginId != 0) {
                ServiceCaller serviceCaller = new ServiceCaller(context);
                serviceCaller.callTrackOrderStatusService(OrderNumber, storeId, LoginId, new IAsyncWorkCompletedCallback() {
                    @Override
                    public void onDone(String result, boolean isComplete) {
                        if (isComplete) {
                            setOrderDetailsData();
                        } else {
                            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Sorry...")
                                    .setContentText("Order Details Not Found")
                                    .show();
                            //Utility.alertForErrorMessage("Order Details Not Found", context);
                        }
                        if (dotDialog.isShowing()) {
                            dotDialog.dismiss();
                        }
                    }
                });
            }
    } else

    {
        // Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, context);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.no_data_found, null);
        TextView nodata = (TextView) view.findViewById(R.id.nodata);
        TextView nodataIcon = (TextView) view.findViewById(R.id.nodataIcon);
        nodataIcon.setTypeface(materialDesignIcons);
        nodataIcon.setText(Html.fromHtml("&#xf5aa;"));
        nodata.setText("No internet Connection found");
        liner_layout.setGravity(Gravity.CENTER);
        liner_layout.removeAllViews();
        liner_layout.addView(view);
    }

}

    private void setOrderDetailsData() {
        List<String> stringList = new ArrayList<>();
        stringList.add("Received");
        stringList.add("Preparing");
        stringList.add("Dispatched");
        //stringList.add("Delivered");
        DbHelper dbHelper = new DbHelper(context);
        Data orderData = dbHelper.getGetAllOrderByUserDataByOrderId(orderId);
        if (orderData != null) {
            tv_status.setText(orderData.getOrderStatus());
            tv_storeName.setText(StoreName);
            if (!orderData.getOrderStatus().equalsIgnoreCase("Store Rejected")) {
                TrackOrderStatusAdapter adapter = new TrackOrderStatusAdapter(context, orderData.getOrderStatus(), orderData.getOrderTime(), stringList);
                recyclerView.setAdapter(adapter);
            } else {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.no_data_found, null);
                TextView nodataIcon = (TextView) view.findViewById(R.id.nodataIcon);
                TextView nodata = (TextView) view.findViewById(R.id.nodata);
                nodata.setTypeface(regular);
                //nodataIcon.setTypeface(materialdesignicons_font);
                //nodataIcon.setText(Html.fromHtml("&#xf187;"));
                nodata.setText("Your Order has been Rejected.");
                nodata.setTextColor(Color.RED);
                nodata.setTextSize(30);
                nodata.setGravity(Gravity.CENTER | Gravity.BOTTOM);
                liner_layout.setGravity(Gravity.BOTTOM);
                liner_layout.removeAllViews();
                liner_layout.addView(view);
            }
            Date startDateTime = null;
            String sdateTime = null;
            try {
                startDateTime = Utility.toCalendar(orderData.getOrderTime());
                sdateTime = Utility.convertDate(startDateTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //get value based on array position Wednesday,15,Jun,6,30,AM,2000,06 index(0,1,2,3,4,5,6,7)
            if (sdateTime != null) {
                String[] startArray = sdateTime.split(",");
                tv_placed.setText("Placed on: " + startArray[1] + "-" + startArray[2] + "-" + startArray[6]);
            }
        }

    }
}