package com.codepath.apps.twitterapp.models;

import com.codepath.apps.twitterapp.TwitterClient;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by weishengsu on 2/19/16.
 */
public class CurrentUser {
    public static Tweet.User user;
    private TwitterClient client;

    public CurrentUser(TwitterClient client) {
        client.getCurrentUser(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Gson gson = new Gson();
                user = gson.fromJson(response.toString(), Tweet.User.class);
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
}
