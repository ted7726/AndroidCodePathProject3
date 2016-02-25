package com.codepath.apps.twitterapp.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.twitterapp.Fragments.MentionsTimelineFragment;
import com.codepath.apps.twitterapp.Fragments.TweetsListFragment;

/**
 * Created by wilsonsu on 2/25/16.
 */
public class TweetsPagerAdapter extends FragmentPagerAdapter {
    private String[] TAB_TITLES = {"Home", "Mentions"};


    public TweetsPagerAdapter(FragmentManager fm) {
        super(fm);
    }



    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new TweetsListFragment();
        } else if (position == 1) {
            return new MentionsTimelineFragment();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}
