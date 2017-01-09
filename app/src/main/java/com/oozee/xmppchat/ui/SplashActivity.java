package com.oozee.xmppchat.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.oozee.xmppchat.R;
import com.oozee.xmppchat.bean.Login;
import com.oozee.xmppchat.utils.Common;
import com.oozee.xmppchat.xmpp.BackgroundXMPP;

public class SplashActivity extends BaseActivity {

    private Activity activity;
    //    private KProgressHUD progressHUD;
    private BackgroundXMPP backgroundXMPP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        activity = SplashActivity.this;

        Toolbar mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        final Login login1 = getLogin();

        if (login1 != null) {

//            progressHUD = KProgressHUD.create(activity)
//                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                    .setCancellable(true).show();


            new Thread(new Runnable() {
                @Override
                public void run() {

                    Looper.prepare();

                    if (Common.getConnectivityStatusString(activity)) {

                        backgroundXMPP = new BackgroundXMPP(activity,
                                Common.DOMAIN, login1.getUsername(), login1.getPassword(), new BackgroundXMPP.ConnectionDone() {
                            @Override
                            public void onConnect() {

                                login = new Login(login1.getUsername(), login1.getPassword(), login1.getUsername() + "@" + Common.DOMAIN);
                                login.setUsername(login1.getUsername());
                                login.setPassword(login1.getPassword());
                                login.setJid(login1.getUsername() + "@" + Common.DOMAIN);

                                saveLogin(login);

//                                progressHUD.dismiss();

                                startActivity(new Intent(activity, UsersListActivity.class));
                                finish();
                            }

                            @Override
                            public void onDisConnect() {

//                                progressHUD.dismiss();
                                Toast.makeText(activity, "Connection not establish.Try again later", Toast.LENGTH_SHORT).show();
                            }
                        });
                        backgroundXMPP.connect();
                    }

                    System.out.println("Background_OnCreate --> Background Service Started");

                }
            }).start();
        } else {
            startActivity(new Intent(activity, LoginActivity.class));
        }
    }
}