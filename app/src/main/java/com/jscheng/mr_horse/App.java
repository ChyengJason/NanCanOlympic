package com.jscheng.mr_horse;

import android.app.Application;

/**
 * Created by cheng on 17-1-8.
 */
public class App extends Application{
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static App getInstance(){
        return instance;
    }
}
