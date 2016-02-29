package com.codepath.apps.twitterapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.apps.twitterapp.Adapters.TweetsArrayAdapter;
import com.codepath.apps.twitterapp.CallBack;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.TwitterApplication;
import com.codepath.apps.twitterapp.TwitterClient;
import com.codepath.apps.twitterapp.Utils.EndlessRecyclerViewScrollListener;
import com.codepath.apps.twitterapp.Views.MessageFriendViewHolder;
import com.codepath.apps.twitterapp.Views.MessageViewHolder;
import com.codepath.apps.twitterapp.Views.TweetViewHolder;
import com.codepath.apps.twitterapp.models.CurrentUser;
import com.codepath.apps.twitterapp.models.Message;
import com.codepath.apps.twitterapp.models.MessagesController;
import com.codepath.apps.twitterapp.models.Tweet;
import com.codepath.apps.twitterapp.models.User;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageActivity extends AppCompatActivity {
    private ArrayList<Message> messages;
    private MessageArrayAdapter messageArrayAdapter;
    private MessagesController client;
    private User user;
    private int maxId;
    @Bind(R.id.rvMessages) RecyclerView rvMessages;
    @Bind(R.id.header_friend) View headerView;
    @Bind(R.id.etSendingText) EditText etSendingText;
    @Bind(R.id.prLoadingSpinner) RelativeLayout prLoadingSpinner;
    @Bind(R.id.tvNetworkUnavailable) TextView tvNetworkUnavailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        final Intent intent = getIntent();
        this.user = Parcels.unwrap(intent.getParcelableExtra("user"));

        setup();
    }

    private void setup() {
        messages = new ArrayList<>();

        client = MessagesController.getInstance();
        maxId = 1;
        messageArrayAdapter = new MessageArrayAdapter(messages);
        rvMessages.setAdapter(messageArrayAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvMessages.setLayoutManager(linearLayoutManager);
//        rvMessages.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount) {
//            }
//        });
        MessageFriendViewHolder viewHolder = new MessageFriendViewHolder(headerView, this);
        viewHolder.setUser(user);
        populateMessage();
    }

    private void populateMessage() {
        prLoadingSpinner.setVisibility(View.VISIBLE);
        client.getMessages(user.userId, new CallBack() {
            @Override
            public void messagesCallBack(ArrayList<Message> returnedMessages) {
                messages.addAll(returnedMessages);
                int position = messages.size()-1;
                messageArrayAdapter.notifyDataSetChanged();
                rvMessages.smoothScrollToPosition(position);
                prLoadingSpinner.setVisibility(View.INVISIBLE);
            }
        });
    }
    @OnClick(R.id.btSend)
    public void onSendMessage() {
        String text = etSendingText.getText().toString();
        prLoadingSpinner.setVisibility(View.VISIBLE);
        client.sendMessage(text, user, new CallBack(){
            @Override
            public void messageCallBack(Message message) {
                messages.add(message);
                int position = messages.size()-1;
                messageArrayAdapter.notifyItemInserted(position);
                rvMessages.smoothScrollToPosition(position);
                prLoadingSpinner.setVisibility(View.INVISIBLE);
                etSendingText.setText("");
            }
        });
    }

    private static class MessageArrayAdapter extends RecyclerView.Adapter<MessageViewHolder> {
        private ArrayList<Message> messages;
        private final int TYPE_SEND = 0, TYPE_RECEIVE = 1;

        public MessageArrayAdapter(ArrayList<Message> messages) {
            this.messages = messages;
        }

        @Override
        public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate( (viewType == TYPE_SEND?R.layout.item_message_send:R.layout.item_message_receive), parent, false);
            MessageViewHolder viewHolder = new MessageViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MessageViewHolder holder, int position) {
            holder.setMessage(messages.get(position));
        }

        @Override
        public int getItemViewType(int position) {
            Message message = messages.get(position);
            if (CurrentUser.user.userId == message.senderId) {
                return TYPE_SEND;
            }
            return TYPE_RECEIVE;
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }
    }

}
