package com.codepath.apps.twitterapp.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.twitterapp.Fragments.MentionsTimelineFragment;
import com.codepath.apps.twitterapp.Fragments.MessageFragment;
import com.codepath.apps.twitterapp.Fragments.SearchFragment;
import com.codepath.apps.twitterapp.Fragments.TweetsListFragment;
import com.codepath.apps.twitterapp.R;

/**
 * Created by wilsonsu on 2/25/16.
 */
public class TweetsPagerAdapter extends FragmentPagerAdapter  implements PagerSlidingTabStrip.IconTabProvider {
    private String[] TAB_TITLES = {"Home", "Mentions", "Message", "Search"};
    private int TAB_ICONS[] = {R.drawable.ic_home, R.drawable.ic_mention, R.drawable.ic_message, R.drawable.ic_search};
    private Fragment[] fragments;


    public TweetsPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new Fragment[TAB_TITLES.length];
    }



    @Override
    public Fragment getItem(int position) {
        if (fragments[position] == null) {
            if (position == 0) {
                fragments[position] = new TweetsListFragment();
            } else if (position == 1) {
                fragments[position] = new  MentionsTimelineFragment();
            } else if (position == 2) {
                fragments[position] = new MessageFragment();
            } else if (position == 3) {
                fragments[position] = new SearchFragment();
            }
        }
        return fragments[position];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }

    @Override
    public int getPageIconResId(int position) {
        return TAB_ICONS[position];
    }



    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}
