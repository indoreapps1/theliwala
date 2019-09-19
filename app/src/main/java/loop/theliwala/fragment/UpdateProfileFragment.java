package loop.theliwala.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loop.theliwala.R;
import loop.theliwala.activity.DashboardActivity;
import loop.theliwala.ballsynccustomprogress.BallTriangleSyncDialog;
import loop.theliwala.database.DbHelper;
import loop.theliwala.framework.IAsyncWorkCompletedCallback;
import loop.theliwala.framework.ServiceCaller;
import loop.theliwala.models.Data;
import loop.theliwala.myalert.SweetAlertDialog;
import loop.theliwala.utilities.CompatibilityUtility;
import loop.theliwala.utilities.FontManager;
import loop.theliwala.utilities.Utility;


/**
 * Created by LALIT on 8/12/2017.
 */

public class UpdateProfileFragment extends Fragment implements View.OnClickListener {

    private String mParam1;
    private String mParam2;

    // TODO: Rename and change types and number of parameters
    public static UpdateProfileFragment newInstance(String param1, String param2) {
        UpdateProfileFragment fragment = new UpdateProfileFragment();
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

    EditText name, email;
    TextView phone, camera_text_view;
    TextInputLayout layout_name, layout_phone, layout_email;
    private Context context;
    private String nameData, phoneData, emailData;
    ImageView image;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    public final int REQUEST_CAMERA = 101;
    public final int SELECT_PHOTO = 102;
    private String userChoosenTask;
    private String base64Image;
    LinearLayout layout_saveProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        if (CompatibilityUtility.isTablet(context)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        View view = inflater.inflate(R.layout.fragment_update_profile, container, false);
        init(view);// initlization..........
        setType();//set icon..........
        return view;
    }

    private void setType() {
        Typeface medium = FontManager.getFontTypeface(context, "fonts/roboto.medium.ttf");
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        camera_text_view.setTypeface(materialdesignicons_font);
        camera_text_view.setText(Html.fromHtml("&#xf100;"));
        name.setTypeface(medium);
        phone.setTypeface(medium);
        email.setTypeface(medium);
    }

    //intilization.............
    private void init(View view) {
        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreencart(true);
        rootActivity.setScreenSave(false);
        rootActivity.setScreenFavourite(false);
        rootActivity.setScreenLocation(false);
        rootActivity.setItemCart();
        rootActivity.setScreenTitle("Profile Edit");
        // rootActivity.save.setVisibility(View.VISIBLE);// visible save ......
        //rootActivity.save.setOnClickListener(this);//set onclick on save .....
        name = (EditText) view.findViewById(R.id.edit_username);
        phone = (TextView) view.findViewById(R.id.edit_phone);
        email = (EditText) view.findViewById(R.id.edit_email);
        layout_name = (TextInputLayout) view.findViewById(R.id.textInputLayout_username);
        layout_phone = (TextInputLayout) view.findViewById(R.id.textInputLayout_phone);
        layout_email = (TextInputLayout) view.findViewById(R.id.textInputLayout_email);
        camera_text_view = (TextView) view.findViewById(R.id.camera_text_view);
        layout_saveProfile = (LinearLayout) view.findViewById(R.id.layout_saveProfile);
        image = (ImageView) view.findViewById(R.id.imageView);
        camera_text_view.setOnClickListener(this);
        layout_saveProfile.setOnClickListener(this);
        setValue();
    }

    //set user value
    private void setValue() {
//        DbHelper dbHelper = new DbHelper(context);
//        Data data = dbHelper.getUserData();
//        if (data != null) {
//            name.setText(data.getName());
//            phone.setText(data.getPhone());//you can not change phone no
//            email.setText(data.getEmailID());
//            String url = data.getProfilePictureUrl();
//            if (url != null && !url.equals("")) {
//                Picasso.get().load(url).placeholder(R.drawable.profile_image).into(image);
//            }
//        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.btn_update_profile:
//                updateProfile();//update profiel and send data to server
            // break;
            case R.id.layout_saveProfile:
                updateProfile();// for update profile
                break;
            case R.id.camera_text_view://open camera ...
                if (checkRuntimePermission()) {
                    selectImage();
                }
                break;
        }
    }

    private void updateProfile() {
        if (validation()) {
            if (Utility.isOnline(getActivity())) {
                final BallTriangleSyncDialog dotDialog = new BallTriangleSyncDialog(context);
                dotDialog.show();
                SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
                int id = sharedPreferences.getInt("userId", 0);
                ServiceCaller serviceCaller = new ServiceCaller(context);
                serviceCaller.UpdateUserProfileService(id, nameData, phoneData, emailData, new IAsyncWorkCompletedCallback() {
                    @Override
                    public void onDone(String workName, boolean isComplete) {
                        if (isComplete) {
                            if (base64Image != null && !base64Image.equals("")) {
                                updateProfileImage();
                            } else {
                                DashboardActivity rootActivity = (DashboardActivity) getActivity();
                                rootActivity.setUserDetail();
                                Toast.makeText(context, "Update Successfully", Toast.LENGTH_SHORT).show();
                                getActivity().getSupportFragmentManager().popBackStack();//back to profile screen
                            }
                        } else {
                            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Sorry...")
                                    .setContentText("Profile Not Updated")
                                    .show();
                            //Utility.alertForErrorMessage("Profile not Updated", getActivity());
                        }
                        if (dotDialog != null) {

                            dotDialog.dismiss();
                        }
                    }
                });
            } else {
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("You are Offline. Please check your Internet Connection.")
                        .show();
                //Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, getActivity());
            }
        }
    }

