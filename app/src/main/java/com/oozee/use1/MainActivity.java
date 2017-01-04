package com.oozee.use1;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.oozee.use1.dataBase.AppDataBase;
import com.oozee.use1.fragment.ChatFragement;
import com.oozee.use1.services.BackgroundXMPPConnection;
import com.oozee.use1.xmpp.BackgroundXMPP;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Activity activity;
    private SharedPreferences preferences;
    private static final String TAG = "MainActivity";
    private boolean mBounded;
    private AppDataBase dbHelper;
    private TextView tv_badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        activity = MainActivity.this;

        Toolbar mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);

        dbHelper = new AppDataBase(activity);

        preferences = getSharedPreferences(Common.sharedPreferences, MODE_PRIVATE);
        preferences.edit().putString(Common.isApplicationOpen, "true").commit();
        preferences.edit().putString(Common.isFragmentOpen, "false").commit();
        preferences.edit().putString(Common.isAppInRecent, "false").commit();
        preferences.edit().putString(Common.killedFromRecent, "false").commit();
        if (!preferences.getString(Common.gotHistory, "").equals("true")) {

            preferences.edit().putString(Common.gotHistory, "false").commit();

        }

        tv_badge = (TextView) mainToolbar.findViewById(R.id.tv_badge);
        mainToolbar.findViewById(R.id.fl_startChatting).setOnClickListener(this);
        findViewById(R.id.btn_startChatting).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_startChatting:
            case R.id.fl_startChatting:

                tv_badge.setText("0");
                tv_badge.setVisibility(GONE);

//                Intent intent = new Intent(activity, ChatFragement.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                finish(); // call this to finish the current activity

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ChatFragement newFragment = ChatFragement.newInstance();
                newFragment.show(ft, "");

                break;

            default:

                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(activity, BackgroundXMPPConnection.class));
        BackgroundXMPP.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();

        preferences.edit().putString(Common.isAppInRecent, "false").commit();

        if (preferences.getString(Common.isFragmentOpen, "").equals("false")) {

            String badges = dbHelper.getNewUnreadMessagesCounts(preferences.getString("user_name", "admin"));
            if (badges != null && !badges.equals("") && !badges.equals("0")) {

                tv_badge.setText(badges);
                tv_badge.setVisibility(VISIBLE);

            } else {

                tv_badge.setVisibility(GONE);

            }
        }

        if (Common.getConnectivityStatusString(activity)) {

            if (!Common.isMyServiceRunning(activity, BackgroundXMPPConnection.class)) {

                Intent startBackgroundService = new Intent(activity,
                        BackgroundXMPPConnection.class);
                startService(startBackgroundService);

            }
        }

        LocalBroadcastManager.getInstance(activity).registerReceiver(updateBadgesReciever,
                new IntentFilter("update_badges_broadcast"));
    }

    private BroadcastReceiver updateBadgesReciever = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String key = intent.getStringExtra("udate_badge");

            if (key.equals("new_messages_recieved")) {

                //String unreadMessageCount = preferences.getString(Common.unreadMessagesCount, "");

                String unreadMessageCount = intent.getStringExtra("badge_count");

                if (unreadMessageCount != null && unreadMessageCount.equals("0")) {

                    tv_badge.setVisibility(GONE);

                } else {

                    if (preferences.getString(Common.isFragmentOpen, "").equals("true")) {

                        tv_badge.setVisibility(GONE);
                        tv_badge.setText("0");

                    } else {

                        tv_badge.setText(unreadMessageCount);
                        if (tv_badge.getVisibility() == GONE) {

                            tv_badge.setVisibility(VISIBLE);

                        }
                    }
                }
            }
        }
    };
}