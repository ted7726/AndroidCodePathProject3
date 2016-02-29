package com.codepath.apps.twitterapp.Views;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
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
import com.codepath.apps.twitterapp.Activities.DetailTweetActivity;
import com.codepath.apps.twitterapp.Activities.GalleryActivity;
import com.codepath.apps.twitterapp.Activities.ProfileActivity;
import com.codepath.apps.twitterapp.CallBack;
import com.codepath.apps.twitterapp.DialogFragment.ComposeDialog;
import com.codepath.apps.twitterapp.DialogFragment.VideoFragmentDialog;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.TwitterApplication;
import com.codepath.apps.twitterapp.TwitterClient;
import com.codepath.apps.twitterapp.Utils.Util;
import com.codepath.apps.twitterapp.models.Tweet;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wilsonsu on 2/20/16.
 */
public class TweetViewHolder extends RecyclerView.ViewHolder {
    private FragmentActivity fragmentActivity;
    @Bind(R.id.ivStatus)
    ImageView ivStatus;
    @Bind(R.id.ivMedia) ImageView ivMedia;
    @Bind(R.id.ivUserProfile) ImageView ivUserProfile;
    @Bind(R.id.tvName) TextView tvName;
    @Bind(R.id.tvUsername) TextView tvUsername;
    @Bind(R.id.tvStatus) TextView  tvStatus;
    @Bind(R.id.tvTweetTags) TextView tvTweetTags;
    @Bind(R.id.tvTweetTexts) TextView tvTweetTexts;
    @Bind(R.id.tvTweetTime) TextView tvTweetTime;
    @Bind(R.id.ibPlayButton) ImageButton ibPlayButton;
    @Bind(R.id.rlMediaView) RelativeLayout rlMediaView;
    @Bind(R.id.ibReplyButton) ImageButton ibReplyButton;
    @Bind(R.id.ibRetweetButton) ImageButton ibRetweetButton;
    @Bind(R.id.ibFavoriteButton) ImageButton ibFavoriteButton;
    @Bind(R.id.tvRetweetCount) TextView tvRetweetCount;
    @Bind(R.id.tvFavoriteCount) TextView tvFavoriteCount;
    private Tweet tweet;

    public TweetViewHolder(final View itemView, final FragmentActivity fragmentActivity) {
        // Stores the itemView in a public final member variable that can be used
        // to access the fragmentActivity from any TweetViewHolder instance.
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.fragmentActivity = fragmentActivity;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;

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
        tvRetweetCount.setText(Integer.toString(tweet.retweetCount));
        tvFavoriteCount.setText(Integer.toString(tweet.favoriteCount));
        ibFavoriteButton.setImageResource(tweet.favorited ? R.drawable.ic_liked : R.drawable.ic_like);

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
        if (tweet.entities!=null && tweet.entities.media!= null && tweet.entities.media.size()>0 && tweet.extendedEntities!=null && tweet.extendedEntities.medias!=null && tweet.extendedEntities.medias.size()>0) {
            final Tweet.EntitiesEntity.Media media = tweet.extendedEntities.medias.get(0);
            if (media.video!=null && media.video.variants.size()>0 && media.video.variants.get(0).url!=null) {
                ibPlayButton.setVisibility(View.VISIBLE);
                final String url = media.video.variants.get(0).url;
                ibPlayButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onVideoPlayClick(url);
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

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick();
            }
        });
    }

    private CallBack parseUpdateTweetHandler() {
        return new CallBack(){
            @Override
            public void tweetCallBack(Tweet tweet) {
                setTweet(tweet);
            }
        };
    }



    /** click handlers: **/
    private void onItemClick() {
        if (fragmentActivity instanceof DetailTweetActivity) {
            return;
        }
        Intent intent = new Intent(fragmentActivity.getApplicationContext(), DetailTweetActivity.class);
        intent.putExtra("tweet", Parcels.wrap(tweet));
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(fragmentActivity, itemView, "TweetContent");
        fragmentActivity.startActivity(intent, options.toBundle());

    }

    @OnClick(R.id.ivMedia)
    public void onMediaClick() {
        Intent galleryIntent = new Intent(fragmentActivity.getApplicationContext(), GalleryActivity.class);
        galleryIntent.putExtra("tweet", Parcels.wrap(tweet));
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(fragmentActivity, ivMedia, "GalleryPhoto");
        fragmentActivity.startActivity(galleryIntent, options.toBundle());

    }

    @OnClick(R.id.ivUserProfile)
    public void onProfileClick() {
        Intent intent = new Intent(fragmentActivity.getApplicationContext(), ProfileActivity.class);
        Tweet passingTweet = tweet;
        if (tweet.retweet != null) {
            passingTweet = tweet.retweet;
        }
        intent.putExtra("user", Parcels.wrap(passingTweet.user));
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(fragmentActivity, ivUserProfile, "TweetProfile");
        fragmentActivity.startActivity(intent, options.toBundle());
    }

    @OnClick(R.id.ibReplyButton)
    public void onReplyClick() {
        FragmentManager fm = fragmentActivity.getSupportFragmentManager();
        ComposeDialog composeFD = ComposeDialog.newInstance(tweet);
        composeFD.show(fm, "reply_fragment");
    }

    @OnClick(R.id.ibFavoriteButton)
    public void onHeart() {
        TwitterClient client = TwitterApplication.getRestClient(); // singleton client
        client.likeTweet(Long.toString(tweet.id), !tweet.favorited, parseUpdateTweetHandler());
    }

    @OnClick(R.id.ibRetweetButton)
    public void onRetweet() {
        TwitterClient client = TwitterApplication.getRestClient(); // singleton client
        client.retweet(Long.toString(tweet.id), !tweet.retweeted, parseUpdateTweetHandler());
    }

    public void onVideoPlayClick(final String url) {
        FragmentManager fm = fragmentActivity.getSupportFragmentManager();
        VideoFragmentDialog videoFD = VideoFragmentDialog.newInstance(url);
        videoFD.show(fm, "fragment_video");
    }

}
