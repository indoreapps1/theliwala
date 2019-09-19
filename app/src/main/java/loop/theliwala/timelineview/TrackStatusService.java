package loop.theliwala.timelineview;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by user on 10/28/2017.
 */

public class TrackStatusService extends Service {
    public static  final long INTERVAl=3000;
    private Handler mHandler=new Handler();
    private Timer mTimer=null;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        mTimer.cancel();
    }

    @Override
    public void onCreate() {
        if (mTimer!=null){
            mTimer.cancel();
        }else {
            mTimer=new Timer();
            mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(),0,INTERVAl);
        }
    }
    private class TimeDisplayTimerTask extends TimerTask{

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(TrackStatusService.this, "hii service", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
