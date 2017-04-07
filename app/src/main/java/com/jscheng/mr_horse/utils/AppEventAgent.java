package com.jscheng.mr_horse.utils;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by cheng on 2017/4/7.
 */
public class AppEventAgent {
    private AppEventAgent(){}
    public static void onEvent(Context context, String eventId){
        MobclickAgent.onEvent(context,eventId);
    }
}
