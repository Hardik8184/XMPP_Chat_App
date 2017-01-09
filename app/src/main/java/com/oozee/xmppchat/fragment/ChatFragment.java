package com.oozee.xmppchat.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.oozee.xmppchat.R;
import com.oozee.xmppchat.adapter.ChatMessagesAdapter;
import com.oozee.xmppchat.bean.ChatMessage;
import com.oozee.xmppchat.database.AppDataBase;
import com.oozee.xmppchat.imgcrop.CropImage;
import com.oozee.xmppchat.imgcrop.CropImageView;
import com.oozee.xmppchat.utils.Common;
import com.oozee.xmppchat.xmpp.BackgroundXMPP;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.app.Activity.RESULT_OK;

/**
 * Created by peacock on 8/10/16.
 */

public class ChatFragment extends Fragment implements View.OnClickListener {

    public static ChatMessagesAdapter chatMessagesAdapter;
    private Activity activity;
    private ArrayList<ChatMessage> chatMessagesList;
    private RecyclerView rv_chatList;
    private EditText et_message;
    private TextView presenceStatus;
    private Timer timer;
    private ImageView imgGallery;
    private LinearLayoutManager llManager;
    private Random random;
    private SharedPreferences preferences;
    private AppDataBase dbHelper;
    private Uri mCropImageUri;
    private String user_jid, other_user;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        activity = getActivity();

        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        preferences.edit().putString(Common.isFragmentOpen, "true").commit();

        dbHelper = new AppDataBase(activity);

        random = new Random();

//        Window window = getDialog().getWindow();
//        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        window.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
//        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//
//        DisplayMetrics displaymetrics = new DisplayMetrics();
//
//        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//
//        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.
//                SOFT_INPUT_ADJUST_RESIZE);
//        getDialog().setCancelable(true);
//        getDialog().setCanceledOnTouchOutside(true);
//        getDialog().getWindow().setLayout(displaymetrics.widthPixels, displaymetrics.heightPixels -
//                65);

        View chat_layout = inflater.inflate(R.layout.chat_layout, container, false);

        chat_layout.setFocusableInTouchMode(true);
        chat_layout.setFocusable(true);

        user_jid = getArguments().getString("user_jid");
        other_user = getArguments().getString("other_user");

        et_message = (EditText) chat_layout.findViewById(R.id.et_message);
        presenceStatus = (TextView) chat_layout.findViewById(R.id.presenceStatus);

        imgGallery = (ImageView) chat_layout.findViewById(R.id.imgGallery);
        imgGallery.setOnClickListener(this);

        rv_chatList = (RecyclerView) chat_layout.findViewById(R.id.rv_chatList);
        llManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        llManager.setStackFromEnd(true);
        rv_chatList.setLayoutManager(llManager);

        chat_layout.findViewById(R.id.ibtn_sendMessage).setOnClickListener(this);

        chatMessagesList = new ArrayList<>();
        chatMessagesAdapter = new ChatMessagesAdapter(activity, chatMessagesList, preferences);

        setRecyclerView();

        presenceStatus();

//        if (getDialog() == null) {
//            return null;
//        }
//        getDialog().getWindow().setWindowAnimations(R.style.ChatLayoutAnimation);

        return chat_layout;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogTheme);
//    }

    private void setRecyclerView() {

        chatMessagesList = dbHelper.getAllMessages(preferences.getString("user_name", "admin"));

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

            case R.id.imgGallery:

                startCropActivity();

                break;
        }
    }

    private void startCropActivity() {

        CropImage.startPickImageActivity(activity);
    }

    @Override
    @SuppressLint("NewApi")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(activity, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(activity, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                System.out.println("CabFinder uri --> " + result.getUri());
                System.out.println("CabFinder img path --> " + new File(getRealPathFromURI(result.getUri())));

//                bitmap = Utils.decodeFile(new File(getRealPathFromURI(result.getUri())), 400, 400);

                String filePath = getRealPathFromURI(result.getUri());
                BackgroundXMPP.sendFile(filePath);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(activity, "cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            startCropImageActivity(mCropImageUri);
        } else {
            Toast.makeText(activity, "cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Start crop image activity for the given image.
     */
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(activity);
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private void presenceStatus() {

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                final int userState = BackgroundXMPP.getPresence(user_jid);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (userState == 1) {
                            presenceStatus.setText(String.format("%s is Online", other_user));
                        } else if (userState == 0) {
                            presenceStatus.setText(String.format("%s is Offline", other_user));
                        } else if (userState == 2) {
                            presenceStatus.setText(String.format("%s is busy", other_user));
                        } else if (userState == 3) {
                            presenceStatus.setText(String.format("%s is away from chat", other_user));
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

            BackgroundXMPP.sendMessage(chatMessage, user_jid);
            dbHelper.insertMessagesToDB(preferences.getString("user_name", "admin"), msgId, preferences.getString("user_name", "admin")
                    , message, String.valueOf(System.currentTimeMillis()), "true");

            chatMessagesList.add(new ChatMessage(preferences.getString("user_name", "admin"), msgId, preferences.getString("user_name", "admin")
                    , message, String.valueOf(System.currentTimeMillis()), "true"));

            chatMessagesAdapter.notifyMe(chatMessagesList);
            rv_chatList.scrollToPosition(chatMessagesAdapter.getItemCount() - 1);

            et_message.setText("");
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//
//        dbHelper.setAllMessagesAsRead();
//        preferences.edit().putString(Common.isAppInRecent, "false").commit();
//
//        if (chatMessagesAdapter != null) {
//
//            chatMessagesList = dbHelper.getAllMessages(preferences.getString("user_name", "admin"));
//
//            chatMessagesAdapter.notifyMe(chatMessagesList);
//
//            rv_chatList.scrollToPosition(chatMessagesAdapter.getItemCount() - 1);
//
//        }
//
//        if (timer != null)
//            presenceStatus();
//
//        LocalBroadcastManager.getInstance(activity).registerReceiver(receiver,
//                new IntentFilter("update_chat_list"));
//    }

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
