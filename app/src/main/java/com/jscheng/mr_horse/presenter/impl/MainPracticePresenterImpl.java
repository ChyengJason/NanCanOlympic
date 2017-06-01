package com.jscheng.mr_horse.presenter.impl;

import android.content.Context;
import android.content.Intent;

import com.jscheng.mr_horse.App;
import com.jscheng.mr_horse.R;
import com.jscheng.mr_horse.model.PatternStatus;
import com.jscheng.mr_horse.model.QuestionDoneType;
import com.jscheng.mr_horse.model.QuestionJsonModel;
import com.jscheng.mr_horse.model.QuestionModel;
import com.jscheng.mr_horse.model.QuestionModelLoadUtil;
import com.jscheng.mr_horse.presenter.PracticePresenter;
import com.jscheng.mr_horse.ui.PracticeActivity;
import com.jscheng.mr_horse.utils.Constants;
import com.jscheng.mr_horse.utils.QuestionDbUtil;
import com.jscheng.mr_horse.utils.SharedPreferencesUtil;
import com.jscheng.mr_horse.view.MvpView;
import com.jscheng.mr_horse.view.PracticeView;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cheng on 2017/4/19.
 */
public class MainPracticePresenterImpl implements PracticePresenter {
    private List<QuestionModel> questionModelList;
    private int patternStatus;
    private Context mContext;
    private PracticeView mPracticeView;

    private int pageNum;
    private int rightNum;
    private int wrongNum;
    private String filename;
    private String catogory;
    private boolean loading;
    private Observable loadSubscription;
    private Observable storeSubscription;
    private Subscriber subscriber;

    public MainPracticePresenterImpl(Context context, Intent intent){
        this.mContext = context;
        this.patternStatus = intent.getIntExtra(Constants.PATTERN_STATUS,PatternStatus.DATI_PATTERN);//默认是答题模式
        this.questionModelList = new ArrayList();
        this.catogory = intent.getStringExtra(Constants.CATOGORY);
        this.filename = intent.getStringExtra(Constants.FILENAME);
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
        changePattern();
        mPracticeView.hideRemoveLayout();
        initFileData();
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
        QuestionModelLoadUtil.saveQuestionDoneId(mContext, catogory, pageNum);
        mPracticeView.showPageNumView(pageNum+1,questionModelList.size());
        showCollect();
    }

    @Override
    public void onAnswerRight(int postion) {
        questionModelList.get(pageNum).setNewDone(QuestionDoneType.DONE_RIGHT);
        questionModelList.get(pageNum).setDone(QuestionDoneType.DONE_RIGHT);
        QuestionModelLoadUtil.setQuestionModelDone(mContext,questionModelList.get(postion));
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
        questionModelList.get(postion).setDone(QuestionDoneType.DONE_WRONG);
        QuestionModelLoadUtil.setQuestionModelDone(mContext, questionModelList.get(postion));
        //加入错题集
        if (!questionModelList.get(postion).isWrong())
            QuestionModelLoadUtil.setQuestionModelToWrong(mContext, questionModelList.get(postion));
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
        intent.putExtra(Constants.FILENAME,this.filename);
        intent.putExtra(Constants.CATOGORY,this.catogory);
        intent.putExtra(Constants.PRACTICE_TYPE,Constants.MAIN_PRACTICE);
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
            model.setCollected(false);
            QuestionModelLoadUtil.setQuestionModelOutCollect(mContext, model);
            showCollect();
        }else {
            model.setCollected(true);
            QuestionModelLoadUtil.setQuestionModelToCollect(mContext,model);
            showCollect();
        }
    }

    /**
     * 显示收藏
     */
    private void showCollect(){
        if(mPracticeView!=null)
            mPracticeView.showCollectView(questionModelList.get(pageNum).isCollected());
    }

    @Override
    public void onClickRubbishLayout() {
    }

    /**
     * 选择换页回调操作，请不要直接调用
     * @param position
     */
    @Override
    public void onItemSelected(int position) {
        if(mPracticeView==null)
            return;
        pageNum = position;
        mPracticeView.changePaperView(pageNum,false,0);
        showCollect();
    }

    private void initFileData() {
        wrongNum = 0;
        rightNum = 0;

        final boolean isStore = (Boolean) SharedPreferencesUtil.getParam(mContext,catogory,false);

        loadSubscription = Observable.create(new Observable.OnSubscribe<List<QuestionModel>>() {
            @Override
            public void call(Subscriber<? super List<QuestionModel>> subscriber) {
                List<QuestionModel> modelList = QuestionModelLoadUtil.getQuestionModelsfromDB(mContext,catogory);
                Logger.e("已从数据库获取到数据");
                subscriber.onNext(modelList);
            }
        });

        storeSubscription = Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                if(isStore==false){
                    final ArrayList<QuestionModel> tempQuestionList = new ArrayList<QuestionModel>();
                    try {
                        List<QuestionJsonModel> questionJsonModelList = QuestionModelLoadUtil.getQuestionJsonModels(mContext,filename);
                        Logger.e("已加载完文件");
                        for(QuestionJsonModel model : questionJsonModelList){
                            tempQuestionList.add(model.toQuestionModel(catogory));
                        }

                        QuestionModelLoadUtil.saveQuestionModelToDB(mContext, tempQuestionList, new QuestionDbUtil.DbProgressListener() {
                            @Override
                            public void loadProgress(int progress) {
                                if(mPracticeView !=null)
                                    mPracticeView.showProcessing((int)(progress*100/tempQuestionList.size()));
                            }
                        });

                        SharedPreferencesUtil.setParam(mContext,catogory,true);
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
                pageNum = QuestionModelLoadUtil.getQuestionDoneId(mContext,catogory);
                if(pageNum>0) mPracticeView.changePaperView(pageNum,false,0);
                mPracticeView.showPageNumView(pageNum+1,questionModelList.size());
                showCollect();
                mPracticeView.sucessProcessing();
                loading = false;
            }
        };

        Observable.concat(storeSubscription,loadSubscription)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
