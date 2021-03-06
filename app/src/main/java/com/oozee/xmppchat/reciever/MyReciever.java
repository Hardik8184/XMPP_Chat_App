package com.oozee.xmppchat.reciever;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.widget.Toast;

import com.oozee.xmppchat.utils.Common;
import com.oozee.xmppchat.xmpp.BackgroundXMPP;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by peacock on 10/10/16.
 */

public class MyReciever extends BroadcastReceiver {

    private SharedPreferences preferences;
    private BackgroundXMPP backgroundXMPP;

    @Override
    public void onReceive(final Context context, Intent intent) {

        preferences = context.getSharedPreferences(Common.sharedPreferences, MODE_PRIVATE);

        if (!Common.getConnectivityStatusString(context)) {

            Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT).show();

            preferences.edit().putString(Common.isDiconnected, "true").commit();

            startXMPP((Activity) context);

//            new Thread(new Runnable() {
//
//                @Override
//                public void run() {
//
//                    Intent startService = new Intent(context, BackgroundXMPPConnection.class);
//                    context.stopService(startService);
//
//                }
//            }).start();
        }

        if (Common.getConnectivityStatusString(context)) {

            Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();

            if (preferences.getString(Common.isDiconnected, "").equals("true")) {

                Toast.makeText(context, "Started", Toast.LENGTH_SHORT).show();

                startXMPP((Activity) context);

//                new Thread(new Runnable() {
//
//                    @Override
//                    public void run() {
//
//                        Intent startService = new Intent(context, BackgroundXMPPConnection.class);
//                        context.startService(startService);
//
//                    }
//                }).start();
            }
        }
    }

    private void startXMPP(final Activity activity) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                Looper.prepare();

                preferences = activity.getSharedPreferences(Common.sharedPreferences, MODE_PRIVATE);

                if (Common.getConnectivityStatusString(activity)) {

                    backgroundXMPP = new BackgroundXMPP(activity,
                            Common.DOMAIN, preferences.getString("user_name", "admin"),
                            preferences.getString("password", "123"), new BackgroundXMPP.ConnectionDone() {
                        @Override
                        public void onConnect() {

//                            progress.dismiss();
//
//                            startActivity(new Intent(activity, MainActivity.class));
//                            finish();
                        }

                        @Override
                        public void onDisConnect() {

//                            progress.dismiss();
//                            Toast.makeText(activity, "Connection not establish.Try again later", Toast.LENGTH_SHORT).show();
                        }
                    });
                    backgroundXMPP.connect();
                }

                System.out.println("Background_OnCreate --> Background Service Started");

            }
        }).start();
    }
}
