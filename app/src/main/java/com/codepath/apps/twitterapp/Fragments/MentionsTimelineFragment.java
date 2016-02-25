package com.codepath.apps.twitterapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wilsonsu on 2/24/16.
 */
public class MentionsTimelineFragment extends TweetsListFragment {
    @Override
    public void timelineApi(boolean isRefresh, long maxId) {
        client.getMentionsTimeline(maxId, timelineHandler(isRefresh));
    }
}
