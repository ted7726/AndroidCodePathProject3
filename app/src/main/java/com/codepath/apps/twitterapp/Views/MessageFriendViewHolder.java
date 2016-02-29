package com.codepath.apps.twitterapp.Views;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterapp.Activities.DetailTweetActivity;
import com.codepath.apps.twitterapp.Activities.MessageActivity;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.Utils.Util;
import com.codepath.apps.twitterapp.models.User;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wilsonsu on 2/27/16.
 */
public class MessageFriendViewHolder extends RecyclerView.ViewHolder {
    private FragmentActivity fragmentActivity;
    private User user;
    @Bind(R.id.tvMessageName) TextView tvMessageName;
    @Bind(R.id.tvMessageLocation) TextView tvMessageLocation;
    @Bind(R.id.tvLastMessages) TextView tvLastMessages;
    @Bind(R.id.ivMessageUserProfile) ImageView ivMessageUserProfile;
    @Bind(R.id.ivCoverBlurredPhoto) ImageView ivCoverBlurredPhoto;

    public MessageFriendViewHolder(View itemView, FragmentActivity fragmentActivity) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.fragmentActivity = fragmentActivity;
    }

    public void setUser(User user) {
        this.user = user;
        tvMessageName.setText(Util.checkStringEmpty(user.name));
        tvLastMessages.setText(Util.checkStringEmpty("@" + user.screenName));
        tvMessageLocation.setText(Util.checkStringEmpty(user.location));
        if (!TextUtils.isEmpty(user.profileImageUrl)) {
            Context context = ivMessageUserProfile.getContext();
            String url = user.getProfileImageUrlBigger();
            Glide.with(context).load(url).fitCenter().placeholder(R.drawable.ic_profile).into(ivMessageUserProfile);
        }
        if (!TextUtils.isEmpty(user.profileCoverPhotoUrl)) {
            Util.blurrLoadingImage(ivCoverBlurredPhoto, user.profileCoverPhotoUrl);
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick();
            }
        });
    }
    private void onItemClick() {
        if (fragmentActivity instanceof MessageActivity) {
            return;
        }
        Intent intent = new Intent(fragmentActivity.getApplicationContext(), MessageActivity.class);
        intent.putExtra("user", Parcels.wrap(user));
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(fragmentActivity, itemView, "userContent");
        fragmentActivity.startActivity(intent, options.toBundle());
    }


}
