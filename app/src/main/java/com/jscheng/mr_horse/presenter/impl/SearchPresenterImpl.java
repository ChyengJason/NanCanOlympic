package com.jscheng.mr_horse.presenter.impl;

import android.content.Context;

import com.jscheng.mr_horse.model.QuestionJsonModel;
import com.jscheng.mr_horse.model.QuestionModel;
import com.jscheng.mr_horse.model.QuestionModelLoad;
import com.jscheng.mr_horse.presenter.SearchPresenter;
import com.jscheng.mr_horse.utils.Constants;
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
    private static final int PageMaxLine = 20;
    private SearchView mSearchView;
    private Context mContext;
    private Observable<List<QuestionModel>> storeSubscription;
    private Observable<List<QuestionModel>> loadSubscription;
    private Subscriber<List<QuestionModel>> subscriber;
    private Subscriber<List<QuestionModel>> loadMoreSubscriber;
    private boolean isSearching;
    private boolean isStoring;
    private int pageNum;
    private boolean isLastPage;

    public SearchPresenterImpl(Context mContext){
        this.mContext = mContext;
        this.isSearching = false;
        this.isStoring = false;
        this.pageNum = 0;
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
        if (isStoring || isSearching)
            return;
        if (searchText == null || searchText.trim().isEmpty())
            return;
        this.pageNum = 0;
        this.isLastPage = false;

        Map<String,String> loadMap = checkData();
        storeAndLoadData(loadMap,searchText);
    }

    @Override
    public void onClickMoreLayout() {
        if (isStoring || isSearching)
            return;
        if (loadSubscription!=null ){
            if (loadMoreSubscriber == null)
                loadMoreSubscriber = new Subscriber<List<QuestionModel>>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        Logger.e("查询错误"+e.toString());
                        isSearching = false;
                        if (mSearchView == null)
                            return;
                        mSearchView.showError(e.toString());
                    }
                    @Override
                    public void onNext(List<QuestionModel> questionModels) {
                        isSearching = false;
                        if (!questionModels.isEmpty())
                            mSearchView.addSearchResults(questionModels);
                        if (isLastPage)
                            mSearchView.showLastPageView();
                        else
                            mSearchView.showMorePageView();
                    }
                };
            loadSubscription.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(loadMoreSubscriber);
        }
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

    private void storeAndLoadData(final Map<String,String> loadMap, final String searchText) {
        storeSubscription = Observable.create(new Observable.OnSubscribe<List<QuestionModel>>() {
            @Override
            public void call(Subscriber<? super List<QuestionModel>> subscriber) {
                subscriber.onStart();
                isStoring = true;
                try {
                    for (String catogory : loadMap.keySet()) {
                        final List<QuestionModel> questionModelList = new ArrayList<QuestionModel>();
                        for (QuestionJsonModel questionJsonModel : QuestionModelLoad.getQuestionJsonModels(mContext, loadMap.get(catogory))) {
                            questionModelList.add(questionJsonModel.toQuestionModel(catogory));
                        }
                        QuestionModelLoad.saveQuestionModelToDB(mContext, questionModelList);
                        Logger.e(questionModelList.size() + "");
                        Logger.e(catogory + "");
                        SharedPreferencesUtil.setParam(mContext, catogory,true);
                    }
                    Logger.e("存储结束");
                    subscriber.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                    Logger.e(e+ "");
                }
            }
        });

        loadSubscription = Observable.create(new Observable.OnSubscribe<List<QuestionModel>>() {
            @Override
            public void call(Subscriber<? super List<QuestionModel>> subscriber) {
                isSearching = true;
                List<QuestionModel> results = QuestionModelLoad.searchQuestionModelsfromDB(mContext,searchText,PageMaxLine,pageNum * PageMaxLine);
                pageNum++;
                if (results.size() < PageMaxLine)
                    isLastPage = true;
                subscriber.onNext(results);
            }
        });

        subscriber = new Subscriber<List<QuestionModel>>() {
            @Override
            public void onStart() {
                super.onStart();
                Logger.e("查询开始");
                if (mSearchView == null)
                    return;
                if (isStoring) mSearchView.showInfo("首次加载会消耗较长时间");
                mSearchView.beginProcess();
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Logger.e("查询错误"+e.toString());
                isStoring = false;
                isSearching = false;
                if (mSearchView == null)
                    return;
                mSearchView.showError(e.toString());
                mSearchView.failProcessing();
            }

            @Override
            public void onNext(List<QuestionModel> results) {
                isStoring = false;
                isSearching = false;
                if (mSearchView == null)
                    return;
                mSearchView.sucessProcessing();
                Logger.e("查询结束"+results.size());
                if (results.size() > 0)
                    mSearchView.showSearchResult(searchText,results);
                else
                    mSearchView.clearSearchResult();

                if (isLastPage){
                    mSearchView.showLastPageView();
                }else {
                    mSearchView.showMorePageView();
                }

                for (QuestionModel model : results){
                    Logger.e(model.toString());
                }
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
