package com.oozee.use1.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.oozee.use1.Common;
import com.oozee.use1.services.BackgroundXMPPConnection;

/**
 * Created by peacock on 10/10/16.
 */

public class MyReciever extends BroadcastReceiver {

    private SharedPreferences preferences;

    @Override
    public void onReceive(final Context context, Intent intent) {

        preferences = context.getSharedPreferences(Common.sharedPreferences, Context.MODE_PRIVATE);

        if (!Common.getConnectivityStatusString(context)) {

            Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT).show();

            preferences.edit().putString(Common.isDiconnected, "true").commit();

            new Thread(new Runnable() {

                @Override
                public void run() {

                    Intent startService = new Intent(context, BackgroundXMPPConnection.class);
                    context.stopService(startService);

                }
            }).start();
        }

        if (Common.getConnectivityStatusString(context)) {

            Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();

            if (preferences.getString(Common.isDiconnected, "").equals("true")) {

                Toast.makeText(context, "Started", Toast.LENGTH_SHORT).show();

                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        Intent startService = new Intent(context, BackgroundXMPPConnection.class);
                        context.startService(startService);

                    }
                }).start();
            }
        }
    }
}
