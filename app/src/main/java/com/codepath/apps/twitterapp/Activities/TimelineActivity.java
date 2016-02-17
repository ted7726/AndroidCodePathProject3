package com.codepath.apps.twitterapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.codepath.apps.twitterapp.Adapters.TweetsArrayAdapter;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.TwitterApplication;
import com.codepath.apps.twitterapp.TwitterClient;
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

public class TimelineActivity extends AppCompatActivity {
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter tweetsArrayAdapter;

    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        init();
        setup();
        populateTimeline();
    }

    private void init() {
        client = TwitterApplication.getRestClient(); // singleton client
        tweets = new ArrayList<>();
        tweetsArrayAdapter = new TweetsArrayAdapter(tweets);
    }
    private void setup() {
        // setup Recycler View
        RecyclerView rvTimeline = (RecyclerView) findViewById(R.id.rvTimeline);
        rvTimeline.setAdapter(tweetsArrayAdapter);
        rvTimeline.setLayoutManager(new LinearLayoutManager(this));
    }

    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Mon Feb 15 17:31:31 +0000 2016
                Gson gson = new GsonBuilder().setDateFormat("EEE MMM dd HH:mm:ss zzzzz yyyy").create();

                Type listType = new TypeToken<ArrayList<Tweet>>() {}.getType();
                ArrayList<Tweet> newTweets = gson.fromJson(response.toString(), listType);
                tweets.addAll(newTweets);
                tweetsArrayAdapter.notifyDataSetChanged();
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

        });
    }

}
