package com.oozee.xmppchat.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.oozee.xmppchat.R;
import com.oozee.xmppchat.dataBase.AppDataBase;
import com.oozee.xmppchat.fragment.ChatFragment;
import com.oozee.xmppchat.fragment.HomeFragment;
import com.oozee.xmppchat.imgcrop.CropImage;
import com.oozee.xmppchat.imgcrop.CropImageView;
import com.oozee.xmppchat.utils.Common;
import com.oozee.xmppchat.xmpp.BackgroundXMPP;

import java.io.File;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public TextView tv_badge;
    private Activity activity;
    private Uri mCropImageUri;
    private String other_user, user_jid;
    private SharedPreferences sharedPreferences;
    private BroadcastReceiver updateBadgesReciever = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String key = intent.getStringExtra("udate_badge");

            if (key.equals("new_messages_recieved")) {

                String unreadMessageCount = intent.getStringExtra("badge_count");

                if (unreadMessageCount != null && unreadMessageCount.equals("0")) {

                    tv_badge.setVisibility(GONE);

                } else {

                    if (sharedPreferences.getString(Common.isFragmentOpen, "").equals("true")) {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = MainActivity.this;

        Toolbar mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);

        AppDataBase dbHelper = new AppDataBase(activity);

        tv_badge = (TextView) mainToolbar.findViewById(R.id.tv_badge);
        mainToolbar.findViewById(R.id.fl_startChatting).setOnClickListener(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putString(Common.isAppInRecent, "false").commit();

        if (sharedPreferences.getString(Common.isFragmentOpen, "").equals("false")) {

            String badges = dbHelper.getNewUnreadMessagesCounts(sharedPreferences.getString("user_name", "admin"));
            if (badges != null && !badges.equals("") && !badges.equals("0")) {

                tv_badge.setText(badges);
                tv_badge.setVisibility(VISIBLE);

            } else {
                tv_badge.setVisibility(GONE);
            }
        }

        other_user = getIntent().getStringExtra("other_user");
        user_jid = getIntent().getStringExtra("user_jid");

        LocalBroadcastManager.getInstance(activity).registerReceiver(updateBadgesReciever,
                new IntentFilter("update_badges_broadcast"));

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("user_jid", user_jid);
        bundle.putString("other_user", other_user);
        fragment.setArguments(bundle);
        ft.replace(R.id.container, fragment).addToBackStack("main").commit();
        overridePendingTransition(R.anim.slideup, R.anim.slide_down);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.fl_startChatting:

                if (BackgroundXMPP.connection.isConnected()) {

                    tv_badge.setText("0");
                    tv_badge.setVisibility(GONE);

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ChatFragment newFragment = new ChatFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("user_jid", user_jid);
                    bundle.putString("other_user", other_user);
                    newFragment.setArguments(bundle);
                    ft.replace(R.id.container, newFragment).addToBackStack("home").commit();
                    overridePendingTransition(R.anim.slideup, R.anim.slide_down);


                } else {
                    Toast.makeText(activity, "Please wait connection is establish", Toast.LENGTH_SHORT).show();
                }

                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {

        System.out.println("XMPP Chat App Count --> " + getSupportFragmentManager().getBackStackEntryCount());

        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
            finish();
//            Intent intent = new Intent(activity, UsersListActivity.class);
//            startActivity(intent);
//            overridePendingTransition(R.anim.slideup, R.anim.slide_down);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        BackgroundXMPP.disconnect();
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
}