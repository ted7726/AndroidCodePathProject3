package com.codepath.apps.twitterapp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

/**
 * Created by wilsonsu on 2/20/16.
 */
@org.parceler.Parcel
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

    public User() {
    }
}
