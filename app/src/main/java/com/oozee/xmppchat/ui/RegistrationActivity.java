package com.oozee.xmppchat.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.oozee.xmppchat.R;
import com.oozee.xmppchat.ofrestclient.client.RestApiClient;
import com.oozee.xmppchat.ofrestclient.client.Status;
import com.oozee.xmppchat.ofrestclient.entity.User;

public class RegistrationActivity extends BaseActivity implements View.OnClickListener {

    private Activity activity;

    private EditText edtUserName, edtName, edtEmail, edtPassword, edtCPassword;
    private String strUserName;
    private String strName;
    private String strEmail;
    private String strPassword;

    private KProgressHUD progressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        activity = RegistrationActivity.this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ico_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edtUserName = (EditText) findViewById(R.id.edtUserName);
        edtName = (EditText) findViewById(R.id.edtName);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtCPassword = (EditText) findViewById(R.id.edtCPassword);

        findViewById(R.id.btnRegister).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btnRegister:

                strUserName = edtUserName.getText().toString();
                strName = edtName.getText().toString();
                strEmail = edtEmail.getText().toString();
                strPassword = edtPassword.getText().toString();
                String strCPassword = edtCPassword.getText().toString();

                if (strUserName.equals("")) {
                    edtUserName.setError("Enter user name");
                } else if (strName.equals("")) {
                    edtName.setError("Enter name");
                } else if (strEmail.equals("")) {
                    edtEmail.setError("Enter email address");
                } else if (strPassword.equals("")) {
                    edtPassword.setError("Enter password");
                } else if (!strCPassword.equals(strPassword)) {
                    edtCPassword.setError("Password and confirm password dose not match");
                } else {
                    saveUser();
                }

                break;

            default:

                break;
        }
    }

    private void saveUser() {

        progressHUD = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true).show();

        User user = new User(strUserName, strName, strEmail);
        user.setPassword(strPassword);
        new RestApiClient(this, getAccount()).createUser(user, new Response.Listener<Status>() {
            @Override
            public void onResponse(Object mTag, Status response) {
                progressHUD.dismiss();

                Toast.makeText(activity, "Saved!", Toast.LENGTH_SHORT).show();
                finish();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressHUD.dismiss();
                System.out.println("XMPP Chat App Error --> " + error.getMessage());
                treat(error);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}