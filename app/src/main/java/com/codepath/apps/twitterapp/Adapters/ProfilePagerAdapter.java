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
import com.codepath.apps.twitterapp.Fragments.UserTimelineFragment;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.models.User;

/**
 * Created by wilsonsu on 2/28/16.
 */



public class ProfilePagerAdapter extends FragmentPagerAdapter {
    private String[] TAB_TITLES = {"Home", "Followings", "Followers"};
    private Fragment[] fragments;
    User user;


    public ProfilePagerAdapter(FragmentManager fm, User user) {
        super(fm);
        this.user = user;
        fragments = new Fragment[TAB_TITLES.length];
    }



    @Override
    public Fragment getItem(int position) {
        if (fragments[position] == null) {
            if (position == 0) {
                UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
                userTimelineFragment.setUserId(user.id);
                fragments[position] = userTimelineFragment;
            } else if (position == 1) {
                MessageFragment messageFragment = new MessageFragment();
                messageFragment.setUser(user);
                fragments[position] = messageFragment;
            } else if (position == 2) {
                FollowersFragment followersFragment = new FollowersFragment();
                followersFragment.setUser(user);
                fragments[position] = followersFragment;
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


