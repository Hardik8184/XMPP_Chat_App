package com.oozee.use1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.oozee.use1.services.BackgroundXMPPConnection;
import com.oozee.use1.xmpp.BackgroundXMPP;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    private Activity activity;

    private EditText edtUserName, edtPassword, edtOtherUser;
    private String strUserName, strPassword, strOtherUser;

    private static final String TAG = "SplashActivity";
    private SharedPreferences preferences;
    private BackgroundXMPP backgroundXMPP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        activity = SplashActivity.this;
        preferences = activity.getSharedPreferences(Common.sharedPreferences, Context.MODE_PRIVATE);

        Toolbar mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);

        edtUserName = (EditText) findViewById(R.id.edtUserName);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtOtherUser = (EditText) findViewById(R.id.edtOtherUser);

        findViewById(R.id.btnLogin).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btnLogin:

                strUserName = edtUserName.getText().toString();
                strPassword = edtPassword.getText().toString();
                strOtherUser = edtOtherUser.getText().toString();

                if (strUserName.equals("")) {
                    Toast.makeText(activity, "Enter user name", Toast.LENGTH_SHORT).show();
                } else if (strPassword.equals("")) {
                    Toast.makeText(activity, "Enter password", Toast.LENGTH_SHORT).show();
                } else {

                    preferences.edit().putString("user_name", strUserName).commit();
                    preferences.edit().putString("password", strPassword).commit();
                    preferences.edit().putString("other_user", strOtherUser).commit();

                    preferences.edit().putString("user_jid", strUserName + "@192.168.1.141").commit();
                    preferences.edit().putString("other_user_jid", strOtherUser + "@192.168.1.141").commit();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            Looper.prepare();

                            preferences = getSharedPreferences(Common.sharedPreferences, MODE_PRIVATE);

                            if (Common.getConnectivityStatusString(activity)) {

                                backgroundXMPP = new BackgroundXMPP(activity,
                                        Common.DOMAIN, strUserName, strPassword, "1");
                                backgroundXMPP.connect();
                            }

                            System.out.println("Background_OnCreate --> Background Service Started");

                        }
                    }).start();

                    startActivity(new Intent(activity, MainActivity.class));
                    finish();
                }

                break;

            default:

                break;
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (Common.getConnectivityStatusString(activity)) {

//            if (!Common.isMyServiceRunning(activity, BackgroundXMPPConnection.class)) {
//
//                Intent startBackgroundService = new Intent(activity,
//                        BackgroundXMPPConnection.class);
//                startService(startBackgroundService);
//
//            }
//        }
    }
}