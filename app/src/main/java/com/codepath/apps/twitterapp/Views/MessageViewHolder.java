package com.codepath.apps.twitterapp.Views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.Utils.Util;
import com.codepath.apps.twitterapp.models.Message;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wilsonsu on 2/27/16.
 */
public class MessageViewHolder extends RecyclerView.ViewHolder{
    @Bind(R.id.ivMessageUserProfile) ImageView ivMessageUserProfile;
    @Bind(R.id.tvMessage) TextView tvMessage;
    @Bind(R.id.tvMessageTime) TextView tvMessageTime;

    public MessageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setMessage(Message message) {
        tvMessage.setText(Util.checkStringEmpty(message.text));
        tvMessageTime.setText(Util.converTimetoRelativeTime(message.time));
        if (!TextUtils.isEmpty(message.sender.profileImageUrl)) {
            Context context = ivMessageUserProfile.getContext();
            String url = message.sender.profileImageUrl;
            Glide.with(context).load(url).fitCenter().placeholder(R.drawable.ic_profile).into(ivMessageUserProfile);
        }
    }
}
