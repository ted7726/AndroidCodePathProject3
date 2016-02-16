package com.codepath.apps.twitterapp.Adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.codepath.apps.twitterapp.models.Tweet;

import java.util.ArrayList;

/**
 * Created by weishengsu on 2/15/16.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    public TweetsArrayAdapter(Context context, ArrayList<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }
}