    //update profile image
    private void updateProfileImage() {
        if (validation()) {
            if (Utility.isOnline(getActivity())) {
                final BallTriangleSyncDialog dotDialog = new BallTriangleSyncDialog(context);
                dotDialog.show();
                SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
                int id = sharedPreferences.getInt("userId", 0);
                ServiceCaller serviceCaller = new ServiceCaller(context);
                serviceCaller.UploadImageService(id, base64Image, new IAsyncWorkCompletedCallback() {
                    @Override
                    public void onDone(String workName, boolean isComplete) {
                        if (isComplete) {
                            DashboardActivity rootActivity = (DashboardActivity) getActivity();
                            rootActivity.setUserDetail();
                            Toast.makeText(context, "Update Successfully", Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().popBackStack();//back to profile screen
                        } else {
                            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Sorry...")
                                    .setContentText("Your Profile Not Updated")
                                    .show();
                            //Utility.alertForErrorMessage("Your Profile not Updated", getActivity());
                        }
                        if (dotDialog != null) {
                            dotDialog.dismiss();
                        }
                    }
                });
            } else {
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("You are Offline. Please check your Internet Connection.")
                        .show();
                //Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, getActivity());
            }
        }
    }

    //validation for enter imformation ......
    private boolean validation() {
        // String emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        String emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        nameData = name.getText().toString();
        phoneData = phone.getText().toString();
        emailData = email.getText().toString();
        if (nameData.length() == 0) {
            layout_name.setError("Please Enter User Name");
            requestFocus(name);
            return false;
        } else {
            layout_name.setErrorEnabled(false);
            email.setSelection(emailData.length());
        }
//        if (phoneData.length() == 0) {
//            layout_phone.setError("Please Enter Phone");
//            requestFocus(phone);
//            return false;
//        } else if (phoneData.length() != 10) {
//            layout_phone.setError("Please Enter Valid Phone Number");
//            requestFocus(phone);
//            return false;
//        } else {
//            layout_phone.setErrorEnabled(false);
//        }
        if (emailData.length() == 0) {
            layout_email.setError("Please Enter Email Id");
            requestFocus(email);
            return false;
        } else if (!emailData.matches(emailRegex)) {
            layout_email.setError("Please Enter Valid Email Id");
            requestFocus(email);
            return false;
        } else {
            layout_email.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Profile Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //open camera
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    //select image from android.widget.Gallery
    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select Photo"), SELECT_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PHOTO) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
        }
    }


    private void onSelectFromGalleryResult(Intent data) {
        Uri uri = null;
        if (data != null) {
            try {
                uri = data.getData();
                //Bitmap bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());
                //base64Image = Utility.encodeToBase64(bm, Bitmap.CompressFormat.JPEG, 100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (uri != null) {
            // profile_image.setImageURI(uri);
            // Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//            base64Image = Utility.encodeToBase64(thumbnail, Bitmap.CompressFormat.JPEG, 100);
            addUriAsFile(uri);
//            image.setImageURI(uri);
        }
    }

    //add bitmap
    private Boolean addUriAsFile(final Uri uri) {
        new AsyncTask<Void, Void, Boolean>() {
            Bitmap bm;

            @Override
            protected Boolean doInBackground(Void... voids) {

                Boolean flag = false;
                File sdcardPath = Utility.getFilePath(context);
                sdcardPath.mkdirs();
                File imgFile = new File(sdcardPath, System.currentTimeMillis() + ".png");
                try {
                    InputStream iStream = context.getContentResolver().openInputStream(uri);
                    byte[] inputData = Utility.getBytes(iStream);

                    FileOutputStream fOut = new FileOutputStream(imgFile);
                    fOut.write(inputData);
                    //   bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    iStream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                bm = decodeSampledBitmapFromResource(imgFile.getAbsolutePath(), 500, 500);
                if (bm != null) {
                    base64Image = Utility.encodeToBase64(bm, Bitmap.CompressFormat.JPEG, 100);
                }
                return flag;
            }

            @Override
            protected void onPostExecute(Boolean flag) {
                super.onPostExecute(flag);
                if (bm != null) {
                    image.setImageBitmap(bm);
                }
            }
        }.execute();

        return true;
    }

    //create bitmap
    public Bitmap decodeSampledBitmapFromResource(String str,
                                                  int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(str, options);
    }

    //calculate size
    public int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private void onCaptureImageResult(Intent data) {

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        Uri uri = Utility.getImageUri(context, thumbnail);
        base64Image = Utility.encodeToBase64(thumbnail, Bitmap.CompressFormat.JPEG, 100);
        if (uri != null) {
            // profile_image.setImageURI(uri);
            Picasso.get().load(uri).into(image);
            // addUriAsFile(uri);
        }
     /*   ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    //check storage and camera run time permission
    private Boolean checkRuntimePermission() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("Storage");
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("Camera");
       /* if (!addPermission(permissionsList, Manifest.permission.WRITE_CONTACTS))
            permissionsNeeded.add("Write Contacts");*/

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return false;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    //add run time permission
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    //show permission alert
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .setCancelable(false)
                .create()
                .show();
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
                    selectImage();
                } else {
                    // Permission Denied
                    Toast.makeText(context, "Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
