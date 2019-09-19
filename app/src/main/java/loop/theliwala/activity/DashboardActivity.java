package loop.theliwala.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

import loop.theliwala.R;
import loop.theliwala.adapter.NavMenuCustomAdapter;
import loop.theliwala.database.DbHelper;
import loop.theliwala.fragment.MyBasketFragment;
import loop.theliwala.fragment.OrdersFragment;
import loop.theliwala.fragment.HomeFragment;
import loop.theliwala.fragment.UserProfileFragment;
import loop.theliwala.framework.IAsyncWorkCompletedCallback;
import loop.theliwala.framework.ServiceCaller;
import loop.theliwala.models.Data;
import loop.theliwala.models.MyBasket;
import loop.theliwala.myalert.SweetAlertDialog;
import loop.theliwala.utilities.CompatibilityUtility;
import loop.theliwala.utilities.Contants;
import loop.theliwala.utilities.FontManager;
import loop.theliwala.utilities.Utility;

import static loop.theliwala.utilities.Utility.isOnline;


public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    ListView list_menuitem;
    private Boolean CheckOrientation = false;
    public TextView title, cart, save, location, favourite, cart_dot, tv_profile, logout, tv_username, tv_userphone;
    private Typeface regular, materialdesignicons_font, medium;
    DrawerLayout drawer;
    private LinearLayout logoutLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chechPortaitAndLandSacpe();//chech Portait And LandSacpe Orientation
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        init(toolbar);
        setIcons();
        Intent intent = getIntent();
        int NavigateValue = intent.getIntExtra("NavigateFlag", 0);
        String orderNumber = intent.getStringExtra("orderNumber");
        int orderId = intent.getIntExtra("orderId", 0);
        int storeId = intent.getIntExtra("storeId", 0);
        int LoginId = intent.getIntExtra("LoginId", 0);
        String storeName = intent.getStringExtra("storeName");
        if (NavigateValue == 1) {//come from payment and notification  onclick
            OrdersFragment fragment = OrdersFragment.newInstance("", "");
            moveFragment(fragment);
        } else {
            setUpDashboardFragment();
        }
        getUserProfileService();//get user profile
        setItemCart();// add item in cart...........
        Listmenu();//list menu
        GetAllCitiesService();
        setUserDetail();
        checkForceUpdate();

    }

    public void checkForceUpdate() {

        new AsyncTask<Void, String, String>() {
            @Override
            protected String doInBackground(Void... voids) {

                String newVersion = null;
                try {
                    newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + DashboardActivity.this.getPackageName() + "&hl=it")
                            .timeout(30000)
                            .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                            .referrer("http://www.google.com")
                            .get()
                            .select("div[itemprop=softwareVersion]")
                            .first()
                            .ownText();
                    return newVersion;
                } catch (Exception e) {
                    return newVersion;
                }
            }

            @Override
            protected void onPostExecute(String onlineVersion) {
                super.onPostExecute(onlineVersion);
                if (onlineVersion != null && !onlineVersion.isEmpty()) {
                    if (Float.valueOf(Utility.getAppVersionName(DashboardActivity.this)) < Float.valueOf(onlineVersion)) {
                        //show dialog
                        showUpdateDialog(DashboardActivity.this);
                    } else {
                        //  MoveNextScreen();
                    }
                }

                Log.d("update", "Current version " + Utility.getAppVersionName(DashboardActivity.this) + "playstore version " + onlineVersion);
            }
        }.execute();
    }

    private void showUpdateDialog(final Context context) {
        //alert for error message

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        Typeface robotoRegular = Typeface.createFromAsset(context.getAssets(), "fonts/roboto.regular.ttf");
        Typeface robotoMedium = Typeface.createFromAsset(context.getAssets(), "fonts/roboto.medium.ttf");
        final AlertDialog alert = builder.create();
        View view = alert.getLayoutInflater().inflate(R.layout.custom_update_alert, null);
        TextView title = (TextView) view.findViewById(R.id.textMessage);
        TextView title2 = (TextView) view.findViewById(R.id.textMessage2);
        title.setTypeface(robotoMedium);
        title2.setTypeface(robotoRegular);
        Button ok = (Button) view.findViewById(R.id.buttonUpdate);
        Button buttonCancel = (Button) view.findViewById(R.id.buttonCancel);
        ok.setTypeface(robotoMedium);
        buttonCancel.setTypeface(robotoMedium);
        alert.setCustomTitle(view);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + "microsysindia.sirjohn" + "&hl=en"));
                context.startActivity(intent);
                alert.dismiss();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                //MoveNextScreen();
            }
        });
        alert.show();
        alert.setCanceledOnTouchOutside(false);
    }

    public void setUserDetail() {
        SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt("userId", 0);
        if (id != 0) {
            showLogin(false);
//            tv_userphone.setText(data.getPhone());
//            tv_username.setText(data.getName());
            //String url = data.getProfilePictureUrl();
            // Picasso.with(this).load(url).placeholder(R.drawable.profile_image).into(profileImage);
        } else {
            showLogin(true);
        }
    }

    //add item in  cart or not.........
    public void setItemCart() {
        DbHelper dbHelper = new DbHelper(this);
        List<MyBasket> myBasket = dbHelper.GetAllBasketOrderData();
        if (myBasket != null && myBasket.size() > 0) {
            //for no of added item in basket
            cart_dot.setText(String.valueOf(myBasket.size()));
            cart_dot.setVisibility(View.VISIBLE);
        } else {
            cart_dot.setVisibility(View.GONE);
        }
    }

    //set icons........
    private void setIcons() {
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(this, "fonts/materialdesignicons-webfont.otf");
        medium = FontManager.getFontTypeface(this, "fonts/roboto.medium.ttf");
        regular = FontManager.getFontTypeface(this, "fonts/roboto.regular.ttf");
        title.setTypeface(medium);
        save.setTypeface(regular);
        cart.setTypeface(materialdesignicons_font);
        cart.setText(Html.fromHtml("&#xf111;"));
        location.setTypeface(materialdesignicons_font);
        location.setText(Html.fromHtml("&#xf34e;"));
        favourite.setTypeface(materialdesignicons_font);
        favourite.setText(Html.fromHtml("&#xf2d1;"));
        logout.setTypeface(regular);
        //LogoutIcon.setTypeface(materialdesignicons_font);
    }

    private void init(Toolbar toolbar) {
        title = (TextView) toolbar.findViewById(R.id.title);
        cart = (TextView) toolbar.findViewById(R.id.cart);
        save = (TextView) toolbar.findViewById(R.id.save);
        location = (TextView) toolbar.findViewById(R.id.location);
        favourite = (TextView) toolbar.findViewById(R.id.favourite);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_userphone = (TextView) findViewById(R.id.tv_userphone);
        tv_profile = (TextView) findViewById(R.id.tv_profile);
        logout = (TextView) findViewById(R.id.logout);
//        LogoutIcon = (TextView) findViewById(R.id.LogoutIcon);
//        logoutLayout = (LinearLayout) findViewById(R.id.logoutLayout);
//        logoutLayout.setOnClickListener(this);
//        phoneNumber = (TextView) findViewById(R.id.phoneNumber);
        cart_dot = (TextView) findViewById(R.id.cart_dot);
        logout.setOnClickListener(this);
        tv_profile.setOnClickListener(this);
        cart.setOnClickListener(this);
        location.setOnClickListener(this);
    }

    //chech Portait And LandSacpe Orientation
    public void chechPortaitAndLandSacpe() {
        if (CompatibilityUtility.isTablet(DashboardActivity.this)) {
            CheckOrientation = true;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            CheckOrientation = false;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    //open default fragment
    private void setUpDashboardFragment() {
        Fragment fragment;
        fragment = HomeFragment.newInstance(0, "");
        moveFragment(fragment);
    }

    private void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                //.addToBackStack(null)
                .commit();
    }

    //list in menu..
    private void Listmenu() {
        list_menuitem = (ListView) findViewById(R.id.list_menu);
        List<String> icondatalist = new ArrayList<>();
        icondatalist.add(String.valueOf(Html.fromHtml("&#xf2dc;")));
        // icondatalist.add(String.valueOf(Html.fromHtml("&#xf09c;")));
//        icondatalist.add(String.valueOf(Html.fromHtml("&#xF34e;")));
//        icondatalist.add(String.valueOf(Html.fromHtml("&#xf4ce;")));
        icondatalist.add(String.valueOf(Html.fromHtml("&#xf5f8;")));
        //icondatalist.add(String.valueOf(Html.fromHtml("&#xf53e;")));
        icondatalist.add(String.valueOf(Html.fromHtml("&#xf279;")));
        icondatalist.add(String.valueOf(Html.fromHtml("&#xF076;")));
        //icondatalist.add(String.valueOf(Html.fromHtml("&#xf004;")));
        List<String> stringList = new ArrayList<>();
        stringList.add("Home");
        // stringList.add("Notifications");
//        stringList.add("Change Location");
//        stringList.add("Favourite Stores");
        stringList.add("Saved Addresses");
        // stringList.add("Track My Order");
        stringList.add("Orders");
        stringList.add("Basket");
        // stringList.add("My Profile");
        //..................
        NavMenuCustomAdapter custom_adapter = new NavMenuCustomAdapter(DashboardActivity.this, stringList, icondatalist);
        list_menuitem.setAdapter(custom_adapter);
    }

    public void showLogin(boolean showALogin) {
        if (showALogin) {
            logout.setText("Login");
        } else {
            logout.setText("Logout");
        }
    }

    // set titiles........
    public void setScreenTitle(String heading) {
        this.setTitle(heading);
        title.setText(heading);
    }

    // set card icon.....
    public void setScreencart(boolean screencart) {
        if (screencart) {
            this.cart.setVisibility(View.VISIBLE);
        } else {
            this.cart.setVisibility(View.GONE);
        }
    }

    //set save ....
    public void setScreenSave(boolean screenSave) {
        if (screenSave) {
            this.save.setVisibility(View.VISIBLE);
        } else {
            this.save.setVisibility(View.GONE);
        }


    }

    //set location ....
    public void setScreenLocation(boolean screenLocation) {
        if (screenLocation) {
            this.location.setVisibility(View.VISIBLE);
        } else {
            this.location.setVisibility(View.GONE);
        }


    }

    //set location ....
    public void setScreenFavourite(boolean screenFavourite) {
//        if (screenFavourite) {
//            this.favourite.setVisibility(View.VISIBLE);
//        } else {
        this.favourite.setVisibility(View.GONE);
//        }
    }

    //set cart dot....
    public void setScreenCartDot(boolean screenCartDot) {
        if (screenCartDot) {
            this.cart_dot.setVisibility(View.VISIBLE);
        } else {
            this.cart_dot.setVisibility(View.GONE);
        }


    }


    @Override
    public void onBackPressed() {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag("OrderPlacedFragment");
        if (fragment != null) {//order placed done so start flow again
            Intent intent = new Intent(DashboardActivity.this, DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    //get user profile
    private void getUserProfileService() {
        if (isOnline(DashboardActivity.this)) {
            SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
            int id = sharedPreferences.getInt("userId", 0);
            if (id != 0) {
                ServiceCaller serviceCaller = new ServiceCaller(DashboardActivity.this);
                serviceCaller.getUserProfileService(id, new IAsyncWorkCompletedCallback() {
                    @Override
                    public void onDone(String workName, boolean isComplete) {
                    }
                });
            }
        }
    }

    //hide navigation view
    public void NavHide() {
        drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //for hid keyboard when tab outside edittext box
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cart:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new MyBasketFragment()).addToBackStack(null).commit();
                break;
            case R.id.location:
//                getSupportFragmentManager().beginTransaction().replace(R.id.container, new LocationFragment()).addToBackStack(null).commit();
                break;
            case R.id.logout:
                logout();
                break;
            case R.id.tv_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new UserProfileFragment()).addToBackStack(null).commit();
                NavHide();
                break;
        }
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt("userId", 0);
        if (id != 0) {
            deleteUserData();// delete user details from database............
        } else {
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));// move login activity.....
        }
    }

    //    GetAllCities api
    private void GetAllCitiesService() {

        if (isOnline(DashboardActivity.this)) {
            ServiceCaller serviceCaller = new ServiceCaller(DashboardActivity.this);
            serviceCaller.callGetCitiesDataService(Contants.LIST_PAGE_SIZE, 1, new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String workName, boolean isComplete) {

                }
            });
        }
    }

    // delete user details from database............
    private void deleteUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt("userId", 0);
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
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
                        SharedPreferences locationPrefs = getSharedPreferences("LocationPreferences", Context.MODE_PRIVATE);
                        // localityId = locationPrefs.getInt("LocalityId", 0);
                        SharedPreferences.Editor editor = locationPrefs.edit();
                        editor.clear();
                        editor.apply();
                        SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = sharedPreferences.edit();
                        editor1.clear();
                        editor1.apply();
