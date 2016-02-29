package com.codepath.apps.twitterapp.Fragments;

import com.codepath.apps.twitterapp.Views.MessageFriendViewHolder;

/**
 * Created by wilsonsu on 2/28/16.
 */
public class FollowersFragment extends MessageFragment{


    public void populateUsers() {
        client.getFollowersList(friendlistHandler());
    }

}
