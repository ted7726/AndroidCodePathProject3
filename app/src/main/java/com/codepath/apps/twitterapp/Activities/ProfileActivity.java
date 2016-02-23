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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterapp.Adapters.TweetsArrayAdapter;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.Utils.Util;
import com.codepath.apps.twitterapp.models.User;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {
    @Bind(R.id.rvProfile) RecyclerView rvProfile;
    @Bind(R.id.ivCoverPhoto) ImageView ivCoverPhoto;
    @Bind(R.id.ivCoverBlurredPhoto) ImageView ivCoverBlurredPhoto;
    @Bind(R.id.ivUserProfile) ImageView ivUserProfile;
    @Bind(R.id.tvProfileDescription) TextView tvProfileDescription;
    @Bind(R.id.tvProfileName) TextView tvProfileName;
    @Bind(R.id.tvProfileUsername) TextView tvProfileUsername;
    @Bind(R.id.ablProfileAppBar) AppBarLayout ablProfileAppBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        User user = Parcels.unwrap(intent.getParcelableExtra("user"));
        setUser(user);
        rvProfile.setAdapter(TweetsArrayAdapter.sharedAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvProfile.setLayoutManager(linearLayoutManager);

        ablProfileAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d("DEBUG", "parent offset: " + verticalOffset);
            }
        });



        rvProfile.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("DEBUG", "offset: " + rvProfile.computeVerticalScrollOffset());
            }
        });

    }

    private void setUser(User user) {
        if (user == null) return;
        tvProfileName.setText(Util.checkStringEmpty(user.name));
        tvProfileUsername.setText(Util.checkStringEmpty("@" + user.screenName));
        tvProfileDescription.setText(Util.checkStringEmpty(user.description));

        if (!TextUtils.isEmpty(user.profileCoverPhotoUrl)) {
            ivCoverBlurredPhoto.setVisibility(View.INVISIBLE);
            Glide.with(ivCoverPhoto.getContext()).load(user.profileCoverPhotoUrl).placeholder(R.drawable.ic_profile).into(ivCoverPhoto);
            Util.blurrLoadingImage(ivCoverBlurredPhoto, user.profileCoverPhotoUrl);
        }

        if (!TextUtils.isEmpty(user.profileImageUrl)) {
            Context context = ivUserProfile.getContext();
            String url = user.profileImageUrl;
            Glide.with(context).load(url).fitCenter().placeholder(R.drawable.ic_profile).into(ivUserProfile);
        }

    }




}
