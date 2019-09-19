package loop.theliwala.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import es.dmoral.toasty.Toasty;
import loop.theliwala.R;
import loop.theliwala.activity.DashboardActivity;
import loop.theliwala.ballsynccustomprogress.BallTriangleSyncDialog;
import loop.theliwala.database.DbHelper;
import loop.theliwala.framework.IAsyncWorkCompletedCallback;
import loop.theliwala.framework.ServiceCaller;
import loop.theliwala.models.Addresses;
import loop.theliwala.models.ContentData;
import loop.theliwala.models.ContentDataAsArray;
import loop.theliwala.models.Data;
import loop.theliwala.myalert.SweetAlertDialog;
import loop.theliwala.utilities.CompatibilityUtility;
import loop.theliwala.utilities.Contants;
import loop.theliwala.utilities.FontManager;
import loop.theliwala.utilities.Utility;

/**
 * Created by LALIT on 8/13/2017.
 */
public class AddNewAddressFragment extends Fragment implements View.OnClickListener {
    private int addressid;
    private Boolean editFlag;

    // TODO: Rename and change types and number of parameters
    public static AddNewAddressFragment newInstance(int addressid, Boolean editFlag) {
        AddNewAddressFragment fragment = new AddNewAddressFragment();
        Bundle args = new Bundle();
        args.putInt("addressid", addressid);
        args.putBoolean("editFlag", editFlag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            addressid = getArguments().getInt("addressid");
            editFlag = getArguments().getBoolean("editFlag");
        }
    }

