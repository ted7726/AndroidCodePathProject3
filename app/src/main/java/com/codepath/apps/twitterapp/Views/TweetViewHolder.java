package com.codepath.apps.twitterapp.Views;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterapp.DialogFragment.VideoFragmentDialog;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.Utils.Util;
import com.codepath.apps.twitterapp.models.Tweet;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wilsonsu on 2/20/16.
 */
public class TweetViewHolder extends RecyclerView.ViewHolder {
    private FragmentActivity timelineActivity;
    @Bind(R.id.ivStatus)
    ImageView ivStatus;
    @Bind(R.id.ivMedia) ImageView ivMedia;
    @Bind(R.id.ivUserProfile) ImageView ivUserProfile;
    @Bind(R.id.tvName)
    TextView tvName;
    @Bind(R.id.tvUsername) TextView tvUsername;
    @Bind(R.id.tvStatus) TextView  tvStatus;
    @Bind(R.id.tvTweetTags) TextView tvTweetTags;
    @Bind(R.id.tvTweetTexts) TextView tvTweetTexts;
    @Bind(R.id.tvTweetTime) TextView tvTweetTime;
    @Bind(R.id.ibPlayButton)
    ImageButton ibPlayButton;
    @Bind(R.id.rlMediaView)
    RelativeLayout rlMediaView;

    public TweetViewHolder(View itemView, FragmentActivity timelineActivity) {
        // Stores the itemView in a public final member variable that can be used
        // to access the timelineActivity from any TweetViewHolder instance.
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.timelineActivity = timelineActivity;

    }

    public void setTweet(Tweet tweet) {

        if (tweet.retweet!=null) {
            Util.setLayoutHeight(true, ivStatus);
            Util.setLayoutHeight(true, tvStatus);
            tvStatus.setText(tweet.user.name + " Retweeted");
            ivStatus.setImageResource(R.drawable.ic_retweeted);
            tweet = tweet.retweet;
        } else if (tweet.replyTweetId != null && tweet.replyTweetUserScreenName != null) {
            Util.setLayoutHeight(true, ivStatus);
            Util.setLayoutHeight(true, tvStatus);
            tvStatus.setText("In reply to " + tweet.replyTweetUserScreenName);
            ivStatus.setImageResource(R.drawable.ic_reply);
        } else {
            Util.setLayoutHeight(false, ivStatus);
            Util.setLayoutHeight(false, tvStatus);
        }

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
            Glide.with(context).load(url).fitCenter().placeholder(R.drawable.ic_profile).into(ivUserProfile);
        }
        ibPlayButton.setVisibility(View.INVISIBLE);
        ivMedia.setImageResource(0);
        if (tweet.entities!=null && tweet.entities.media!= null && tweet.entities.media.size()>0) {
            final Tweet.EntitiesEntity.Media media = tweet.entities.media.get(0);
            if (media.video!=null && media.video.variants.size()>0 && media.video.variants.get(0).url!=null) {
                ibPlayButton.setVisibility(View.VISIBLE);
                final String url = media.video.variants.get(0).url;

                ibPlayButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fm = timelineActivity.getSupportFragmentManager();
                        VideoFragmentDialog videoFD = VideoFragmentDialog.newInstance(url);
                        videoFD.show(fm, "fragment_video");
                    }
                });
            }

            if (!TextUtils.isEmpty(media.media_url)) {
                Context context = ivMedia.getContext();
                Glide.with(context).load(media.media_url).override(media.sizes.medium.w, media.sizes.medium.h).fitCenter().placeholder(R.drawable.ic_pics).into(ivMedia);
            }
            Util.setLayoutHeight(true, rlMediaView);
        } else {
            Util.setLayoutHeight(false, rlMediaView);

        }
    }

}
