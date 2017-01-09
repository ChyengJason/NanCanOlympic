package com.jscheng.mr_horse;

import android.app.Application;

import com.jscheng.mr_horse.utils.Constants;
import com.jscheng.mr_horse.utils.SharedPreferencesUtil;

/**
 * Created by cheng on 17-1-8.
 */
public class App extends Application{
    private static App instance;
    private static int dayNightTheme;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        dayNightTheme= (int)SharedPreferencesUtil.getParam(this, Constants.THEME,R.style.SunAppTheme);//测试使用
    }

    public static App getInstance(){
        return instance;
    }

    public static int getDayNightTheme() { return dayNightTheme; }

    public static void setDayNightTheme(int theme) {dayNightTheme = theme; }
}