    private EditText edt_name, edt_phone, edt_landmark, edt_address, edt_city, edt_state;
    private View view;
    private TextView saveaddress;
    private String address, phone, name, landMark, city, state;
    private LinearLayout save_address;
    private Context context;
    private Typeface materialdesignicons_font, regular, medium;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        if (CompatibilityUtility.isTablet(context)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.fragment_add_new_address, container, false);
        init();//intilization............
        return view;
    }

    //set icons
    private void setIcon() {
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        medium = FontManager.getFontTypeface(context, "fonts/roboto.medium.ttf");
        regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        saveaddress.setTypeface(medium);
        edt_landmark.setTypeface(regular);
        edt_phone.setTypeface(regular);
        edt_name.setTypeface(regular);
        edt_address.setTypeface(regular);
        edt_city.setTypeface(regular);
        edt_state.setTypeface(regular);

    }

    //intilization............
    public void init() {
        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreencart(true);
        rootActivity.setScreenSave(false);
        rootActivity.setScreenFavourite(false);
        rootActivity.setScreenLocation(false);
        rootActivity.setItemCart();
        edt_name = view.findViewById(R.id.edt_name);
        edt_phone = view.findViewById(R.id.edt_phone);
        edt_landmark = view.findViewById(R.id.edt_landmark);
        save_address = view.findViewById(R.id.save_address);
        saveaddress = view.findViewById(R.id.saveaddress);
        edt_address = view.findViewById(R.id.edt_address);
        edt_city = view.findViewById(R.id.edt_city);
        edt_state = view.findViewById(R.id.edt_state);
        save_address.setOnClickListener(this);
        if (editFlag) {//only for edit
            saveaddress.setText("Update Address");
            setValueForEditAddrss();
        } else {
            saveaddress.setText("Save Address");
        }

        setIcon();
    }

    //sho prefilds value for edit
    private void setValueForEditAddrss() {
        ServiceCaller serviceCaller = new ServiceCaller(context);
        serviceCaller.callGetAllAddressByIdService(addressid, new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
                if (isComplete) {
                    if (!workName.trim().equalsIgnoreCase("no") && !workName.equalsIgnoreCase("")) {
                        ContentDataAsArray contentDataAsArray = new Gson().fromJson(workName, ContentDataAsArray.class);
                        for (Data data : contentDataAsArray.getData()) {
                            if (data != null) {
                                edt_name.setText(data.getName());
                                edt_phone.setText(data.getPhone());
                                edt_landmark.setText(data.getLandmark());
                                edt_address.setText(data.getAddress());
                                edt_city.setText(data.getCity());
                                edt_state.setText(data.getState());
                            }
                        }
                    }
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_address:
                if (editFlag) {
                    setUpdateAddress();
                } else {
                    setAddNewAddress();
                }
                break;
        }

    }

    private void moveFragmentWithTag(Fragment fragment, String tag) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment, tag)
                .addToBackStack(null)
                .commit();
    }

    //validation for new address............
    private boolean isValidate() {
        name = edt_name.getText().toString();
        phone = edt_phone.getText().toString();
        landMark = edt_landmark.getText().toString();
        address = edt_address.getText().toString();
        city = edt_city.getText().toString();
        state = edt_state.getText().toString();
        if (name.length() == 0) {
            edt_name.setError("Please Enter Name ");
            requestFocus(edt_name);
            return false;
        } else if (phone.isEmpty()) {
            edt_phone.setError("Please Enter Phone Number ");
            requestFocus(edt_phone);
            return false;
        } else if (phone.length() != 10) {
            edt_phone.setError("Please Enter Valid Phone Number ");
            requestFocus(edt_phone);
            return false;
        } else if (address.length() == 0) {
            edt_address.setError("Please Enter Address");
            requestFocus(edt_address);
            return false;
        } else if (city.length() == 0) {
            edt_city.setError("Please Enter City Name");
            requestFocus(edt_city);
            return false;
        } else if (state.length() == 0) {
            edt_state.setError("Please Enter State Name");
            requestFocus(edt_state);
            return false;
        }
        return true;
    }

    //add new address..............
    public void setAddNewAddress() {
        if (isValidate()) {
            if (Utility.isOnline(context)) {
                final BallTriangleSyncDialog dotDialog = new BallTriangleSyncDialog(context);
                dotDialog.show();
                SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
                int id = sharedPreferences.getInt("userId", 0);
                ServiceCaller serviceCaller = new ServiceCaller(context);
                serviceCaller.SetNewAddressService(id, name, phone, landMark, address, city, state, new IAsyncWorkCompletedCallback() {
                    @Override
                    public void onDone(String workName, boolean isComplete) {
                        if (isComplete) {
                            Toasty.success(context, Contants.ADD_NEW_ADDRESS).show();
                            //getActivity().getSupportFragmentManager().popBackStack();//back to profile screen
                            Intent intent = new Intent("change_tab");
                            intent.putExtra("flag", "newAddress");
                            context.sendBroadcast(intent);
                        } else {
                            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Sorry...")
                                    .setContentText(Contants.DoNot_NEW_ADDRESS)
                                    .show();
                            //Utility.alertForErrorMessage(Contants.DoNot_NEW_ADDRESS, context);
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
    }

    //update address..............
    public void setUpdateAddress() {
        // check validation for new address............
        if (isValidate()) {
            if (Utility.isOnline(context)) {
                final BallTriangleSyncDialog dotDialog = new BallTriangleSyncDialog(context);
                dotDialog.show();
                ServiceCaller serviceCaller = new ServiceCaller(context);
                //get login id from data base ..................
                serviceCaller.updateAddressService(addressid, name, phone, landMark, address, city, state, new IAsyncWorkCompletedCallback() {
                    @Override
                    public void onDone(String workName, boolean isComplete) {
                        if (isComplete) {
                            editFlag=false;
                            Toasty.success(context, "Update Address Successfully.").show();
                            Intent intent = new Intent("change_tab");
                            intent.putExtra("flag", "updateAddress");
                            context.sendBroadcast(intent);
                        } else {
                            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Sorry...")
                                    .setContentText("Address not Update Successfully")
                                    .show();
                        }
                        dotDialog.dismiss();
                    }
                });
            } else {
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("You are Offline. Please check your Internet Connection.")
                        .show();
            }
        }
    }

    //regquest focus ................
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}





