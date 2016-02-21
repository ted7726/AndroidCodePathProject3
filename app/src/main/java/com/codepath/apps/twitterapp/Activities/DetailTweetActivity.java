package com.codepath.apps.twitterapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.codepath.apps.twitterapp.DialogFragment.ComposeDialog;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.TwitterApplication;
import com.codepath.apps.twitterapp.TwitterClient;
import com.codepath.apps.twitterapp.Views.TweetActionViewHolder;
import com.codepath.apps.twitterapp.Views.TweetViewHolder;
import com.codepath.apps.twitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailTweetActivity extends AppCompatActivity implements ComposeDialog.ComposeDialogListener {
    @Bind(R.id.tweetContentView) View tweetContentView;
    @Bind(R.id.tweetActionView) View tweetActionView;
    private Tweet tweet;
    private TweetViewHolder viewHolder;
    private TweetActionViewHolder viewActionHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tweet);
        ButterKnife.bind(this);
        viewHolder = new TweetViewHolder(tweetContentView, this);
        viewActionHolder = new TweetActionViewHolder(tweetActionView, this);

        Intent intent = getIntent();
        Tweet tweet = (Tweet) Parcels.unwrap(intent.getParcelableExtra("tweet"));
        setTweet(tweet);
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
        viewHolder.setTweet(tweet);
        viewActionHolder.setTweet(tweet);
    }

    @Override
    public void onFinishComposeDialog(String composeText) {
        TwitterClient client = TwitterApplication.getRestClient(); // singleton client
        composeText = "@"+tweet.user.screenName + " " + composeText;

        client.replyTweet(composeText, Long.toString(tweet.id), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                tweet = Tweet.fromJson(response);
                setTweet(tweet);
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
}
