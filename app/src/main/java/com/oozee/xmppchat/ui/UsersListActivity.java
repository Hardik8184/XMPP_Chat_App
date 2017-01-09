package com.oozee.xmppchat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.oozee.xmppchat.R;
import com.oozee.xmppchat.adapter.UserViewAdapter;
import com.oozee.xmppchat.bean.Login;
import com.oozee.xmppchat.ofrestclient.client.RestApiClient;
import com.oozee.xmppchat.ofrestclient.client.Status;
import com.oozee.xmppchat.ofrestclient.entity.Roster;
import com.oozee.xmppchat.ofrestclient.entity.Rosters;
import com.oozee.xmppchat.ofrestclient.entity.User;
import com.oozee.xmppchat.ofrestclient.entity.Users;
import com.oozee.xmppchat.utils.Common;

import java.util.ArrayList;
import java.util.List;

public class UsersListActivity extends BaseActivity {
    private UserViewAdapter adapter;
    private List<User> users;
    private Login login;
    private KProgressHUD progressHUD;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_list);
        users = new ArrayList<>();

        login = getLogin();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(UsersListActivity.this));
        adapter = new UserViewAdapter(users, login, new UserViewAdapter.ClickListener() {
            @Override
            public void onClick(User mItem) {
                populateRoster(mItem);
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void populateRoster(final User mItem) {

        progressHUD = KProgressHUD.create(UsersListActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true).show();

        new RestApiClient(this, this.getAccount()).getUserRoster(login.getUsername(), new Response.Listener<Rosters>() {
            @Override
            public void onResponse(Object mTag, Rosters response) {

                if (response != null && response.getRoster() != null) {
                    for (int i = 0; i < response.getRoster().size(); i++) {

                        System.out.println("getUsername --> " + mItem.getUsername());
                        System.out.println("getSubscriptionType --> " + response.getRoster().get(i).getSubscriptionType());

                        if (mItem.getUsername().equals(response.getRoster().get(i).getNickname()) &&
                                response.getRoster().get(i).getSubscriptionType() == 3) {

                            progressHUD.dismiss();

                            Intent intent = new Intent(UsersListActivity.this, MainActivity.class);
                            intent.putExtra("other_user", response.getRoster().get(i).getNickname());
                            intent.putExtra("user_jid", response.getRoster().get(i).getJid());
                            startActivity(intent);

                            return;

                        } else {

                            saveRoster(mItem);
                        }
                    }
                } else
                    saveRoster(mItem);

                progressHUD.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("XMPP Chat App Error --> " + error.getMessage());
                progressHUD.dismiss();
            }
        });
    }

    private void saveRoster(final User mItem) {

        Roster roster = new Roster(mItem.getUsername() + "@" + Common.DOMAIN, mItem.getUsername(), 3);

        new RestApiClient(this, getAccount()).addUserToRoster(login.getUsername(), roster, new Response.Listener<Status>() {
            @Override
            public void onResponse(Object mTag, Status response) {

                progressHUD.dismiss();

                Toast.makeText(UsersListActivity.this, "saved!", Toast.LENGTH_SHORT).show();

                System.out.println("XMPP Chat App user detail --> " + mItem.toString());
                Intent intent = new Intent(UsersListActivity.this, MainActivity.class);
                intent.putExtra("other_user", mItem.getUsername());
                intent.putExtra("user_jid", mItem.getUsername() + "@" + Common.DOMAIN);
                startActivity(intent);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressHUD.dismiss();
//                finish();
            }
        });
    }

    public void onResume() {
        super.onResume();
        populateUserList();
    }

    private void populateUserList() {

        progressHUD = KProgressHUD.create(UsersListActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true).show();

        new RestApiClient(UsersListActivity.this, UsersListActivity.this.getAccount()).getUsers(
                new Response.Listener<Users>() {
                    @Override
                    public void onResponse(Object obj, Users user) {

                        progressHUD.dismiss();

                        if (user != null && user.getUsers() != null) {
                            users.clear();
                            users.addAll(user.getUsers());
                            adapter.notifyDataSetChanged();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressHUD.dismiss();
                        treat(error);
                    }
                });
    }

//    protected void retry(Error error) {
//        populateUserList();
//    }

//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_create_user) {
//            startActivity(new Intent(getActivity(), UserActivity.class));
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
