package com.codepath.apps.twitterapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by wilsonsu on 2/27/16.
 */
public class Message {
    @SerializedName("created_at")
    public Date time;
    public EntitiesEntity entities;
    @SerializedName("id_str")
    public String messageId;
    public User recipient;
    public User sender;
    @SerializedName("recipient_screen_name")
    public String recipientScreenName;
    @SerializedName("sender_screen_name")
    public String senderScreenName;
    @SerializedName("recipient_id")
    public long recipientId;
    @SerializedName("sender_id")
    public long senderId;

    public String text;

    public static class EntitiesEntity {
        public List<?> hashtags;
        public List<?> urls;
    }
}
