package com.codepath.apps.twitterapp;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
//	public static final String REST_CONSUMER_KEY = "jT1JnZAVImepjUFGoUfx8i6Ch";
//	public static final String REST_CONSUMER_SECRET = "6VuqpkYLq1R5oai8obhGtEiiXPBp39uDdXDlJIgHZw3OhK0xUE";
	public static final String REST_CONSUMER_KEY = "4QHE4kyiC3c3T1U1KW4oc7PFt";
	public static final String REST_CONSUMER_SECRET = "BDsANeiRZjbi5FwKgh7FwQ56dGBIPKq4AFPXRRMMS3ZWyCcZx8";
//	public static final String REST_CONSUMER_KEY = "GH7FU0AemrboNrOgcsVeTQ";
//	public static final String REST_CONSUMER_SECRET = "9OTdKBiifxjOddUmP3IabYsTilp2ehoatN8iGrrWXw";

	public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	public void getHomeTimeline(long maxId, AsyncHttpResponseHandler handler) {
		String apiURL = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		if (maxId > 1 ) {
			params.put("max_id", maxId);
		}
		client.get(apiURL, params, handler);
	}

	public void getCurrentUser(JsonHttpResponseHandler handler) {
		getClient().get(getApiUrl("account/verify_credentials.json"), handler);
	}

	public void postNewTweet(String text, JsonHttpResponseHandler handler) {
		if (text == null) return;
		getClient().post(getApiUrl("statuses/update.json"), new RequestParams("status", text), handler);
	}

	public void replyTweet(String text, String tweetId, JsonHttpResponseHandler handler) {
		if (text == null || tweetId == null) return;
		RequestParams requestParams = new RequestParams("status", text);
		requestParams.put("in_reply_to_status_id", tweetId);
		getClient().post(getApiUrl("statuses/update.json"), requestParams, handler);
	}

	public void getTweet(String tweetId, JsonHttpResponseHandler handler) {
		if (tweetId == null) return;
		getClient().post(getApiUrl("statuses/show.json"), new RequestParams("id", tweetId), handler);
	}

	public void likeTweet(String tweetId, boolean isLike, JsonHttpResponseHandler handler) {
		if (tweetId == null) return;
		String uri = isLike ? "favorites/create.json" : "favorites/destroy.json";
		getClient().post(getApiUrl(uri), new RequestParams("id", tweetId), handler);
	}

	public void retweet(String tweetId, boolean isRetweet, JsonHttpResponseHandler handler) {
		if (tweetId == null) return;
		String uri = (isRetweet ? "statuses/retweet/" : "statuses/unretweet/") + tweetId +".json";
		getClient().post(getApiUrl(uri), new RequestParams("id", tweetId), handler);
	}



	// COMPOSE TWEET

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}