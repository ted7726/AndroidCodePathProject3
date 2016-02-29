package com.codepath.apps.twitterapp.Activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.twitterapp.Adapters.TweetsPagerAdapter;
import com.codepath.apps.twitterapp.CallBack;
import com.codepath.apps.twitterapp.DialogFragment.ComposeDialog;
import com.codepath.apps.twitterapp.Fragments.TweetsListFragment;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.TwitterApplication;
import com.codepath.apps.twitterapp.TwitterClient;
import com.codepath.apps.twitterapp.models.CurrentUser;
import com.codepath.apps.twitterapp.models.Tweet;

import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TimelineActivity extends AppCompatActivity implements ComposeDialog.ComposeDialogListener {
    @Bind(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @Bind(R.id.vpViewPager) ViewPager vpViewPager;
    @Bind(R.id.pgTabs) PagerSlidingTabStrip pgSlidingTab;
    @Bind(R.id.fabComposeButton) FloatingActionButton composeButton;
    private static final String TAG = "Wilson-DEBUG";
    private TweetsPagerAdapter tweetsPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        setup(savedInstanceState);
    }

    private void setup(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        tweetsPagerAdapter = new TweetsPagerAdapter(getSupportFragmentManager());
        vpViewPager.setAdapter(tweetsPagerAdapter);
        pgSlidingTab.setViewPager(vpViewPager);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

        pgSlidingTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setupComposeButton(position);
            }

            @Override
            public void onPageSelected(int position) {
                setupComposeButton(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        // loading user profile
        TwitterClient client = TwitterApplication.getRestClient(); // singleton client
        new CurrentUser(client);
    }

    private void setupComposeButton(int item) {
        if (item < 2) {
            composeButton.setImageResource(android.R.drawable.ic_menu_edit);
        } else {
            composeButton.setImageResource(R.drawable.ic_profile_white);
        }
    }

    @OnClick(R.id.fabComposeButton)
    public void onClickCompose() {
        if (vpViewPager.getCurrentItem()<2) {
            FragmentManager fm = getSupportFragmentManager();
            ComposeDialog composeDialog = ComposeDialog.newInstance();
            composeDialog.show(fm, "compose_fragment");
        }
        else {
            startUserProfile();
        }
    }

    public void onFinishComposeDialog(String composeText) {
        onFinishComposeDialog(composeText, null);
    }


    public void onFinishComposeDialog(String composeText, String tweetId) {
        Fragment fragment = tweetsPagerAdapter.getItem(vpViewPager.getCurrentItem());
        if (!(fragment instanceof TweetsListFragment)) {
            return;
        }
        TweetsListFragment tweetsListFragment = (TweetsListFragment) fragment;
        if (tweetId==null) {
            tweetsListFragment.onFinishComposeDialog(composeText);
        }
        tweetsListFragment.onFinishComposeDialog(composeText, tweetId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startUserProfile() {
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        intent.putExtra("user", Parcels.wrap(CurrentUser.user));
        startActivity(intent);
    }
}
