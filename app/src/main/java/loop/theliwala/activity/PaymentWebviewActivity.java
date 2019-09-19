package loop.theliwala.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.UrlQuerySanitizer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.gson.Gson;

import loop.theliwala.R;
import loop.theliwala.ballsynccustomprogress.BallTriangleSyncDialog;
import loop.theliwala.database.DbHelper;
import loop.theliwala.framework.IAsyncWorkCompletedCallback;
import loop.theliwala.framework.ServiceCaller;
import loop.theliwala.models.ContentData;
import loop.theliwala.models.Data;
import loop.theliwala.myalert.SweetAlertDialog;
import loop.theliwala.utilities.CompatibilityUtility;
import loop.theliwala.utilities.Contants;


/**
 * Created by user on 10/29/2017.
 */

public class PaymentWebviewActivity extends AppCompatActivity {
    private WebView browser = null;
    private String requestedURL = null;
    private ConnectionTimeoutHandler timeoutHandler = null;
    private static int PAGE_LOAD_PROGRESS = 0;
    public static final String KEY_REQUESTED_URL = "requested_url";
    /**
     * A string which will be added in url if server want to send something to client after everything has been done.
     * <p>
     * When you load any url to webview and after task done it doen't go back to caller activity. User must have to press back key.
     * <p>
     * So what we do, we will check url each time for a string if loading url contains the string, we assume that we got result.
     * <p>
     * In my example my server returns success string into url.
     */
    public static final String CALLBACK_URL = "payment_id";
    private Float NetPayable;
    private String OrderNo;
    private int storeId, OrderId;
    private BallTriangleSyncDialog spotsDialog;
    private Boolean paymentDone=true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (CompatibilityUtility.isTablet(PaymentWebviewActivity.this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(R.layout.payment_web_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        initComponents();
        addListener();
        OrderNo = getIntent().getStringExtra("OrderNo");
        OrderId = getIntent().getIntExtra("OrderId", 0);
        NetPayable = getIntent().getFloatExtra("NetPayable", 0);
        storeId = getIntent().getIntExtra("storeId", 0);

        requestedURL = getIntent().getStringExtra(KEY_REQUESTED_URL);
        if (requestedURL != null) {
            browser.loadUrl(requestedURL);
        } else {
            Log.e(Contants.LOG_TAG, "Can not process ... URL missing.");
        }
        spotsDialog = new BallTriangleSyncDialog(this);
        spotsDialog.show();
}

    public void initComponents() {
        browser = (WebView) findViewById(R.id.my_webkit);
        WebSettings webSettings = browser.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    public void addListener() {
        browser.setWebViewClient(new MyWebViewClient());
        browser.setWebChromeClient(new MyWebChromeClient());
    }

    //Custom web view client
    public class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i(Contants.LOG_TAG, "Loading url : " + url);
           /* if (url.contains(CALLBACK_URL)) {
                Log.i(Contants.LOG_TAG, "Callback url matched... Handling the result");
                UrlQuerySanitizer sanitizer = new UrlQuerySanitizer();
                sanitizer.setAllowUnregisteredParamaters(true);
                sanitizer.parseUrl(String.valueOf(url));
                String transaction_id = sanitizer.getValue("transaction_id");
                if (transaction_id != null) {
                }
                String payment_id = sanitizer.getValue("payment_id");
                String PaymentOrderId = sanitizer.getValue("id");
                sendConfirmDetailsToServer(transaction_id, payment_id, PaymentOrderId);
                return true;
            }
            if (spotsDialog.isShowing()) {
                spotsDialog.dismiss();
            }*/
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            timeoutHandler = new ConnectionTimeoutHandler(PaymentWebviewActivity.this, view);
            timeoutHandler.execute();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            super.onPageFinished(view, url);

            if (timeoutHandler != null)
                timeoutHandler.cancel(true);

            Log.i(Contants.LOG_TAG, "Loading url : " + url);
            // Do all your result processing here
            if (url.contains(CALLBACK_URL)) {
                Log.i(Contants.LOG_TAG, "Callback url matched... Handling the result");
                UrlQuerySanitizer sanitizer = new UrlQuerySanitizer();
                sanitizer.setAllowUnregisteredParamaters(true);
                sanitizer.parseUrl(String.valueOf(url));
                String transaction_id = sanitizer.getValue("transaction_id");
                String payment_id = sanitizer.getValue("payment_id");
                String PaymentOrderId = sanitizer.getValue("id");
//                Intent intent = new Intent(PaymentWebviewActivity.this, DashboardActivity.class);
//                intent.putExtra("NavigationFlage", 1);
//                intent.putExtra("OrderNo", OrderNo);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
                if(paymentDone) {
                    sendConfirmDetailsToServer(transaction_id, payment_id, PaymentOrderId);
                }

            }
            if (spotsDialog.isShowing()) {
                spotsDialog.dismiss();
            }
        }
        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {

            Log.i(Contants.LOG_TAG, "GOT Page error : code : " + errorCode + " Desc : " + description);
            showError(PaymentWebviewActivity.this, errorCode);
            //TODO We can show customized HTML page when page not found/ or server not found error.
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

    //Custom web chrome client
    public class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            PAGE_LOAD_PROGRESS = newProgress;
            //Log.i(TAG, "Page progress [" + PAGE_LOAD_PROGRESS + "%]");
            super.onProgressChanged(view, newProgress);
        }
    }

    private void showError(Context mContext, int errorCode) {
        //Prepare message
        String message = null;
        String title = null;
        if (errorCode == WebViewClient.ERROR_AUTHENTICATION) {
            message = "User authentication failed on server";
            title = "Auth Error";
        } else if (errorCode == WebViewClient.ERROR_TIMEOUT) {
            message = "The server is taking too much time to communicate. Try again later.";
            title = "Connection Timeout";
        } else if (errorCode == WebViewClient.ERROR_TOO_MANY_REQUESTS) {
            message = "Too many requests during this load";
            title = "Too Many Requests";
        } else if (errorCode == WebViewClient.ERROR_UNKNOWN) {
            message = "Generic error";
            title = "Unknown Error";
        } else if (errorCode == WebViewClient.ERROR_BAD_URL) {
            message = "Check entered URL..";
            title = "Malformed URL";
        } else if (errorCode == WebViewClient.ERROR_CONNECT) {
            message = "Failed to connect to the server";
            title = "Connection";
        } else if (errorCode == WebViewClient.ERROR_FAILED_SSL_HANDSHAKE) {
            message = "Failed to perform SSL handshake";
            title = "SSL Handshake Failed";
        } else if (errorCode == WebViewClient.ERROR_HOST_LOOKUP) {
            message = "Server or proxy hostname lookup failed";
            title = "Host Lookup Error";
        } else if (errorCode == WebViewClient.ERROR_PROXY_AUTHENTICATION) {
            message = "User authentication failed on proxy";
            title = "Proxy Auth Error";
        } else if (errorCode == WebViewClient.ERROR_REDIRECT_LOOP) {
            message = "Too many redirects";
            title = "Redirect Loop Error";
        } else if (errorCode == WebViewClient.ERROR_UNSUPPORTED_AUTH_SCHEME) {
            message = "Unsupported authentication scheme (not basic or digest)";
            title = "Auth Scheme Error";
        } else if (errorCode == WebViewClient.ERROR_UNSUPPORTED_SCHEME) {
            message = "Unsupported URI scheme";
            title = "URI Scheme Error";
        } else if (errorCode == WebViewClient.ERROR_FILE) {
            message = "Generic file error";
            title = "File";
        } else if (errorCode == WebViewClient.ERROR_FILE_NOT_FOUND) {
            message = "File not found";
            title = "File";
        } else if (errorCode == WebViewClient.ERROR_IO) {
            message = "The server failed to communicate. Try again later.";
            title = "IO Error";
        }

        if (message != null) {
            new AlertDialog.Builder(mContext)
                    .setMessage(message)
                    .setTitle(title)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    setResult(RESULT_CANCELED);
                                    //finish();
                                    dialog.dismiss();
                                }
                            }).show();
        }
    }

    public class ConnectionTimeoutHandler extends AsyncTask<Void, Void, String> {

        private static final String PAGE_LOADED = "PAGE_LOADED";
        private static final String CONNECTION_TIMEOUT = "CONNECTION_TIMEOUT";
        private static final long CONNECTION_TIMEOUT_UNIT = 60000L; //1 minute

        private Context mContext = null;
        private WebView webView;
        private Time startTime = new Time();
        private Time currentTime = new Time();
        private Boolean loaded = false;

        public ConnectionTimeoutHandler(Context mContext, WebView webView) {
            this.mContext = mContext;
            this.webView = webView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.startTime.setToNow();
            PaymentWebviewActivity.PAGE_LOAD_PROGRESS = 0;
        }

        @Override
        protected void onPostExecute(String result) {

            if (CONNECTION_TIMEOUT.equalsIgnoreCase(result)) {
                showError(this.mContext, WebViewClient.ERROR_TIMEOUT);

                this.webView.stopLoading();
            } else if (PAGE_LOADED.equalsIgnoreCase(result)) {
                //Toast.makeText(this.mContext, "Page load success", Toast.LENGTH_LONG).show();
            } else {
                //Handle unknown events here
            }
        }

        @Override
        protected String doInBackground(Void... params) {

            while (!loaded) {
                currentTime.setToNow();
                if (PaymentWebviewActivity.PAGE_LOAD_PROGRESS != 100
                        && (currentTime.toMillis(true) - startTime.toMillis(true)) > CONNECTION_TIMEOUT_UNIT) {
                    return CONNECTION_TIMEOUT;
                } else if (PaymentWebviewActivity.PAGE_LOAD_PROGRESS == 100) {
                    loaded = true;
                }
            }
            return PAGE_LOADED;
        }
    }

    //send payment confirm details to server
    private void sendConfirmDetailsToServer(final String transaction_id, String payment_id, String PaymentOrderId) {
        final ServiceCaller serviceCaller = new ServiceCaller(this);
        SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt("userId", 0);
        if (id != 0) {
            paymentDone=false;
            spotsDialog.show();
            String PaymentMode = "ONLINE";
            serviceCaller.sendConfirmDetailsToServerService(id, OrderId, OrderNo, storeId, transaction_id, payment_id, PaymentOrderId, PaymentMode, new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        ContentData data = new Gson().fromJson(result, ContentData.class);
                        if (data != null) {
                            if (data.getResponse() != null && data.getResponse().getSuccess()) {
                                Data detailsData = data.getData();
                                if (detailsData != null) {
//                                    for (Payments payments : detailsData.getPayments()) {
                                    Intent confirmIntent = new Intent(PaymentWebviewActivity.this, ConfirmPaymentActivity.class);
                                    confirmIntent.putExtra("TransactionId", transaction_id);
                                    confirmIntent.putExtra("OrderNo", OrderNo);
                                    confirmIntent.putExtra("NetPayable", NetPayable);
                                    confirmIntent.putExtra("orderId", OrderId);
                                    confirmIntent.putExtra("paymentMode", "online");
                                    confirmIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(confirmIntent);
                                    //finish();
                                } else {
                                    new SweetAlertDialog(PaymentWebviewActivity.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Sorry...")
                                            .setContentText("Payment Not Successful")
                                            .show();
                                   // Utility.alertForErrorMessage("Payment not successful ", PaymentWebviewActivity.this);
                                }

                            }
                        }
                    }
                    //finish();
                    if (spotsDialog.isShowing()) {
                        spotsDialog.dismiss();
                    }
                }
            });
        }
    }
}
   /* public static void callWebview(Activity activity, String requestedURL, int requestCode) {

        Intent intent = new Intent(activity, PaymentWebviewActivity.class);
        intent.putExtra(PaymentWebviewActivity.KEY_REQUESTED_URL, requestedURL);
        activity.startActivityForResult(intent, requestCode);
    }*/
//}
