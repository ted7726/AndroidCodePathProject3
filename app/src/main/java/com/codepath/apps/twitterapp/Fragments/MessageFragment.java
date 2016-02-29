package com.codepath.apps.twitterapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.apps.twitterapp.CallBack;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.TwitterApplication;
import com.codepath.apps.twitterapp.TwitterClient;

import com.codepath.apps.twitterapp.Views.MessageFriendViewHolder;
import com.codepath.apps.twitterapp.models.User;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wilsonsu on 2/27/16.
 */
public class MessageFragment extends Fragment {
    private UsersArrayAdapter usersArrayAdapter;
    private ArrayList<User> users;
    protected TwitterClient client;
    protected User user;
    @Bind(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @Bind(R.id.prLoadingSpinner) RelativeLayout prLoadingSpinner;
    @Bind(R.id.tvNetworkUnavailable) TextView tvNetworkUnavailable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }
    private void init() {
        users = new ArrayList<>();
        usersArrayAdapter = new UsersArrayAdapter(getActivity(), users);
        client = TwitterApplication.getRestClient(); // singleton client
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets_list, parent, false);
        RecyclerView rvTimeline = (RecyclerView) view.findViewById(R.id.rvTimeline);
        rvTimeline.setAdapter(usersArrayAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvTimeline.setLayoutManager(linearLayoutManager);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getActivity());
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateUsers();
            }
        });
        tvNetworkUnavailable.setVisibility(View.INVISIBLE);
        populateUsers();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void populateUsers() {
        if (user!=null) {
            client.getFriendList(user.id, friendlistHandler());
        } else {
            client.getFriendList(friendlistHandler());
        }
    }

    protected CallBack friendlistHandler() {
        return new CallBack() {
            @Override
            public void usersCallBack(ArrayList<User> returnedUsers) {
                users.clear();
                users.addAll(returnedUsers);
                usersArrayAdapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailureCallBack(JSONObject errorResponse) {
                swipeContainer.setRefreshing(false);
            }
        };
    }

    public static class UsersArrayAdapter extends RecyclerView.Adapter<MessageFriendViewHolder> {
        private ArrayList<User> users;
        private FragmentActivity fragmentActivity;

        public UsersArrayAdapter(FragmentActivity fragmentActivity, ArrayList<User> users) {
            this.users = users;
            this.fragmentActivity = fragmentActivity;
        }

        @Override
        public MessageFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.item_message_friends, parent, false);
            MessageFriendViewHolder viewHolder = new MessageFriendViewHolder(view, fragmentActivity);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MessageFriendViewHolder holder, int position) {
            holder.setUser(users.get(position));
        }

        @Override
        public int getItemCount() {
            return users.size();
        }
    }
}
