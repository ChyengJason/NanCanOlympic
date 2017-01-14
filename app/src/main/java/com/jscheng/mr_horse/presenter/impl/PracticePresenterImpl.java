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
import com.jscheng.mr_horse.presenter.PracticePresenter;
import com.jscheng.mr_horse.utils.QuestionDbUtil;
import com.jscheng.mr_horse.utils.SharedPreferencesUtil;
import com.jscheng.mr_horse.view.PracticeView;
import com.jscheng.mr_horse.view.MvpView;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cheng on 17-1-7.
 */
public class PracticePresenterImpl implements PracticePresenter {
    private List<QuestionModel> questionModelList;
    private PatternStatus patternStatus;
    private PracticeView mPracticeView;
    private Context mContext;
    private int pageNum;
    private int rightNum;
    private int wrongNum;
    private String filename;
    private String catogory;
    private String classification;

    private boolean loading;
    private Observable loadSubscription;
    private Observable storeSubscription;
    private Subscriber subscriber;

    public PracticePresenterImpl(Context context, Intent intent){
        this.mContext = context;
        this.patternStatus = PatternStatus.DATI_PATTERN;//默认是答题模式，测试使用背题模式
        this.questionModelList = new ArrayList();
        this.pageNum = 0;
        this.catogory = intent.getStringExtra(Constants.CATOGORY);

        if(!catogory.equals(Constants.COLLECT) && !(catogory.equals(Constants.WRONG))){
            this.filename = intent.getStringExtra(Constants.FILENAME);
            this.classification = "";
        }else {
            this.classification = intent.getStringExtra(Constants.CLASSIFICATION);
            this.filename = "";
        }
    }

    @Override
    public void attachView(MvpView view) {
        this.mPracticeView = (PracticeView) view;
        init();
    }

    @Override
    public void detachView(boolean retainInstance) {
        if(!retainInstance) {
            this.mPracticeView = null;
            this.mContext = null;
            if (subscriber!=null)
                subscriber.unsubscribe();
        }
    }

    public void init() {
        initPattern();
        if(catogory.equals(Constants.COLLECT) ) {
            mPracticeView.hideRemoveLayout();
            initStoreData();
        }else if(catogory.equals(Constants.WRONG)){
            mPracticeView.showRemoveLayout();
            initStoreData();
        }else {
            mPracticeView.hideRemoveLayout();
            initFileData();
        }
    }

    private void initStoreData() {
        wrongNum = 0;
        rightNum = 0;
        questionModelList = new ArrayList<>();
        loadSubscription = Observable.create(new Observable.OnSubscribe<List<QuestionModel>>() {
            @Override
            public void call(Subscriber<? super List<QuestionModel>> subscriber) {
                if (catogory.equals(Constants.COLLECT)) {
                    questionModelList = QuestionModelLoad.getCollectQuestionModel(mContext,classification);

                }else {
                    questionModelList = QuestionModelLoad.getWrongQuestionModel(mContext,classification);
                }
                subscriber.onNext(questionModelList);
            }
        });

        subscriber = new Subscriber<List<QuestionModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mPracticeView.showError(e.toString());
                Logger.e(e.toString()+"  "+catogory.toString()+"  "+classification);
            }

