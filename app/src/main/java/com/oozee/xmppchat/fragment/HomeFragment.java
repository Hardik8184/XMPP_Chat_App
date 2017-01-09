package com.oozee.xmppchat.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.oozee.xmppchat.R;
import com.oozee.xmppchat.utils.Common;
import com.oozee.xmppchat.xmpp.BackgroundXMPP;

import static android.view.View.GONE;

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View chat_layout = inflater.inflate(R.layout.activity_home, container, false);

        chat_layout.setFocusableInTouchMode(true);
        chat_layout.setFocusable(true);

        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        preferences.edit().putString(Common.isApplicationOpen, "true").commit();
        preferences.edit().putString(Common.isFragmentOpen, "false").commit();
        preferences.edit().putString(Common.isAppInRecent, "false").commit();
        preferences.edit().putString(Common.killedFromRecent, "false").commit();
        if (!preferences.getString(Common.gotHistory, "").equals("true")) {

            preferences.edit().putString(Common.gotHistory, "false").commit();

        }

        preferences.edit().putString(Common.isAppInRecent, "false").commit();

        chat_layout.findViewById(R.id.btn_startChatting).setOnClickListener(this);

        return chat_layout;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_startChatting:

                activity.tv_badge.setText("0");
                activity.tv_badge.setVisibility(GONE);

                if (BackgroundXMPP.connection.isConnected()) {

                    FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                    ChatFragment newFragment = new ChatFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("user_jid", getArguments().getString("user_jid"));
                    bundle.putString("other_user", getArguments().getString("other_user"));
                    newFragment.setArguments(bundle);
                    ft.replace(R.id.container, newFragment).addToBackStack("home").commit();
                    activity.overridePendingTransition(R.anim.slideup, R.anim.slide_down);

                } else {
                    Toast.makeText(activity, "Please wait connection is establish", Toast.LENGTH_SHORT).show();
                }

                break;

            default:
                break;
        }
    }
}