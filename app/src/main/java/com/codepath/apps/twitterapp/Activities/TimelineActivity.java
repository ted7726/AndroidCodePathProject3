package com.codepath.apps.twitterapp.Activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.codepath.apps.twitterapp.Adapters.TweetsArrayAdapter;
import com.codepath.apps.twitterapp.DialogFragment.ComposeDialog;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.TwitterApplication;
import com.codepath.apps.twitterapp.TwitterClient;
import com.codepath.apps.twitterapp.Utils.EndlessRecyclerViewScrollListener;
import com.codepath.apps.twitterapp.models.CurrentUser;
import com.codepath.apps.twitterapp.models.Tweet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TimelineActivity extends AppCompatActivity implements ComposeDialog.ComposeDialogListener {
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter tweetsArrayAdapter;
    private TwitterClient client;
    private long maxId;
    private static final String TAG = "Wilson-DEBUG";
    @Bind(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @Bind(R.id.prLoadingSpinner) RelativeLayout prLoadingSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        init();
        setup();
        populateTimeline(0);
    }

    private void init() {
        client = TwitterApplication.getRestClient(); // singleton client
        tweets = new ArrayList<>();
        tweetsArrayAdapter = new TweetsArrayAdapter(tweets);
        maxId = 1;
    }
    private void setup() {

        ButterKnife.bind(this);
        // setup Recycler View
        RecyclerView rvTimeline = (RecyclerView) findViewById(R.id.rvTimeline);
        rvTimeline.setAdapter(tweetsArrayAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTimeline.setLayoutManager(linearLayoutManager);
        rvTimeline.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Log.d(TAG, "on load more" + page);
                populateTimeline(page);
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateTimeline(0);
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        new CurrentUser(client);

        FloatingActionButton composeButton = (FloatingActionButton) findViewById(R.id.fabComposeButton);
        composeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                ComposeDialog composeDialog = ComposeDialog.newInstance();
                composeDialog.show(fm, "compose_fragment");
            }
        });
    }

    private void populateTimeline(int page) {
        if (page>0) {
            prLoadingSpinner.setVisibility(View.VISIBLE);
            client.getHomeTimeline(maxId, timelineHandler(false));
        } else {
            client.getHomeTimeline(maxId, timelineHandler(true));
            maxId = 1;
        }
    }

    private JsonHttpResponseHandler timelineHandler(final boolean isRefresh) {
        return new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d(TAG, "onSuccess");
                // Mon Feb 15 17:31:31 +0000 2016
                Gson gson = gsonCreatorFortweeterDateFormater();
                Type listType = new TypeToken<ArrayList<Tweet>>() {}.getType();
                ArrayList<Tweet> newTweets = gson.fromJson(response.toString(), listType);
                if (isRefresh) {
                    tweets.clear();
                }
                tweets.addAll(newTweets);
                tweetsArrayAdapter.notifyDataSetChanged();
                if (newTweets.size()>0) {
                    maxId = newTweets.get(newTweets.size()-1).id;
                    Log.d(TAG, "get max id:" + maxId);
                }
                swipeContainer.setRefreshing(false);
                prLoadingSpinner.setVisibility(View.INVISIBLE);
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                prLoadingSpinner.setVisibility(View.INVISIBLE);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        };
    }

    public void onFinishComposeDialog(String composeText) {
        prLoadingSpinner.setVisibility(View.VISIBLE);
        client.postNewTweet(composeText, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Gson gson = gsonCreatorFortweeterDateFormater();
                Tweet tweet = gson.fromJson(response.toString(), Tweet.class);
                tweets.add(0,tweet);
                tweetsArrayAdapter.notifyDataSetChanged();
                prLoadingSpinner.setVisibility(View.INVISIBLE);
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                prLoadingSpinner.setVisibility(View.INVISIBLE);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private Gson gsonCreatorFortweeterDateFormater() {
        // Mon Feb 15 17:31:31 +0000 2016
        return new GsonBuilder().setDateFormat("EEE MMM dd HH:mm:ss zzzzz yyyy").create();
    }
}
