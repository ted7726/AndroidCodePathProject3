package com.codepath.apps.twitterapp.Views;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.codepath.apps.twitterapp.DialogFragment.ComposeDialog;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.TwitterApplication;
import com.codepath.apps.twitterapp.TwitterClient;
import com.codepath.apps.twitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wilsonsu on 2/20/16.
 */
public class TweetActionViewHolder extends RecyclerView.ViewHolder{
    private FragmentActivity fragmentActivity;
    @Bind(R.id.ibReplyButton) ImageButton ibReplyButton;
    @Bind(R.id.ibRetweetButton) ImageButton ibRetweetButton;
    @Bind(R.id.ibFavoriteButton) ImageButton ibFavoriteButton;
    @Bind(R.id.tvRetweetCount) TextView tvRetweetCount;
    @Bind(R.id.tvFavoriteCount) TextView tvFavoriteCount;
    public TweetActionViewHolder(View itemView, FragmentActivity fragmentActivity) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.fragmentActivity = fragmentActivity;
    }
    public void setTweet(final Tweet tweet) {
        tvRetweetCount.setText(Integer.toString(tweet.retweetCount));
        tvFavoriteCount.setText(Integer.toString(tweet.favoriteCount));
        ibFavoriteButton.setImageResource(tweet.favorited ? R.drawable.ic_liked : R.drawable.ic_like);
        ibRetweetButton.setImageResource(tweet.retweeted ? R.drawable.ic_retweeted : R.drawable.ic_retweet);

        ibReplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = fragmentActivity.getSupportFragmentManager();
                ComposeDialog composeFD = ComposeDialog.newInstance();
                composeFD.show(fm, "reply_fragment");
            }
        });

        ibFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwitterClient client = TwitterApplication.getRestClient(); // singleton client
                client.likeTweet(Long.toString(tweet.id), !tweet.favorited, parseUpdateTweetHandler());
            }
        });

        ibRetweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwitterClient client = TwitterApplication.getRestClient(); // singleton client
                client.retweet(Long.toString(tweet.id), !tweet.retweeted, parseUpdateTweetHandler());
            }
        });
    }

    private JsonHttpResponseHandler parseUpdateTweetHandler() {
        return new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Tweet tweet = Tweet.fromJson(response);
                setTweet(tweet);
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        };
    }
}
