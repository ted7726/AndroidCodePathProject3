package com.codepath.apps.twitterapp.models;

import com.codepath.apps.twitterapp.PersistModel.PersistentTweet;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wilsonsu on 2/26/16.
 */
public class TweetsController {
    private static TweetsController mInstance;
    private HashMap<String, Tweet> tweets;

    public static synchronized TweetsController getInstance() {
        if (mInstance == null) {
            mInstance = new TweetsController();
        }
        return mInstance;
    }

    private TweetsController() {
        tweets = new HashMap<>();
        ArrayList<Tweet> tweetsList = PersistentTweet.getAll();
        setTweets(tweetsList);
    }

    public void setTweet(Tweet tweet) {
        tweets.put(Long.toString(tweet.id), tweet);
    }

    public void setTweets(ArrayList<Tweet> tweetsList) {
        int len = tweetsList.size();
        for (int i=0;i<len; ++i) {

            Tweet tweet = tweetsList.get(i);
            if (tweet!=null) {
                tweets.put(Long.toString(tweet.id), tweet);
            }
        }
    }

    public Tweet getTweet(long tweetId) {
        return tweets.get(tweetId);
    }
}
