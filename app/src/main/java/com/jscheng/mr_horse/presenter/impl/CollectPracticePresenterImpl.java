package com.jscheng.mr_horse.presenter.impl;

import android.content.Context;
import android.content.Intent;
import com.jscheng.mr_horse.App;
import com.jscheng.mr_horse.R;
import com.jscheng.mr_horse.model.PatternStatus;
import com.jscheng.mr_horse.model.QuestionDoneType;
import com.jscheng.mr_horse.model.QuestionModel;
import com.jscheng.mr_horse.model.QuestionModelLoadUtil;
import com.jscheng.mr_horse.presenter.PracticePresenter;
import com.jscheng.mr_horse.ui.PracticeActivity;
import com.jscheng.mr_horse.utils.Constants;
import com.jscheng.mr_horse.utils.SharedPreferencesUtil;
import com.jscheng.mr_horse.view.MvpView;
import com.jscheng.mr_horse.view.PracticeView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cheng on 2017/4/19.
 */
public class CollectPracticePresenterImpl implements PracticePresenter {
    private List<QuestionModel> questionModelList;
    private int patternStatus;
    private PracticeView mPracticeView;
    private Context mContext;
    private int pageNum;
    private int rightNum;
    private int wrongNum;
    private boolean loading;
    private String catogory;
    private Observable loadSubscription;
    private Subscriber subscriber;

    public CollectPracticePresenterImpl(Context context, Intent intent) {
        this.mContext = context;
        this.patternStatus = intent.getIntExtra(Constants.PATTERN_STATUS,PatternStatus.DATI_PATTERN);//默认是答题模式
        this.questionModelList = new ArrayList();
        this.catogory = intent.getStringExtra(Constants.CATOGORY);
        this.pageNum = intent.getIntExtra(Constants.PAGE_NUM,-1);
    }

    @Override
    public void attachView(MvpView view) {
        this.mPracticeView = (PracticeView) view;
        init();
    }

    private void init() {
        changePattern();
        mPracticeView.hideRemoveLayout();
        initStoreData();
    }

    private void initStoreData() {
        wrongNum = 0;
        rightNum = 0;
        loadSubscription = Observable.create(new Observable.OnSubscribe<List<QuestionModel>>() {
            List<QuestionModel> temQuestionList = new ArrayList<>();
            @Override
            public void call(Subscriber<? super List<QuestionModel>> subscriber) {
                temQuestionList = QuestionModelLoadUtil.getCollectQuestionModel(mContext,catogory);
                subscriber.onNext(temQuestionList);
            }
        });

        subscriber = new Subscriber<List<QuestionModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mPracticeView.showError(e.toString());
            }

            @Override
            public void onNext(List<QuestionModel> modelList) {
                if(mPracticeView ==null)
                    return;
                questionModelList.addAll(modelList);
                mPracticeView.initPaperAdapter(questionModelList,patternStatus);
                getPageNumFromDb();
                if(pageNum>0)
                    mPracticeView.changePaperView(pageNum,false,0);
                mPracticeView.showPageNumView(pageNum+1,questionModelList.size());
                showCollect();
                mPracticeView.sucessProcessing();
                loading = false;
            }
        };

        loadSubscription
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
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

    private void changePattern() {
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
        mPracticeView.showPageNumView(pageNum+1,questionModelList.size());
        QuestionModelLoadUtil.saveQuetionCollectDoneId(mContext, catogory, questionModelList.get(position).getQuestionNum());
        showCollect();
    }

    @Override
    public void onAnswerRight(int postion) {
        questionModelList.get(pageNum).setNewDone(QuestionDoneType.DONE_RIGHT);
        int haveDoneNum = (int) SharedPreferencesUtil.getParam(mContext,Constants.HAVE_DONE_NUM,0);
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
    }

    @Override
    public void onClickDatiPattern() {
        if(patternStatus==PatternStatus.DATI_PATTERN || loading)
            return;
        patternStatus=PatternStatus.DATI_PATTERN;
        mPracticeView.changeToDatiView();
        mPracticeView.changeAdapterPattern(patternStatus);
    }

    @Override
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
        Intent intent = new Intent(mContext, PracticeActivity.class);
        intent.putExtra(Constants.PAGE_NUM,this.pageNum);
        intent.putExtra(Constants.CATOGORY,this.catogory);
        intent.putExtra(Constants.PRACTICE_TYPE,Constants.COLLECT_PRACTICE);
        intent.putExtra(Constants.PATTERN_STATUS,patternStatus);

        int dayNightTheme = App.getDayNightTheme();
        if (dayNightTheme == R.style.SunAppTheme) {
            App.setDayNightTheme(R.style.NightAppTheme);
            mPracticeView.changeToNightTheme(intent);
        } else {
            App.setDayNightTheme(R.style.SunAppTheme);
            mPracticeView.changeToSunTheme(intent);
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
            removeCollect(model);
        }else {
            model.setCollected(true);
            QuestionModelLoadUtil.setQuestionModelToCollect(mContext,model);
            showCollect();
        }
    }

    /**
     * 收藏题移除(收藏模式下使用)
     */
    private void removeCollect(QuestionModel model){
        model.setCollected(false);
        QuestionModelLoadUtil.setQuestionModelOutCollect(mContext,model);

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
        QuestionModelLoadUtil.saveQuetionCollectDoneId(mContext, catogory, questionModelList.get(pageNum).getQuestionNum());
        showCollect();
    }

    @Override
    public void onClickRubbishLayout() {

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
        if(mPracticeView==null)return;
        pageNum = position;
        mPracticeView.changePaperView(pageNum,false,0);
        showCollect();
    }

    private void finish(){
        mPracticeView.finishActivity();
    }

    private void getPageNumFromDb() {
        if (questionModelList == null || questionModelList.isEmpty())
            throw new RuntimeException("you need to init questionModelList");
        if (this.pageNum >= 0)
            return;
        int questionId = QuestionModelLoadUtil.getQuestionCollectDoneId(mContext,catogory);
        for (int i=0; i<questionModelList.size(); i++){
            if (questionModelList.get(i).getQuestionNum() == questionId){
                this.pageNum = i;
                break;
            }
        }
        if (pageNum < 0) pageNum = 0;
    }
}
