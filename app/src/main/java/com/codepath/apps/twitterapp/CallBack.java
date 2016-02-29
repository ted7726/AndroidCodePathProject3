package com.codepath.apps.twitterapp;

import com.codepath.apps.twitterapp.models.Message;
import com.codepath.apps.twitterapp.models.Tweet;
import com.codepath.apps.twitterapp.models.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wilsonsu on 2/25/16.
 */
public class CallBack{
    public void tweetsCallBack(ArrayList<Tweet> tweets) {}
    public void tweetCallBack(Tweet tweet) {}
    public void userCallBack(User user) {}
    public void usersCallBack(ArrayList<User> users) {}
    public void messagesCallBack(ArrayList<Message> messages) {}
    public void messageCallBack(Message message) {}
    public void onFailureCallBack(JSONObject errorResponse) {}
    public void task() {}

}
