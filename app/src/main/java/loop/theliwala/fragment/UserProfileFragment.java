package loop.theliwala.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import loop.theliwala.R;
import loop.theliwala.activity.DashboardActivity;
import loop.theliwala.activity.LoginActivity;
import loop.theliwala.database.DbHelper;
import loop.theliwala.models.Data;
import loop.theliwala.myalert.SweetAlertDialog;
import loop.theliwala.utilities.CompatibilityUtility;
import loop.theliwala.utilities.FontManager;

/**
 * Created by LALIT on 8/11/2017.
 */

public class UserProfileFragment extends Fragment implements View.OnClickListener {
    private String mParam1;
    private String mParam2;

    // TODO: Rename and change types and number of parameters
    public static UserProfileFragment newInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("param1");
            mParam2 = getArguments().getString("param2");
        }
    }

    TextView tv_UserName, tv_UserPhone, tv_order_icon, tv_orders, tv_address, tv_address_icon, tv_logout_icon, tv_logout,
            textView_UserEmail, tv_editProfile;
    private Context context;
    private ImageView imageView;
    private LinearLayout layout_orders, layout_address, logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        if (CompatibilityUtility.isTablet(context)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        init(view);
        return view;
    }

    // initilization ...............
    private void init(View view) {
        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreencart(true);
        rootActivity.setItemCart();
        rootActivity.setScreenSave(false);
        rootActivity.setScreenFavourite(false);
        rootActivity.setScreenLocation(false);
        rootActivity.setScreenTitle("Profile");
        tv_UserName = (TextView) view.findViewById(R.id.textView_UserName);
        tv_UserPhone = (TextView) view.findViewById(R.id.textView_UserPhoneNo);
        textView_UserEmail = (TextView) view.findViewById(R.id.textView_UserEmail);
        tv_editProfile = (TextView) view.findViewById(R.id.tv_editProfile);
        tv_order_icon = (TextView) view.findViewById(R.id.tv_order_icon);
        tv_orders = (TextView) view.findViewById(R.id.tv_orders);
        tv_address = (TextView) view.findViewById(R.id.tv_address);
        tv_address_icon = (TextView) view.findViewById(R.id.tv_address_icon);
        tv_logout_icon = (TextView) view.findViewById(R.id.tv_logout_icon);
        tv_logout = (TextView) view.findViewById(R.id.tv_logout);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        layout_orders = (LinearLayout) view.findViewById(R.id.layout_orders);
        layout_address = (LinearLayout) view.findViewById(R.id.layout_address);
        logout = (LinearLayout) view.findViewById(R.id.logout);
        setIcon();
        setValue();
        tv_editProfile.setOnClickListener(this);// set onclick....
        layout_orders.setOnClickListener(this);// set onclick....
        layout_address.setOnClickListener(this);// set onclick....
        logout.setOnClickListener(this);// set onclick....
    }

    //set icon
    private void setIcon() {
        Typeface medium = FontManager.getFontTypeface(context, "fonts/roboto.medium.ttf");
        Typeface regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(getActivity(), "fonts/materialdesignicons-webfont.otf");
        tv_UserName.setTypeface(medium);
        tv_UserPhone.setTypeface(medium);
        textView_UserEmail.setTypeface(medium);
        tv_orders.setTypeface(medium);
        tv_address.setTypeface(medium);
        tv_logout.setTypeface(medium);
        tv_order_icon.setTypeface(materialdesignicons_font);
        tv_editProfile.setTypeface(materialdesignicons_font);
        tv_address_icon.setTypeface(materialdesignicons_font);
        tv_logout_icon.setTypeface(materialdesignicons_font);
        tv_editProfile.setText(Html.fromHtml("&#xf3eb;"));
        tv_order_icon.setText(Html.fromHtml("&#xf279;"));
        tv_address_icon.setText(Html.fromHtml("&#xf5f8;"));
        tv_logout_icon.setText(Html.fromHtml("&#xf259;"));
    }


    //set user value
    private void setValue() {
        DbHelper dbHelper = new DbHelper(context);
//        Data data = dbHelper.getUserData();
//        if (data != null) {
//            tv_UserName.setText(data.getName());
//            tv_UserPhone.setText(data.getPhone());
//            textView_UserEmail.setText(data.getEmailID());
//            String url = data.getProfilePictureUrl();
//            if (url != null && !url.equals("")) {
//                Picasso.get().load(url).placeholder(R.drawable.profile_image).into(imageView);
//            }
//        }
    }

    //onclick...............
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_editProfile://update profile........
                UpdateProfileFragment fragment = UpdateProfileFragment.newInstance("", "");
                moveFragment(fragment);
                break;
            case R.id.layout_orders:
                OrdersFragment orderFragment = OrdersFragment.newInstance("", "");
                moveFragment(orderFragment);
                break;
            case R.id.layout_address:
                UserAddressFragment addressFragment = UserAddressFragment.newInstance(false, 0);
                moveFragment(addressFragment);
                break;
            case R.id.logout:
                logout();
                break;
        }
    }

    //user logout.................
    private void logout() {
        // delete user details from database............
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be logout")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.setTitleText("Logout!")
                                .setContentText("Your are Logout Successfully!")
                                .setConfirmText("OK")
                                .showCancelButton(false)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        sDialog.dismiss();
                        // logout
                        int localityId = 0;
                        SharedPreferences locationPrefs = context.getSharedPreferences("LocationPreferences", Context.MODE_PRIVATE);
                        // localityId = locationPrefs.getInt("LocalityId", 0);
                        SharedPreferences.Editor editor = locationPrefs.edit();
                        editor.clear();
                        editor.apply();
                        SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = sharedPreferences.edit();
                        editor1.clear();
                        editor1.apply();
//                        dbHelper.deleteUserData();
//                        dbHelper.deleteAllAddressData();
//                        dbHelper.deleteFavouriteStoreData();
//                        dbHelper.deleteAllBasketOrderData();
//                        dbHelper.deleteMyAllOrderHistoryData();
//                        dbHelper.deleteTrackOrderData();
//                        dbHelper.deleteSelectedStoreData();
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .show();
    }

    //move to fragment
    public void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