            @Override
            public void onNext(List<QuestionModel> modelList) {
                if(mPracticeView ==null)
                    return;

                mPracticeView.initPaperAdapter(questionModelList,patternStatus);

                mPracticeView.showPageNumView(pageNum+1,questionModelList.size());
                showCollect();
                loading = false;
            }
        };

        loadSubscription
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    private void initFileData() {
        wrongNum = 0;
        rightNum = 0;

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

                        QuestionModelLoad.saveQuestionModelToDB(mContext, questionModelList, new QuestionDbUtil.DbProgressListener() {
                            @Override
                            public void loadProgress(int progress) {
                                if(mPracticeView !=null)
                                    mPracticeView.showProcessing((int)(progress*100/questionModelList.size()));
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                subscriber.onCompleted();
            }
        });


        subscriber = new Subscriber<List<QuestionModel>>() {
            @Override
            public void onStart() {
                super.onStart();
                loading = true;
                if (isStore == false) {
                    mPracticeView.beginProcessing();
                    mPracticeView.showInfo("首次加载会消耗较长时间");
                }
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e.toString());
                mPracticeView.showError(e.toString());
                mPracticeView.failProcessing();
            }

            @Override
            public void onNext(List<QuestionModel> ModelList) {
                if(mPracticeView ==null)
                    return;
                questionModelList.addAll(ModelList);
                mPracticeView.initPaperAdapter(questionModelList,patternStatus);
                pageNum = QuestionModelLoad.getQuestionDoneNum(mContext,catogory);
                if(pageNum>0)
                    mPracticeView.changePaperView(pageNum,false,0);
                mPracticeView.showPageNumView(pageNum+1,questionModelList.size());

                showCollect();

                if(isStore==false)
                    SharedPreferencesUtil.setParam(mContext,catogory,true);

                mPracticeView.sucessProcessing();
                loading = false;
            }
        };

        Observable.concat(storeSubscription,loadSubscription)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    private void initPattern() {
        if(mPracticeView ==null)
            return;
        if(patternStatus ==PatternStatus.DATI_PATTERN )
            mPracticeView.changeToDatiView();
        else
            mPracticeView.changeToBeitiView();
    }

    @Override
    public void onPageSelected(int position) {
        this.pageNum = position;
        if(!catogory.equals(Constants.COLLECT) && !(catogory.equals(Constants.WRONG))) {
            QuestionModelLoad.saveQuestionDoneNum(mContext, catogory, pageNum);
        }
        mPracticeView.showPageNumView(pageNum+1,questionModelList.size());
        showCollect();
    }

    @Override
    public void onAnswerRight(int postion) {

        questionModelList.get(pageNum).setNewDone(QuestionDoneType.DONE_RIGHT);
        if(!catogory.equals(Constants.COLLECT) && !(catogory.equals(Constants.WRONG))) {
            questionModelList.get(pageNum).setDone(QuestionDoneType.DONE_RIGHT);
            QuestionModelLoad.setQuestionModelDone(mContext,questionModelList.get(postion));
        }

        int haveDoneNum = (int)SharedPreferencesUtil.getParam(mContext,Constants.HAVE_DONE_NUM,0);
        SharedPreferencesUtil.setParam(mContext,Constants.HAVE_DONE_NUM,++haveDoneNum);

        this.pageNum = postion;
        if(questionModelList!=null &&!questionModelList.isEmpty()){
            int pageCount = questionModelList.size();
            if((pageNum+1)<pageCount){
                pageNum++;
                mPracticeView.changePaperView(pageNum,true,300);
            }
        }

        mPracticeView.showRightNumView(++rightNum);
        showCollect();
    }

    @Override
    public void onAnswerWrong(int postion) {
        mPracticeView.showWrongNumView(++wrongNum);
        int haveDoneNum = (int)SharedPreferencesUtil.getParam(mContext,Constants.HAVE_DONE_NUM,0);
        SharedPreferencesUtil.setParam(mContext,Constants.HAVE_DONE_NUM,++haveDoneNum);

        questionModelList.get(postion).setNewDone(QuestionDoneType.DONE_WRONG);
        if(!catogory.equals(Constants.COLLECT) && !(catogory.equals(Constants.WRONG))) {
            questionModelList.get(postion).setDone(QuestionDoneType.DONE_WRONG);
            QuestionModelLoad.setQuestionModelDone(mContext, questionModelList.get(postion));
            //加入错题集
            QuestionModelLoad.setQuestionModelToWrong(mContext, questionModelList.get(postion));
        }
    }

    public void onClickDatiPattern() {
        if(patternStatus==PatternStatus.DATI_PATTERN || loading)
            return;
        patternStatus=PatternStatus.DATI_PATTERN;
        mPracticeView.changeToDatiView();
        mPracticeView.changeAdapterPattern(patternStatus);
    }

    public void onClickBeitiPattern() {
        if(patternStatus==PatternStatus.BETI_PATTERN || loading)
            return;
        patternStatus=PatternStatus.BETI_PATTERN;
        mPracticeView.changeToBeitiView();
        mPracticeView.changeAdapterPattern(patternStatus);
    }

    @Override
    public void changeTheme() {
        if(subscriber!=null)
            subscriber.unsubscribe();

        int dayNightTheme = App.getDayNightTheme();
        if (dayNightTheme == R.style.SunAppTheme) {
            App.setDayNightTheme(R.style.NightAppTheme);
            mPracticeView.changeToNightTheme();
        } else {
            App.setDayNightTheme(R.style.SunAppTheme);
            mPracticeView.changeToSunTheme();
        }

        SharedPreferencesUtil.setParam(mContext,Constants.THEME,App.getDayNightTheme());
    }

    @Override
    public void onClickQuestionsLayout() {
        if(loading==false && questionModelList!=null && !questionModelList.isEmpty())
            mPracticeView.showQuestionDailog(questionModelList,pageNum);
    }

    @Override
    public void onClickCollect() {
        if(loading || questionModelList.isEmpty())
            return;
        QuestionModel model = questionModelList.get(pageNum);

        if(model.isCollected()){
            if(catogory.equals(Constants.COLLECT)) {//收藏模式下，会移除错题
                removeCollect(model);
            }
            else {//正常模式下，只是设置为非收藏
                model.setCollected(false);
                QuestionModelLoad.setQuestionModelOutCollect(mContext, model);
                showCollect();
            }
        }else {
            model.setCollected(true);
            QuestionModelLoad.setQuestionModelToCollect(mContext,model);
            showCollect();
        }
    }

    /**
     * 错题移除（错题模式下使用）
     */
    private void removeWrong(QuestionModel model){
        model.setWrong(false);
        QuestionModelLoad.setQuestionModelOutWrong(mContext,model);

        questionModelList.remove(model);
        if(questionModelList.isEmpty()){
            finish();
            return;
        }
        if(pageNum>=questionModelList.size()){
            pageNum = questionModelList.size()-1;
        }
        mPracticeView.changePaperView(pageNum,false,0);
        mPracticeView.showPageNumView(pageNum+1,questionModelList.size());
        showCollect();
    }

    /**
     * 收藏题移除(收藏模式下使用)
     */
    private void removeCollect(QuestionModel model){
        model.setCollected(false);
        QuestionModelLoad.setQuestionModelOutCollect(mContext,model);

        questionModelList.remove(model);
        if(questionModelList.isEmpty()){
            finish();
            return;
        }
        if(pageNum>=questionModelList.size()){
            pageNum = questionModelList.size()-1;
        }
        mPracticeView.changePaperView(pageNum,false,0);
        mPracticeView.showPageNumView(pageNum+1,questionModelList.size());
        showCollect();
    }

    @Override
    public void onClickRubbishLayout() {
        QuestionModel model = questionModelList.get(pageNum);
        removeWrong(model);
    }

    /**
     * 显示收藏
     */
    private void showCollect(){
        if(mPracticeView!=null)
            mPracticeView.showCollectView(questionModelList.get(pageNum).isCollected());
    }

    /**
     * 选择换页回调操作，请不要直接调用
     * @param position
     */
    @Override
    public void onItemSelected(int position) {
        pageNum = position;
        mPracticeView.changePaperView(pageNum,false,0);
        showCollect();
    }

    private void finish(){
        mPracticeView.finishActivity();
    }
}
