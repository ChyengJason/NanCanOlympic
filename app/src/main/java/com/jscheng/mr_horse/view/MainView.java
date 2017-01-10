package com.jscheng.mr_horse.view;

/**
 * Created by cheng on 17-1-8.
 */
public interface MainView extends MvpView {
    void changeToNightTheme();
    void changeToSunTheme();
    void showDate(String date);
}
