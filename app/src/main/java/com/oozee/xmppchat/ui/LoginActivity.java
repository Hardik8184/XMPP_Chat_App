package com.oozee.xmppchat.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.oozee.xmppchat.R;
import com.oozee.xmppchat.bean.Login;
import com.oozee.xmppchat.ofrestclient.entity.Account;
import com.oozee.xmppchat.ofrestclient.entity.AuthenticationMode;
import com.oozee.xmppchat.ofrestclient.entity.AuthenticationToken;
import com.oozee.xmppchat.utils.Common;
import com.oozee.xmppchat.xmpp.BackgroundXMPP;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private Account account;
    private Activity activity;
    private Login login;

    private EditText edtUserName, edtPassword, edtOtherUser;
    private String strUserName, strPassword, strOtherUser;

    private BackgroundXMPP backgroundXMPP;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        activity = LoginActivity.this;

        Toolbar mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);

        edtUserName = (EditText) findViewById(R.id.edtUserName);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtOtherUser = (EditText) findViewById(R.id.edtOtherUser);

        findViewById(R.id.btnLogin).setOnClickListener(this);
        findViewById(R.id.txtRegister).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btnLogin:

                progress = new ProgressDialog(activity);
                progress.setMessage("Please wait..");
                progress.show();

                strUserName = edtUserName.getText().toString();
                strPassword = edtPassword.getText().toString();
                strOtherUser = edtOtherUser.getText().toString();

                if (strUserName.equals("")) {
                    Toast.makeText(activity, "Enter user name", Toast.LENGTH_SHORT).show();
                } else if (strPassword.equals("")) {
                    Toast.makeText(activity, "Enter password", Toast.LENGTH_SHORT).show();
                } else {

                    sharedPreferences.edit().putString("user_name", strUserName).commit();
                    sharedPreferences.edit().putString("password", strPassword).commit();
                    sharedPreferences.edit().putString("other_user", strOtherUser).commit();

                    sharedPreferences.edit().putString("user_jid", strUserName + "@95.138.180.254").commit();
                    sharedPreferences.edit().putString("other_user_jid", strOtherUser + "@95.138.180.254").commit();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            Looper.prepare();

                            if (Common.getConnectivityStatusString(activity)) {

                                backgroundXMPP = new BackgroundXMPP(activity,
                                        Common.DOMAIN, strUserName, strPassword, new BackgroundXMPP.ConnectionDone() {
                                    @Override
                                    public void onConnect() {

                                        login = new Login(strUserName, strPassword, strUserName + "@" + Common.DOMAIN);
                                        login.setUsername(strUserName);
                                        login.setPassword(strPassword);
                                        login.setJid(strUserName + "@" + Common.DOMAIN);

                                        saveLogin(login);

                                        progress.dismiss();

                                        startActivity(new Intent(activity, UsersListActivity.class));
                                        finish();
                                    }

                                    @Override
                                    public void onDisConnect() {

                                        progress.dismiss();
                                        Toast.makeText(activity, "Connection not establish.Try again later", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                backgroundXMPP.connect();
                            }

                            System.out.println("Background_OnCreate --> Background Service Started");

                        }
                    }).start();
                }

                break;

            case R.id.txtRegister:

                startActivity(new Intent(activity, RegistrationActivity.class));

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

        account = getAccount();
        if (account == null)
            validateAndSave();
    }

    private void validateAndSave() {

        if (this.account == null) {
            this.account = new Account(Common.DOMAIN, Integer.parseInt(Common.PORT));
        }
        this.account.setHost(Common.DOMAIN);
        this.account.setPort(Integer.parseInt(Common.PORT));
        AuthenticationToken authenticationToken;
        authenticationToken = new AuthenticationToken(Common.USERNAME, Common.PASSWORD);
        authenticationToken.setUsername(Common.USERNAME);
        authenticationToken.setPassword(Common.PASSWORD);
        authenticationToken.setAuthMode(AuthenticationMode.values()[0]);
        this.account.setAuthenticationToken(authenticationToken);
        saveAccount(this.account);
    }
}