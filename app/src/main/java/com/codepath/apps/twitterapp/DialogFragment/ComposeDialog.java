package com.codepath.apps.twitterapp.DialogFragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.models.CurrentUser;
import com.codepath.apps.twitterapp.models.Tweet;
import com.codepath.apps.twitterapp.models.User;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by weishengsu on 2/19/16.
 */
public class ComposeDialog extends DialogFragment {

    @Bind(R.id.etComposeTweet) EditText etComposeTweet;
    @Bind(R.id.ivMessageUserProfile) ImageView ivComposeUserProfile;
    @Bind(R.id.tvWordCounts) TextView tvWordCounts;
    @Bind(R.id.tvMessageName) TextView tvComposeName;
    @Bind(R.id.tvComposeUsername) TextView tvComposeUsername;
    @Bind(R.id.btDialogCompose) FloatingActionButton btDialogCompose;
    @Bind(R.id.btDialogDismiss) ImageButton btDialogDismiss;
    private String replyTweetId;
    private String replyTweetUserScreen;

    public static ComposeDialog newInstance() {
        return newInstance(null);
    }

    public static ComposeDialog newInstance(Tweet replyTweet) {
        ComposeDialog frag = new ComposeDialog();
        Bundle args = new Bundle();
        args.putString("replyTweetUserScreen",(replyTweet!=null ? replyTweet.user.screenName : ""));
        args.putString("replyTweetId", (replyTweet!=null ? Long.toString(replyTweet.id) : ""));
        frag.setArguments(args);
        return frag;
    }

    public interface ComposeDialogListener {
        void onFinishComposeDialog(String composeText);
        void onFinishComposeDialog(String composeText, String tweetId);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.compose_fragment, container);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        replyTweetId = getArguments().getString("replyTweetId");
        replyTweetUserScreen = getArguments().getString("replyTweetUserScreen");

        // Show soft keyboard automatically and request focus to field
        etComposeTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int len = 140 - s.length();
                tvWordCounts.setText(Integer.toString(len));
                if (len > 20) {
                    tvWordCounts.setTextColor(0xff99CC00);
                } else if (len > 0) {
                    tvWordCounts.setTextColor(0xffffbb33);
                } else {
                    tvWordCounts.setTextColor(0xffcc0000);
                }

                if (len > 0) {
                    btDialogCompose.setEnabled(true);
                } else {
                    btDialogCompose.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        btDialogDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btDialogCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComposeDialogListener listener = (ComposeDialogListener) getActivity();
                if (replyTweetId.length()>0 && replyTweetUserScreen.length()>0) {
                    listener.onFinishComposeDialog("@" + replyTweetUserScreen + " " + etComposeTweet.getText().toString(), replyTweetId);
                } else {
                    listener.onFinishComposeDialog(etComposeTweet.getText().toString());
                }
                dismiss();
            }
        });

        etComposeTweet.requestFocus();
        if (CurrentUser.user != null) {
            User user = CurrentUser.user;
            tvComposeName.setText(user.name);
            tvComposeUsername.setText("@"+user.screenName);
            Glide.with(this).load(user.profileImageUrl).fitCenter().placeholder(R.drawable.ic_profile).into(ivComposeUserProfile);
        }

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}
