package com.codepath.apps.twitterapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.apps.twitterapp.Adapters.TweetsArrayAdapter;
import com.codepath.apps.twitterapp.DialogFragment.ComposeDialog;
import com.codepath.apps.twitterapp.PersistModel.PersistentTweet;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.TwitterApplication;
import com.codepath.apps.twitterapp.TwitterClient;
import com.codepath.apps.twitterapp.Utils.EndlessRecyclerViewScrollListener;
import com.codepath.apps.twitterapp.Utils.RecyclerViewItemClickListener;
import com.codepath.apps.twitterapp.Utils.Util;
import com.codepath.apps.twitterapp.models.CurrentUser;
import com.codepath.apps.twitterapp.models.Tweet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TimelineActivity extends AppCompatActivity implements ComposeDialog.ComposeDialogListener {
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter tweetsArrayAdapter;
    private TwitterClient client;
    private long maxId;
    private int detailTweetPosition;
    private static final String TAG = "Wilson-DEBUG";
    @Bind(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @Bind(R.id.prLoadingSpinner) RelativeLayout prLoadingSpinner;
    @Bind(R.id.tvNetworkUnavailable) TextView tvNetworkUnavailable;

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
        tweetsArrayAdapter = new TweetsArrayAdapter(this, tweets);
        maxId = 1;
        detailTweetPosition = -1;
    }
    private void setup() {

        ButterKnife.bind(this);
        tvNetworkUnavailable.setVisibility(View.INVISIBLE);
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
                populateTimeline(page);
            }
        });

        final AppCompatActivity appCompatActivity = this;
        rvTimeline.addOnItemTouchListener(new RecyclerViewItemClickListener(this, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                detailTweetPosition = position;
                Intent intent = new Intent(getApplicationContext(), DetailTweetActivity.class);
                intent.putExtra("tweet", Parcels.wrap(tweets.get(position)));
                View tweetContentView = view.findViewById(R.id.tweetContentView);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(appCompatActivity, tweetContentView, "TweetContent");
                startActivity(intent, options.toBundle());
            }
        }));

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
        boolean hasNetwork = (isNetworkAvailable() && isOnline());
        toggleNetworkUnavailable(!hasNetwork);
        if (page>0) {
            prLoadingSpinner.setVisibility(View.VISIBLE);
            client.getHomeTimeline(maxId, timelineHandler(false));
        } else {
            if (!hasNetwork) {
                addTweets(PersistentTweet.getAll());
                swipeContainer.setRefreshing(false);
                return;
            }
            client.getHomeTimeline(maxId, timelineHandler(true));
            maxId = 1;
        }
    }

    private JsonHttpResponseHandler timelineHandler(final boolean isRefresh) {
        return new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Mon Feb 15 17:31:31 +0000 2016
                Gson gson = Util.gsonCreatorFortweeterDateFormater();
                Type listType = new TypeToken<ArrayList<Tweet>>() {}.getType();
                ArrayList<Tweet> newTweets = gson.fromJson(response.toString(), listType);

                try {
                    Util.persistData(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (isRefresh) {
                    tweets.clear();
                }

                addTweets(newTweets);
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                swipeContainer.setRefreshing(false);
                prLoadingSpinner.setVisibility(View.INVISIBLE);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        };
    }

    private void addTweets(ArrayList<Tweet> newTweets) {
        tweets.addAll(newTweets);
        tweetsArrayAdapter.notifyDataSetChanged();
        if (newTweets.size()>0) {
            maxId = newTweets.get(newTweets.size()-1).id;
        }
        swipeContainer.setRefreshing(false);
        prLoadingSpinner.setVisibility(View.INVISIBLE);
    }

    public void onFinishComposeDialog(String composeText) {
        prLoadingSpinner.setVisibility(View.VISIBLE);
        client.postNewTweet(composeText, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Tweet tweet = Tweet.fromJson(response);
                tweets.add(0, tweet);
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

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (detailTweetPosition > 0) {
            Tweet tweet = (Tweet) Parcels.unwrap(data.getParcelableExtra("tweet"));
            tweets.set(detailTweetPosition, tweet);
            tweetsArrayAdapter.notifyDataSetChanged();
        }
    }

    private void toggleNetworkUnavailable(final boolean show) {
        if ((tvNetworkUnavailable.getVisibility() == View.VISIBLE) == show) {
            return;
        }
        Util.alphaAnimationCreator(tvNetworkUnavailable, show, 800);
    }
}
