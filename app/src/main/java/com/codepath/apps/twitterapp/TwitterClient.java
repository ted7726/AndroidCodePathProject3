package com.codepath.apps.twitterapp;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.codepath.apps.twitterapp.Utils.Util;
import com.codepath.apps.twitterapp.models.Tweet;
import com.codepath.apps.twitterapp.models.User;
import com.codepath.oauth.OAuthBaseClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

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
	public static final String REST_CONSUMER_KEY = "jT1JnZAVImepjUFGoUfx8i6Ch";
	public static final String REST_CONSUMER_SECRET = "6VuqpkYLq1R5oai8obhGtEiiXPBp39uDdXDlJIgHZw3OhK0xUE";
//	public static final String REST_CONSUMER_KEY = "4QHE4kyiC3c3T1U1KW4oc7PFt";
//	public static final String REST_CONSUMER_SECRET = "BDsANeiRZjbi5FwKgh7FwQ56dGBIPKq4AFPXRRMMS3ZWyCcZx8";
//	public static final String REST_CONSUMER_KEY = "GH7FU0AemrboNrOgcsVeTQ";
//	public static final String REST_CONSUMER_SECRET = "9OTdKBiifxjOddUmP3IabYsTilp2ehoatN8iGrrWXw";

	public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	public void getHomeTimeline(long maxId, CallBack callBack) {
		RequestParams params = new RequestParams();
		if (maxId > 1 ) params.put("max_id", maxId);
		client.get(getApiUrl("statuses/home_timeline.json"), params, tweetsHandler(callBack));
	}

	public void getMentionsTimeline(long maxId, CallBack callBack) {
		RequestParams params = new RequestParams();
		if (maxId > 1 ) params.put("max_id", maxId);
		getClient().get(getApiUrl("statuses/mentions_timeline.json"), params, tweetsHandler(callBack));
	}

	public void getUserTimeline(String userId, long maxId, CallBack callBack) {
		RequestParams params = new RequestParams();
		if (maxId > 1 ) params.put("max_id", maxId);
		if (userId != null) params.put("user_id", userId);
		getClient().get(getApiUrl("statuses/user_timeline.json"), params, tweetsHandler(callBack));
	}

	public void getCurrentUser(CallBack callBack) {
		getClient().get(getApiUrl("account/verify_credentials.json"), userHandler(callBack));
	}

	public void getUser(User user, CallBack callBack) {
		RequestParams params = new RequestParams("user_id", user.id);
		params.put("screen_name", user.screenName);
		getClient().get(getApiUrl("users/show.json"), params, userHandler(callBack));
	}

	public void postNewTweet(String text, CallBack callBack) {
		if (text == null) return;
		getClient().post(getApiUrl("statuses/update.json"), new RequestParams("status", text), tweetHandler(callBack));
	}

	public void replyTweet(String text, String tweetId, CallBack callBack) {
		if (text == null || tweetId == null) return;
		RequestParams requestParams = new RequestParams("status", text);
		requestParams.put("in_reply_to_status_id", tweetId);
		getClient().post(getApiUrl("statuses/update.json"), requestParams, tweetHandler(callBack));
	}

	public void getTweet(String tweetId, CallBack callBack) {
		if (tweetId == null) return;
		getClient().post(getApiUrl("statuses/show.json"), new RequestParams("id", tweetId), tweetHandler(callBack));
	}

	public void likeTweet(String tweetId, boolean isLike, CallBack callBack) {
		if (tweetId == null) return;
		String uri = isLike ? "favorites/create.json" : "favorites/destroy.json";
		getClient().post(getApiUrl(uri), new RequestParams("id", tweetId), tweetHandler(callBack));
	}

	public void retweet(String tweetId, boolean isRetweet, CallBack callBack) {
		if (tweetId == null) return;
		String uri = (isRetweet ? "statuses/retweet/" : "statuses/unretweet/") + tweetId +".json";
		getClient().post(getApiUrl(uri), new RequestParams("id", tweetId), tweetHandler(callBack));
	}

	private JsonHttpResponseHandler tweetHandler(final CallBack callBack) {
		return new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				if (callBack!=null) {
					callBack.tweetCallBack((new Gson()).fromJson(response.toString(), Tweet.class));
				}
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				callBack.onFailureCallBack(errorResponse);
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		};
	}


	private JsonHttpResponseHandler userHandler(final CallBack callBack) {
		return new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

				if (callBack!=null) {
					callBack.userCallBack((new Gson()).fromJson(response.toString(), User.class));
				}
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				callBack.onFailureCallBack(errorResponse);
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		};
	}

	private JsonHttpResponseHandler tweetsHandler(final CallBack callBack) {
		final Type listType = new TypeToken<ArrayList<Tweet>>() {}.getType();
		final Gson gson = Util.gsonCreatorFortweeterDateFormater();
		return new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
				ArrayList<Tweet> tweets = gson.fromJson(response.toString(), listType);
				if (callBack!=null) {
					callBack.tweetsCallBack(tweets, response);
				}
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				callBack.onFailureCallBack(errorResponse);
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		};
	}


	public Boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}

	public boolean isOnline() {
		Runtime runtime = Runtime.getRuntime();
		try {
			Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
			int     exitValue = ipProcess.waitFor();
			return (exitValue == 0);
		} catch (IOException e)          { e.printStackTrace(); }
		catch (InterruptedException e) { e.printStackTrace(); }
		return false;
	}

}