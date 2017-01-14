package com.jscheng.mr_horse.view;

import android.content.Intent;

/**
 * Created by cheng on 17-1-14.
 */
public interface WrongView extends MvpView {
    void setBarTitle(String title);
    void showFljcNumTv(int num);
    void showMkszyNumTv(int num);
    void showMzdsxNumTv(int num);
    void showSxddNumTv(int num);
    void showZgjdsNumTv(int num);
    void showDeleteView();
    void hideDeleteView();
    void showProcessing();
    void showError();
    void hideProcessing();
    void startActivityForResult(Intent intent);
}
