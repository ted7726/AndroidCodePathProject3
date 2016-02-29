package com.codepath.apps.twitterapp.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.twitterapp.Fragments.FollowersFragment;
import com.codepath.apps.twitterapp.Fragments.MentionsTimelineFragment;
import com.codepath.apps.twitterapp.Fragments.MessageFragment;
import com.codepath.apps.twitterapp.Fragments.SearchFragment;
import com.codepath.apps.twitterapp.Fragments.TweetsListFragment;
import com.codepath.apps.twitterapp.R;

/**
 * Created by wilsonsu on 2/28/16.
 */



public class ProfilePagerAdapter extends FragmentPagerAdapter {
    private String[] TAB_TITLES = {"Home", "Followings", "Followers"};
    private Fragment[] fragments;


    public ProfilePagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new Fragment[TAB_TITLES.length];
    }



    @Override
    public Fragment getItem(int position) {
        if (fragments[position] == null) {
            if (position == 0) {
                fragments[position] = new TweetsListFragment();
            } else if (position == 1) {
                fragments[position] = new MessageFragment();
            } else if (position == 2) {
                fragments[position] = new FollowersFragment();
            }
        }
        return fragments[position];
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


