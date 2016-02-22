package com.codepath.apps.twitterapp.models;

/**
 * Created by wilsonsu on 2/20/16.
 */
import com.codepath.apps.twitterapp.Utils.Util;
import com.google.gson.Gson;

import org.json.JSONObject;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by weishengsu on 2/15/16.
 */
@org.parceler.Parcel
public class Tweet{

    @SerializedName("created_at")
    public Date time;
    public long id;
    public String text;
    public String source;

    public User user;

    @SerializedName("retweeted_status")
    public Tweet retweet;
    @SerializedName("retweet_count")
    public int retweetCount;
    @SerializedName("favorite_count")
    public int favoriteCount;
    public boolean favorited;
    public boolean retweeted;
    @SerializedName("in_reply_to_status_id_str")
    public String replyTweetId;
    @SerializedName("in_reply_to_screen_name")
    public String replyTweetUserScreenName;

    public static Tweet fromJson(JSONObject response) {
        Gson gson = Util.gsonCreatorFortweeterDateFormater();
        Tweet tweet = gson.fromJson(response.toString(), Tweet.class);
        return tweet;
    }

    public EntitiesEntity entities;
    @SerializedName("extended_entities")
    public ExtendedEntities extendedEntities;

    @org.parceler.Parcel
    public static class ExtendedEntities {
        @SerializedName("media")
        public List<EntitiesEntity.Media> medias;

        public ExtendedEntities() {
        }
    }

    @org.parceler.Parcel
    public static class EntitiesEntity {
        public List<UrlsEntity> urls;
        @SerializedName("user_mentions")
        public List<User> users;
        public List<Media> media;

        @org.parceler.Parcel
        public static class UrlsEntity {
            public String url;
            public String expanded_url;

            public UrlsEntity() {
            }
        }
        @org.parceler.Parcel
        public static class Media {
            @SerializedName("id_str")
            public String id;
            public String media_url;
            public String url;
            public String type;

            public SizesEntity sizes;
            @SerializedName("video_info")
            public VideoEntity video;

            @org.parceler.Parcel
            public static class SizesEntity {
                public SizeEntity large;
                public SizeEntity medium;
                public SizeEntity thumb;
                public SizeEntity small;

                @org.parceler.Parcel
                public static class SizeEntity {
                    public int w;
                    public int h;
                    public String resize;
                    public SizeEntity() {
                    }
                }
                public SizesEntity() {
                }
            }
            @org.parceler.Parcel
            public static class VideoEntity{
                public List<Integer> aspect_ratio;
                public List<VariantsEntity> variants;
                @org.parceler.Parcel
                public static class VariantsEntity{
                    public String content_type;
                    public String url;

                    public VariantsEntity() {
                    }
                }

                public VideoEntity() {
                }
            }

            public Media() {
            }
        }

        public EntitiesEntity() {
        }
    }


}
