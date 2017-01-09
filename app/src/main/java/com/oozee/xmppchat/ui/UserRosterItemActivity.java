package com.oozee.xmppchat.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.oozee.xmppchat.R;
import com.oozee.xmppchat.ofrestclient.client.RestApiClient;
import com.oozee.xmppchat.ofrestclient.client.Status;
import com.oozee.xmppchat.ofrestclient.entity.Roster;
import com.oozee.xmppchat.ofrestclient.entity.User;
import com.oozee.xmppchat.ofrestclient.entity.wrapper.RosterGroup;

import java.util.Arrays;

public class UserRosterItemActivity extends BaseActivity {
    private static final String TAG = "UserRosterItemActivity";
    private EditText groups;
    private EditText jid;
    private EditText nickname;
    private Roster roster;
    private User user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_roster_item);
        this.actionBar.setDisplayHomeAsUpEnabled(true);
        this.jid = (EditText) findViewById(R.id.jid);
        this.nickname = (EditText) findViewById(R.id.nickname);
        this.groups = (EditText) findViewById(R.id.groups);
        Button delete = (Button) findViewById(R.id.delete);
        ((Button) findViewById(R.id.save)).setOnClickListener(new C02501());
        delete.setOnClickListener(new C02522());
        this.user = (User) getIntent().getSerializableExtra("user");
        if (getIntent().hasExtra("roster")) {
            this.roster = (Roster) getIntent().getSerializableExtra("roster");
            this.jid.setText(this.roster.getJid());
            this.nickname.setText(this.roster.getNickname());
            if (this.roster.getGroups() != null) {
                this.groups.setText(this.roster.getGroups().toString());
            }
            this.actionBar.setTitle(this.roster.getJid());
            return;
        }
        this.actionBar.setTitle((int) R.string.title_activity_add_user_to_roster);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteRoster() {
        new RestApiClient(this, getAccount()).deleteFromRoster(this.user.getUsername(), this.roster.getJid(), new C04883(), new C04894());
    }

    private void saveRoster() {
        Roster roster = new Roster(this.jid.getText().toString(), this.nickname.getText().toString(), 0);
        String[] mGroups = this.groups.getText().toString().trim().split(",");
        if (mGroups.length > 0) {
            roster.setGroups(new RosterGroup(Arrays.asList(mGroups)));
        }
        new RestApiClient(this, getAccount()).addUserToRoster(this.user.getUsername(), roster, new C04905(), new C04916());
    }

    private void updateRoster() {
        Roster roster = new Roster(this.jid.getText().toString(), this.nickname.getText().toString(), this.roster.getSubscriptionType());
        String[] mGroups = this.groups.getText().toString().trim().split(",");
        if (mGroups.length > 0) {
            roster.setGroups(new RosterGroup(Arrays.asList(mGroups)));
        }
        new RestApiClient(this, getAccount()).updateRosterEntry(this.user.getUsername(), roster, new C04927(), new C04938());
    }

    /* renamed from: com.oozee.openfirerestclient.activity.UserRosterItemActivity.1 */
    class C02501 implements OnClickListener {
        C02501() {
        }

        public void onClick(View v) {
            if (UserRosterItemActivity.this.roster != null) {
                UserRosterItemActivity.this.updateRoster();
            } else {
                UserRosterItemActivity.this.saveRoster();
            }
        }
    }

    /* renamed from: com.oozee.openfirerestclient.activity.UserRosterItemActivity.2 */
    class C02522 implements OnClickListener {

        C02522() {
        }

        public void onClick(View v) {
//            AppDialogs.showDeleteUserAlert(UserRosterItemActivity.this, new C02511());
        }

        /* renamed from: com.oozee.openfirerestclient.activity.UserRosterItemActivity.2.1 */
        class C02511 implements DialogInterface.OnClickListener {
            C02511() {
            }

            public void onClick(DialogInterface dialog, int which) {
                UserRosterItemActivity.this.deleteRoster();
            }
        }
    }

    /* renamed from: com.oozee.openfirerestclient.activity.UserRosterItemActivity.3 */
    class C04883 implements Listener<Status> {
        C04883() {
        }

        public void onResponse(Object mTag, Status response) {
            Log.d(UserRosterItemActivity.TAG, "deleteRoster : onResponse: status : " + response);
            Toast.makeText(UserRosterItemActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();
            UserRosterItemActivity.this.finish();
        }
    }

    /* renamed from: com.oozee.openfirerestclient.activity.UserRosterItemActivity.4 */
    class C04894 implements ErrorListener {
        C04894() {
        }

        public void onErrorResponse(VolleyError error) {
        }
    }

    /* renamed from: com.oozee.openfirerestclient.activity.UserRosterItemActivity.5 */
    class C04905 implements Listener<Status> {
        C04905() {
        }

        public void onResponse(Object mTag, Status response) {
            Log.d(UserRosterItemActivity.TAG, "saveRoster : onResponse: status : " + response);
            Toast.makeText(UserRosterItemActivity.this, "saved!", Toast.LENGTH_SHORT).show();
            UserRosterItemActivity.this.finish();
        }
    }

    /* renamed from: com.oozee.openfirerestclient.activity.UserRosterItemActivity.6 */
    class C04916 implements ErrorListener {
        C04916() {
        }

        public void onErrorResponse(VolleyError error) {
            UserRosterItemActivity.this.finish();
        }
    }

    /* renamed from: com.oozee.openfirerestclient.activity.UserRosterItemActivity.7 */
    class C04927 implements Listener<Status> {
        C04927() {
        }

        public void onResponse(Object mTag, Status response) {
            Log.d(UserRosterItemActivity.TAG, "onResponse: updateRoster : status : " + response);
            Toast.makeText(UserRosterItemActivity.this, "Updated!", Toast.LENGTH_SHORT).show();
        }
    }

    /* renamed from: com.oozee.openfirerestclient.activity.UserRosterItemActivity.8 */
    class C04938 implements ErrorListener {
        C04938() {
        }

        public void onErrorResponse(VolleyError error) {
        }
    }
}
