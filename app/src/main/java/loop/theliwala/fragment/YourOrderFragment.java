package loop.theliwala.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import loop.theliwala.R;
import loop.theliwala.activity.DashboardActivity;
import loop.theliwala.adapter.YourOrderAdpater;
import loop.theliwala.ballsynccustomprogress.BallTriangleSyncDialog;
import loop.theliwala.database.DbHelper;
import loop.theliwala.framework.IAsyncWorkCompletedCallback;
import loop.theliwala.framework.ServiceCaller;
import loop.theliwala.models.Addresses;
import loop.theliwala.models.ContentData;
import loop.theliwala.models.CreateOrderDetails;
import loop.theliwala.models.Data;
import loop.theliwala.models.MyBasket;
import loop.theliwala.myalert.SweetAlertDialog;
import loop.theliwala.utilities.CompatibilityUtility;
import loop.theliwala.utilities.FontManager;
import loop.theliwala.utilities.Utility;


/**
 * Created by LALIT on 8/14/2017.
 */
public class YourOrderFragment extends Fragment implements View.OnClickListener {
    private int addressId;
    private int StoreId;

    public static YourOrderFragment newInstance(int addressId, int StoreId) {
        YourOrderFragment fragment = new YourOrderFragment();
        Bundle args = new Bundle();
        args.putInt("AddressId", addressId);
        args.putInt("StoreId", StoreId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            addressId = getArguments().getInt("AddressId");
            StoreId = getArguments().getInt("StoreId");
        }
    }


    YourOrderAdpater adapter;
    View view;
    TextView tv_continue, storeName, tv_specialDiscount_charges, tv_specialDiscount,
            rupee_icon, icon_rupees, tv_rupees_icon, rupees_icon,
            tv_deliverTo, tv_deliveryAddress, quantity, dish_name, price, action, total, total_amount, shipping, shipping_charges, grand_amount, grand_total, tv_payOnline;
    private Context context;
    private int loginID;
    private float promoCodeDiscount;
    private LinearLayout layout_promoCode, tv_continueLayout;
    private ArrayList<CreateOrderDetails> orderDetailsesList;
    private double totalPrice = 0;
    private int storeId;
    int opTime, endTime;
    private Boolean editFlag = false;
    private double SubTotalPrice = 0;
    //private double totalDiscountPrice = 0;
    private int promoCodeId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        if (CompatibilityUtility.isTablet(context)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        view = inflater.inflate(R.layout.fragment_add_yourorder, container, false);
        init();
        return view;
    }


    //set icons..........
    private void setIcons() {
        Typeface italic = FontManager.getFontTypeface(context, "fonts/roboto.italic.ttf");
        Typeface medium = FontManager.getFontTypeface(context, "fonts/roboto.medium.ttf");
        Typeface regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        Typeface bold = FontManager.getFontTypeface(context, "fonts/roboto.bold.ttf");
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(getActivity().getApplicationContext(), "fonts/materialdesignicons-webfont.otf");
        rupee_icon.setTypeface(materialdesignicons_font);
        rupee_icon.setText(Html.fromHtml("&#xf1af;"));
        icon_rupees.setTypeface(materialdesignicons_font);
        icon_rupees.setText(Html.fromHtml("&#xf1af;"));
        rupees_icon.setTypeface(materialdesignicons_font);
        rupees_icon.setText(Html.fromHtml("&#xf1af;"));

        tv_rupees_icon.setTypeface(materialdesignicons_font);
        tv_rupees_icon.setText(Html.fromHtml("&#xf1af;"));
        tv_deliverTo.setTypeface(medium);
        tv_deliveryAddress.setTypeface(regular);
        quantity.setTypeface(regular);
        dish_name.setTypeface(regular);
        action.setTypeface(regular);
        price.setTypeface(regular);
        total.setTypeface(regular);
        total_amount.setTypeface(regular);
        shipping.setTypeface(regular);
        tv_specialDiscount.setTypeface(regular);
        shipping_charges.setTypeface(regular);
        tv_specialDiscount_charges.setTypeface(regular);
        grand_amount.setTypeface(medium);
        grand_total.setTypeface(medium);
        storeName.setTypeface(medium);
    }

