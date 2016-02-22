package com.codepath.apps.twitterapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterapp.Adapters.GalleryAdapter;
import com.codepath.apps.twitterapp.DialogFragment.GalleryPhotoFragment;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.Utils.Blur;
import com.codepath.apps.twitterapp.Utils.Util;
import com.codepath.apps.twitterapp.models.Tweet;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GalleryActivity extends AppCompatActivity {
    @Bind(R.id.tvGalleryName) TextView tvName;
    @Bind(R.id.tvGalleryTweetTexts) TextView tvTweetTexts;
    @Bind(R.id.tvGalleryBiggerTweetTexts) TextView tvTweetBiggerTexts;
    @Bind(R.id.tvGalleryTweetTime) TextView tvTweetTime;
    @Bind(R.id.tvGalleryUsername) TextView tvUsername;
    @Bind(R.id.ivGalleryUserProfile) ImageView  ivUserProfile;
    @Bind(R.id.rlGalleryCaption) RelativeLayout rlGalleryCaption;
    @Bind(R.id.pvGalleryPagerView) ViewPager pvGalleryPagerView;

    private GalleryAdapter galleryAdapter;
    private boolean captionIsShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        Tweet tweet = (Tweet) Parcels.unwrap(intent.getParcelableExtra("tweet"));
        tvName.setText(Util.checkStringEmpty(tweet.user.name));
        tvUsername.setText(Util.checkStringEmpty("@" + tweet.user.screenName));
        tvTweetTexts.setText(Util.checkStringEmpty(tweet.text));
        tvTweetBiggerTexts.setText(Util.checkStringEmpty(tweet.text));
        tvTweetTime.setText(Util.converTimetoRelativeTime(tweet.time));

        captionIsShown = false;
        tvTweetBiggerTexts.setVisibility(View.INVISIBLE);
        if (tweet.user!=null && !TextUtils.isEmpty(tweet.user.profileImageUrl)) {
            ivUserProfile.setImageResource(0);
            Context context = ivUserProfile.getContext();
            String url = tweet.user.profileImageUrl;
            Glide.with(context).load(url).fitCenter().into(ivUserProfile);
        }

        if (tweet.extendedEntities!=null && tweet.extendedEntities.medias!= null && tweet.extendedEntities.medias.size()>0) {
            galleryAdapter = new GalleryAdapter(getSupportFragmentManager(), getApplicationContext(), tweet.extendedEntities.medias);
            pvGalleryPagerView.setAdapter(galleryAdapter);
        }

        rlGalleryCaption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GalleryPhotoFragment galleryPhotoFragment = (GalleryPhotoFragment)galleryAdapter.getItem(pvGalleryPagerView.getCurrentItem());
                galleryPhotoFragment.blurPhoto(!captionIsShown);
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), (captionIsShown?R.anim.move_down:R.anim.move_up));
                rlGalleryCaption.startAnimation(animation);
                Util.alphaAnimationCreator(tvTweetBiggerTexts, !captionIsShown);
                Util.alphaAnimationCreator(tvTweetTexts, captionIsShown);
                captionIsShown = !captionIsShown;
            }
        });


    }
}
