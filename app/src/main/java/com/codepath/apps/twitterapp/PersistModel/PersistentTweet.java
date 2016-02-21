package com.codepath.apps.twitterapp.PersistModel;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.codepath.apps.twitterapp.Utils.Util;
import com.codepath.apps.twitterapp.models.Tweet;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by weishengsu on 2/15/16.
 */
@Table(name = "Tweet")
public class PersistentTweet extends Model{
    @Column(name = "tweetId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String id;
    @Column(name = "tweetContent")
    public String tweetContent;

    public PersistentTweet() {
        super();
    }

    public PersistentTweet(String id, String tweetContent) {
        super();
        this.id = id;
        this.tweetContent = tweetContent;
    }

    public static PersistentTweet fromJson(JSONObject response) {
        Long id = null;
        try {
            id = response.getLong("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (id!=null) {
            return new PersistentTweet(Long.toString(id), response.toString());
        }
        return null;
    }

    public static ArrayList<Tweet> getAll() {
        // This is how you execute a query
        List<PersistentTweet> persistentTweets = new Select()
                .from(PersistentTweet.class).execute();

        ArrayList<Tweet> tweets = new ArrayList<>();
        for (int i=0;i<persistentTweets.size(); ++i) {
            tweets.add(Util.tweetConverter(persistentTweets.get(i)));
        }
        return tweets;
    }
}
