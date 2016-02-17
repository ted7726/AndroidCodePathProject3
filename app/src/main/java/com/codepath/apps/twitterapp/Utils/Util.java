package com.codepath.apps.twitterapp.Utils;

import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;

/**
 * Created by weishengsu on 2/17/16.
 */
public class Util {
    public static String converTimetoRelativeTime(Date time) {
        String relativeDate = DateUtils.getRelativeTimeSpanString(time.getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_TIME).toString();
        relativeDate.replaceFirst("hour", "h");
        relativeDate.replaceFirst("minute", "min");
        return relativeDate;
    }

    public static String numberConverter(int n) {
        String text =  (n > 1000 ? n / 1000 + "k" : n + "");
        return text;
    }
    public static String checkStringEmpty(String s) {
        if (s==null)return "";
        return TextUtils.isEmpty(s)?"":s;
    }

    public static void setLayoutHeight(boolean isWrapContent, View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = isWrapContent ? ViewGroup.LayoutParams.WRAP_CONTENT : 0;
        view.setLayoutParams(params);
    }
}