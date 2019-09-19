package loop.theliwala.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import loop.theliwala.R;
import loop.theliwala.activity.DashboardActivity;
import loop.theliwala.adapter.OrderDetailAdapter;
import loop.theliwala.adapter.TrackOrderStatusAdapter;
import loop.theliwala.ballsynccustomprogress.BallTriangleSyncDialog;
import loop.theliwala.database.DbHelper;
import loop.theliwala.framework.IAsyncWorkCompletedCallback;
import loop.theliwala.framework.ServiceCaller;
import loop.theliwala.models.Addresses;
import loop.theliwala.models.ContentData;
import loop.theliwala.models.Data;
import loop.theliwala.models.OrderDetails;
import loop.theliwala.myalert.SweetAlertDialog;
import loop.theliwala.utilities.CompatibilityUtility;
import loop.theliwala.utilities.FontManager;
import loop.theliwala.utilities.Utility;

public class OrderDetailFragment extends Fragment {
    private String orderNumber, StoreName;
    private int storeId;
    private int orderId;

    // TODO: Rename and change types and number of parameters
    public static OrderDetailFragment newInstance(int orderId, String orderNumber, int storeId, String StoreName) {
        OrderDetailFragment fragment = new OrderDetailFragment();
        Bundle args = new Bundle();
        args.putString("orderNumber", orderNumber);
        args.putInt("storeId", storeId);
        args.putInt("orderId", orderId);
        args.putString("StoreName", StoreName);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            orderNumber = getArguments().getString("orderNumber");
            storeId = getArguments().getInt("storeId");
            orderId = getArguments().getInt("orderId");
            StoreName = getArguments().getString("StoreName");
        }
    }

    TextView tv_specialDiscount, tv_gst, total_gst,
            icon_rupees, tv_rupees_icon, rupees_icon, tv_rupees_icon_gst, net_price_amount, tv_net_price,
            tv_deliverTo, tv_deliveryAddress, quantity, dish_name, price, net_price_rupees_icon, icon_rupees_promo,
            total, total_amount, shipping, shipping_charges, grand_amount, grand_total, sub_total, sub_amount, icon_rupees_sub,
            tv_discount, tv_discount_rupees_icon, tv_orderPlaced, tv_Discount_charges, tv_promocode, tv_promocode_value, storeName, orderStatus;
    private Context context;
    LinearLayout liner_layout, layout_orderDetails;
    List<Data> data;
    OrderDetailAdapter adapter;
    private RecyclerView listrecycler, recyclerView;
    View view;
    int LoginId;
    Typeface regular, materialDesignIcons;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        if (CompatibilityUtility.isTablet(context)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        view = inflater.inflate(R.layout.fragment_order_detail, container, false);
        init();//initlization..............
        setIcons();//set icons..........
        GetOrderByOrderNumber();
        return view;
    }

    //GetOrderByOrderNumber api method....
    private void GetOrderByOrderNumber() {
        if (Utility.isOnline(context)) {
            final BallTriangleSyncDialog dotDialog = new BallTriangleSyncDialog(context);
            dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(context);
            serviceCaller.GetOrderByOrderNumberService(orderNumber, storeId, new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        ContentData contentData = new Gson().fromJson(result, ContentData.class);
                        if (contentData != null) {
                            Data data = contentData.getData();
                            if (data != null) {
                                setOrderDetailsData(data);
                            }
                        }

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
        } else {
            // Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, context);
            Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.no_data_found, null);
            TextView nodata = (TextView) view.findViewById(R.id.nodata);
            TextView nodataIcon = (TextView) view.findViewById(R.id.nodataIcon);
            nodataIcon.setTypeface(materialdesignicons_font);
            nodataIcon.setText(Html.fromHtml("&#xf5aa;"));
            nodata.setText("No internet Connection found");
            layout_orderDetails.setGravity(Gravity.CENTER);
            layout_orderDetails.removeAllViews();
            layout_orderDetails.addView(view);
        }
    }

    private void setOrderDetailsData(Data data) {
//        DbHelper dbHelper = new DbHelper(context);
//        Data orderData = dbHelper.getGetAllOrderByUserDataByOrderId(orderId);
        if (data != null) {
            storeName.setText(data.getStoreName());
            orderStatus.setText(data.getOrderStatus());
            total_amount.setText(String.valueOf(data.getTotalPrice()));
            grand_amount.setText(String.valueOf(data.getGrandTotal()));
            tv_Discount_charges.setText(String.valueOf(data.getSpecialDiscount()));
            shipping_charges.setText(String.valueOf(data.getDeliveryCharge()));
//            tv_promocode_value.setText(String.valueOf(data.getPromoDiscount()));
            net_price_amount.setText(String.valueOf(data.getNetPrice()));
            sub_amount.setText(String.valueOf(data.getSubTotal()));
            total_gst.setText(String.valueOf(data.getTotalGST()));

            Date startDateTime = null;
            String sdateTime = null;
            try {
                startDateTime = Utility.toCalendar(data.getOrderTime());
                sdateTime = Utility.convertDate(startDateTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            DbHelper dbHelper = new DbHelper(context);
            Addresses addresses = dbHelper.getAllAddressesData(data.getAddressId());
            if (addresses != null) {
                tv_deliveryAddress.setText(addresses.getCompleteAddress() + "," + addresses.getZipCode() + "\n" + "+91 " + addresses.getPhoneNumber());
//            //get value based on array position Wednesday,15,Jun,6,30,AM,2000,06 index(0,1,2,3,4,5,6,7)
            }
            if (sdateTime != null) {
                String[] startArray = sdateTime.split(",");
                tv_orderPlaced.setText("Order Placed on: " + startArray[1] + "-" + startArray[2] + "-" + startArray[6]);
            }

            OrderDetails[] orderDetailses = data.getOrderDetails();
            if (orderDetailses != null) {
                adapter = new OrderDetailAdapter(context, orderDetailses);
                listrecycler.setAdapter(adapter);
            }
        }
    }


    //set icons..........
    private void setIcons() {
        Typeface italic = FontManager.getFontTypeface(context, "fonts/roboto.italic.ttf");
        Typeface medium = FontManager.getFontTypeface(context, "fonts/roboto.medium.ttf");
        Typeface regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        Typeface bold = FontManager.getFontTypeface(context, "fonts/roboto.bold.ttf");
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(getActivity().getApplicationContext(), "fonts/materialdesignicons-webfont.otf");
        //rupee_icon.setTypeface(materialdesignicons_font);
        // rupee_icon.setText(Html.fromHtml("&#xf1af;"));
        icon_rupees.setTypeface(materialdesignicons_font);
        icon_rupees.setText(Html.fromHtml("&#xf1af;"));
        rupees_icon.setTypeface(materialdesignicons_font);
        rupees_icon.setText(Html.fromHtml("&#xf1af;"));
        net_price_rupees_icon.setTypeface(materialdesignicons_font);
        net_price_rupees_icon.setText(Html.fromHtml("&#xf1af;"));
        tv_rupees_icon_gst.setTypeface(materialdesignicons_font);
        tv_rupees_icon_gst.setText(Html.fromHtml("&#xf1af;"));
        icon_rupees_promo.setTypeface(materialdesignicons_font);
        icon_rupees_promo.setText(Html.fromHtml("&#xf1af;"));
        tv_discount_rupees_icon.setTypeface(materialdesignicons_font);
        tv_discount_rupees_icon.setText(Html.fromHtml("&#xf1af;"));
        tv_deliverTo.setTypeface(medium);
        tv_deliveryAddress.setTypeface(regular);
        quantity.setTypeface(regular);
        dish_name.setTypeface(regular);
        price.setTypeface(regular);
        total.setTypeface(regular);
        total_amount.setTypeface(regular);
        tv_discount.setTypeface(regular);
        icon_rupees_sub.setTypeface(materialdesignicons_font);
        icon_rupees_sub.setText(Html.fromHtml("&#xf1af;"));
        tv_promocode.setTypeface(regular);
        tv_promocode_value.setTypeface(regular);
        shipping.setTypeface(regular);
        shipping_charges.setTypeface(regular);
        grand_amount.setTypeface(medium);
        tv_net_price.setTypeface(medium);
        net_price_amount.setTypeface(medium);
        grand_total.setTypeface(medium);
        sub_total.setTypeface(medium);
        sub_amount.setTypeface(medium);
        storeName.setTypeface(regular);
        orderStatus.setTypeface(regular);
        tv_orderPlaced.setTypeface(regular);
    }

    //initlization..............
    private void init() {
        regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        materialDesignIcons = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreencart(false);
        rootActivity.setScreenSave(false);
        rootActivity.setScreenFavourite(false);
        rootActivity.setScreenLocation(false);
        rootActivity.setScreenCartDot(false);
        rootActivity.setScreenTitle("Order Details");
        icon_rupees = (TextView) view.findViewById(R.id.icon_rupees);
        rupees_icon = (TextView) view.findViewById(R.id.rupees_icon);
        quantity = (TextView) view.findViewById(R.id.quantity);
        dish_name = (TextView) view.findViewById(R.id.dish_name);
        price = (TextView) view.findViewById(R.id.price);
        total = (TextView) view.findViewById(R.id.total);
        total_amount = (TextView) view.findViewById(R.id.total_amount);
        tv_net_price = (TextView) view.findViewById(R.id.tv_net_price);
        net_price_amount = (TextView) view.findViewById(R.id.net_price_amount);
        storeName = (TextView) view.findViewById(R.id.storeName);
        icon_rupees_promo = (TextView) view.findViewById(R.id.icon_rupees_promo);
        tv_promocode = (TextView) view.findViewById(R.id.tv_promocode);
        tv_promocode_value = (TextView) view.findViewById(R.id.tv_promocode_value);
        tv_specialDiscount = (TextView) view.findViewById(R.id.tv_specialDiscount);
        shipping_charges = (TextView) view.findViewById(R.id.shipping_charges);
        shipping = (TextView) view.findViewById(R.id.shipping);
        tv_discount = (TextView) view.findViewById(R.id.tv_discount);
        tv_Discount_charges = (TextView) view.findViewById(R.id.tv_Discount_charges);
        tv_gst = (TextView) view.findViewById(R.id.tv_gst);
        total_gst = (TextView) view.findViewById(R.id.total_gst);
        net_price_rupees_icon = (TextView) view.findViewById(R.id.net_price_rupees_icon);
        tv_rupees_icon_gst = (TextView) view.findViewById(R.id.tv_rupees_icon_gst);
        sub_total = (TextView) view.findViewById(R.id.sub_total);
        icon_rupees_sub = (TextView) view.findViewById(R.id.icon_rupees_sub);
        sub_amount = (TextView) view.findViewById(R.id.sub_amount);
        grand_amount = (TextView) view.findViewById(R.id.grant_amount);
        grand_total = (TextView) view.findViewById(R.id.grand_total);
        tv_rupees_icon = (TextView) view.findViewById(R.id.tv_rupees_icon);
        tv_discount_rupees_icon = (TextView) view.findViewById(R.id.tv_discount_rupees_icon);
        tv_deliverTo = (TextView) view.findViewById(R.id.tv_deliverTo);
        tv_deliveryAddress = (TextView) view.findViewById(R.id.tv_deliveryAddress);
        tv_orderPlaced = (TextView) view.findViewById(R.id.tv_orderPlaced);
        orderStatus = (TextView) view.findViewById(R.id.orderStatus);
        liner_layout = (LinearLayout) view.findViewById(R.id.liner_layout);
        listrecycler = (RecyclerView) view.findViewById(R.id.listrecycler);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        listrecycler.setLayoutManager(layoutManager);
        LinearLayoutManager layoutManagerStatus = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManagerStatus);
        setStatus();
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
                serviceCaller.callTrackOrderStatusService(orderNumber, storeId, LoginId, new IAsyncWorkCompletedCallback() {
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
        } else {
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
                nodata.setGravity(Gravity.CENTER);
                liner_layout.setGravity(Gravity.CENTER);
                liner_layout.removeAllViews();
                liner_layout.addView(view);
            }
        }
    }
}