    //initlization..............
    private void init() {
        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreencart(false);
        rootActivity.setScreenSave(false);
        rootActivity.setScreenFavourite(false);
        rootActivity.setScreenLocation(false);
        rootActivity.setScreenCartDot(false);
        rootActivity.setScreenTitle("Your Order");
        tv_continue = (TextView) view.findViewById(R.id.tv_continue);
        rupee_icon = (TextView) view.findViewById(R.id.rupee_icon);
        icon_rupees = (TextView) view.findViewById(R.id.icon_rupees);
        rupees_icon = (TextView) view.findViewById(R.id.rupees_icon);
        quantity = (TextView) view.findViewById(R.id.quantity);
        dish_name = (TextView) view.findViewById(R.id.dish_name);
        action = (TextView) view.findViewById(R.id.action);
        price = (TextView) view.findViewById(R.id.price);
        total = (TextView) view.findViewById(R.id.total);
        total_amount = (TextView) view.findViewById(R.id.total_amount);
        shipping = (TextView) view.findViewById(R.id.shipping);
        tv_specialDiscount = (TextView) view.findViewById(R.id.tv_specialDiscount);
        shipping_charges = (TextView) view.findViewById(R.id.shipping_charges);
        tv_specialDiscount_charges = (TextView) view.findViewById(R.id.tv_specialDiscount_charges);
        grand_amount = (TextView) view.findViewById(R.id.grant_amount);
        grand_total = (TextView) view.findViewById(R.id.grand_total);
        tv_payOnline = (TextView) view.findViewById(R.id.tv_continue);
        tv_rupees_icon = (TextView) view.findViewById(R.id.tv_rupees_icon);
        tv_deliverTo = (TextView) view.findViewById(R.id.tv_deliverTo);
        tv_deliveryAddress = (TextView) view.findViewById(R.id.tv_deliveryAddress);
        //tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        storeName = (TextView) view.findViewById(R.id.storeName);
        layout_promoCode = (LinearLayout) view.findViewById(R.id.layout_promoCode);
        tv_continueLayout = (LinearLayout) view.findViewById(R.id.tv_continueLayout);
        tv_continueLayout.setOnClickListener(this);
        //  tv_cancel.setOnClickListener(this);
        setIcons();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.listrecycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        getStoreIdFromPreferences();
        orderDetailsesList = new ArrayList<CreateOrderDetails>();
        List<MyBasket> orderList = new DbHelper(context).GetAllBasketOrderData();
        if (orderList != null && orderList.size() > 0) {
            adapter = new YourOrderAdpater(context, orderList);
            recyclerView.setAdapter(adapter);
            orderDetails(orderList);
        }
//        if (addresses != null) {
//            tv_deliveryAddress.setText(addresses.getCompleteAddress() + "," + addresses.getZipCode() + "\n" + "+91" + addresses.getPhoneNumber());
//        }
//        Data sData = dbHelper.getStoreData(storeId);
//        if (sData != null) {
//            storeName.setText(sData.getStoreName());
//        }

    }

    //get selected store id
    private void getStoreIdFromPreferences() {
        SharedPreferences prefs = context.getSharedPreferences("StoreIdPreferences", Context.MODE_PRIVATE);
        if (prefs != null) {
            storeId = prefs.getInt("StoreId", 0);
        }
    }

