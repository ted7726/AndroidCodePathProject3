package com.codepath.apps.twitterapp.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by wilsonsu on 2/20/16.
 */
@Parcel
public class User {
    @SerializedName("id_str")
    public String id;
    @SerializedName("id")
    public long userId;
    public String name;
    @SerializedName("screen_name")
    public String screenName;
    public String description;
    public String url;
    @SerializedName("profile_image_url")
    public String profileImageUrl;


    public String profileImageUrlBigger;

    public String location;
    @SerializedName("followers_count")
    public int followersCount;
    @SerializedName("friends_count")
    public int friendsCount;
    @SerializedName("listed_count")
    public int listedCount;
    @SerializedName("favourites_count")
    public int favouritesCount;
    @SerializedName("statuses_count")
    public int statusesCount;
    @SerializedName("profile_banner_url")
    public String profileCoverPhotoUrl;
    public boolean following;
    @SerializedName("follow_request_sent")
    public boolean followRequestSent;

    public User() {
    }

    public static User fromJson(JSONObject response) {
        Gson gson = new Gson();
        User user = gson.fromJson(response.toString(), User.class);
        return user;
    }


    public String getProfileImageUrlBigger() {
        return profileImageUrl.replace("normal","bigger");
    }
}
