package com.codepath.apps.twitterapp.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by wilsonsu on 2/20/16.
 */
@Parcel
public class User {
    @SerializedName("id_str")
    public String id;
    public String name;
    @SerializedName("screen_name")
    public String screenName;
    public String description;
    public String url;
    @SerializedName("profile_image_url")
    public String profileImageUrl;

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



}
