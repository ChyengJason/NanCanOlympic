package com.jscheng.mr_horse.presenter.impl;

import android.content.Context;
import android.content.Intent;

import com.jscheng.mr_horse.App;
import com.jscheng.mr_horse.R;
import com.jscheng.mr_horse.model.QuestionDoneType;
import com.jscheng.mr_horse.model.QuestionModelLoad;
import com.jscheng.mr_horse.utils.Constants;
import com.jscheng.mr_horse.model.QuestionJsonModel;
import com.jscheng.mr_horse.model.QuestionModel;
import com.jscheng.mr_horse.model.PatternStatus;
import com.jscheng.mr_horse.presenter.AnswerPresenter;
import com.jscheng.mr_horse.utils.JsonUtil;
import com.jscheng.mr_horse.utils.SharedPreferencesUtil;
import com.jscheng.mr_horse.view.AnswerView;
import com.jscheng.mr_horse.view.MvpView;
import com.jscheng.mr_horse.wiget.QuestionDailog;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by cheng on 17-1-7.
 */
public class AnswerPresenterImpl implements AnswerPresenter{
    private List<QuestionModel> questionModelList;
    private PatternStatus patternStatus;
    private AnswerView mAnswerView;
    private Context mContext;
    private int pageNum;
    private int rightNum;
    private int wrongNum;
    private String filename;
    private String catogory;
    private boolean loading;
    private Subscription loadSubscription;

    public AnswerPresenterImpl(Context context, Intent intent){
        this.mContext = context;
        this.patternStatus = PatternStatus.DATI_PATTERN;//默认是答题模式，测试使用背题模式
        this.questionModelList = new ArrayList();
        this.pageNum = 0;
        this.catogory = intent.getStringExtra("catogory");
        if(!catogory.equals(Constants.COLLECT) && !(catogory.equals(Constants.WRONG))){
            this.filename = intent.getStringExtra("filename");
        }else {
            this.filename = "";
        }
    }

    @Override
    public void attachView(MvpView view) {
        this.mAnswerView = (AnswerView) view;
        init();
    }

    @Override
    public void detachView(boolean retainInstance) {
        if(!retainInstance) {
            this.mAnswerView = null;
        }
    }

    public void init() {
        initPattern();
        initData();
    }

    private void initData() {
        wrongNum = 0;
        rightNum = 0;

        Subscriber subscriber = new Subscriber<List<QuestionModel>>() {
            @Override
            public void onStart() {
                super.onStart();
                loading = true;
                mAnswerView.showProcessing();
            }

            @Override
            public void onCompleted() {
                if (mAnswerView==null)
                    return;
                mAnswerView.hideProcessing();
                loading = false;
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e.toString());
                mAnswerView.showError(e.toString());
            }

            @Override
            public void onNext(List<QuestionModel> questionModelList) {
                if(mAnswerView==null)
                    return;
                mAnswerView.initPaperAdapter(questionModelList,patternStatus);
                pageNum = QuestionModelLoad.getQuestionDoneNum(mContext,catogory);
                if(pageNum>0)
                    mAnswerView.changePaperView(pageNum,false,0);
                mAnswerView.showPageNumView(pageNum+1,questionModelList.size());
            }
        };

        loadSubscription = Observable.create(new Observable.OnSubscribe<List<QuestionJsonModel>>() {
            @Override
            public void call(Subscriber<? super List<QuestionJsonModel>> subscriber) {
                subscriber.onStart();
                try {
                    List<QuestionJsonModel> questionModelList = QuestionModelLoad.getQuestionJsonModels(mContext,filename);
                    subscriber.onNext(questionModelList);
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }

                subscriber.onCompleted();
            }
        }).map(new Func1<List<QuestionJsonModel>, List<QuestionModel>>() {
            @Override
            public List<QuestionModel> call(List<QuestionJsonModel> questionJsonModel) {

                for(QuestionJsonModel model : questionJsonModel){
                    questionModelList.add(model.toQuestionModel());
                }
                return questionModelList;
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(subscriber);
    }

    private void initPattern() {
        if(mAnswerView==null)
            return;
        if(patternStatus ==PatternStatus.DATI_PATTERN )
            mAnswerView.changeToDatiView();
        else
            mAnswerView.changeToBeitiView();
    }

    @Override
    public void onPageSelected(int position) {
        this.pageNum = position;
        QuestionModelLoad.saveQuestionDoneNum(mContext,catogory,pageNum);
        mAnswerView.showPageNumView(pageNum+1,questionModelList.size());
    }

    @Override
    public void onAnswerRight(int postion) {
        this.pageNum = postion;
        if(questionModelList!=null &&!questionModelList.isEmpty()){
            int pageCount = questionModelList.size();
            if((pageNum+1)<pageCount){
                pageNum++;
                mAnswerView.changePaperView(pageNum,true,300);
            }
        }
        int haveDoneNum = (int)SharedPreferencesUtil.getParam(mContext,Constants.HAVE_DONE_NUM,0);
        SharedPreferencesUtil.setParam(mContext,Constants.HAVE_DONE_NUM,++haveDoneNum);
        mAnswerView.showRightNumView(++rightNum);
    }

    @Override
    public void onAnswerWrong(int postion) {
        mAnswerView.showWrongNumView(++wrongNum);
        int haveDoneNum = (int)SharedPreferencesUtil.getParam(mContext,Constants.HAVE_DONE_NUM,0);
        SharedPreferencesUtil.setParam(mContext,Constants.HAVE_DONE_NUM,++haveDoneNum);
    }

    public void onClickDatiPattern() {
        if(patternStatus==PatternStatus.DATI_PATTERN)
            return;
        patternStatus=PatternStatus.DATI_PATTERN;
        mAnswerView.changeToDatiView();
        mAnswerView.changeAdapterPattern(patternStatus);
    }

    public void onClickBeitiPattern() {
        if(patternStatus==PatternStatus.BETI_PATTERN)
            return;
        patternStatus=PatternStatus.BETI_PATTERN;
        mAnswerView.changeToBeitiView();
        mAnswerView.changeAdapterPattern(patternStatus);
    }

    @Override
    public void changeTheme() {
        if(loadSubscription!=null)
            loadSubscription.unsubscribe();

        int dayNightTheme = App.getDayNightTheme();
        if (dayNightTheme == R.style.SunAppTheme) {
            App.setDayNightTheme(R.style.NightAppTheme);
            mAnswerView.changeToNightTheme();
        } else {
            App.setDayNightTheme(R.style.SunAppTheme);
            mAnswerView.changeToSunTheme();
        }

        SharedPreferencesUtil.setParam(mContext,Constants.THEME,App.getDayNightTheme());
    }

    @Override
    public void onClickQuestionsLayout() {
        if(loading==false && questionModelList!=null && !questionModelList.isEmpty())
            mAnswerView.showQuestionDailog(questionModelList,pageNum);
    }

    @Override
    public void onItemSelected(int position) {
        pageNum = position;
        mAnswerView.changePaperView(pageNum,false,0);
    }
}
