package com.codepath.apps.twitterapp.Fragments;

import com.codepath.apps.twitterapp.Views.MessageFriendViewHolder;
import com.codepath.apps.twitterapp.models.User;

/**
 * Created by wilsonsu on 2/28/16.
 */
public class FollowersFragment extends MessageFragment {
    public void populateUsers() {
        if (user!=null) {
            client.getFollowersList(user.id, friendlistHandler());
        } else {
            client.getFollowersList(friendlistHandler());
        }
    }
}
