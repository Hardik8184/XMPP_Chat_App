package com.oozee.xmppchat.adapter;

import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oozee.xmppchat.R;
import com.oozee.xmppchat.bean.Login;
import com.oozee.xmppchat.ofrestclient.entity.User;

import java.util.List;

public class UserViewAdapter extends Adapter<UserViewAdapter.ViewHolder> {

    private final List<User> users;
    private Login login;
    private ClickListener clickListener;

    public UserViewAdapter(List<User> properties, Login login, ClickListener clickListener) {
        this.users = properties;
        this.login = login;
        this.clickListener = clickListener;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false));
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mItem = this.users.get(position);

        if (!login.getUsername().equals(users.get(position).getUsername())) {
            holder.mUsernameView.setText(String.format("User Name : %s", this.users.get(position).getUsername()));
            holder.mNameView.setText(String.format("Name : %s", this.users.get(position).getName()));
        } else {
            holder.mView.setVisibility(View.GONE);
        }

        holder.mView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null)
                    clickListener.onClick(holder.mItem);
            }
        });
    }

    public int getItemCount() {
        return this.users.size();
    }

    public interface ClickListener {
        void onClick(User mItem);
    }

    class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        final TextView mNameView;
        final TextView mUsernameView;
        final View mView;
        User mItem;

        ViewHolder(View view) {
            super(view);
            this.mView = view;
            this.mUsernameView = (TextView) view.findViewById(R.id.username);
            this.mNameView = (TextView) view.findViewById(R.id.name);
        }

        public String toString() {
            return super.toString() + " '" + this.mNameView.getText() + "'";
        }
    }
}
