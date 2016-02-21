package com.codepath.apps.twitterapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
    @Bind(R.id.tvGalleryTweetTime) TextView tvTweetTime;
    @Bind(R.id.tvGalleryUsername) TextView tvUsername;
    @Bind(R.id.ivGalleryPhotoView) ImageView ivPhotoView;
    @Bind(R.id.ivGalleryBlurredPhotoView) ImageView  ivBlurredPhotoView;
    @Bind(R.id.ivGalleryUserProfile) ImageView  ivUserProfile;
    @Bind(R.id.rlGalleryCaption) RelativeLayout rlGalleryCaption;

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
        tvTweetTime.setText(Util.converTimetoRelativeTime(tweet.time));

        if (tweet.user!=null && !TextUtils.isEmpty(tweet.user.profileImageUrl)) {
            ivUserProfile.setImageResource(0);
            Context context = ivUserProfile.getContext();
            String url = tweet.user.profileImageUrl;
            Glide.with(context).load(url).fitCenter().into(ivUserProfile);
        }

        ivBlurredPhotoView.setVisibility(View.INVISIBLE);

        if (tweet.entities!=null && tweet.entities.media!= null && tweet.entities.media.size()>0) {
            Tweet.EntitiesEntity.Media media = tweet.entities.media.get(0);
            if (!TextUtils.isEmpty(media.media_url)) {

                final Context context = ivBlurredPhotoView.getContext();
                Glide.with(ivPhotoView.getContext()).load(media.media_url).fitCenter().into(ivPhotoView);
                Picasso.with(context).load(media.media_url).transform(new Transformation() {
                    @Override
                    public Bitmap transform(Bitmap source) {
                        Bitmap b = Blur.fastblur(context, source, 20);
                        source.recycle();
                        return b;
                    }
                    @Override
                    public String key() {
                        return "blur";
                    }
                }).into(ivBlurredPhotoView);
            }
        }
        ivPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.alphaAnimationCreator(ivBlurredPhotoView, true);
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_up);
                rlGalleryCaption.startAnimation(animation);
            }
        });

        ivBlurredPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.alphaAnimationCreator(ivBlurredPhotoView, false);
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_down);
                rlGalleryCaption.startAnimation(animation);
            }
        });
    }
}
