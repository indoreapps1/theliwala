package loop.theliwala.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;
import loop.theliwala.R;
import loop.theliwala.activity.DashboardActivity;
import loop.theliwala.adapter.ProductListAdapter;
import loop.theliwala.ballsynccustomprogress.BallTriangleSyncDialog;
import loop.theliwala.database.DbHelper;
import loop.theliwala.framework.IAsyncWorkCompletedCallback;
import loop.theliwala.framework.ServiceCaller;
import loop.theliwala.framworkretrofit.ApiClient;
import loop.theliwala.framworkretrofit.ApiInterface;
import loop.theliwala.models.ContentDataAsArray;
import loop.theliwala.models.Data;
import loop.theliwala.models.MyBasket;
import loop.theliwala.utilities.CompatibilityUtility;
import loop.theliwala.utilities.FontManager;
import loop.theliwala.utilities.Utility;
import retrofit2.Call;
import retrofit2.Callback;


public class ProductListFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match

    // TODO: Rename and change types of parameters
    private int categoryId;
    private String cName;


    public ProductListFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ProductListFragment newInstance(int categoryId, String cName) {
        ProductListFragment fragment = new ProductListFragment();
        Bundle args = new Bundle();
        args.putInt("categoryId", categoryId);
        args.putString("cName", cName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getInt("categoryId");
            cName = getArguments().getString("cName");
        }
    }

    private TextView qty, price, rupay_icon, icon_view;
    private LinearLayout linearLayout, layout_itemCart;
    RecyclerView recyclerView;
    private Context context;
    View view;
    private List<Data> productList;
    Typeface materialDesignIcons, bold;
    float sPrice, quntity, qtyP, totalPrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_product_list, container, false);
        if (CompatibilityUtility.isTablet(context)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreencart(true);
        rootActivity.setItemCart();
        rootActivity.setScreenLocation(false);
        rootActivity.setScreenFavourite(false);
        rootActivity.setScreenSave(false);
        materialDesignIcons = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        init();
        getAllProductList();
        return view;
    }

    // initilization............
    private void init() {
        productList = new ArrayList<>();
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
        layout_itemCart = (LinearLayout) view.findViewById(R.id.layout_itemCart);
        qty = (TextView) view.findViewById(R.id.qty);
        price = (TextView) view.findViewById(R.id.price);
        rupay_icon = (TextView) view.findViewById(R.id.rupay_icon);
        icon_view = (TextView) view.findViewById(R.id.icon_view);
        recyclerView = (RecyclerView) view.findViewById(R.id.CategoriesItems_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        // orderPlaceLayout = (LinearLayout) view.findViewById(R.id.orderPlaceLayout);
        //  tv_placeOrder = (TextView) view.findViewById(R.id.tv_placeOrder);
        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreenTitle(cName);
        bold = FontManager.getFontTypeface(context, "fonts/roboto.bold.ttf");
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        rupay_icon.setTypeface(materialdesignicons_font);
        icon_view.setTypeface(materialdesignicons_font);
        rupay_icon.setText(Html.fromHtml("&#xf1af;"));
        icon_view.setText(Html.fromHtml("&#xf572;"));
        // tv_placeOrder.setTypeface(bold);
        layout_itemCart.setOnClickListener(this);
        checkBasketExistsDataOrNot();
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
            layout_itemCart.setVisibility(View.VISIBLE);
            qty.setText(format.format(quntity));
            price.setText(format.format(totalPrice));
        } else {
            layout_itemCart.setVisibility(View.GONE);
        }
    }

    private void noDataFound() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.no_data_found, null);
        TextView nodataIcon = (TextView) view.findViewById(R.id.nodataIcon);
        TextView nodata = (TextView) view.findViewById(R.id.nodata);
        nodataIcon.setTypeface(materialDesignIcons);
        nodataIcon.setText(Html.fromHtml("&#xf187;"));
        nodata.setText("Any product not found for this Category");

        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.removeAllViews();
        linearLayout.addView(view);
    }

    //get all Product list data
    private void getAllProductList() {
        if (Utility.isOnline(context)) {
            final BallTriangleSyncDialog dotDialog = new BallTriangleSyncDialog(context);
            dotDialog.show();
            ApiInterface apiInterface = ApiClient.getAPIClient().create(ApiInterface.class);
            apiInterface.GetAllProduct(categoryId).enqueue(new Callback<ContentDataAsArray>() {
                @Override
                public void onResponse(Call<ContentDataAsArray> call, retrofit2.Response<ContentDataAsArray> response) {
                    ContentDataAsArray contentData = response.body();
                    if (contentData != null) {
                        for (Data result : contentData.getData()) {
                            productList.addAll(Arrays.asList(result));
                        }
                    }
                    if (productList != null && productList.size() > 0) {
                        ProductListAdapter adapter = new ProductListAdapter(context, productList, categoryId, ProductListFragment.this);
                        recyclerView.setAdapter(adapter);
                    } else {
                        noDataFound();
                    }
                    dotDialog.dismiss();
                }

                @Override
                public void onFailure(Call<ContentDataAsArray> call, Throwable t) {
                    noDataFound();
                    dotDialog.dismiss();
                }
            });
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_itemCart:
                if (layout_itemCart.getVisibility() == View.VISIBLE) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new MyBasketFragment()).addToBackStack(null).commit();
                }
                break;
        }
    }
}
