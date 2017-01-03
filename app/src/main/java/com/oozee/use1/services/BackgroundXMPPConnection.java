package com.oozee.use1.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Looper;

import com.oozee.use1.Common;
import com.oozee.use1.xmpp.BackgroundXMPP;


/**
 * Created by peacock on 8/10/16.
 */

public class BackgroundXMPPConnection extends IntentService {

    private BackgroundXMPP backgroundXMPP;

    private ConnectivityManager connectivityManager;

    private PendingIntent pendGps;

    private AlarmManager manager;

    private SharedPreferences preferences;

    public BackgroundXMPPConnection() {
        super("BackgroundXMPPConnection");
    }

//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }

    @Override
    public void onCreate() {
        super.onCreate();

        new Thread(new Runnable() {
            @Override
            public void run() {

                Looper.prepare();

                preferences = getSharedPreferences(Common.sharedPreferences, MODE_PRIVATE);

                connectivityManager = (ConnectivityManager) getSystemService(Context.
                        CONNECTIVITY_SERVICE);

                if (Common.getConnectivityStatusString(BackgroundXMPPConnection.this)) {

//                    if (preferences.getString("selectUser", "1").equals("1")) {
                    backgroundXMPP = new BackgroundXMPP(BackgroundXMPPConnection.this,
                            Common.DOMAIN, Common.CHAT_USERNAME_1, Common.PASSWORD_1, "1");
//                    } else {
//                        backgroundXMPP = new BackgroundXMPP(BackgroundXMPPConnection.this,
//                                Common.DOMAIN, Common.CHAT_USERNAME_2, Common.PASSWORD_2, "2");
//                    }
                    backgroundXMPP.connect();

                }

                System.out.println("Background_OnCreate --> Background Service Started");

            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        /* if service is stopped working(not by calling stopService) due to other assistance then
           because of following code it will be restarted automatically. */
        PendingIntent restartService = PendingIntent.getService(this, 0,
                new Intent(this, BackgroundXMPPConnection.class), Intent.FILL_IN_ACTION);

        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, System.currentTimeMillis(),
                60000, restartService);

        return START_STICKY;

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        super.onTaskRemoved(rootIntent);

        preferences.edit().putString(Common.killedFromRecent, "true").commit();
        preferences.edit().putString(Common.isFragmentOpen, "false").commit();
        preferences.edit().putString(Common.isAppInRecent, "false").commit();
        preferences.edit().putString(Common.isApplicationOpen, "false").commit();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            System.out.println("BackgroundService_OnDestroy --> Destroy");
        } catch (Exception e) {
            System.out.println("BackgroundService_OnDestroyException --> " + e.getMessage());

        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
