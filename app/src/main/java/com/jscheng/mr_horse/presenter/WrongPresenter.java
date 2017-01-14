package com.jscheng.mr_horse.presenter;

import android.content.Intent;

/**
 * Created by cheng on 17-1-14.
 */
public interface WrongPresenter extends MvpPresenter {
    void onClickRubbish();
    void onClickCancel();
    void deleteFljc();
    void deleteMkszy();
    void deleteMzdsx();
    void deleteSxdd();
    void deleteZgjsds();
    void onClickFljcLayout();
    void onClickMkszyLayout();
    void onClickMzdsxLayout();
    void onClickSxddLayout();
    void onClickZgjdsLayout();
    void onActivityForResult();
}
