package com.codepath.apps.twitterapp.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.apps.twitterapp.Adapters.TweetsArrayAdapter;
import com.codepath.apps.twitterapp.CallBack;
import com.codepath.apps.twitterapp.DialogFragment.ComposeDialog;
import com.codepath.apps.twitterapp.PersistModel.PersistentTweet;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.TwitterApplication;
import com.codepath.apps.twitterapp.TwitterClient;
import com.codepath.apps.twitterapp.Utils.EndlessRecyclerViewScrollListener;
import com.codepath.apps.twitterapp.Utils.Util;
import com.codepath.apps.twitterapp.models.CurrentUser;
import com.codepath.apps.twitterapp.models.Tweet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wilsonsu on 2/24/16.
 */
public class TweetsListFragment extends Fragment implements ComposeDialog.ComposeDialogListener {
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter tweetsArrayAdapter;
    private long maxId;
    public TwitterClient client;
    @Bind(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @Bind(R.id.prLoadingSpinner) RelativeLayout prLoadingSpinner;
    @Bind(R.id.tvNetworkUnavailable) TextView tvNetworkUnavailable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets_list, parent, false);
        setup(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getActivity());
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTimeline();
            }
        });
        refreshTimeline();
    }

    private void init() {
        tweets = new ArrayList<>();
        tweetsArrayAdapter = new TweetsArrayAdapter(getActivity(), tweets);
        client = TwitterApplication.getRestClient(); // singleton client
        maxId = 1;
    }

    private void setup(View view) {
        // setup Recycler View
        RecyclerView rvTimeline = (RecyclerView) view.findViewById(R.id.rvTimeline);
        rvTimeline.setAdapter(tweetsArrayAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvTimeline.setLayoutManager(linearLayoutManager);
        rvTimeline.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                populateTimeline(page);
            }
        });
    }

    // override this method
    public void timelineApi(boolean isRefresh, long maxId) {
        client.getHomeTimeline(maxId, timelineHandler(isRefresh));
    }

    public void refreshTimeline() {
        populateTimeline(0);
    }

    private void populateTimeline(int page) {
        if (prLoadingSpinner==null || swipeContainer==null || tvNetworkUnavailable==null) return;
        boolean hasNetwork = (client.isNetworkAvailable() && client.isOnline());
        toggleNetworkUnavailable(!hasNetwork);

        if (page>0) {
            prLoadingSpinner.setVisibility(View.VISIBLE);
            timelineApi(false, maxId);
        } else {
            if (!hasNetwork) {
                addTweets(PersistentTweet.getAll());
                swipeContainer.setRefreshing(false);
                return;
            }
            timelineApi(true, maxId);
            maxId = 1;
        }
    }

    public CallBack timelineHandler(final boolean isRefresh) {

        return new CallBack() {
            @Override
            public void tweetsCallBack(ArrayList<Tweet> newTweets, JSONArray response) {
                try {
                    Util.persistData(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (isRefresh) {
                    tweets.clear();
                }
                addTweets(newTweets);
            }

            @Override
            public void onFailureCallBack(JSONObject errorResponse) {
                swipeContainer.setRefreshing(false);
                prLoadingSpinner.setVisibility(View.INVISIBLE);
            }
        };
    }

    private void addTweets(ArrayList<Tweet> newTweets) {
        tweets.addAll(newTweets);
        tweetsArrayAdapter.notifyDataSetChanged();
        if (newTweets.size()>0) {
            maxId = newTweets.get(newTweets.size()-1).id;
        }
        swipeContainer.setRefreshing(false);
        prLoadingSpinner.setVisibility(View.INVISIBLE);
    }



    public void onFinishComposeDialog(String composeText) {
        prLoadingSpinner.setVisibility(View.VISIBLE);
        client.postNewTweet(composeText, new CallBack() {
            @Override
            public void tweetCallBack(Tweet tweet) {
                tweets.add(0, tweet);
                tweetsArrayAdapter.notifyDataSetChanged();
                prLoadingSpinner.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailureCallBack(JSONObject errorResponse) {
                prLoadingSpinner.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));
        int position = findTweet(tweet);
        if (position>=0) {
            tweets.set(position, tweet);
            tweetsArrayAdapter.notifyDataSetChanged();
        }
    }

    private int findTweet(Tweet tweet) {
        int len = tweets.size();
        for (int i=0;i<len;++i) {
            if (tweets.get(i).id == tweet.id) return i;
        }
        return -1;
    }


    private void toggleNetworkUnavailable(final boolean show) {
        if ((tvNetworkUnavailable.getVisibility() == View.VISIBLE) == show) {
            return;
        }
        Util.alphaAnimationCreator(tvNetworkUnavailable, show, 800);
    }
}
