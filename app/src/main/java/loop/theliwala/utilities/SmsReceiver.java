package loop.theliwala.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Neeraj on 4/12/2017.
 */
public class SmsReceiver extends BroadcastReceiver {
    Context mContext;
    public static String verificationCode;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (Object aPdusObj : pdusObj) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                    String senderAddress = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();
                    // verification code from sms
                    String otpCode = getVerificationCode(message);
                    if (otpCode != null) {
                        verificationCode = otpCode;
                        if (Contants.IS_DEBUG_LOG) {
                            Log.d(Contants.LOG_TAG, "OTP received: " + verificationCode);
                        }
                    }

                    if (Contants.IS_DEBUG_LOG) {
                        Log.d(Contants.LOG_TAG, "Received SMS: " + message + ", Sender: " + senderAddress);
                    }
                   /* // if the SMS is not from our gateway, ignore the message
                    if (!senderAddress.toLowerCase().contains(Contants.SENDER.toLowerCase())) {
                        return;
                    }*/

                    /*PhoneNumberVerificationActivity Sms = new PhoneNumberVerificationActivity();
                    Sms.recivedSms(verificationCode);*/

                    Intent myIntent = new Intent("otp");
                    myIntent.putExtra("message", verificationCode);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);

                }

            }
        } catch (Exception e) {
            Log.d(Contants.LOG_TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Getting the OTP from sms message body
     * ':' is the separator of OTP from the message
     *
     * @param message
     * @return
     */
    private String getVerificationCode(String message) {
        String code = null;
        Pattern digitPatternRegex = Pattern.compile("\\d{4}");
        Matcher matcher = digitPatternRegex.matcher(message);
        while (matcher.find()) {
            if (Contants.IS_DEBUG_LOG) {
                Log.d(Contants.LOG_TAG, "code: " + matcher.group(0));
            }
            code = matcher.group(0);
        }

        return code;
    }


}
