package com.oozee.use1.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.oozee.use1.Common;
import com.oozee.use1.R;
import com.oozee.use1.bean.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by peacock on 21/9/16.
 */

public class ChatMessagesAdapter extends RecyclerView.Adapter<ChatMessagesAdapter.
        CustomViewHolder> {

    private Context context;

    private ArrayList<ChatMessage> chatMessageList;

    public ChatMessagesAdapter(Context context, ArrayList<ChatMessage> chatMessagesList) {
        this.context = context;
        this.chatMessageList = chatMessagesList;
    }

//    public void add(ChatMessage object) {
//        chatMessageList.add(object);
//    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View customView = LayoutInflater.from(context).inflate(R.layout.chat_messages_layout,
                parent, false);

        CustomViewHolder viewHolder = new CustomViewHolder(customView);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        String sender = chatMessageList.get(position).getSender();

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

        if (sender.equals(Common.CHAT_USERNAME_1)) {

            params.gravity = Gravity.RIGHT;

        } else {

            params.gravity = Gravity.LEFT;

        }

        holder.ll_chatLayoutInside.
                setBackgroundResource(sender.equals(Common.CHAT_USERNAME_1) ? R.drawable.sender :
                        R.drawable.reciever);

        holder.tv_message.setLayoutParams(params);

        holder.tv_senderName.setLayoutParams(params);
        holder.tv_senderName.setTextColor(Color.rgb(171, 176, 175));

        holder.tv_dateTime.setLayoutParams(params);
        holder.tv_dateTime.setTextColor(Color.rgb(171, 176, 175));

        if (sender.equals(Common.CHAT_USERNAME_1)) {

            //params.rightMargin = 10;

            holder.ll_chatLayout.setGravity(Gravity.RIGHT);

            //holder.tv_message.setGravity(Gravity.END);
            //holder.tv_message.setTextColor(Color.BLACK);

            holder.tv_senderName.setGravity(Gravity.END);
            //holder.tv_senderName.setTextColor(Color.rgb(171, 176, 175));
            /*holder.tv_senderName.setTextColor(context.getResources().
                    getColor(android.R.color.background_light));*/

            holder.tv_dateTime.setGravity(Gravity.END);
            //holder.tv_dateTime.setTextColor(Color.rgb(191, 196, 195));
            /*holder.tv_dateTime.setTextColor(context.getResources().
                    getColor(android.R.color.background_light));*/

        } else {

            //params.leftMargin = 10;

            holder.ll_chatLayout.setGravity(Gravity.LEFT);

            //holder.tv_message.setGravity(Gravity.START);
            //holder.tv_message.setTextColor(Color.parseColor("#6A6A6A"));

            holder.tv_senderName.setGravity(Gravity.START);

            //holder.tv_senderName.setTextColor(Color.RED);

            holder.tv_dateTime.setGravity(Gravity.START);

        }

        holder.tv_message.setText(chatMessageList.get(position).getMessage());

        holder.tv_senderName.setText(chatMessageList.get(position).getSender());

        holder.tv_dateTime.setText(dateTimeFromTimeMillis(chatMessageList.get(position).
                getDate_time()));

    }

    public void notifyMe(ArrayList<ChatMessage> chatMessagesList) {

        if (this.chatMessageList == chatMessagesList) {

            return;

        }

        this.chatMessageList = chatMessagesList;

        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {

        if (chatMessageList != null) {

            return chatMessageList.size();

        } else {

            return 0;

        }
    }

    private String dateTimeFromTimeMillis(String timeMillis) {

        Date msgDate = new Date(Long.valueOf(timeMillis));

        SimpleDateFormat formatter = new SimpleDateFormat("dd'-'MM'-'y h:mm aa");

        String dateTime = formatter.format(msgDate);

        return dateTime;

    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout ll_chatLayout, ll_chatLayoutInside;

        private TextView tv_message, tv_senderName, tv_dateTime;

        public CustomViewHolder(View convertView) {
            super(convertView);

            ll_chatLayout = (LinearLayout) convertView.findViewById(R.id.ll_chatLayout);

            ll_chatLayoutInside = (LinearLayout) convertView.findViewById(R.id.ll_chatLayoutInside);

            tv_message = (TextView) convertView.findViewById(R.id.tv_message);

            tv_senderName = (TextView) convertView.findViewById(R.id.tv_senderName);

            tv_dateTime = (TextView) convertView.findViewById(R.id.tv_dateTime);

        }
    }
}