//                        dbHelper.deleteAllAddressData();
//                        dbHelper.deleteFavouriteStoreData();
//                        dbHelper.deleteAllBasketOrderData();
//                        dbHelper.deleteMyAllOrderHistoryData();
//                        dbHelper.deleteTrackOrderData();
//                        dbHelper.deleteSelectedStoreData();
                        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        showLogin(true);
                    }
                })
                .show();

//        new AlertDialog.Builder(DashboardActivity.this)
//                .setTitle("Logout")
//                .setCancelable(false)
//                .setMessage("Would you like to Logout?")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        LoginManager.getInstance().logOut();//for facebook logout
//                        // logout
//                        int localityId = 0;
//                        SharedPreferences locationPrefs = getSharedPreferences("LocationPreferences", Context.MODE_PRIVATE);
//                        // localityId = locationPrefs.getInt("LocalityId", 0);
//                        SharedPreferences.Editor editor = locationPrefs.edit();
//                        editor.clear();
//                        editor.apply();
//                        dbHelper.deleteUserData(loginId);
//                        dbHelper.deleteAllAddressData();
//                        dbHelper.deleteFavouriteStoreData();
//                        dbHelper.deleteAllBasketOrderData();
//                        dbHelper.deleteMyAllOrderHistoryData();
//                        dbHelper.deleteTrackOrderData();
//                        dbHelper.deleteSelectedStoreData();
//                        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        showLogin(true);
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // user doesn't want to logout
//                        dialog.dismiss();
//                    }
//                })
//                .show();
    }
}
