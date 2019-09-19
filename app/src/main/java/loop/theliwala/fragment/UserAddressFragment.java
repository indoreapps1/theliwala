package loop.theliwala.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.List;

import loop.theliwala.R;
import loop.theliwala.activity.DashboardActivity;
import loop.theliwala.adapter.ViewPagerAdapter;
import loop.theliwala.utilities.CompatibilityUtility;


public class UserAddressFragment extends Fragment implements TabHost.OnTabChangeListener {
    private Boolean navigateFlag;
    private int storeId;

    public static UserAddressFragment newInstance(Boolean flag, int storeId) {
        UserAddressFragment fragment = new UserAddressFragment();
        Bundle args = new Bundle();
        args.putBoolean("NavigateFlag", flag);
        args.putInt("StoreId", storeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            navigateFlag = getArguments().getBoolean("NavigateFlag");
            storeId = getArguments().getInt("StoreId");
        }
    }

    private Context context;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    LinearLayout indicator;
    View view;
    int addressId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        //check orientation..........................
        if (CompatibilityUtility.isTablet(context)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.fragment_user_address, container, false);
        init();//initaization..........................
        return view;
    }

    //....................................initaization..........................//
    public void init() {
        //indicator = (LinearLayout) view.findViewById(R.id.indicator);
        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreencart(true);
        rootActivity.setScreenSave(false);
        rootActivity.setScreenFavourite(false);
        rootActivity.setScreenLocation(false);
        rootActivity.setItemCart();
        rootActivity.setScreenTitle("Delivery Address");
        //rootActivity.viewpagerindicator();
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        //..................... set tab layout.......................//
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager();
    }

    @Override
    public void onResume() {
        getActivity().registerReceiver(mReceiverLocation, new IntentFilter("change_tab"));
        super.onResume();
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(mReceiverLocation);
        super.onPause();
    }

    private BroadcastReceiver mReceiverLocation = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Boolean flag = intent.getBooleanExtra("flag", true);
                addressId = intent.getIntExtra("addressid", 0);
                String address = intent.getStringExtra("flag");
                if (flag) {
                    viewPager.setCurrentItem(1);
                    ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
                    adapter.addFragment(new UserAddressListFragment().newInstance(navigateFlag, storeId), "Saved");
                    adapter.addFragment(new AddNewAddressFragment().newInstance(addressId, true), "Add New");
                    viewPager.setAdapter(adapter);
                    viewPager.setCurrentItem(1);
                } else {
                    ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
                    adapter.addFragment(new UserAddressListFragment().newInstance(navigateFlag, storeId), "Saved");
                    adapter.addFragment(new AddNewAddressFragment().newInstance(0, false), "Add New");
                    viewPager.setAdapter(adapter);
                    viewPager.setCurrentItem(1);
                }
                if (address.equalsIgnoreCase("newAddress")) {
                    viewPager.setCurrentItem(0);
                } else if (address.equalsIgnoreCase("updateAddress")) {
                    viewPager.setCurrentItem(0);
                }
                // Log.e("data", flag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    // ..................set view pager.........................
    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new UserAddressListFragment().newInstance(navigateFlag, storeId), "Saved");
        adapter.addFragment(new AddNewAddressFragment().newInstance(0, false), "Add New");
        viewPager.setAdapter(adapter);
    }


    @Override
    public void onTabChanged(String s) {
        tabLayout.getRootView().setBackgroundColor(Color.GREEN);
    }
}





