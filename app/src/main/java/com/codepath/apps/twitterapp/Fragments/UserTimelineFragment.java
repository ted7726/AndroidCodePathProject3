package com.codepath.apps.twitterapp.Fragments;

/**
 * Created by wilsonsu on 2/25/16.
 */
public class UserTimelineFragment extends TweetsListFragment {
    private String userId;
    @Override
    public void timelineApi(boolean isRefresh, long maxId) {
        if (userId!=null) {
            client.getUserTimeline(userId, maxId, timelineHandler(isRefresh));
        }
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
