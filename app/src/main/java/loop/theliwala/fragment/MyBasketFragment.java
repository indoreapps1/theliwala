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
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
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

import java.text.DecimalFormat;
import java.util.List;

import loop.theliwala.R;
import loop.theliwala.activity.DashboardActivity;
import loop.theliwala.adapter.BasketAdapter;
import loop.theliwala.adapter.BasketInnerAdapter;
import loop.theliwala.database.DbHelper;
import loop.theliwala.models.Data;
import loop.theliwala.models.MyBasket;
import loop.theliwala.utilities.CompatibilityUtility;
import loop.theliwala.utilities.FontManager;


public class MyBasketFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // TODO: Rename and change types and number of parameters
    public static MyBasketFragment newInstance(String param1, String param2) {
        MyBasketFragment fragment = new MyBasketFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private Typeface materialDesignIcons, medium, regular, bold;
    TextView arrow_icon, tv_continue, checkOutIcon,qty,price;
    LinearLayout layout_basket, continuelayout, checkoutLayout;
    private Context context;
    View view;
    //    private double totalPrice = 0;
    private List<MyBasket> basketdata;
    BasketInnerAdapter adapter;
    float sPrice, quntity, qtyP, totalPrice;

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
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.fragment_my_basket, container, false);
        initViews();//initilization........
        return view;
    }


    //set the icons
    private void setIcon() {
        arrow_icon.setTypeface(materialDesignIcons);
        arrow_icon.setText(Html.fromHtml("&#xf054;"));
        tv_continue.setTypeface(medium);
        // tv_yourOrder.setTypeface(bold);
    }

    //initilization........
    private void initViews() {
        materialDesignIcons = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        medium = FontManager.getFontTypeface(context, "fonts/roboto.medium.ttf");
        regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        bold = FontManager.getFontTypeface(context, "fonts/roboto.bold.ttf");
        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreenTitle("My Basket");
        rootActivity.setScreencart(false);
        rootActivity.setScreenSave(false);
        rootActivity.setScreenFavourite(false);
        rootActivity.setScreenLocation(false);
        rootActivity.setScreenCartDot(false);
        arrow_icon = view.findViewById(R.id.arrow_icon);
        tv_continue = view.findViewById(R.id.tv_continue);
        //tv_yourOrder = (TextView) view.findViewById(R.id.tv_yourOrder);
        continuelayout = view.findViewById(R.id.continuelayout);
        layout_basket = view.findViewById(R.id.layout_basket);
        checkoutLayout = view.findViewById(R.id.checkoutLayout);
        checkOutIcon = view.findViewById(R.id.checkOutIcon);
        qty = view.findViewById(R.id.qty);
        price = view.findViewById(R.id.price);
        checkOutIcon.setTypeface(materialDesignIcons);
        checkOutIcon.setText(Html.fromHtml("&#xf054;"));
        continuelayout.setOnClickListener(this);
        checkoutLayout.setOnClickListener(this);
        setIcon();//set the icons
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        DbHelper dbHelper = new DbHelper(context);
        List<MyBasket> orderList = dbHelper.GetAllBasketOrderData();
//        List<Data> orderList = dbHelper.getAllSelectedStoreData();
        if (orderList != null && orderList.size() > 0) {
            checkoutLayout.setVisibility(View.VISIBLE);
            adapter = new BasketInnerAdapter(context, orderList);
            recyclerView.setAdapter(adapter);
            checkBasketExistsDataOrNot();

        } else {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.no_data_found, null);
            TextView nodataIcon = (TextView) view.findViewById(R.id.nodataIcon);
            TextView nodata = (TextView) view.findViewById(R.id.nodata);
            nodata.setTypeface(regular);
            nodataIcon.setTypeface(materialDesignIcons);
            nodataIcon.setText(Html.fromHtml("&#xf077;"));
            nodata.setText("Your Basket is Empty");
            layout_basket.setGravity(Gravity.CENTER);
            layout_basket.removeAllViews();
            layout_basket.addView(view);
            continuelayout.setVisibility(View.VISIBLE);
            checkoutLayout.setVisibility(View.GONE);
        }
    }

    //notifiy adapter if data delete
    private void notifiyAdapter() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();

        }
    }

    // check basket exists then show layout.
    public void checkBasketExistsDataOrNot() {
        DbHelper dbHelper = new DbHelper(context);
        List<MyBasket> myBasket = dbHelper.GetAllBasketOrderData();
        if (myBasket != null && myBasket.size() > 0) {
            //for no of added item in basket
            quntity = 0;
            sPrice = 0;
            totalPrice = 0;
            qtyP = 0;
            for (int i = 0; i < myBasket.size(); i++) {
                MyBasket newListData = dbHelper.getBasketOrderData(myBasket.get(i).getProductId());
                quntity = newListData.getQuantity();
                sPrice = newListData.getPrice();
                qtyP = qtyP + (sPrice * quntity);
            }
            totalPrice = totalPrice + qtyP;
            DecimalFormat format = new DecimalFormat("#.00");
//            qty.setText(format.format(quntity));
//            price.setText("\u20B9"+format.format(totalPrice));
        }
    }

    //get message from basketinneradapter for notify adapter
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("basketItem")) {
                boolean basketFlag = intent.getBooleanExtra("basketFlag", false);
                //Do whatever you want with the code here
                if (basketFlag) {
                    notifiyAdapter();
                }
            }
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, new IntentFilter("basketItem"));
        super.onResume();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.continuelayout:
                setUpHomeFragment();
                break;
            case R.id.checkoutLayout:
                UserAddressFragment fragment = UserAddressFragment.newInstance(true, 0);
                moveFragment(fragment);
                break;
        }
    }

    //open home fragement
    private void setUpHomeFragment() {
        Fragment fragment;
        fragment = HomeFragment.newInstance(0, "");
        moveFragment(fragment);
    }

    //move to fragment
    public void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
