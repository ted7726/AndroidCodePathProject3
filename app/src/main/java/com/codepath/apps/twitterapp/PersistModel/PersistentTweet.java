package com.codepath.apps.twitterapp.PersistModel;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.codepath.apps.twitterapp.Utils.Util;
import com.codepath.apps.twitterapp.models.Tweet;
import com.codepath.apps.twitterapp.models.User;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by weishengsu on 2/15/16.
 */
@Table(name = "Tweet")
public class PersistentTweet extends Model{
    @SerializedName("created_at") @Column(name = "time")
    public Date time;
    @Column(name = "tweetId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public long id;
    @Column(name = "text")
    public String text;
    @Column(name = "source")
    public String source;
    @Column(name = "User", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public UserEntity user;
    @SerializedName("retweeted_status") @Column(name = "retweet", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public PersistentTweet retweet;
    @SerializedName("retweet_count") @Column(name = "retweetCount")
    public int retweetCount;
    @SerializedName("favorite_count") @Column(name = "favoriteCount")
    public int favoriteCount;
    @Column(name = "favorited")
    public boolean favorited;
    @Column(name = "retweeted")
    public boolean retweeted;
    @SerializedName("in_reply_to_status_id_str") @Column(name = "replyTweetId")
    public String replyTweetId;
    @SerializedName("in_reply_to_screen_name") @Column(name = "replyTweetUserScreenName")
    public String replyTweetUserScreenName;

    public PersistentTweet() {
        super();
    }

    public static PersistentTweet fromJson(JSONObject response) {
        Gson gson = Util.gsonCreatorFortweeterDateFormater();
        PersistentTweet tweet = gson.fromJson(response.toString(), PersistentTweet.class);
        return tweet;
    }

    public static ArrayList<Tweet> getAll() {
        // This is how you execute a query
        List<PersistentTweet> persistentTweets = new Select()
                .from(PersistentTweet.class).execute();
//                .orderBy("Name id")


        ArrayList<Tweet> tweets = new ArrayList<>();
        for (int i=0;i<persistentTweets.size(); ++i) {
            tweets.add(Util.tweetConverter(persistentTweets.get(i)));
        }
        return tweets;
    }




    @Column(name = "entities", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public EntitiesEntity entities;
}
