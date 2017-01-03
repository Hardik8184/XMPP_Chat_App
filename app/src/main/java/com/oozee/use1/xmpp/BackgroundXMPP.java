package com.oozee.use1.xmpp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;
import com.oozee.use1.Common;
import com.oozee.use1.MainActivity;
import com.oozee.use1.R;
import com.oozee.use1.bean.ChatMessage;
import com.oozee.use1.dataBase.AppDataBase;
import com.oozee.use1.fragment.ChatFragement;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.ExceptionCallback;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.iqrequest.IQRequestHandler;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.PlainStreamElement;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.delay.packet.DelayInformation;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager.AutoReceiptMode;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

import static org.jivesoftware.smackx.pubsub.AccessModel.roster;

public class BackgroundXMPP {

    private static XMPPTCPConnection connection;
    private static Gson gson;

    //private int unreadMessageCount = 0;
    private AppDataBase dbHelper;

    private ChatManagerListenerImpl mChatManagerListener;

    private String loginUser, passwordUser;

    private Context context;

    private String serverAddress;
    private static String userSelect = "";

    private DiscussionHistory history;

    private SharedPreferences preferences;

    public BackgroundXMPP(Context context, String serverAdress, String logiUser,
                          String passwordser, String userSelect) {

        this.serverAddress = serverAdress;
        this.loginUser = logiUser;
        BackgroundXMPP.userSelect = userSelect;
        this.passwordUser = passwordser;
        this.context = context;

        init();

    }

    private static MessageListener mMessageListener;

    public static void sendMessage(ChatMessage chatMessage) {

        try {
            if (connection.isAuthenticated()) {

                String body = gson.toJson(chatMessage);

                // createChat(receiver@server,MessageListener)
                ChatManager chatManager = ChatManager.getInstanceFor(connection);
                Chat chat;
                Message message;

//                if (userSelect.equals("1")) {
                chat = chatManager.createChat(Common.CHAT_USER_2_JID, mMessageListener);
                message = new Message(Common.CHAT_USERNAME_2);
//                } else {
//                    chat = chatManager.createChat(Common.CHAT_USER_1_JID, mMessageListener);
//                    message = new Message(Common.CHAT_USERNAME_1);
//                }

                message.setBody(body);
//                message.setFrom(chatMessage.getSender());
                message.setStanzaId(chatMessage.getMsg_id());
                message.setType(Message.Type.chat);
                chat.sendMessage(message);

            } else {
                System.out.println("SendMsgError --> Client Not Connected");
            }
        } catch (Exception e) {
            System.out.println("SendMsgException --> " + e.getMessage());
        }
    }

