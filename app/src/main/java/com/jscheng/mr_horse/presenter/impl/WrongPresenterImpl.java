package com.jscheng.mr_horse.presenter.impl;

import android.content.Context;
import android.content.Intent;

import com.jscheng.mr_horse.model.QuestionModel;
import com.jscheng.mr_horse.model.QuestionModelLoad;
import com.jscheng.mr_horse.presenter.WrongPresenter;
import com.jscheng.mr_horse.utils.Constants;
import com.jscheng.mr_horse.view.MvpView;
import com.jscheng.mr_horse.view.WrongView;
import com.orhanobut.logger.Logger;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by cheng on 17-1-14.
 */
public class WrongPresenterImpl implements WrongPresenter{
    private WrongView wrongView;
    private Context context;
    private boolean isCollect;//错题或收藏
    private boolean isWrong;

    public WrongPresenterImpl(Context context,Intent intent){
        this.context = context;
        initCatogory(intent);
    }

    @Override
    public void attachView(MvpView view) {
        this.wrongView = (WrongView)view;
        init();
    }

    private void init() {
        if (isCollect) wrongView.setBarTitle(Constants.COLLECT_NAME);
        else wrongView.setBarTitle(Constants.WRONG_NAME);
        showQuestionNum();
    }

    private void initCatogory(Intent intent) {
        String catogory = intent.getStringExtra("catogory");
        Logger.e(catogory);
        if(catogory!=null && catogory.equals(Constants.COLLECT)){
            isCollect = true;
            isWrong = false;
        }else {
            isCollect = false;
            isWrong = true;
        }
    }

    private void showQuestionNum() {
        if (isCollect) {
            wrongView.showFljcNumTv(QuestionModelLoad.getCollectQuestionModelNum(context,Constants.FLJC));
            wrongView.showMkszyNumTv(QuestionModelLoad.getCollectQuestionModelNum(context,Constants.MKSZY));
            wrongView.showMzdsxNumTv(QuestionModelLoad.getCollectQuestionModelNum(context,Constants.MZDSX));
            wrongView.showSxddNumTv(QuestionModelLoad.getCollectQuestionModelNum(context,Constants.SXDD));
            wrongView.showZgjdsNumTv(QuestionModelLoad.getCollectQuestionModelNum(context,Constants.ZGJDS));
        }else {
            wrongView.showFljcNumTv(QuestionModelLoad.getWrongQuestionModelNum(context,Constants.FLJC));
            wrongView.showMkszyNumTv(QuestionModelLoad.getWrongQuestionModelNum(context,Constants.MKSZY));
            wrongView.showMzdsxNumTv(QuestionModelLoad.getWrongQuestionModelNum(context,Constants.MZDSX));
            wrongView.showSxddNumTv(QuestionModelLoad.getWrongQuestionModelNum(context,Constants.SXDD));
            wrongView.showZgjdsNumTv(QuestionModelLoad.getWrongQuestionModelNum(context,Constants.ZGJDS));
        }
    }

    @Override
    public void detachView(boolean retainInstance) {
        if(retainInstance==false){
            this.wrongView = null;
        }
    }
}
