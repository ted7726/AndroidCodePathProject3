package com.codepath.apps.twitterapp.Activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.bumptech.glide.Glide;
import com.codepath.apps.twitterapp.Adapters.ProfilePagerAdapter;
import com.codepath.apps.twitterapp.Adapters.TweetsArrayAdapter;
import com.codepath.apps.twitterapp.Adapters.TweetsPagerAdapter;
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
import butterknife.OnClick;

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
    @Bind(R.id.tvTweetsCount) TextView tvTweetsCount;
    @Bind(R.id.tvFollowersCount) TextView tvFollowersCount;
    @Bind(R.id.tvFollowingsCount) TextView tvFollowingsCount;
    @Bind(R.id.tvCoverTitleTitleBar) TextView tvCoverTitleTitleBar;
    @Bind(R.id.tvCoverScreenTextTitleBar) TextView tvCoverScreenTextTitleBar;
    @Bind(R.id.ivCoverUserProfile) ImageView ivCoverUserProfile;
    @Bind(R.id.rlCoverTitleBar) RelativeLayout rlCoverTitleBar;
    @Bind(R.id.rlDim) RelativeLayout rlDim;
    @Bind(R.id.ibFollowButton) ImageButton followButton;
    @Bind(R.id.vpViewPager) ViewPager vpViewPager;
    @Bind(R.id.pgTabs) PagerSlidingTabStrip pgSlidingTab;
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

        TwitterClient client = TwitterApplication.getRestClient(); // singleton client
        client.getUser(user, parseUserCallback());
        setUser(user);
        userProfileOriginY = Float.MIN_VALUE;
        userProfileHeight = ablProfileAppBar.getHeight();


        ablProfileAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                handleScrolling(appBarLayout, verticalOffset);
            }
        });

        ProfilePagerAdapter profilePagerAdapter= new ProfilePagerAdapter(getSupportFragmentManager(), user);
        vpViewPager.setAdapter(profilePagerAdapter);
        pgSlidingTab.setViewPager(vpViewPager);

    }

    private void setUser(User user) {
        this.user = user;
        if (user == null) return;

        // setup View
        tvProfileName.setText(Util.checkStringEmpty(user.name));
        tvCoverTitleTitleBar.setText(Util.checkStringEmpty(user.name));
        tvProfileUsername.setText(Util.checkStringEmpty("@" + user.screenName));
        tvCoverScreenTextTitleBar.setText(Util.checkStringEmpty(user.screenName));
        tvProfileDescription.setText(Util.checkStringEmpty(user.description));
        tvFollowersCount.setText(Util.checkStringEmpty(Integer.toString(user.followersCount)));
        tvFollowingsCount.setText(Util.checkStringEmpty(Integer.toString(user.friendsCount)));
        tvTweetsCount.setText(Util.checkStringEmpty(Integer.toString(user.listedCount)));
        followButton.setImageResource((user.following?R.drawable.ic_following:R.drawable.ic_follow));

        if (!TextUtils.isEmpty(user.profileCoverPhotoUrl)) {
            ivCoverBlurredPhoto.setAlpha(0.0f);
            Glide.with(ivCoverPhoto.getContext()).load(user.profileCoverPhotoUrl).placeholder(R.drawable.ic_profile).into(ivCoverPhoto);
            Util.blurrLoadingImage(ivCoverBlurredPhoto, user.profileCoverPhotoUrl);
            Glide.with(ivCoverPhotoTitleBar.getContext()).load(user.profileCoverPhotoUrl).placeholder(R.drawable.ic_profile).into(ivCoverPhotoTitleBar);
            Util.blurrLoadingImage(ivCoverBlurredPhotoTitleBar, user.profileCoverPhotoUrl);
        }

        if (!TextUtils.isEmpty(user.profileImageUrl)) {
            Context context = ivUserProfile.getContext();
            String url = user.getProfileImageUrlBigger();
            Glide.with(context).load(url).fitCenter().placeholder(R.drawable.ic_profile).into(ivUserProfile);
            Glide.with(ivCoverUserProfile.getContext()).load(url).fitCenter().placeholder(R.drawable.ic_profile).into(ivCoverUserProfile);
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

    private void handleScrolling(AppBarLayout appBarLayout, int verticalOffset) {
        float scale = 1.0f + (verticalOffset / 400.0f);
        if (scale < 0.5f) scale = 0.5f;
        ivUserProfile.setScaleX(scale);
        ivUserProfile.setScaleY(scale);

        if (userProfileOriginY == Float.MIN_VALUE) {
            userProfileOriginY = ivUserProfile.getY();
        }
        float coverTitleBarOffSet = tvProfileUsername.getY() + verticalOffset + 175.0f;

        if (coverTitleBarOffSet < 270) {
            coverTitleBarOffSet = 270;
        }
        rlCoverTitleBar.setY(coverTitleBarOffSet);
        if (scale == 0.5f) {
            ivUserProfile.setY(userProfileOriginY + (verticalOffset + 400.0f * (1 - scale)) / 2);
            rlTitleBar.setVisibility(View.VISIBLE);
        } else {
            rlTitleBar.setVisibility(View.INVISIBLE);
        }
        if (verticalOffset > -270) {
            rlTitleBar.setY(verticalOffset + 87.5f);
        }
        float alpha = Math.abs(verticalOffset / 400.0f);
        if (alpha > 1 ) alpha = 1.0f;
        ivCoverBlurredPhoto.setAlpha(alpha);
        ivCoverBlurredPhotoTitleBar.setAlpha(alpha);
        rlDim.setAlpha(Math.abs((verticalOffset + 200.0f) / 400.0f));

        Log.d("DEBUG", " ======= " + verticalOffset + " ======= " + tvProfileUsername.getY() + "," + rlCoverTitleBar.getY());

    }

    @OnClick(R.id.ibFollowButton)
    public void onFollowButtonClick () {
        TwitterClient client = TwitterApplication.getRestClient(); // singleton client
        if (user.following) {
            client.unfollowUser(user, followCallBackHandler(true));
        } else {
            client.followUser(user, followCallBackHandler(false));
        }
        followButton.setAlpha(0.5f);
    }

    private CallBack followCallBackHandler(final boolean isFollowing) {
        return new CallBack(){
            @Override
            public void userCallBack(User user) {
                followButton.setAlpha(1.0f);
                setUser(user);
                followButton.setImageResource(((!isFollowing)?R.drawable.ic_following:R.drawable.ic_follow));
            }
        };
    }

    @OnClick(R.id.ibMessageButton)
    public void onMessageCLick () {
        Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
        intent.putExtra("user", Parcels.wrap(user));

        startActivity(intent);
    }

}
