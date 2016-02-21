package com.codepath.apps.twitterapp.Adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.Views.TweetViewHolder;
import com.codepath.apps.twitterapp.models.Tweet;

import java.util.ArrayList;

/**
 * Created by weishengsu on 2/15/16.
 */
public class TweetsArrayAdapter extends RecyclerView.Adapter<TweetViewHolder> {

    private ArrayList<Tweet> tweets;
    private FragmentActivity timelineActivity;

    public TweetsArrayAdapter(FragmentActivity timelineActivity, ArrayList<Tweet> tweets) {
        this.tweets = tweets;
        this.timelineActivity = timelineActivity;
    }

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_tweet, parent, false);
        TweetViewHolder viewHolder = new TweetViewHolder(view, timelineActivity);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position) {
        holder.setTweet(tweets.get(position));
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }
}