    public static void disconnect() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                connection.disconnect();
            }
        }).start();
    }

    private void init() {

        preferences = context.getSharedPreferences(Common.sharedPreferences, Context.MODE_PRIVATE);

        dbHelper = new AppDataBase(context);

        gson = new Gson();
        mMessageListener = new MessageListener(context);

        mChatManagerListener = new ChatManagerListenerImpl();

        if (Common.getConnectivityStatusString(context)) {

            initialiseConnection();

        }
    }

    private String sender, msg;

    private void initialiseConnection() {

        XMPPTCPConnectionConfiguration connConfig = XMPPTCPConnectionConfiguration.builder().
                setHost(serverAddress).setPort(5222).setDebuggerEnabled(true).
                setSecurityMode(ConnectionConfiguration.SecurityMode.disabled).
                setUsernameAndPassword(loginUser, passwordUser).setServiceName(serverAddress).
                build();

        XMPPTCPConnection.setUseStreamManagementDefault(true);
        XMPPTCPConnection.setUseStreamManagementResumptionDefault(true);

        connection = new XMPPTCPConnection(connConfig);
        connection.setUseStreamManagement(true);
        connection.setUseStreamManagementResumption(true);

        XMPPConnectionListener connectionListener = new XMPPConnectionListener();

        connection.addConnectionListener(connectionListener);
        //connection.setPacketReplyTimeout(100000);
        connection.setPacketReplyTimeout(XMPPTCPConnectionConfiguration.DEFAULT_CONNECT_TIMEOUT);

        connection.addStanzaAcknowledgedListener(new StanzaListener() {
            @Override
            public void processPacket(Stanza packet) throws NotConnectedException {

//                String id = packet.getStanzaId();
//                if (StringUtils.isNullOrEmpty(id)) {
//                }
            }
        });

        /* If connection with xmpp server is clesed due to any assistance then because of following
           code itself it will try to reconnect to the server. Everytime it will call
           reconnectingIn(int arg0) method(See below XMPPConnectionListener class)*/
        ReconnectionManager reconnectionManager = ReconnectionManager.getInstanceFor(connection);
        ReconnectionManager.setEnabledPerDefault(true);
        reconnectionManager.enableAutomaticReconnection();
        reconnectionManager.setReconnectionPolicy(ReconnectionManager.ReconnectionPolicy.
                FIXED_DELAY);

        connection.addSyncStanzaListener(new StanzaListener() {

            @Override
            public void processPacket(Stanza packet) throws NotConnectedException {

                Message message = (Message) packet;

                System.out.println("stanza_Back --> " + message.getBody());

                System.out.println("ChatApp_Messages --> " + message.getBody());

                try {

                    JSONObject jsonObject = new JSONObject(message.getBody());

                    sender = jsonObject.optString("sender");
                    msg = jsonObject.optString("message");

//                    ChatMessage chatMessage = new ChatMessage(jsonObject.optString("user_id"), jsonObject.optString("msg_id")
//                            , jsonObject.optString("sender"), jsonObject.optString("message"), jsonObject.optString("date_time")
//                            , jsonObject.optString("is_msg_read"));
//
//                    ChatFragement.chatMessagesAdapter.add(chatMessage);
//                    new Handler(Looper.getMainLooper()).post(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            ChatFragement.chatMessagesAdapter.notifyDataSetChanged();
//
//                        }
//                    });
//
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                DelayInformation delay = message.getExtension("delay", "urn:xmpp:delay");

                /* msg format : saveSearchIDNNO;@;action;@;msg
                   Note : if (action == "S") then saveSearchIDNNO'll give saveSearchIDNNO otherwise
                   saveSearchIDNNO = "0". */

                String isMsgRead = null;

                if (sender.equals(Common.CHAT_USERNAME_1)) {

                    isMsgRead = "true";

                } else {

                    if (preferences.getString(Common.isFragmentOpen, "").equals("true") &&
                            preferences.getString(Common.isAppInRecent, "").equals("false")) {

                        isMsgRead = "true";

                    } else {

                        isMsgRead = "false";

                    }
                }

                if (delay == null) {

                    delay = message.getExtension("x", "jabber:x:delay");

                }

                long time = 0;

                if (delay == null)
                    time = System.currentTimeMillis();
                else
                    time = Common.getDateInMills(delay.getStamp().toString());

                if (msg != null && !msg.equals("") && msg.length() > 0) {

                    dbHelper.insertMessagesToDB(Common.CHAT_USERNAME_1, "1", sender, msg, String.valueOf(time), isMsgRead);

                    /* (Application Is Completely Closed) Or (Fragement Is Opened And Application Is
                        In Recent Mode). */
                    if (preferences.getString(Common.isApplicationOpen, "").equals("false") ||
                            (preferences.getString(Common.isFragmentOpen, "").equals("true") &&
                                    preferences.getString(Common.isAppInRecent, "").
                                            equals("true"))) {

                        //Code For Notification When Application Is Completely Closed.
                        System.out.println("ChatApp -- BackgroundXMPP -- addSyncStanzaListener --> "
                                + "App is Closed, " + message.getBody());

                        chatMessagesNotification(dbHelper.getNewUnreadMessagesForNotification());

                    } else {

                        if (preferences.getString(Common.isFragmentOpen, "").equals("true") &&
                                preferences.getString(Common.isAppInRecent, "").equals("false")) {

                            Intent broadCastChat = new Intent("update_chat_list");
                            broadCastChat.putExtra("live_chat_broadcast",
                                    "live_chat_broadcast_success");
                            broadCastChat.putExtra("user_id", Common.CHAT_USERNAME_1);
                            broadCastChat.putExtra("msg_id", "1");
                            broadCastChat.putExtra("sender", sender);
                            broadCastChat.putExtra("message", msg);
                            broadCastChat.putExtra("date_time", String.valueOf(time));
                            broadCastChat.putExtra("is_msg_read", "true");
                            LocalBroadcastManager.getInstance(context).sendBroadcast(broadCastChat);

                        } else {

                            /* When ChatFrafement Is Open But Application Is In Recent Mode Then
                               Badges Should Be Updated As Well As Notification Also Arrise. */
                            if (isMsgRead.equals("false")) {

                                ArrayList<String> notificationMessages = dbHelper.
                                        getNewUnreadMessagesForNotification();

                                System.out.println("ChatApp -- BackgroundXMPP -- " +
                                        "addSyncStanzaListener --> " + /*preferences.
                                        getString(Common.unreadMessagesCount, "")*/
                                        notificationMessages.size());

                                Intent updateBadges = new Intent("update_badges_broadcast");
                                updateBadges.putExtra("udate_badge", "new_messages_recieved");
                                updateBadges.putExtra("badge_count",
                                        String.valueOf(notificationMessages.size()));

                                /*preferences.edit().putString(Common.unreadMessagesCount,
                                        String.valueOf(unreadMessageCount)).commit();*/

                                LocalBroadcastManager.getInstance(context).
                                        sendBroadcast(updateBadges);

                                //Code For Notification
                                if (preferences.getString(Common.isAppInRecent, "").
                                        equals("true")) {

                                    System.out.println("ChatApp -- BackgroundXMPP -- " +
                                            "addSyncStanzaListener -->  App In Recent, Fragment " +
                                            "Closed, " + message.getBody());

                                    chatMessagesNotification(notificationMessages);

                                }
                            }
                        }
                    }

                    if (preferences.getString(Common.gotHistory, "").equals("false")) {

                        preferences.edit().putString(Common.gotHistory, "true").commit();

                        //sendMsgtoGroup("Hiiiii ... ");

                    }
                }
            }
        }, new StanzaTypeFilter(Message.class));
    }

    public void connect() {

        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected synchronized Boolean doInBackground(Void... arg0) {

                if (Common.getConnectivityStatusString(context)) {

                    if (connection.isConnected())
                        return false;

                    try {

                        connection.connect();

                        DeliveryReceiptManager dm = DeliveryReceiptManager.
                                getInstanceFor(connection);
                        dm.setAutoReceiptMode(AutoReceiptMode.always);
                        dm.addReceiptReceivedListener(new ReceiptReceivedListener() {

                            @Override
                            public void onReceiptReceived(String fromid, String toid, String msgid,
                                                          Stanza packet) {

                                Message message = (Message) packet;

                                System.out.println("ReceiptReceivedListener --> " +
                                        message.getBody());

                            }
                        });

                        System.out.println("Notification_Messages --> Connected Successfully");

//                        joinGroup();

                    } catch (Exception e) {

                        System.out.println("Notification_Messages --> Connection Losted");

                        initialiseConnection();

                        connect();

                        System.out.println("Notification_Messages --> " + e.getMessage());

                        return false;

                    }
                }

                return true;

            }
        }.execute();
    }

    private void login() {

        try {

            Presence presence = new Presence(Presence.Type.available);

            connection.sendStanza(presence);
            connection.login(loginUser, passwordUser);

            DelayExtensionProvider.install();

            System.out.println("LoginSuccess --> Logged In Successfully");

        } catch (Exception e) {

            System.out.println("LoginException --> " + e.getMessage());

        }
    }

