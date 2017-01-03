package com.oozee.use1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.oozee.use1.services.BackgroundXMPPConnection;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    private Activity activity;

    private static final String TAG = "SplashActivity";
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        activity = SplashActivity.this;
        preferences = activity.getSharedPreferences(Common.sharedPreferences, Context.MODE_PRIVATE);

        Toolbar mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);

        findViewById(R.id.btnUser1).setOnClickListener(this);
        findViewById(R.id.btnUser2).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btnUser1:

                preferences.edit().putString("selectUser", "1").commit();


                break;

            case R.id.btnUser2:

                preferences.edit().putString("selectUser", "2").commit();

                break;

            default:

                break;
        }

        Intent startBackgroundService = new Intent(activity,
                BackgroundXMPPConnection.class);
        startService(startBackgroundService);

        startActivity(new Intent(activity, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Common.getConnectivityStatusString(activity)) {

            if (!Common.isMyServiceRunning(activity, BackgroundXMPPConnection.class)) {

                Intent startBackgroundService = new Intent(activity,
                        BackgroundXMPPConnection.class);
                startService(startBackgroundService);

            }
        }
    }
}