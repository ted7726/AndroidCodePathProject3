package com.codepath.apps.twitterapp;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.codepath.apps.twitterapp.PersistModel.PersistentTweet;
import com.codepath.apps.twitterapp.Utils.Util;
import com.codepath.apps.twitterapp.models.Message;
import com.codepath.apps.twitterapp.models.Tweet;
import com.codepath.apps.twitterapp.models.TweetsController;
import com.codepath.apps.twitterapp.models.User;
import com.codepath.oauth.OAuthBaseClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

	public static final String REST_CONSUMER_KEY = "RZWwRfcmXwa9jcxgTTdbjXijl";
	public static final String REST_CONSUMER_SECRET = "cGyAmLBj096a91UChhJG6qweNfnJvOO2NYuyQYMrNDJHROYuAo";

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

	public void getSearchTimeline(String query, long maxId, CallBack callBack) {
		if (query == null)return;
		RequestParams params = new RequestParams("q", query);
		if (maxId > 1 ) params.put("max_id", maxId);
		getClient().get(getApiUrl("search/tweets.json"), params, searchHandler(callBack));
	}

	public void getCurrentUser(CallBack callBack) {
		getClient().get(getApiUrl("account/verify_credentials.json"), userHandler(callBack));
	}

	public void getUser(User user, CallBack callBack) {
		RequestParams params = new RequestParams("user_id", user.id);
		params.put("screen_name", user.screenName);
		getClient().get(getApiUrl("users/show.json"), params, userHandler(callBack));
	}

	public void getFriendList(CallBack callBack) {
		getClient().get(getApiUrl("friends/list.json"), new RequestParams(), friendsHandler(callBack));
	}
	public void getFriendList(String userId, CallBack callBack) {
		getClient().get(getApiUrl("friends/list.json"), new RequestParams("user_id", userId), friendsHandler(callBack));
	}

	public void getFollowersList(CallBack callBack) {
		getClient().get(getApiUrl("followers/list.json"), new RequestParams(), friendsHandler(callBack));
	}
	public void getFollowersList(String userId, CallBack callBack) {
		getClient().get(getApiUrl("followers/list.json"), new RequestParams("user_id", userId), friendsHandler(callBack));
	}

	public void getMessages(CallBack callBack) {
		getClient().get(getApiUrl("direct_messages.json"), new RequestParams(), messagesHandler(callBack));
	}
	public void getMessagesSent(CallBack callBack) {
		getClient().get(getApiUrl("direct_messages/sent.json"), new RequestParams(), messagesHandler(callBack));
	}

	public void sendMessage(String text, User user, CallBack callBack) {
		if (text == null) return;
		RequestParams params = new RequestParams("user_id", user.id);
		params.put("text", text);
		getClient().post(getApiUrl("direct_messages/new.json"), params, messageHandler(callBack));
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

	public void followUser(User user, CallBack callBack) {
		if (user==null) return;
		getClient().post(getApiUrl("friendships/create.json"), new RequestParams("user_id", user.id), userHandler(callBack));
	}

	public void unfollowUser(User user, CallBack callBack) {
		if (user==null) return;
		getClient().post(getApiUrl("friendships/destroy.json"), new RequestParams("user_id", user.id), userHandler(callBack));
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
				try {
					persistData(response);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Gson gson = Util.gsonCreatorFortweeterDateFormater();
				Tweet tweet = gson.fromJson(response.toString(), Tweet.class);
				TweetsController.getInstance().setTweet(tweet);
				if (callBack!=null) {
					callBack.tweetCallBack(tweet);
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
				try {
					persistData(response);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				ArrayList<Tweet> tweets = gson.fromJson(response.toString(), listType);
				TweetsController.getInstance().setTweets(tweets);
				if (callBack!=null) {
					callBack.tweetsCallBack(tweets);
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

	private JsonHttpResponseHandler searchHandler(final CallBack callBack) {
		final Type listType = new TypeToken<ArrayList<Tweet>>() {}.getType();
		final Gson gson = Util.gsonCreatorFortweeterDateFormater();
		return new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				JSONArray jsonArray = null;
				try {
					jsonArray = response.getJSONArray("statuses");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (jsonArray==null) {
					callBack.onFailureCallBack(response);
					return;
				}
				ArrayList<Tweet> tweets = gson.fromJson(jsonArray.toString(), listType);
				if (callBack!=null) {
					callBack.tweetsCallBack(tweets);
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
				Log.d("DEBUG", "User:" + response.toString());
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

	private JsonHttpResponseHandler friendsHandler(final CallBack callBack) {
		return new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				JSONArray jsonArrayUsers = null;
				try {
					jsonArrayUsers = response.getJSONArray("users");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (callBack!=null && jsonArrayUsers!=null) {
					final Type listType = new TypeToken<ArrayList<User>>() {}.getType();
					ArrayList<User> users = (new Gson()).fromJson(jsonArrayUsers.toString(), listType);
					callBack.usersCallBack(users);
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
	private JsonHttpResponseHandler messageHandler(final CallBack callBack) {
		final Gson gson = Util.gsonCreatorFortweeterDateFormater();
		return new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				callBack.messageCallBack(gson.fromJson(response.toString(), Message.class));
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				callBack.onFailureCallBack(errorResponse);
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		};

	}


	private JsonHttpResponseHandler messagesHandler(final CallBack callBack) {
		final Gson gson = Util.gsonCreatorFortweeterDateFormater();
		final Type listType = new TypeToken<ArrayList<Message>>() {}.getType();

		return new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
				// filter the messages including the user id
				ArrayList<Message> messages = gson.fromJson(response.toString(), listType);
				callBack.messagesCallBack(messages);
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

	private static void persistData(JSONArray response) throws JSONException {
		for (int i=0;i<response.length();++i) {
			persistData(response.getJSONObject(i));
		}
	}
	private static void persistData(JSONObject response) throws JSONException {
		PersistentTweet tweet = PersistentTweet.fromJson(response);
		if (tweet!=null) {
			tweet.save();
		}
	}

}