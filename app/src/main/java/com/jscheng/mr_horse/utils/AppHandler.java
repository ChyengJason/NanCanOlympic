package com.jscheng.mr_horse.utils;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by cheng on 2017/3/27.
 */
public class AppHandler {
    private static AppHandler instance;

    public static AppHandler getInstance(){
        if (instance == null)
            instance = new AppHandler();
        return instance;
    }

    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            if (msg.getCallback() !=null )
                msg.getCallback().run();
        }
    };

    public void post(Runnable runnable){
        handler.post(runnable);
    }

    public void postDelay(Runnable runnable,long delayTime){
        handler.postDelayed(runnable,delayTime);
    }

    public void removeRunnable(Runnable runnable){
        handler.removeCallbacks(runnable);
    }
}
