package com.codepath.apps.twitterapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.Utils.Util;
import com.codepath.apps.twitterapp.models.Tweet;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by weishengsu on 2/15/16.
 */
public class TweetsArrayAdapter extends RecyclerView.Adapter<TweetsArrayAdapter.TweetViewHolder> {

    private ArrayList<Tweet> tweets;

    public TweetsArrayAdapter(ArrayList<Tweet> tweets) {
        this.tweets = tweets;
    }

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_tweet, parent, false);
        TweetViewHolder viewHolder = new TweetViewHolder(view);
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

    public static class TweetViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ivStatus) ImageView ivStatus;
        @Bind(R.id.ivMedia) ImageView ivMedia;
        @Bind(R.id.ivUserProfile) ImageView ivUserProfile;
        @Bind(R.id.tvName) TextView tvName;
        @Bind(R.id.tvUsername) TextView tvUsername;
        @Bind(R.id.tvStatus) TextView  tvStatus;
        @Bind(R.id.tvTweetTags) TextView tvTweetTags;
        @Bind(R.id.tvTweetTexts) TextView tvTweetTexts;
        @Bind(R.id.tvTweetTime) TextView tvTweetTime;

        public TweetViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any TweetViewHolder instance.
            super(itemView);
            ButterKnife.bind(this, itemView);
            Util.setLayoutHeight(false, ivStatus);
            Util.setLayoutHeight(false, tvStatus);
        }

        public void setTweet(Tweet tweet) {

            tvName.setText(Util.checkStringEmpty(tweet.user.name));
            tvUsername.setText(Util.checkStringEmpty("@" + tweet.user.screenName));
            tvTweetTexts.setText(Util.checkStringEmpty(tweet.text));
            tvTweetTime.setText(Util.converTimetoRelativeTime(tweet.time));
            if (tweet.entities!= null && tweet.entities.users!=null && tweet.entities.users.size()>0) {
                int len = tweet.entities.users.size();
                String tags = "";
                for (int i=0;i<len;++i) {
                    tags += " @"+tweet.entities.users.get(i).screenName;
                }
                tvTweetTags.setText(tags);
                Util.setLayoutHeight(true, tvTweetTags);
            } else {
                tvTweetTags.setText("");
                Util.setLayoutHeight(false, tvTweetTags);
            }
            if (tweet.user!=null && !TextUtils.isEmpty(tweet.user.profileImageUrl)) {
                ivUserProfile.setImageResource(0);
                Context context = ivUserProfile.getContext();
                String url = tweet.user.profileImageUrl;
                Glide.with(context).load(url).override(48, 48).fitCenter().placeholder(R.drawable.ic_profile).into(ivUserProfile);
            }
            ivMedia.setImageResource(0);
            if (tweet.entities!=null && tweet.entities.media!= null && tweet.entities.media.size()>0) {
                Tweet.EntitiesEntity.Media media = tweet.entities.media.get(0);
                if (!TextUtils.isEmpty(media.media_url)) {
                    Context context = ivMedia.getContext();
                    Glide.with(context).load(media.media_url).override(media.sizes.medium.w, media.sizes.medium.h).fitCenter().placeholder(R.drawable.ic_pics).into(ivMedia);
                }
            }
        }

    }
}
