package com.codepath.apps.twitterapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.codepath.apps.twitterapp.PersistModel.PersistentTweet;
import com.codepath.apps.twitterapp.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by wilsonsu on 2/28/16.
 */
public class SearchFragment extends TweetsListFragment {
    @Bind(R.id.etSearch) EditText etSearch;
    @Override
    public void timelineApi(boolean isRefresh, long maxId) {
        client.getSearchTimeline(etSearch.getText().toString(), maxId, timelineHandler(isRefresh));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_list, parent, false);
        super.setup(view);
        return view;
    }

    @OnClick(R.id.ibSearchButton)
    public void onSearchClick() {
        refreshTimeline();
    }

}
