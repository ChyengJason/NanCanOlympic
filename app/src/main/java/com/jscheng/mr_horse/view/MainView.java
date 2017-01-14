package com.jscheng.mr_horse.view;

import android.content.Intent;

/**
 * Created by cheng on 17-1-8.
 */
public interface MainView extends MvpView {
    void changeToNightTheme();
    void changeToSunTheme();
    void showDate(String date);
    void showHaveDoneNum(String s);
    void showPunchDayNum(String s);
    void startActivityForResult(Intent intent);
}
