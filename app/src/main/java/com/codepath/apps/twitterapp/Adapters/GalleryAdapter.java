package com.codepath.apps.twitterapp.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.codepath.apps.twitterapp.DialogFragment.GalleryPhotoFragment;
import com.codepath.apps.twitterapp.models.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wilsonsu on 2/21/16.
 */
public class GalleryAdapter extends FragmentStatePagerAdapter {
    private List<Tweet.EntitiesEntity.Media> medias;
    private Context context;
    private ArrayList<GalleryPhotoFragment> recycledFragments;

    public GalleryAdapter(FragmentManager fm, Context context, List<Tweet.EntitiesEntity.Media> medias) {
        super(fm);
        this.context = context;
        this.medias = medias;
        recycledFragments = new ArrayList<>();
        for (int i=0;i<medias.size();++i) recycledFragments.add(null);
    }

    @Override
    public Fragment getItem(int position) {

        GalleryPhotoFragment galleryPhotoFragment = recycledFragments.get(position);
        if (galleryPhotoFragment == null) {
            Tweet.EntitiesEntity.Media media = medias.get(position);
            galleryPhotoFragment = new GalleryPhotoFragment();
            galleryPhotoFragment.setMedia(media);
            recycledFragments.set(position, galleryPhotoFragment);
        }
        return recycledFragments.get(position);
    }

    @Override
    public int getCount() {
        return medias.size();
    }

}
