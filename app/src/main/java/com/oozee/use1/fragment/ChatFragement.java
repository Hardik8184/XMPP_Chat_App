package com.oozee.use1.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.oozee.use1.Common;
import com.oozee.use1.R;
import com.oozee.use1.adapter.ChatMessagesAdapter;
import com.oozee.use1.bean.ChatMessage;
import com.oozee.use1.dataBase.AppDataBase;
import com.oozee.use1.xmpp.BackgroundXMPP;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by peacock on 8/10/16.
 */

public class ChatFragement extends DialogFragment implements View.OnClickListener {

    private Activity activity;
    private ArrayList<ChatMessage> chatMessagesList;
    private RecyclerView rv_chatList;
    private EditText et_message;
    private TextView presenceStatus;
    private Timer timer;
    private LinearLayoutManager llManager;
    public static ChatMessagesAdapter chatMessagesAdapter;
    private Random random;
    private SharedPreferences preferences;
    private AppDataBase dbHelper;

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String key = intent.getStringExtra("live_chat_broadcast");

            if (key.equals("live_chat_broadcast_success")) {

                String user_id = intent.getStringExtra("user_id");
                String msg_id = intent.getStringExtra("msg_id");
                String sender = intent.getStringExtra("sender");
                String message = intent.getStringExtra("message");
                String date_time = intent.getStringExtra("date_time");
                String is_msg_read = intent.getStringExtra("is_msg_read");

                if (chatMessagesList != null && chatMessagesList.size() > 0) {

                    chatMessagesList.add(new ChatMessage(user_id, msg_id, sender, message, date_time, is_msg_read));

                    chatMessagesAdapter.notifyMe(chatMessagesList);

                    rv_chatList.scrollToPosition(chatMessagesAdapter.getItemCount() - 1);

                } else {

                    setRecyclerView();

                }
            }
        }
    };

    public static ChatFragement newInstance() {

        ChatFragement chatFragement = new ChatFragement();

        return chatFragement;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        activity = getActivity();

        preferences = activity.getSharedPreferences(Common.sharedPreferences,
                Activity.MODE_PRIVATE);
        preferences.edit().putString(Common.isFragmentOpen, "true").commit();
//        preferences.edit().putString(Common.unreadMessagesCount, "0").commit();

        dbHelper = new AppDataBase(activity);

        random = new Random();

        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        DisplayMetrics displaymetrics = new DisplayMetrics();

        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_ADJUST_RESIZE);
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setLayout(displaymetrics.widthPixels, displaymetrics.heightPixels -
                65);

        View chat_layout = inflater.inflate(R.layout.chat_layout, container, false);

        chat_layout.setFocusableInTouchMode(true);
        chat_layout.setFocusable(true);

        et_message = (EditText) chat_layout.findViewById(R.id.et_message);
        presenceStatus = (TextView) chat_layout.findViewById(R.id.presenceStatus);

        rv_chatList = (RecyclerView) chat_layout.findViewById(R.id.rv_chatList);
        llManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        llManager.setStackFromEnd(true);
        rv_chatList.setLayoutManager(llManager);

        chat_layout.findViewById(R.id.ibtn_sendMessage).setOnClickListener(this);

        chatMessagesList = new ArrayList<>();
        chatMessagesAdapter = new ChatMessagesAdapter(activity, chatMessagesList, preferences);
