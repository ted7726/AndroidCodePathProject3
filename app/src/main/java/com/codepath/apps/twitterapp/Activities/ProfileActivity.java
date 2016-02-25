package com.codepath.apps.twitterapp.Activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterapp.Adapters.TweetsArrayAdapter;
import com.codepath.apps.twitterapp.CallBack;
import com.codepath.apps.twitterapp.Fragments.UserTimelineFragment;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.TwitterApplication;
import com.codepath.apps.twitterapp.TwitterClient;
import com.codepath.apps.twitterapp.Utils.Util;
import com.codepath.apps.twitterapp.models.Tweet;
import com.codepath.apps.twitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {
    @Bind(R.id.ivCoverPhoto) ImageView ivCoverPhoto;
    @Bind(R.id.ivCoverBlurredPhoto) ImageView ivCoverBlurredPhoto;
    @Bind(R.id.ivCoverPhotoTitleBar) ImageView ivCoverPhotoTitleBar;
    @Bind(R.id.ivCoverBlurredPhotoTitleBar) ImageView ivCoverBlurredPhotoTitleBar;
    @Bind(R.id.ivUserProfile) ImageView ivUserProfile;
    @Bind(R.id.tvProfileDescription) TextView tvProfileDescription;
    @Bind(R.id.tvProfileName) TextView tvProfileName;
    @Bind(R.id.tvProfileUsername) TextView tvProfileUsername;
    @Bind(R.id.ablProfileAppBar) AppBarLayout ablProfileAppBar;
    @Bind(R.id.rlTitleBar) RelativeLayout rlTitleBar;
    private UserTimelineFragment userTimelineFragment;
    private User user;
    private float userProfileOriginY;
    private int userProfileHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        User user = Parcels.unwrap(intent.getParcelableExtra("user"));
        if (savedInstanceState==null) {
            setUserTimelineFragment((UserTimelineFragment)getSupportFragmentManager().findFragmentById(R.id.user_timeline_fragment));
        }

        TwitterClient client = TwitterApplication.getRestClient(); // singleton client
        client.getUser(user, parseUserCallback());
        setUser(user);
        userProfileOriginY = Float.MIN_VALUE;
        userProfileHeight = ablProfileAppBar.getHeight();


        ablProfileAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            float scale = 1.0f + (verticalOffset / 150.0f);
            if (scale < 0.75f) scale = 0.75f;
            ivUserProfile.setScaleX(scale);
            ivUserProfile.setScaleY(scale);

            if (userProfileOriginY == Float.MIN_VALUE) {
                userProfileOriginY = ivUserProfile.getY();
            }
            if (scale == 0.75f) {
                ivUserProfile.setY(userProfileOriginY + verticalOffset / 2);
            }

            float alpha = Math.abs(verticalOffset / 150.0f);
            if (alpha > 1 ) alpha = 1.0f;
            ivCoverBlurredPhoto.setAlpha(alpha);
                Log.d("DEBUG"," ======= "+verticalOffset+" =======");
            if (verticalOffset < -260) {
                rlTitleBar.setVisibility(View.VISIBLE);
            } else {
                rlTitleBar.setVisibility(View.INVISIBLE);

            }
            }
        });

    }

    private void setUser(User user) {
        this.user = user;
        if (user == null) return;

        // setup View
        tvProfileName.setText(Util.checkStringEmpty(user.name));
        tvProfileUsername.setText(Util.checkStringEmpty("@" + user.screenName));
        tvProfileDescription.setText(Util.checkStringEmpty(user.description));

        if (!TextUtils.isEmpty(user.profileCoverPhotoUrl)) {
            ivCoverBlurredPhoto.setAlpha(0.0f);
            Glide.with(ivCoverPhoto.getContext()).load(user.profileCoverPhotoUrl).placeholder(R.drawable.ic_profile).into(ivCoverPhoto);
            Util.blurrLoadingImage(ivCoverBlurredPhoto, user.profileCoverPhotoUrl);
            Glide.with(ivCoverPhotoTitleBar.getContext()).load(user.profileCoverPhotoUrl).placeholder(R.drawable.ic_profile).into(ivCoverPhotoTitleBar);
            Util.blurrLoadingImage(ivCoverBlurredPhotoTitleBar, user.profileCoverPhotoUrl);
        }

        if (!TextUtils.isEmpty(user.profileImageUrl)) {
            Context context = ivUserProfile.getContext();
            String url = user.profileImageUrl;
            Glide.with(context).load(url).fitCenter().placeholder(R.drawable.ic_profile).into(ivUserProfile);
        }

        if (userTimelineFragment != null) {
            userTimelineFragment.setUserId(user.id);
        }
    }

    public void setUserTimelineFragment(UserTimelineFragment userTimelineFragment) {
        this.userTimelineFragment = userTimelineFragment;
        if (userTimelineFragment != null && user!=null) {
            userTimelineFragment.setUserId(user.id);
        }

    }

    private CallBack parseUserCallback() {
        return new CallBack() {
            @Override
            public void userCallBack(User user) {
                setUser(user);
            }
        };
    }




}
