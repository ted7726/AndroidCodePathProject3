package com.codepath.apps.twitterapp.DialogFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

import com.codepath.apps.twitterapp.R;

/**
 * Created by wilsonsu on 2/19/16.
 */
public class VideoFragmentDialog extends DialogFragment {
    private String videoUrl;

    public VideoFragmentDialog() {
    }

    public static VideoFragmentDialog newInstance(String url) {
        VideoFragmentDialog frag = new VideoFragmentDialog();
        Bundle args = new Bundle();
        args.putString("url", url);

        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.video_fragment_layout, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        VideoView videoView = (VideoView) view.findViewById(R.id.videoView);
        videoView.setZOrderOnTop(true);

        String url = getArguments().getString("url", "");
        if (url.length()>0) {
            videoView.setVideoPath(url);
            videoView.start();
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        params.height = WindowManager.LayoutParams.MATCH_PARENT;

        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }
}
