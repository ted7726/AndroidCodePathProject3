package com.codepath.apps.twitterapp.DialogFragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.Utils.Blur;
import com.codepath.apps.twitterapp.Utils.Util;
import com.codepath.apps.twitterapp.models.Tweet;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wilsonsu on 2/21/16.
 */
public class GalleryPhotoFragment extends Fragment {
    private Tweet.EntitiesEntity.Media media;
    @Bind(R.id.ivGalleryPhotoView) ImageView ivPhotoView;
    @Bind(R.id.ivGalleryBlurredPhotoView) ImageView ivBlurredPhotoView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.item_gallery_photo, container, false);
        ButterKnife.bind(this, rootView);
        setupGalleryPhoto();
        return rootView;
    }

    public void setMedia(Tweet.EntitiesEntity.Media media) {
        this.media = media;
    }

    private void setupGalleryPhoto() {
        if (media == null) {
            return;
        }

        ivBlurredPhotoView.setVisibility(View.INVISIBLE);
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

    public void blurPhoto(boolean blur) {
        Util.alphaAnimationCreator(ivBlurredPhotoView, blur);
    }
}
