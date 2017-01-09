package com.oozee.xmppchat.ui;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.oozee.xmppchat.R;
import com.oozee.xmppchat.bean.Login;
import com.oozee.xmppchat.ofrestclient.entity.Account;
import com.oozee.xmppchat.utils.Error;

public class BaseActivity extends AppCompatActivity {
    public Account account;
    public Login login;
    public SharedPreferences sharedPreferences;
    public Toolbar toolbar;
    protected ActionBar actionBar;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        getAccount();
    }

    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if (this.toolbar != null) {
            setSupportActionBar(this.toolbar);
        }
        if (getSupportActionBar() != null) {
            this.actionBar = getSupportActionBar();
        }
    }

    public void saveAccount(Account account) {
        this.account = account;
        this.sharedPreferences.edit().putString("account", new Gson().toJson(this.account)).apply();
    }

    public void saveLogin(Login login) {
        this.login = login;
        this.sharedPreferences.edit().putString("login", new Gson().toJson(this.login)).apply();
    }

    public Login getLogin() {
        this.login = new Gson().fromJson(this.sharedPreferences.getString("login", ""), Login.class);
        return this.login;
    }

    public Account getAccount() {
        this.account = new Gson().fromJson(this.sharedPreferences.getString("account", ""), Account.class);
        return this.account;
    }

    public void treat(final Error error) {
        if (this.toolbar != null) {
            Snackbar.make(this.toolbar, error.message(), Snackbar.LENGTH_SHORT).setAction("RETRY",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            lambda$treat$0(error, view);
                        }
                    }).show();
        }
    }

    /* synthetic */ void lambda$treat$0(Error error, View view) {
        retry(error);
    }

    public void treat(VolleyError error) {
        treat(new Error(error.getMessage()));
    }

    protected void retry(Error error) {
    }

    public void bake(String success) {
        Toast.makeText(this, success, Toast.LENGTH_SHORT).show();
    }

    public void indicate(boolean progress) {
    }
}
