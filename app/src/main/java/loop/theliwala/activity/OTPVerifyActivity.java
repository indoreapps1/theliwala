package loop.theliwala.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;

import java.util.List;

import es.dmoral.toasty.Toasty;
import loop.theliwala.R;
import loop.theliwala.ballsynccustomprogress.BallTriangleSyncDialog;
import loop.theliwala.database.DbHelper;
import loop.theliwala.framework.IAsyncWorkCompletedCallback;
import loop.theliwala.framework.ServiceCaller;
import loop.theliwala.models.ContentDataAsArray;
import loop.theliwala.models.Data;
import loop.theliwala.myalert.SweetAlertDialog;
import loop.theliwala.utilities.CompatibilityUtility;
import loop.theliwala.utilities.Contants;
import loop.theliwala.utilities.FontManager;
import loop.theliwala.utilities.Utility;


/**
 * Created by LALIT on 8/9/2017.
 */
public class OTPVerifyActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_proceed, lock_icon, tv_hint_otp;
    Typeface materialdesignicons_font, medium, regular, italic;
    EditText edt_otp;
    private Boolean CheckOrientation = false;
    private String otpData;
    String phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);// Removes action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);// Removes title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_otp_verify);
        chechPortaitAndLandSacpe();//chech Portait And LandSacpe Orientation
        init();//component intilization
        setIcon();//set icon in textview
        phone = getIntent().getStringExtra("phone");
    }

    //chech Portait And LandSacpe Orientation
    public void chechPortaitAndLandSacpe() {
        if (CompatibilityUtility.isTablet(OTPVerifyActivity.this)) {
            CheckOrientation = true;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            CheckOrientation = false;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    //set icon in textview
    private void setIcon() {
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(this, "fonts/materialdesignicons-webfont.otf");
        lock_icon.setTypeface(materialdesignicons_font);
        lock_icon.setText(Html.fromHtml("&#xf341;"));
        medium = FontManager.getFontTypeface(this, "fonts/roboto.medium.ttf");
        regular = FontManager.getFontTypeface(this, "fonts/roboto.regular.ttf");
        italic = FontManager.getFontTypeface(this, "fonts/roboto.italic.ttf");
        tv_proceed.setTypeface(medium);
        edt_otp.setTypeface(regular);


    }

    //component intilization
    private void init() {
        tv_proceed = (TextView) findViewById(R.id.tv_proceed);
        lock_icon = (TextView) findViewById(R.id.lock_icon);
        edt_otp = (EditText) findViewById(R.id.edt_otp);
        tv_proceed.setOnClickListener(this);//set onclick
        // get data from data base........


    }

    //onclick
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_proceed:
                if (validation()) {
                    verifyOTP(otpData);
                }
                break;
        }
    }

    //sign up with phone method.......
    private void resendOtp() {
        //check online.....
        if (Utility.isOnline(this)) {
            final BallTriangleSyncDialog dotDialog = new BallTriangleSyncDialog(OTPVerifyActivity.this);
            dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(this);
            serviceCaller.callLoginService(phone, new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String workName, boolean isComplete) {
                    if (isComplete) {
                        Toasty.success(OTPVerifyActivity.this, "Otp Send SuccessFully").show();
                    } else {
                        new SweetAlertDialog(OTPVerifyActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText(Contants.Dont_SEND_MESSAGE)
                                .show();
                        //  Utility.alertForErrorMessage(Contants.Dont_SEND_MESSAGE, OTPVerifyActivity.this);
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
            //Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, this);//off line msg....
        }

    }

    //validation for phone
    private boolean validation() {
        otpData = edt_otp.getText().toString();
        if (otpData.length() == 0) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Sorry...")
                    .setContentText("Please Enter Otp")
                    .show();
            // Utility.alertForErrorMessage("Please Enter OTP", OTPVerifyActivity.this);
            return false;
        }
        return true;
    }

    //  resend otp....................
    private void verifyOTP(String otp) {
        if (Utility.isOnline(this)) {
            final BallTriangleSyncDialog dotDialog = new BallTriangleSyncDialog(OTPVerifyActivity.this);
            dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(OTPVerifyActivity.this);
            serviceCaller.callOtpVerifiyService(phone, otp, new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String workName, boolean isComplete) {
                    if (isComplete) {
                        if (!workName.trim().equalsIgnoreCase("no")) {
                            ContentDataAsArray contentData = new Gson().fromJson(workName, ContentDataAsArray.class);
                            if (contentData != null) {
                                for (Data result : contentData.getData()) {
                                    if (result.getStatus() == 1) {
                                        SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putInt("userId", result.getLoginId());
                                        editor.apply();
                                        Intent intent = new Intent(OTPVerifyActivity.this, DashboardActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        Toasty.success(OTPVerifyActivity.this, "Thankyou for Register With us").show();
                                    } else {
                                        new SweetAlertDialog(OTPVerifyActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText("Sorry...")
                                                .setContentText(" You can't access Please contact to Admin")
                                                .show();
                                    }
                                }
                            } else {
                                edt_otp.setError("Enter Valid Otp");
                            }
                        } else {
                            new SweetAlertDialog(OTPVerifyActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Sorry...")
                                    .setContentText("Can't Reach to server! or Enter Valid Otp")
                                    .show();
                        }
                    } else {
                        new SweetAlertDialog(OTPVerifyActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Sorry...")
                                .setContentText("Can't Reach to server! Try Again")
                                .show();
                    }

                    if (dotDialog.isShowing()) {
                        dotDialog.dismiss();
                    }
                }

            });

        } else {
            //Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, this);//off line msg....
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("You are Offline. Please check your Internet Connection.")
                    .show();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //check storage and camera run time permission
//    private Boolean checkRuntimePermission() {
//        List<String> permissionsNeeded = new ArrayList<String>();
//
//        final List<String> permissionsList = new ArrayList<String>();
//        if (!addPermission(permissionsList, Manifest.permission.SEND_SMS))
//            permissionsNeeded.add("Send SMS");
//        if (!addPermission(permissionsList, Manifest.permission.RECEIVE_SMS))
//            permissionsNeeded.add("Receive SMS");
//        if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE))
//            permissionsNeeded.add("Read Phone State");
//        if (!addPermission(permissionsList, Manifest.permission.READ_SMS))
//            permissionsNeeded.add("Read SMS");
//
//        if (permissionsList.size() > 0) {
//            if (permissionsNeeded.size() > 0) {
//                // Need Rationale
//                String message = "You Need To Grant Access To " + permissionsNeeded.get(0);
//                for (int i = 1; i < permissionsNeeded.size(); i++) {
//                    message = message + ", " + permissionsNeeded.get(i);
//                    showMessageOKCancel(message,
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    ActivityCompat.requestPermissions(OTPVerifyActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
//                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
//                                }
//                            });
//                }
//                return false;
//            }
//            ActivityCompat.requestPermissions(OTPVerifyActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
//                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
//            return false;
//        }
//        return true;
//    }

    //add run time permission
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(OTPVerifyActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(OTPVerifyActivity.this, permission))
                return false;
        }
        return true;
    }

    //show permission alert
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener
            okListener) {
        new AlertDialog.Builder(OTPVerifyActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
//                Map<String, Integer> perms = new HashMap<String, Integer>();
//                // Initial
//                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
//                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
//                // Fill with results
//                for (int i = 0; i < permissions.length; i++) {
//                    perms.put(permissions[i], grantResults[i]);
//                    if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
//                            && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                        // All Permissions Granted
//                        //selectImage();
//                    } else {
//                        // Permission Denied
//                        Toast.makeText(OTPVerifyActivity.this, "Permission is Denied", Toast.LENGTH_SHORT)
//                                .show();
//                    }
//                }
//            }
//            break;
//            default:
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }

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
}