    private void orderDetails(List<MyBasket> orderList) {
        for (MyBasket order : orderList) {
            CreateOrderDetails orderDetails = new CreateOrderDetails();
            orderDetails.setProductId(order.getProductId());
            orderDetails.setQuantity(order.getQuantity());
            orderDetailsesList.add(orderDetails);
        }
    }
    //.....................calculate total price with all discount.................//
//    private double calculateTotalQuantityPrice(float Quantity, float itemPrice, float Discount) {
//        double totalPrice = 0;
//        double priceAfterDiscount = 0;
//        double totalPriceAfterDiscount = 0;
//        double totalPriceWithQuantity = Quantity * itemPrice;
//        if (Discount != 0) {
//            priceAfterDiscount = (totalPriceWithQuantity / 100.0f) * Discount;// discount in total Quantity with price
//            totalPriceAfterDiscount = totalPriceWithQuantity - priceAfterDiscount;//get total price after discount amount
//            totalDiscountPrice = totalDiscountPrice + priceAfterDiscount;//get total discount amount
//            totalPrice = totalPriceAfterDiscount;//total price after discount
//        } else {
//            totalPrice = totalPriceWithQuantity;//total price with Quantity
//        }
//        return totalPrice;
//    }

//
//    private void calculateTotalPrice(List<MyBasket> orderList) {
//
//        for (MyBasket order : orderList) {
//            //  double totalProductPrice = calculateTotalQuantityPrice(order.getQuantity(), order.getPrice(), order.getDiscount());
//            //    totalPrice = totalPrice + totalProductPrice;
////            SubTotalPrice = SubTotalPrice + order.getPrice();
//            // SubTotalPrice = SubTotalPrice + order.getQuantity() * order.getPrice();
//            CreateOrderDetails orderDetails = new CreateOrderDetails();
//            orderDetails.setProductId(order.getProductId());
//            orderDetails.setQuantity(order.getQuantity());
////            orderDetails.setTotalPrice(totalProductPrice);
//            orderDetailsesList.add(orderDetails);
//        }
//        if (promoCodeDiscount != 0) {
//            totalDiscountPrice = totalDiscountPrice + promoCodeDiscount;
//            tv_specialDiscount_charges.setText(String.valueOf(totalDiscountPrice));//show discount
////            totalPrice = calculatePromoCodeDiscount(totalPrice);
//        }
////        total_amount.setText(String.valueOf(SubTotalPrice));
////        grand_amount.setText(String.valueOf(totalPrice));
////        tv_specialDiscount_charges.setText(String.valueOf(totalDiscountPrice));
//    }

    //calculate promo code discount on total amount
//    private double calculatePromoCodeDiscount(double totalPrice) {
////        double priceAfterPromoCodeDiscount = 0;
////        //priceAfterPromoCodeDiscount = (totalPrice / 100.0f) * promoCodeDiscount;// promo code discount in total priceAfterDiscount
////        priceAfterPromoCodeDiscount = totalPrice - promoCodeDiscount;
////        return priceAfterPromoCodeDiscount;
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_continueLayout:
                //compareDates();
                createNewOrder();
                break;
        }
    }

    //create new Order

    public void createNewOrder() {
        if (Utility.isOnline(context)) {
            final BallTriangleSyncDialog dotDialog = new BallTriangleSyncDialog(context);
            dotDialog.show();
            SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
            int id = sharedPreferences.getInt("userId", 0);
            ServiceCaller serviceCaller = new ServiceCaller(context);
            serviceCaller.createOrderService(storeId, addressId, id, promoCodeId, orderDetailsesList, new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        if (result != null) {
                            OrderConfirmFragment fragment = OrderConfirmFragment.newInstance(addressId, result);
                            moveFragmentWithTag(fragment, "OrderPlacedFragment");
                        } else {
                            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Sorry...")
                                    .setContentText("Your Order Not placed Successfully")
                                    .show();
                            //Utility.alertForErrorMessage("Order not Placed Successfully", context);
                        }
                    } else {
                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Sorry...")
                                .setContentText("Your order Not Placed Successfully")
                                .show();
                        //Utility.alertForErrorMessage("Order not Placed Successfully", context);
                    }
                    if (dotDialog.isShowing()) {
                        dotDialog.dismiss();
                    }
                }
            });


        } else {
            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("You are Offline. Please check your Internet Connection.")
                    .show();
            //Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, context);
        }
    }

    private void moveFragmentWithTag(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment, tag)
                .addToBackStack(null)
                .commit();
    }
}