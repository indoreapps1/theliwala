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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TabHost;

import loop.theliwala.R;
import loop.theliwala.activity.DashboardActivity;
import loop.theliwala.adapter.ViewPagerAdapter;
import loop.theliwala.utilities.CompatibilityUtility;


public class OrdersFragment extends Fragment implements TabHost.OnTabChangeListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static OrdersFragment newInstance(String param1, String param2) {
        OrdersFragment fragment = new OrdersFragment();
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
    private Context context;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    View view;
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
        view = inflater.inflate(R.layout.fragment_orders, container, false);
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
        rootActivity.setScreenTitle("Orders");
        //rootActivity.viewpagerindicator();
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        setupViewPager();
        //..................... set tab layout.......................//
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    // ..................set view pager.........................
    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new TrackOrderFragment().newInstance("",""), "Placed Orders");
        adapter.addFragment(new MyAllOrderHistoryFragment().newInstance(0, ""), "Completed Orders");
        viewPager.setAdapter(adapter);
        //call into track order fragment and myall order history fragment

    }


    @Override
    public void onTabChanged(String s) {
        tabLayout.getRootView().setBackgroundColor(Color.GREEN);
    }
}

