package loop.theliwala.utilities;

import android.os.Environment;

/**
 * Created by Lalit on 7/25/2017.
 */
public class Contants {
    public static final Boolean IS_DEBUG_LOG = true;

    public static final String LOG_TAG = "sirjohn";
    public static final String APP_NAME = "appName"; // Do NOT change this value; meant for user preference

    public static final String DEFAULT_APPLICATION_NAME = "sirjohn";

    public static final String APP_DIRECTORY = "/E" + DEFAULT_APPLICATION_NAME + "Directory/";
    public static final String DATABASE_NAME = "sirjohn.db";// Environment.getExternalStorageDirectory() +  APP_DIRECTORY + "evergreen.db";

    public static String SERVICE_BASE_URL = "http://theliwala.loopfusion.in//apis/";

    public static String outputBasePath = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static String outputDirectoryLocation = outputBasePath + "/sirjohnUnzipped/";


    public static final int LIST_PAGE_SIZE = 50;
    public static String InternetMessage = " You are Online.";
    public static final String BAD_NETWORK_MESSAGE = "We are trying hard to get latest content but the network seems to be slow. " +
            "You may continue to use app and get latest with in the app itself. ";


    public static final String OFFLINE_MESSAGE = "Oops! You are Offline. Please check your Internet Connection.";
    public static final String SEND_MESSAGE = "OTP Send to Your Phone Number Successfully";
    public static final String ADD_NEW_ADDRESS = "Add New Address Successfully";
    public static final String MESSAGE_FOR_UNREGISTRED_USER = "You Are Not a Registered User!Please Login First..";
    public static final String DoNot_NEW_ADDRESS = "Your Address Do Not Add Successfully";
    public static final String SEND_OTP_MESSAGE = "Your Registration Successfully";
    public static final String DoNot_SEND_OTP_MESSAGE = "OTP NOT Correct.Please Enter Valid OTP ";
    public static final String Dont_SEND_MESSAGE = "OTP Do Not Send Successfully";
    public static final String Dont_GetAddress_MESSAGE = "Some Problem For Geting Address";
    public static final String No_DATA_FOUND_MESSAGE = "No Record Found";
    public static final String Login = "login.php";
    public static final String otpverify = "otpverify.php";
    public static final String GetUserProfile = "Login/GetUserProfile";
    public static final String UpdateProfile = "Login/UpdateProfile";
    public static final String AddNewAddress = "addNewAddress.php";
    public static final String UpdateAddress = "UpdateAddress.php";
    public static final String GetAllAddress = "GetAllAddress.php";
    public static final String GetAllAddressById = "GetAllAddressById.php";
    public static final String SELECT_CITY_URL = "Cities/GetAllCities";
    public static final String GET_ALL_LOCALITIES_URL = "Localities/GetAllLocalities";
    public static final String DeleteAddress = "DeleteAddress.php";
    public static final String GetAllCategory = "getAllCategory.php";
    public static final String GetAllBestSell = "GetAllBestSell.php";
    public static final String GetAllNewArrive = "GetAllNewArrive.php";
    public static final String GetAllProductByCategory = "Product/GetAllProductByCategory";
    public static final String GetAllStoreByLocality = "Store/GetAllStoreByLocality";
    public static final String GetStoreDetailsById = "Store/GetStoreDetailsById";
    public static final String ApplyPromoCode = "PromoCode/ApplyPromoCode";
    public static final String CreateOrder = "Order/CreateOrder";
    public static final String GetOrderByOrderNumber = "Order/GetOrderByOrderNumber";
    public static final String TrackOrderStatus = "Order/TrackOrderStatus";
    public static final String GetFavouriteStoreByUser = "Store/GetFavouriteStoreByUser";
    public static final String GetMyAllOrderHistory = "Order/MyAllOrderList";
    public static final String GetAllOrderByUser = "Order/GetAllOrderByUser";
    public static final String AddUpdateFavourite = "Store/AddUpdateFavouriteStoreByUser";
    public static final String UploadProfilePicture = "UploadProfilePicture/UploadProfilePicture";
    public static final String SocialUserLogin = "Login/SocialUserLogin";
    public static final String RemoveFavouriteStoreByUser = "Store/RemoveFavouriteStoreByUser";
    public static final String CreatePaymentOrder = "PaymentGateway/CreatePaymentOrder";
    public static final String PaymentConfirmation = "Payment/PaymentConfirmation";
    public static final String Notification = "Notification/SendNotification";
    public static final String GetAllMenuListByStoreId = "MenuMaster/GetAllMenuListByStoreId";
    public static final String PaymentConfirmforCOD = "Payment/PaymentConfirmforCOD";
}
