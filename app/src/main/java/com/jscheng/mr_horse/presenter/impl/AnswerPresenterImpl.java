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
import com.jscheng.mr_horse.utils.QuestionDB;
import com.jscheng.mr_horse.utils.QuestionDbUtil;
import com.jscheng.mr_horse.utils.SharedPreferencesUtil;
import com.jscheng.mr_horse.view.AnswerView;
import com.jscheng.mr_horse.view.MvpView;
import com.jscheng.mr_horse.wiget.QuestionDailog;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private Observable loadSubscription;
    private Observable storeSubscription;
//    private Observable dbCopySubscription;
    private Subscriber subscriber;

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
//        final boolean isFirst = (Boolean) SharedPreferencesUtil.getParam(mContext,"first",true);
        final boolean isStore = (Boolean) SharedPreferencesUtil.getParam(mContext,catogory,false);

        loadSubscription = Observable.create(new Observable.OnSubscribe<List<QuestionModel>>() {
            @Override
            public void call(Subscriber<? super List<QuestionModel>> subscriber) {
                List<QuestionModel> modelList = QuestionModelLoad.getQuestionModelsfromDB(mContext,catogory);
                Logger.e("已从数据库获取到数据");
                subscriber.onNext(modelList);
            }
        });

        storeSubscription = Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                if(isStore==false){
                    try {
                        List<QuestionJsonModel> questionJsonModelList = QuestionModelLoad.getQuestionJsonModels(mContext,filename);
                        Logger.e("已加载完文件");
                        for(QuestionJsonModel model : questionJsonModelList){
                            questionModelList.add(model.toQuestionModel(catogory));
                        }
                        QuestionModelLoad.saveQuestionModelToDB(mContext,questionModelList);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                subscriber.onCompleted();
            }
        });

//        dbCopySubscription = Observable.create(new Observable.OnSubscribe<Object>() {
//            @Override
//            public void call(Subscriber<? super Object> subscriber) {
//                if(isFirst){
//                    try {
//                        QuestionDB.CopySqliteFileFromRawToDatabases(mContext,"Mr_Horse.db");
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
////                SharedPreferencesUtil.setParam(mContext,"first",false);
//                subscriber.onCompleted();
//            }
//        });

        subscriber = new Subscriber<List<QuestionModel>>() {
            @Override
            public void onStart() {
                super.onStart();
                loading = true;
                if (isStore == false) {
                    mAnswerView.showProcessing();
                    mAnswerView.showInfo("首次加载会消耗较长时间");
                }
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e.toString());
                mAnswerView.showError(e.toString());
                mAnswerView.hideProcessing();
            }

            @Override
            public void onNext(List<QuestionModel> ModelList) {
                if(mAnswerView==null)
                    return;
                questionModelList.addAll(ModelList);
                mAnswerView.initPaperAdapter(questionModelList,patternStatus);
                pageNum = QuestionModelLoad.getQuestionDoneNum(mContext,catogory);
                if(pageNum>0)
                    mAnswerView.changePaperView(pageNum,false,0);
                mAnswerView.showPageNumView(pageNum+1,questionModelList.size());
                showCollect();
                if(questionModelList.get(pageNum).isCollected()==true)
                    mAnswerView.showCollectView(true);
                else
                    mAnswerView.showCollectView(false);

                if(isStore==false)
                    SharedPreferencesUtil.setParam(mContext,catogory,true);

                if (mAnswerView==null)
                    return;
                mAnswerView.hideProcessing();
                loading = false;
            }
        };

        Observable.concat(storeSubscription,loadSubscription)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
        showCollect();
    }

    @Override
    public void onAnswerRight(int postion) {

        questionModelList.get(pageNum).setNewDone(QuestionDoneType.DONE_RIGHT);
        questionModelList.get(pageNum).setDone(QuestionDoneType.DONE_RIGHT);
        QuestionModelLoad.setQuestionModelDone(mContext,questionModelList.get(postion));

        int haveDoneNum = (int)SharedPreferencesUtil.getParam(mContext,Constants.HAVE_DONE_NUM,0);
        SharedPreferencesUtil.setParam(mContext,Constants.HAVE_DONE_NUM,++haveDoneNum);

        this.pageNum = postion;
        if(questionModelList!=null &&!questionModelList.isEmpty()){
            int pageCount = questionModelList.size();
            if((pageNum+1)<pageCount){
                pageNum++;
                mAnswerView.changePaperView(pageNum,true,300);
            }
        }

        mAnswerView.showRightNumView(++rightNum);
        showCollect();
    }

    @Override
    public void onAnswerWrong(int postion) {
        mAnswerView.showWrongNumView(++wrongNum);
        int haveDoneNum = (int)SharedPreferencesUtil.getParam(mContext,Constants.HAVE_DONE_NUM,0);
        SharedPreferencesUtil.setParam(mContext,Constants.HAVE_DONE_NUM,++haveDoneNum);
        questionModelList.get(postion).setNewDone(QuestionDoneType.DONE_WRONG);
        questionModelList.get(postion).setDone(QuestionDoneType.DONE_WRONG);
        QuestionModelLoad.setQuestionModelDone(mContext,questionModelList.get(postion));
        //加入错题集
        QuestionModelLoad.setQuestionModelToWrongCollect(mContext,questionModelList.get(postion));
    }

    public void onClickDatiPattern() {
        if(patternStatus==PatternStatus.DATI_PATTERN || loading)
            return;
        patternStatus=PatternStatus.DATI_PATTERN;
        mAnswerView.changeToDatiView();
        mAnswerView.changeAdapterPattern(patternStatus);
    }

    public void onClickBeitiPattern() {
        if(patternStatus==PatternStatus.BETI_PATTERN || loading)
            return;
        patternStatus=PatternStatus.BETI_PATTERN;
        mAnswerView.changeToBeitiView();
        mAnswerView.changeAdapterPattern(patternStatus);
    }

    @Override
    public void changeTheme() {
        if(subscriber!=null)
            subscriber.unsubscribe();

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
    public void onClickCollect() {
        if(loading || questionModelList.isEmpty())
            return;
        QuestionModel model = questionModelList.get(pageNum);
        if(model.isCollected()){
            model.setCollected(false);
            QuestionModelLoad.setQuestionModelOutCollect(mContext,model);
        }else {
            model.setCollected(true);
            QuestionModelLoad.setQuestionModelToCollect(mContext,model);
        }
        showCollect();
    }

    private void showCollect(){
        mAnswerView.showCollectView(questionModelList.get(pageNum).isCollected());
    }

    @Override
    public void onItemSelected(int position) {
        pageNum = position;
        mAnswerView.changePaperView(pageNum,false,0);
        showCollect();
    }
}
