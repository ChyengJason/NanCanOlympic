package com.jscheng.mr_horse.presenter.impl;

import android.content.Context;

import com.jscheng.mr_horse.model.QuestionJsonModel;
import com.jscheng.mr_horse.model.QuestionModel;
import com.jscheng.mr_horse.model.QuestionModelLoad;
import com.jscheng.mr_horse.presenter.SearchPresenter;
import com.jscheng.mr_horse.utils.Constants;
import com.jscheng.mr_horse.utils.QuestionDbUtil;
import com.jscheng.mr_horse.utils.SharedPreferencesUtil;
import com.jscheng.mr_horse.view.MvpView;
import com.jscheng.mr_horse.view.SearchView;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cheng on 2017/3/21.
 */
public class SearchPresenterImpl implements SearchPresenter {
    private SearchView mSearchView;
    private Context mContext;
    private Observable<List<QuestionModel>> storeSubscription;
    private Observable<List<QuestionModel>> loadSubscription;
    private Subscriber subscriber;
    private boolean isLoading;

    public SearchPresenterImpl(Context mContext){
        this.mContext = mContext;
        this.isLoading = false;
    }

    @Override
    public void attachView(MvpView view) {
        this.mSearchView =(SearchView)view;
    }

    @Override
    public void detachView(boolean retainInstance) {
        this.mSearchView = null;
        this.mContext = null;
        if (subscriber!=null)
            subscriber.unsubscribe();
    }

    @Override
    public void onClickSearch(String searchText) {
        if (isLoading)
            return;
        Map<String,String> loadMap = checkData();
        storeAndLoadData(loadMap);
    }

    private Map<String,String> checkData() {
        Map loadMap = new HashMap();
        if ( !(boolean)SharedPreferencesUtil.getParam(mContext, Constants.FLJC,false) ){
            loadMap.put(Constants.FLJC,Constants.FLJC_JSON_NAME);
        }
        if ( !(boolean)SharedPreferencesUtil.getParam(mContext, Constants.MKSZY,false) ){
            loadMap.put(Constants.MKSZY,Constants.MKSZY_JSON_NAME);
        }
        if ( !(boolean)SharedPreferencesUtil.getParam(mContext, Constants.MZDSX,false) ){
            loadMap.put(Constants.MZDSX,Constants.MZDSX_JSON_NAME);
        }
        if ( !(boolean)SharedPreferencesUtil.getParam(mContext, Constants.ZGJDS,false) ){
            loadMap.put(Constants.ZGJDS,Constants.ZGJDS_JSON_NAME);
        }
        if ( !(boolean)SharedPreferencesUtil.getParam(mContext, Constants.SXDD,false) ){
            loadMap.put(Constants.SXDD,Constants.SXDD_JSON_NAME);
        }
        return loadMap;
    }

    private void storeAndLoadData(final Map<String,String> loadMap) {
        storeSubscription = Observable.create(new Observable.OnSubscribe<List<QuestionModel>>() {
            @Override
            public void call(Subscriber<? super List<QuestionModel>> subscriber) {
                subscriber.onStart();
                final List<QuestionModel> questionModelList = new ArrayList<QuestionModel>();
                try {
                    for (String catogory : loadMap.keySet()) {
                        String filename = loadMap.get(catogory);
                        for (QuestionJsonModel questionJsonModel : QuestionModelLoad.getQuestionJsonModels(mContext, filename)) {
                            questionModelList.add(questionJsonModel.toQuestionModel(catogory));
                        }
                    }
                    QuestionModelLoad.saveQuestionModelToDB(mContext, questionModelList, new QuestionDbUtil.DbProgressListener() {
                        @Override
                        public void loadProgress(int progress) {
                            if (mSearchView != null)
                                mSearchView.showProcessing((int) (progress * 100 / questionModelList.size()));
                        }
                    });
                    for (String catogory : loadMap.keySet()) {
                        SharedPreferencesUtil.setParam(mContext, catogory,true);
                    }
                    Logger.e(questionModelList.size() + "");
                    subscriber.onNext(questionModelList);
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });

        loadSubscription = Observable.create(new Observable.OnSubscribe<List<QuestionModel>>() {
            @Override
            public void call(Subscriber<? super List<QuestionModel>> subscriber) {
                subscriber.onNext(null);
            }
        });

        subscriber = new Subscriber() {
            @Override
            public void onStart() {
                super.onStart();
                Logger.e("查询开始");
                isLoading = true;
                if (mSearchView == null)
                    return;
                mSearchView.beginProcess();
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                isLoading = false;
                if (mSearchView == null)
                    return;
                mSearchView.showError(e.toString());
                mSearchView.failProcessing();
            }

            @Override
            public void onNext(Object o) {
                isLoading = false;
                if (mSearchView == null)
                    return;
                mSearchView.sucessProcessing();
                Logger.e("查询结束");
            }
        };

        if (loadMap.isEmpty() ) {
            loadSubscription.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        } else {
            Observable.concat(storeSubscription, loadSubscription)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }
}