//    private void joinGroup() {
//
//        try {
//
//            if (connection.isAuthenticated()) {
//
//                MultiUserChatManager muc = MultiUserChatManager.getInstanceFor(connection);
//
//                MultiUserChat chat = muc.getMultiUserChat(Common.CHAT_ROOM);
//
//                long maxTimeMillis = dbHelper.getMaxTimeMillis();
//
//                SimpleDateFormat df = new SimpleDateFormat("dd'-'MM'-'y hh:mm:ss.SSS aa");
//                df.setTimeZone(TimeZone.getTimeZone("GMT"));
//                String result = df.format(maxTimeMillis);
//                Date date = df.parse(result);
//
//                if (preferences.getString(Common.gotHistory, "").equals("false")) {
//
//                    history = new DiscussionHistory();
//                    history.setSince(null);
//                    history.setSince(date);
//
//                } else {
//
//                    /*long maxTimeMillis = dbHelper.getMaxTimeMillis();
//
//                    SimpleDateFormat df = new SimpleDateFormat("dd'-'MM'-'y hh:mm:ss.SSS aa");
//                    df.setTimeZone(TimeZone.getTimeZone("GMT"));
//                    String result = df.format(maxTimeMillis);
//                    Date date = df.parse(result);*/
//
//                    history = new DiscussionHistory();
//                    history.setSince(date);
//
//                    String str = preferences.getString(Common.killedFromRecent, "");
//
//                    if (str.equals("true")) {
//
//                        if (preferences.getString(Common.isDiconnected, "").equals("true")) {
//
//                            /* If net is disconnected and then connected again then to get only new
//                             * messages. */
//                            history.setMaxStanzas(-1);
//
//                            System.out.println("setStanzaDisconnect --> called");
//
//                        } else {
//
//                            history.setMaxStanzas(0);
//
//                        }
//                    } else {
//
//                        /*if (preferences.getString(Common.isApplicationOpen, "").equals("true") &&
//                                preferences.getString(Common.isDiconnected, "").equals("true")) {
//
//                            history.setMaxStanzas(0);
//
//                        } else {*/
//
//                        history.setMaxStanzas(Integer.MIN_VALUE);
//
//                        //}
//                    }
//
//                    System.out.println("BackgroundXMPP_Else --> " + maxTimeMillis + ", " + result +
//                            ", " + date + ", " + str);
//
//                }
//
//                if (!chat.isJoined()) {
//
//                    chat.createOrJoin(Common.CHAT_USERNAME, null, history,
//                            SmackConfiguration.getDefaultPacketReplyTimeout());
//
//                }
//            } else {
//
//                System.out.println("JoinGroupAuthenticationError --> Client Not Connected");
//
//            }
//        } catch (Exception e) {
//
//            System.out.println("JoinGroupException --> " + e.getMessage());
//
//        }
//    }

    private void sendSystemNotification(String msg) {

        long when = System.currentTimeMillis();

        boolean lollipop = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, new Intent(),
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager mNotificationManager = (NotificationManager) context.
                getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mNotifyBuilder;

        if (lollipop) {

            mNotifyBuilder = new NotificationCompat.Builder(context).setContentTitle("ChatApp").
                    setStyle(new NotificationCompat.BigTextStyle().bigText(msg)).
                    setContentText(msg).setColor(Color.TRANSPARENT).setLargeIcon(BitmapFactory.
                    decodeResource(context.getResources(), R.mipmap.ic_launcher)).
                    setPriority(Notification.PRIORITY_HIGH).setSmallIcon(R.mipmap.ic_launcher).
                    setWhen(when).setAutoCancel(true);

            /* if you setPriority(Notification.PRIORITY_HIGH) then notification will be shown as
               dialog at top of device. (minimum required API : 21)*/

        } else {

            mNotifyBuilder = new NotificationCompat.Builder(context).
                    setStyle(new NotificationCompat.BigTextStyle().bigText(msg)).
                    setContentTitle("ChatApp").setContentText(msg).
                    setSmallIcon(R.mipmap.ic_launcher).setWhen(when).setAutoCancel(true);

        }

        // Set pending intent
        mNotifyBuilder.setContentIntent(resultPendingIntent);

        // Set Vibrate, Sound and Light
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        mNotifyBuilder.setSound(notification);
        long[] pattern = {1000, 1000, 1000, 1000};
        mNotifyBuilder.setVibrate(pattern);

        // now you can notify with newly created notification id
        mNotificationManager.notify(9002, mNotifyBuilder.build()); //if u want saperate notification for each and every message then replace 9002 with any random number which will be generated randomly everytime when notification occurs(see following method i.e. sendSystemNotification(String msg, String action))

    }

    // Chatting Notification Like WhatsApp
    private void chatMessagesNotification(ArrayList<String> msgsList) {

        long when = System.currentTimeMillis();

        PendingIntent chatMessagesPendingIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(Common.CHAT_ROOM);
        if ((msgsList.size() - 6) <= 0) {

            for (int i = 0; i < msgsList.size(); i++) {

                inboxStyle.addLine(msgsList.get(i));

            }
        } else {

            for (int i = (msgsList.size() - 6); i < msgsList.size(); i++) {

                inboxStyle.addLine(msgsList.get(i));

            }
        }

        NotificationCompat.Builder mNotifyBuilder;

        if (msgsList.size() == 1) {

            inboxStyle.setSummaryText("" + msgsList.size() + " New Message");

        } else if (msgsList.size() > 1) {

            inboxStyle.setSummaryText("" + msgsList.size() + " New Messages");

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (msgsList.size() == 1) {

                mNotifyBuilder = new NotificationCompat.Builder(context).
                        setContentTitle(Common.CHAT_ROOM).setStyle(inboxStyle).
                        setDefaults(Notification.DEFAULT_ALL).setColor(Color.TRANSPARENT).
                        setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                                R.mipmap.ic_launcher)).
                        setContentText(msgsList.get(0)).setGroupSummary(true).
                        setGroup("chatApp").setSmallIcon(R.mipmap.ic_launcher).
                        setCategory(NotificationCompat.CATEGORY_MESSAGE).
                        setPriority(Notification.PRIORITY_HIGH).setWhen(when).setAutoCancel(true);

            } else {

                mNotifyBuilder = new NotificationCompat.Builder(context).
                        setContentTitle(Common.CHAT_ROOM).setStyle(inboxStyle).
                        setDefaults(Notification.DEFAULT_ALL).setColor(Color.TRANSPARENT).
                        setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                                R.mipmap.ic_launcher)).setContentText("" + msgsList.size() +
                        " New Messages").setSmallIcon(R.mipmap.ic_launcher).
                        setCategory(NotificationCompat.CATEGORY_MESSAGE).setGroupSummary(true).
                        setGroup("chatApp").setPriority(Notification.PRIORITY_HIGH).setWhen(when).
                        setAutoCancel(true);

            }
        } else {

            mNotifyBuilder = new NotificationCompat.Builder(context).
                    setContentTitle(Common.CHAT_ROOM).setStyle(inboxStyle).
                    setDefaults(Notification.DEFAULT_ALL).setColor(Color.TRANSPARENT).
                    setContentText("" + msgsList.size() + " New Message").
                    setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                            R.mipmap.ic_launcher)).setGroupSummary(true).setGroup("chatApp").
                    setCategory(NotificationCompat.CATEGORY_MESSAGE).
                    setSmallIcon(R.mipmap.ic_launcher).setWhen(when).setAutoCancel(true);

        }

        mNotifyBuilder.setContentIntent(chatMessagesPendingIntent);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        mNotifyBuilder.setSound(notification);
        long[] pattern = {1000, 1000, 1000, 1000};
        mNotifyBuilder.setVibrate(pattern);
        mNotifyBuilder.setLights(Notification.DEFAULT_LIGHTS, 1000, 1000);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.
                from(context);

        //to post your notification to the notification bar
        notificationManagerCompat.notify(0, mNotifyBuilder.build());

    }

    private class ChatManagerListenerImpl implements ChatManagerListener {

        @Override
        public void chatCreated(final Chat chat, final boolean createdLocally) {

            Log.e("working", "admin " + createdLocally);

            chat.addMessageListener(mMessageListener);

        }
    }

    private class XMPPConnectionListener implements ConnectionListener {

        @Override
        public void connected(final XMPPConnection connection) {

            System.out.println("XMPP_Connection --> Connected");

            if (Common.getConnectivityStatusString(context)) {

                if (!connection.isAuthenticated()) {

                    login();
                }
            }
        }

        @Override
        public void connectionClosed() {

            System.out.println("connectionClosed --> Connection Closed");

        }

        @Override
        public void connectionClosedOnError(Exception arg0) {

            System.out.println("connectionClosedOnError --> Connection Closed On Error");

        }

        @Override
        public void reconnectingIn(int arg0) {

            System.out.println("reconnectingIn --> Reconnecting In " + arg0);

        }

        @Override
        public void reconnectionFailed(Exception arg0) {

            System.out.println("reconnectionFailed --> Reconnection Failed");

        }

        @Override
        public void reconnectionSuccessful() {

            System.out.println("reconnectionSuccessful --> Reconnected Successfully");

        }

        @Override
        public void authenticated(XMPPConnection arg0, boolean arg1) {

            Log.e("xmpp", "Authenticated!");

            ChatManager.getInstanceFor(connection).addChatListener(mChatManagerListener);

            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {

                        Thread.sleep(500);

                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        System.out.println("authenticatedExceptin --> " + e.getMessage());

                    }

                }
            }).start();
        }
    }

    private class MessageListener implements ChatMessageListener {

        MessageListener(Context contxt) {
        }

        @Override
        public void processMessage(final org.jivesoftware.smack.chat.Chat chat,
                                   final Message message) {
            Log.i("MyXMPP_MESSAGE_LISTENER", "Xmpp message received: '"
                    + message);

            if (message.getType() == Message.Type.chat
                    && message.getBody() != null) {
                final ChatMessage chatMessage = gson.fromJson(
                        message.getBody(), ChatMessage.class);
//                processMessage(chatMessage);
            }
        }

//        private void processMessage(final ChatMessage chatMessage) {
//
//            ChatFragement.chatMessagesAdapter.add(chatMessage);
//            new Handler(Looper.getMainLooper()).post(new Runnable() {
//
//                @Override
//                public void run() {
//                    ChatFragement.chatMessagesAdapter.notifyDataSetChanged();
//
//                }
//            });
//        }

    }
}
//    private class MessageListener implements ChatMessageListener {
//
//        MessageListener(Context context) {
//        }
//
//        @Override
//        public void processMessage(Chat chat, Message message) {
//
//            if (message.getType() == Message.Type.chat && message.getBody() != null) {
//            }
//        }
//    }

//    public static void sendMsgtoGroup(String msg) {
//
//        try {
//
//            if (connection.isAuthenticated()) {
//
//                MultiUserChatManager muc = MultiUserChatManager.getInstanceFor(connection);
//
//                MultiUserChat chat = muc.getMultiUserChat(Common.CHAT_ROOM);
//
//                Message message = new Message(Common.CHAT_ROOM, Message.Type.groupchat);
//                message.setFrom(Common.CHAT_USERNAME);
//                message.setBody(msg);
//
//                chat.sendMessage(message);
//
//            } else {
//
//                System.out.println("SendMsgError --> Client Not Connected");
//
//            }
//        } catch (Exception e) {
//
//            System.out.println("SendMsgException --> " + e.getMessage());
//
//        }
//    }
