package com.jscheng.mr_horse.view;

/**
 * Created by cheng on 2017/3/21.
 */
public interface SearchView extends MvpView {
    void showProcessing(int i);
    void beginProcess();
    void showError(String s);
    void failProcessing();
    void sucessProcessing();
}
