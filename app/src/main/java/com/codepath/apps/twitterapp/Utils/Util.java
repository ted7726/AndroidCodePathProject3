package com.codepath.apps.twitterapp.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.bumptech.glide.load.engine.Resource;
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
        if (tweet!=null) {
            tweet.save();
        }
    }

    public static Tweet tweetConverter (PersistentTweet persistentTweet) {
        Gson gson = gsonCreatorFortweeterDateFormater();
        if (persistentTweet==null) {
            return null;
        }
        return gson.fromJson(persistentTweet.tweetContent, Tweet.class);
    }
    public static void alphaAnimationCreator(final View view, final boolean isfadeIn) {
        alphaAnimationCreator(view, isfadeIn, 500);
    }

    public static void alphaAnimationCreator(final View view, final boolean isfadeIn, int duration) {
        final float alpha = isfadeIn?0.0f:1.0f;

        view.setVisibility(View.VISIBLE);
        AlphaAnimation fade = new AlphaAnimation(alpha, 1-alpha);
        fade.setAnimationListener(
                new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        view.setVisibility(isfadeIn?View.VISIBLE:View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                }
        );
        fade.setDuration(duration);
        view.startAnimation(fade);
    }

}
