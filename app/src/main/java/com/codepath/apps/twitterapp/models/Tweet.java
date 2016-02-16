package com.codepath.apps.twitterapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by weishengsu on 2/15/16.
 */
public class Tweet {


    /**
     * created_at : Mon Feb 15 17:31:31 +0000 2016
     * id : 699284917490847745
     * id_str : 699284917490847745
     * text : RT @jessejiryudavis: Why you shouldn't introduce yourself or show an "agenda" slide https://t.co/hyNuJGAroU https://t.co/4HtaHPLpaQ
     * source : <a href="http://twitter.com" rel="nofollow">Twitter Web Client</a>
     * truncated : false
     * in_reply_to_status_id : null
     * in_reply_to_status_id_str : null
     * in_reply_to_user_id : null
     * in_reply_to_user_id_str : null
     * in_reply_to_screen_name : null
     * user : {"id":44102767,"id_str":"44102767","name":"Tzu-ping Chung","screen_name":"uranusjr","location":"Taipei, Taiwan","description":"Programmer, (self-styled) musician, Pythonista, Djangonaut, tubaist, man-with-a-thousand-interests-and-zero-specialities. I created @MacDownApp.","url":"https://t.co/4w9v31HOaT","entities":{"url":{"urls":[{"url":"https://t.co/4w9v31HOaT","expanded_url":"https://uranusjr.com","display_url":"uranusjr.com","indices":[0,23]}]},"description":{"urls":[]}},"protected":false,"followers_count":700,"friends_count":179,"listed_count":32,"created_at":"Tue Jun 02 11:38:12 +0000 2009","favourites_count":265,"utc_offset":28800,"time_zone":"Taipei","geo_enabled":true,"verified":false,"statuses_count":6848,"lang":"en-gb","contributors_enabled":false,"is_translator":false,"is_translation_enabled":false,"profile_background_color":"EDECE9","profile_background_image_url":"http://abs.twimg.com/images/themes/theme3/bg.gif","profile_background_image_url_https":"https://abs.twimg.com/images/themes/theme3/bg.gif","profile_background_tile":false,"profile_image_url":"http://pbs.twimg.com/profile_images/710013937/Chi_sleep_normal.png","profile_image_url_https":"https://pbs.twimg.com/profile_images/710013937/Chi_sleep_normal.png","profile_banner_url":"https://pbs.twimg.com/profile_banners/44102767/1406694085","profile_link_color":"088253","profile_sidebar_border_color":"D3D2CF","profile_sidebar_fill_color":"E3E2DE","profile_text_color":"634047","profile_use_background_image":true,"has_extended_profile":false,"default_profile":false,"default_profile_image":false,"following":true,"follow_request_sent":false,"notifications":false}
     * geo : null
     * coordinates : null
     * place : null
     * contributors : null
     * retweeted_status : {"created_at":"Mon Feb 15 17:24:16 +0000 2016","id":699283096588443649,"id_str":"699283096588443649","text":"Why you shouldn't introduce yourself or show an \"agenda\" slide https://t.co/hyNuJGAroU https://t.co/4HtaHPLpaQ","source":"<a href=\"http://bufferapp.com\" rel=\"nofollow\">Buffer<\/a>","truncated":false,"in_reply_to_status_id":null,"in_reply_to_status_id_str":null,"in_reply_to_user_id":null,"in_reply_to_user_id_str":null,"in_reply_to_screen_name":null,"user":{"id":131044458,"id_str":"131044458","name":"A. Jesse Jiryu Davis","screen_name":"jessejiryudavis","location":"East Village, New York City","description":"Staff Software Engineer at @MongoDB working on C, Python, Tornado, and async. Documentary photographer. Student at @villagezendo. He/him/his.","url":"http://t.co/YBRPQvUkyl","entities":{"url":{"urls":[{"url":"http://t.co/YBRPQvUkyl","expanded_url":"http://emptysqua.re","display_url":"emptysqua.re","indices":[0,22]}]},"description":{"urls":[]}},"protected":false,"followers_count":1308,"friends_count":436,"listed_count":150,"created_at":"Fri Apr 09 03:11:17 +0000 2010","favourites_count":873,"utc_offset":-18000,"time_zone":"Eastern Time (US & Canada)","geo_enabled":true,"verified":false,"statuses_count":5059,"lang":"en","contributors_enabled":false,"is_translator":false,"is_translation_enabled":false,"profile_background_color":"000000","profile_background_image_url":"http://pbs.twimg.com/profile_background_images/652861992173215744/gBJvbc4h.png","profile_background_image_url_https":"https://pbs.twimg.com/profile_background_images/652861992173215744/gBJvbc4h.png","profile_background_tile":false,"profile_image_url":"http://pbs.twimg.com/profile_images/808380981/profile_normal.jpg","profile_image_url_https":"https://pbs.twimg.com/profile_images/808380981/profile_normal.jpg","profile_banner_url":"https://pbs.twimg.com/profile_banners/131044458/1443301603","profile_link_color":"555555","profile_sidebar_border_color":"000000","profile_sidebar_fill_color":"000000","profile_text_color":"000000","profile_use_background_image":true,"has_extended_profile":false,"default_profile":false,"default_profile_image":false,"following":false,"follow_request_sent":false,"notifications":false},"geo":null,"coordinates":null,"place":null,"contributors":null,"is_quote_status":false,"retweet_count":2,"favorite_count":2,"entities":{"hashtags":[],"symbols":[],"user_mentions":[],"urls":[{"url":"https://t.co/hyNuJGAroU","expanded_url":"http://bit.ly/1PMFESe","display_url":"bit.ly/1PMFESe","indices":[63,86]}],"media":[{"id":699283096315830273,"id_str":"699283096315830273","indices":[87,110],"media_url":"http://pbs.twimg.com/media/CbRaRWlXIAEe1sQ.jpg","media_url_https":"https://pbs.twimg.com/media/CbRaRWlXIAEe1sQ.jpg","url":"https://t.co/4HtaHPLpaQ","display_url":"pic.twitter.com/4HtaHPLpaQ","expanded_url":"http://twitter.com/jessejiryudavis/status/699283096588443649/photo/1","type":"photo","sizes":{"large":{"w":993,"h":1030,"resize":"fit"},"medium":{"w":600,"h":622,"resize":"fit"},"thumb":{"w":150,"h":150,"resize":"crop"},"small":{"w":340,"h":353,"resize":"fit"}}}]},"extended_entities":{"media":[{"id":699283096315830273,"id_str":"699283096315830273","indices":[87,110],"media_url":"http://pbs.twimg.com/media/CbRaRWlXIAEe1sQ.jpg","media_url_https":"https://pbs.twimg.com/media/CbRaRWlXIAEe1sQ.jpg","url":"https://t.co/4HtaHPLpaQ","display_url":"pic.twitter.com/4HtaHPLpaQ","expanded_url":"http://twitter.com/jessejiryudavis/status/699283096588443649/photo/1","type":"photo","sizes":{"large":{"w":993,"h":1030,"resize":"fit"},"medium":{"w":600,"h":622,"resize":"fit"},"thumb":{"w":150,"h":150,"resize":"crop"},"small":{"w":340,"h":353,"resize":"fit"}}}]},"favorited":false,"retweeted":false,"possibly_sensitive":false,"possibly_sensitive_appealable":false,"lang":"en"}
     * is_quote_status : false
     * retweet_count : 2
     * favorite_count : 0
     * entities : {"hashtags":[],"symbols":[],"user_mentions":[{"screen_name":"jessejiryudavis","name":"A. Jesse Jiryu Davis","id":131044458,"id_str":"131044458","indices":[3,19]}],"urls":[{"url":"https://t.co/hyNuJGAroU","expanded_url":"http://bit.ly/1PMFESe","display_url":"bit.ly/1PMFESe","indices":[84,107]}],"media":[{"id":699283096315830273,"id_str":"699283096315830273","indices":[108,131],"media_url":"http://pbs.twimg.com/media/CbRaRWlXIAEe1sQ.jpg","media_url_https":"https://pbs.twimg.com/media/CbRaRWlXIAEe1sQ.jpg","url":"https://t.co/4HtaHPLpaQ","display_url":"pic.twitter.com/4HtaHPLpaQ","expanded_url":"http://twitter.com/jessejiryudavis/status/699283096588443649/photo/1","type":"photo","sizes":{"large":{"w":993,"h":1030,"resize":"fit"},"medium":{"w":600,"h":622,"resize":"fit"},"thumb":{"w":150,"h":150,"resize":"crop"},"small":{"w":340,"h":353,"resize":"fit"}},"source_status_id":699283096588443649,"source_status_id_str":"699283096588443649","source_user_id":131044458,"source_user_id_str":"131044458"}]}
     * extended_entities : {"media":[{"id":699283096315830273,"id_str":"699283096315830273","indices":[108,131],"media_url":"http://pbs.twimg.com/media/CbRaRWlXIAEe1sQ.jpg","media_url_https":"https://pbs.twimg.com/media/CbRaRWlXIAEe1sQ.jpg","url":"https://t.co/4HtaHPLpaQ","display_url":"pic.twitter.com/4HtaHPLpaQ","expanded_url":"http://twitter.com/jessejiryudavis/status/699283096588443649/photo/1","type":"photo","sizes":{"large":{"w":993,"h":1030,"resize":"fit"},"medium":{"w":600,"h":622,"resize":"fit"},"thumb":{"w":150,"h":150,"resize":"crop"},"small":{"w":340,"h":353,"resize":"fit"}},"source_status_id":699283096588443649,"source_status_id_str":"699283096588443649","source_user_id":131044458,"source_user_id_str":"131044458"}]}
     * favorited : false
     * retweeted : false
     * possibly_sensitive : false
     * possibly_sensitive_appealable : false
     * lang : en
     */

    @SerializedName("created_at")
    public Date created_at;
    public long id;
    public String text;
    public String source;

    public UserEntity user;

    @SerializedName("retweeted_status")
    public Tweet retweet;
    @SerializedName("retweet_count")
    public int retweetCount;
    @SerializedName("favorite_count")
    public int favoriteCount;
    public boolean favorited;
    public boolean retweeted;

    public static class UserEntity {
        public int id;
        public String id_str;
        public String name;
        @SerializedName("screen_name")
        public String screenName;
        public String description;
        public String url;
    }

    public EntitiesEntity entities;

    public static class EntitiesEntity {
        /**
         * url : https://t.co/hyNuJGAroU
         * expanded_url : http://bit.ly/1PMFESe
         * display_url : bit.ly/1PMFESe
         * indices : [84,107]
         */

        public List<UrlsEntity> urls;

        public static class UrlsEntity {
            public String url;
            public String expanded_url;
        }
    }
}
