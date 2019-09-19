package loop.theliwala.fragment;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import loop.theliwala.R;
import loop.theliwala.activity.DashboardActivity;
import loop.theliwala.adapter.StoreDetailAdapter;
import loop.theliwala.database.DbHelper;
import loop.theliwala.models.Data;
import loop.theliwala.utilities.CompatibilityUtility;
import loop.theliwala.utilities.FontManager;

/**
 * Created by LALIT on 8/14/2017.
 */

public class StoreDetailsFragement extends Fragment implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private int storeId;
    private String mParam2;

    // TODO: Rename and change types and number of parameters
    public static StoreDetailsFragement newInstance(int storeId, String param2) {
        StoreDetailsFragement fragment = new StoreDetailsFragement();
        Bundle args = new Bundle();
        args.putInt("StoreId", storeId);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            storeId = getArguments().getInt("StoreId");
            mParam2 = getArguments().getString("param2");
        }
    }

    View view;
    TextView address, phone, email, memu_icon, storeName;
    StoreDetailAdapter mAdapter;
    Typeface materialdesignicons_font, regular, medium;
    private ViewPager viewPager;
    private TextView tv_createOrder, btnFinish, tv_address, tv_phone, tv_email;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private TextView[] dots;
    private LinearLayout createOrder;
    private ArrayList<Integer> store_imageList;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        if (CompatibilityUtility.isTablet(context)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        view = inflater.inflate(R.layout.fragment_store_details, container, false);
        init();    //initilization.................
        return view;
    }

    //set icons .....................
    public void setIcons() {
        medium = FontManager.getFontTypeface(context, "fonts/roboto.medium.ttf");
        regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(getActivity(), "fonts/materialdesignicons-webfont.otf");
        address.setTypeface(materialdesignicons_font);
        address.setText(Html.fromHtml("&#xf4dc;"));
        phone.setTypeface(materialdesignicons_font);
        phone.setText(Html.fromHtml("&#xf3f2;"));
        email.setTypeface(materialdesignicons_font);
        email.setText(Html.fromHtml("&#xf1f0;"));
        memu_icon.setTypeface(materialdesignicons_font);
        memu_icon.setText(Html.fromHtml("&#xf4a3;"));
        tv_email.setTypeface(regular);
        tv_phone.setTypeface(regular);
        tv_address.setTypeface(regular);
        tv_createOrder.setTypeface(medium);
    }

    //initilization.................
    public void init() {
        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreenTitle("Store Detail");
        rootActivity.setScreencart(true);
        rootActivity.setItemCart();
        rootActivity.setScreenSave(false);
        storeName = (TextView) view.findViewById(R.id.storeName);
        address = (TextView) view.findViewById(R.id.address);
        phone = (TextView) view.findViewById(R.id.phone);
        email = (TextView) view.findViewById(R.id.email);
        tv_address = (TextView) view.findViewById(R.id.tv_address);
        tv_phone = (TextView) view.findViewById(R.id.tv_phone);
        tv_email = (TextView) view.findViewById(R.id.tv_email);
        tv_createOrder = (TextView) view.findViewById(R.id.tv_createOrder);
        createOrder = (LinearLayout) view.findViewById(R.id.createOrder);
        createOrder.setOnClickListener(this);
        memu_icon = (TextView) view.findViewById(R.id.memu_icon);
        store_imageList = new ArrayList<Integer>();
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        pager_indicator = (LinearLayout) view.findViewById(R.id.viewPagerCountDots);
        setIcons();
        setValueIntoList();

    }

    //set value based on tab or phone
    private void setValueIntoList() {
        store_imageList.add(R.drawable.store);
        store_imageList.add(R.drawable.store);
        DbHelper dbHelper = new DbHelper(getActivity());
        Data storeData = dbHelper.getStoreData(storeId);
        if (storeData != null) {
            storeName.setText(storeData.getStoreName());
            String address = storeData.getStoreAddress();
            String phone = storeData.getStorePhoneNumber();
            String email = storeData.getStoreEmailId();
            if (address != null && phone != null & email != null) {
                tv_address.setText(address);
                tv_phone.setText(phone);
                tv_email.setText(email);
            } else {
                tv_address.setText(R.string.store_address_detail);
                tv_phone.setText(R.string.store_contact_number_detail);
                tv_email.setText(R.string.store_email_address_detail);
            }

        }
        mAdapter = new StoreDetailAdapter(getActivity(), store_imageList);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(this);
        setUiPageViewController();
    }

    //set view ...............
    private void setUiPageViewController() {
        dotsCount = mAdapter.getCount();
        dots = new TextView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new TextView(getActivity());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(40);
            dots[i].setTextColor(Color.parseColor("#b3b3b3"));
            dots[i].setBackgroundColor(Color.TRANSPARENT);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 8);
            pager_indicator.addView(dots[i], params);
        }
        dots[0].setTextColor(Color.parseColor("#388e3c"));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setTextColor(Color.parseColor("#b2f4b5"));
        }
        dots[position].setTextColor(Color.parseColor("#388e3c"));
        if (position + 1 == dotsCount) {
//            btnNext.setVisibility(View.GONE);
//            btnFinish.setVisibility(View.VISIBLE);

        } else {
//            btnNext.setVisibility(View.VISIBLE);
//            btnFinish.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.createOrder:
                //SelectCategoryFragment fragment = SelectCategoryFragment.newInstance(storeId, "");
                // moveFragment(fragment);
                break;
        }
    }

    private void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
