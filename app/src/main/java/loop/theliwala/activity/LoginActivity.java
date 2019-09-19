package loop.theliwala.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import loop.theliwala.R;
import loop.theliwala.ballsynccustomprogress.BallTriangleSyncDialog;
import loop.theliwala.framework.IAsyncWorkCompletedCallback;
import loop.theliwala.framework.ServiceCaller;
import loop.theliwala.myalert.SweetAlertDialog;
import loop.theliwala.utilities.CompatibilityUtility;
import loop.theliwala.utilities.FontManager;
import loop.theliwala.utilities.Utility;


/**
 * Created by LALIT on 8/9/2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_proceed, phone_icon, SeeMenu;
    Typeface materialdesignicons_font, medium, regular, italic, bold;
    EditText edt_phone;
    private Boolean CheckOrientation = false;
    String phone;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private static final int RC_SIGN_IN = 234;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);// Removes action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);// Removes title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_login);
        chechPortaitAndLandSacpe();//chech Portait And LandSacpe Orientation
        init();//component intilization
        setIcon();//set icon in textview
    }

    //chech Portait And LandSacpe Orientation
    public void chechPortaitAndLandSacpe() {
        if (CompatibilityUtility.isTablet(LoginActivity.this)) {
            CheckOrientation = true;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            CheckOrientation = false;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    //set icons
    private void setIcon() {
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(this, "fonts/materialdesignicons-webfont.otf");
        phone_icon.setTypeface(materialdesignicons_font);
        phone_icon.setText(Html.fromHtml("&#xf3f2;"));
        medium = FontManager.getFontTypeface(this, "fonts/roboto.medium.ttf");
        regular = FontManager.getFontTypeface(this, "fonts/roboto.regular.ttf");
        italic = FontManager.getFontTypeface(this, "fonts/roboto.italic.ttf");
        bold = FontManager.getFontTypeface(this, "fonts/roboto.bold.ttf");
        tv_proceed.setTypeface(medium);
        edt_phone.setTypeface(regular);
        SeeMenu.setTypeface(medium);
    }

    //intilization.............
    private void init() {
        // for gmail initilization..................
        tv_proceed = findViewById(R.id.tv_proceed);
        phone_icon = findViewById(R.id.phone_icon);
        edt_phone = findViewById(R.id.edt_phone);
        SeeMenu = (TextView) findViewById(R.id.tv_see_menu_text);
        tv_proceed.setOnClickListener(this);
        SeeMenu.setOnClickListener(this);
    }

    //onclick................
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_proceed:
                loginWithPhone();//sign up with phone method.......
                break;
            case R.id.tv_see_menu_text:
                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                break;
        }
    }


    //sign up with phone method.......
    private void loginWithPhone() {
        phone = edt_phone.getText().toString();
        if (validation()) {
            //check internet connection
            if (Utility.isOnline(this)) {
                final BallTriangleSyncDialog dotDialog = new BallTriangleSyncDialog(LoginActivity.this);
                dotDialog.show();
                ServiceCaller serviceCaller = new ServiceCaller(this);
                serviceCaller.callLoginService(phone, new IAsyncWorkCompletedCallback() {
                    @Override
                    public void onDone(String workName, boolean isComplete) {
                        if (isComplete) {
                            Toasty.success(LoginActivity.this, "Otp Send SuccessFully").show();
                            Intent intent = new Intent(LoginActivity.this, OTPVerifyActivity.class);
                            intent.putExtra("phone", phone);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Sorry...")
                                    .setContentText("Something went wrong On Server Please Try Again!")
                                    .show();
                            //Utility.alertForErrorMessage(Contants.Dont_SEND_MESSAGE, LoginActivity.this);
                        }
                        if (dotDialog.isShowing()) {
                            dotDialog.dismiss();
                        }
                    }
                });
            } else {
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("You are Offline. Please check your Internet Connection.")
                        .show();
                // Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, this);//off line msg....
            }
        }
    }

    //validation for phone
    private boolean validation() {
        //  String phone = edt_phone.getText().toString();
        if (phone.isEmpty()) {
            edt_phone.setError("Please Enter Mobile Number");
            return false;
        } else if (phone.length() != 10) {
            edt_phone.setError("Please Enter Valid  Mobile Number 10 Digits");
            return false;
        }
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
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    //selectImage();
                } else {
                    // Permission Denied
                    Toast.makeText(LoginActivity.this, "Permission is Denied", Toast.LENGTH_SHORT)
                            .show();

                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
