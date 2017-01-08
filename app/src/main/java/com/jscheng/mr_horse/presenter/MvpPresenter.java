package com.jscheng.mr_horse.presenter;

import com.jscheng.mr_horse.view.MvpView;

/**
 * Created by cheng on 17-1-7.
 */
public interface MvpPresenter<V extends MvpView> {

    /**
     * Bind presenter with MvpView
     * @param view
     */
    void attachView(V view);

    /**
     * @param retainInstance
     * unBind
     */
    void detachView(boolean retainInstance);

}
