package com.codepath.apps.twitterapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.bumptech.glide.load.engine.Resource;
import com.codepath.apps.twitterapp.CallBack;
import com.codepath.apps.twitterapp.DialogFragment.ComposeDialog;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.TwitterApplication;
import com.codepath.apps.twitterapp.TwitterClient;
import com.codepath.apps.twitterapp.Utils.Blur;
import com.codepath.apps.twitterapp.Views.TweetViewHolder;
import com.codepath.apps.twitterapp.models.Tweet;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.apache.http.Header;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailTweetActivity extends AppCompatActivity implements ComposeDialog.ComposeDialogListener {
    @Bind(R.id.tweetContentView)
    View tweetContentView;
    private Tweet tweet;
    private TweetViewHolder viewHolder;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tweet);
        ButterKnife.bind(this);
        viewHolder = new TweetViewHolder(tweetContentView, this);

        final Intent intent = getIntent();
        final Tweet tweet = (Tweet) Parcels.unwrap(intent.getParcelableExtra("tweet"));
        setTweet(tweet);
        TextView tvTweetTexts = (TextView) findViewById(R.id.tvTweetTexts);
        tvTweetTexts.setTextSize(24);

        TwitterClient client = TwitterApplication.getRestClient(); // singleton client
        client.getTweet(Long.toString(tweet.id), parseTweetHandler());
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
        viewHolder.setTweet(tweet);
    }

    @Override
    public void onFinishComposeDialog(String composeText) {
        TwitterClient client = TwitterApplication.getRestClient(); // singleton client
        composeText = "@" + tweet.user.screenName + " " + composeText;
        client.replyTweet(composeText, Long.toString(tweet.id), parseTweetHandler());
    }

    @Override
    public void onFinishComposeDialog(String composeText, String tweetId) {}

    private CallBack parseTweetHandler() {
        return new CallBack() {
            @Override
            public void tweetCallBack(Tweet tweet) {
                setTweet(tweet);
            }
        };
    }

    public void onDismiss(View v) {
        Intent intent = new Intent();
        intent.putExtra("tweet", Parcels.wrap(tweet));
        supportFinishAfterTransition();
    }
}