//        chatMessagesAdapter = new ChatMessagesAdapter(activity);
//        rv_chatList.setAdapter(chatMessagesAdapter);

        setRecyclerView();

        presenceStatus();

        return chat_layout;

    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() == null) {

            return;

        }

        getDialog().getWindow().setWindowAnimations(R.style.ChatLayoutAnimation);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogTheme);

    }

    private void setRecyclerView() {

        chatMessagesList = dbHelper.getAllMessages();

        System.out.println("DB_Count_Array --> " + chatMessagesList.size());

        if (chatMessagesList != null && chatMessagesList.size() > 0) {

            llManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            llManager.setStackFromEnd(true);

            chatMessagesAdapter = new ChatMessagesAdapter(activity, chatMessagesList, preferences);

            rv_chatList.setLayoutManager(llManager);
            rv_chatList.setAdapter(chatMessagesAdapter);

            rv_chatList.scrollToPosition(chatMessagesAdapter.getItemCount() - 1);

        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.ibtn_sendMessage:

                String message = et_message.getText().toString().trim();

                if (message.trim().length() > 0) {

                    sendTextMessage();

                    et_message.getText().clear();

                }

                break;

            case R.id.et_message:

                if (chatMessagesList != null && chatMessagesList.size() > 0) {

                    rv_chatList.scrollToPosition(llManager.findLastCompletelyVisibleItemPosition());

                }

                break;
        }
    }

    private void presenceStatus() {

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                final int userState = BackgroundXMPP.getPresence(preferences.getString("other_user_jid", "admin@95.138.180.254"));

//                System.out.println("XMPP CHAT APP ----> User 1 --> Status of User2");

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (userState == 1) {
                            presenceStatus.setText(String.format("%s is Online", preferences.getString("other_user", "admin")));
                        } else if (userState == 0) {
                            presenceStatus.setText(String.format("%s is Offline", preferences.getString("other_user", "admin")));
                        } else if (userState == 2) {
                            presenceStatus.setText(String.format("%s is busy", preferences.getString("other_user", "admin")));
                        } else if (userState == 3) {
                            presenceStatus.setText(String.format("%s is away from chat", preferences.getString("other_user", "admin")));
                        }
                    }
                });
            }
        }, 1000, 5000);
    }

    public void sendTextMessage() {

        String msgId = "" + random.nextInt(1000);
        String message = et_message.getEditableText().toString();
        if (!message.equalsIgnoreCase("")) {
            ChatMessage chatMessage = new ChatMessage(preferences.getString("user_name", "admin"), msgId,
                    preferences.getString("user_name", "admin"), message, String.valueOf(System.currentTimeMillis()), "true");

            BackgroundXMPP.sendMessage(chatMessage);
            dbHelper.insertMessagesToDB(preferences.getString("user_name", "admin"), msgId, preferences.getString("user_name", "admin")
                    , message, String.valueOf(System.currentTimeMillis()), "true");

            chatMessagesList.add(new ChatMessage(preferences.getString("user_name", "admin"), msgId, preferences.getString("user_name", "admin")
                    , message, String.valueOf(System.currentTimeMillis()), "true"));

            chatMessagesAdapter.notifyMe(chatMessagesList);
            rv_chatList.scrollToPosition(chatMessagesAdapter.getItemCount() - 1);

            et_message.setText("");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

//        XMPPConnection.addConnectionCreationListener(new ConnectionCreationListener() {
//
//            @Override
//            public void connectionCreated(XMPPConnection connection) {
//                roster = connection.getRoster();
//
//                Collection<RosterEntry> entries = roster.getEntries();
//                Presence presence;
//
//                Log.e(TAG, "user count" + entries.size());
//
//                for (RosterEntry entry : entries) {
//                    presence = roster.getPresence(entry.getUser());
//
//                    Log.i(TAG, "" + entry.getUser());
//                    Log.i(TAG, "" + presence.getType().name());
//                    Log.i(TAG, "" + presence.getStatus());
//                }
//
//            }
//        });

        dbHelper.setAllMessagesAsRead();
        preferences.edit().putString(Common.isAppInRecent, "false").commit();

        if (chatMessagesAdapter != null) {

            chatMessagesList = dbHelper.getAllMessages();

            chatMessagesAdapter.notifyMe(chatMessagesList);

            rv_chatList.scrollToPosition(chatMessagesAdapter.getItemCount() - 1);

        }

        if (timer != null)
            presenceStatus();

        LocalBroadcastManager.getInstance(activity).registerReceiver(receiver,
                new IntentFilter("update_chat_list"));
    }

    @Override
    public void onPause() {
        super.onPause();

        preferences.edit().putString(Common.isAppInRecent, "true").commit();

        LocalBroadcastManager.getInstance(activity).unregisterReceiver(receiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        timer.cancel();
        preferences.edit().putString(Common.isFragmentOpen, "false").commit();
        preferences.edit().putString(Common.isAppInRecent, "false").commit();
    }
}
