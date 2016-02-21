package com.codepath.apps.twitterapp.Utils;

import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.twitterapp.PersistModel.PersistentTweet;
import com.codepath.apps.twitterapp.models.Tweet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by weishengsu on 2/17/16.
 */
public class Util {
    public static String converTimetoRelativeTime(Date time) {
        String relativeDate = DateUtils.getRelativeTimeSpanString(time.getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_TIME).toString();
        relativeDate.replaceFirst("hour", "h");
        relativeDate.replaceFirst("minute", "min");
        return relativeDate;
    }

    public static String numberConverter(int n) {
        String text =  (n > 1000 ? n / 1000 + "k" : n + "");
        return text;
    }
    public static String checkStringEmpty(String s) {
        if (s==null)return "";
        return TextUtils.isEmpty(s)?"":s;
    }

    public static void setLayoutHeight(boolean isWrapContent, View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = isWrapContent ? ViewGroup.LayoutParams.WRAP_CONTENT : 0;
        view.setLayoutParams(params);
    }
    public static Gson gsonCreatorFortweeterDateFormater() {
        // Mon Feb 15 17:31:31 +0000 2016
        return new GsonBuilder().setDateFormat("EEE MMM dd HH:mm:ss zzzzz yyyy").create();
    }

    public static void persistData(JSONArray response) throws JSONException {
        for (int i=0;i<response.length();++i) {
            persistData(response.getJSONObject(i));
        }
    }
    public static void persistData(JSONObject response) throws JSONException {
        PersistentTweet tweet = PersistentTweet.fromJson(response);
        tweet.save();
    }

    public static Tweet tweetConverter (PersistentTweet persistentTweet) {
        Gson gson = new Gson();
        String json = gson.toJson(persistentTweet, PersistentTweet.class);
        return gson.fromJson(json, Tweet.class);
    }


}
