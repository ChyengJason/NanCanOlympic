package com.jscheng.mr_horse.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jscheng.mr_horse.App;
import com.jscheng.mr_horse.utils.Constants;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by cheng on 17-1-8.
 */
public class BaseActivity extends AppCompatActivity {

    protected int activityTheme;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTheme = App.getDayNightTheme();
        setTheme(activityTheme);
        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(this, Constants.YOU_MENG_APP_KEY, Constants.CHANNEL_ID));
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        Logger.e("theme",(activityTheme)+"");
        Logger.e("theme",App.getDayNightTheme()+"");
        if(getActivityTheme()!=App.getDayNightTheme()){
            recreate();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public int getActivityTheme(){
        return activityTheme;
    }
}