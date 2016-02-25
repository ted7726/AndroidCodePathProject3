package com.codepath.apps.twitterapp.models;

import com.codepath.apps.twitterapp.CallBack;
import com.codepath.apps.twitterapp.TwitterClient;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by weishengsu on 2/19/16.
 */
public class CurrentUser {
    public static User user;
    private TwitterClient client;

    public CurrentUser(TwitterClient client) {
        client.getCurrentUser(new CallBack() {
            @Override
            public void userCallBack(User returnUser) {
                user = returnUser;
            }
        });
    }
}
