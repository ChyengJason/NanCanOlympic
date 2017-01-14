package com.jscheng.mr_horse.presenter.impl;

import android.content.Context;
import android.content.Intent;

import com.jscheng.mr_horse.model.QuestionModelLoad;
import com.jscheng.mr_horse.presenter.WrongPresenter;
import com.jscheng.mr_horse.ui.PracticeActivity;
import com.jscheng.mr_horse.utils.Constants;
import com.jscheng.mr_horse.view.MvpView;
import com.jscheng.mr_horse.view.WrongView;
import com.orhanobut.logger.Logger;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by cheng on 17-1-14.
 */
public class WrongPresenterImpl implements WrongPresenter{
    private WrongView wrongView;
    private Context context;
    private boolean isCollect;//错题或收藏
    private boolean isWrong;
    private boolean isLoading;

    public WrongPresenterImpl(Context context,Intent intent){
        this.context = context;
        this.isLoading = false;
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
        String catogory = intent.getStringExtra(Constants.CATOGORY);
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

    @Override
    public void onClickRubbish() {
        if(isLoading)return;
        wrongView.showDeleteView();
    }

    @Override
    public void onClickCancel() {
        if(isLoading)return;
        wrongView.hideDeleteView();
    }

    @Override
    public void deleteFljc() {
        if(isLoading)return;
        if(isCollect){
            deleteCollect(Constants.FLJC);
        }else {
            deleteWrong(Constants.FLJC);
        }
    }

    @Override
    public void deleteMkszy() {
        if(isLoading)return;
        if(isCollect){
            deleteCollect(Constants.MKSZY);
        }else {
            deleteWrong(Constants.MKSZY);
        }
    }

    @Override
    public void deleteMzdsx() {
        if(isLoading)return;
        if(isCollect){
            deleteCollect(Constants.MZDSX);
        }else {
            deleteWrong(Constants.MZDSX);
        }
    }

    @Override
    public void deleteSxdd() {
        if(isLoading)return;
        if(isCollect){
            deleteCollect(Constants.SXDD);
        }else {
            deleteWrong(Constants.SXDD);
        }
    }

    @Override
    public void deleteZgjsds() {
        if(isLoading)return;
        if(isCollect){
            deleteCollect(Constants.ZGJDS);
        }else {
            deleteWrong(Constants.ZGJDS);
        }
    }

    @Override
    public void onClickFljcLayout() {
        Intent intent = new Intent(context, PracticeActivity.class);
        if(isCollect)
            intent.putExtra(Constants.CATOGORY,Constants.COLLECT);
        else
            intent.putExtra(Constants.CATOGORY,Constants.WRONG);
        intent.putExtra(Constants.CLASSIFICATION,Constants.FLJC);
        wrongView.startActivityForResult(intent);
    }

    @Override
    public void onClickMkszyLayout() {
        Intent intent = new Intent(context, PracticeActivity.class);
        if(isCollect)
            intent.putExtra(Constants.CATOGORY,Constants.COLLECT);
        else
            intent.putExtra(Constants.CATOGORY,Constants.WRONG);
        intent.putExtra(Constants.CLASSIFICATION,Constants.MKSZY);
        wrongView.startActivityForResult(intent);
    }

    @Override
    public void onClickMzdsxLayout() {
        Intent intent = new Intent(context, PracticeActivity.class);
        if(isCollect)
            intent.putExtra(Constants.CATOGORY,Constants.COLLECT);
        else
            intent.putExtra(Constants.CATOGORY,Constants.WRONG);
        intent.putExtra(Constants.CLASSIFICATION,Constants.MZDSX);
        wrongView.startActivityForResult(intent);
    }

    @Override
    public void onClickSxddLayout() {
        Intent intent = new Intent(context, PracticeActivity.class);
        if(isCollect)
            intent.putExtra(Constants.CATOGORY,Constants.COLLECT);
        else
            intent.putExtra(Constants.CATOGORY,Constants.WRONG);
        intent.putExtra(Constants.CLASSIFICATION,Constants.SXDD);
        wrongView.startActivityForResult(intent);
    }

    @Override
    public void onClickZgjdsLayout() {
        Intent intent = new Intent(context, PracticeActivity.class);
        if(isCollect)
            intent.putExtra(Constants.CATOGORY,Constants.COLLECT);
        else
            intent.putExtra(Constants.CATOGORY,Constants.WRONG);
        intent.putExtra(Constants.CLASSIFICATION,Constants.ZGJDS);
        wrongView.startActivityForResult(intent);
    }

    @Override
    public void onActivityForResult() {
        showQuestionNum();
    }

    private void deleteCollect(final String catogory){
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                QuestionModelLoad.removeCollectQuestionModels(context,catogory);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        wrongView.showProcessing();
                        isLoading = true;
                    }

                    @Override
                    public void onCompleted() {
                        wrongView.hideProcessing();
                        wrongView.hideDeleteView();
                        showQuestionNum();
                        isLoading = false;
                    }

                    @Override
                    public void onError(Throwable e) {
                        wrongView.hideProcessing();
                        isLoading = false;
                    }

                    @Override
                    public void onNext(Object o) {
                    }
                });
    }

    private void deleteWrong(final String catogory){
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                QuestionModelLoad.removeWrongQuestionModels(context,catogory);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onStart() {
                        wrongView.showProcessing();
                        isLoading = true;
                    }

                    @Override
                    public void onCompleted() {
                        wrongView.hideProcessing();
                        wrongView.hideDeleteView();
                        showQuestionNum();
                    }

                    @Override
                    public void onError(Throwable e) {
                        wrongView.hideProcessing();
                        isLoading = false;
                    }

                    @Override
                    public void onNext(Object o) {
                        wrongView.hideProcessing();
                        isLoading = false;
                    }
                });
    }
}
