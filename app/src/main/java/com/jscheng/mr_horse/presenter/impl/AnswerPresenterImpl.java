package com.jscheng.mr_horse.presenter.impl;

import android.content.Context;

import com.jscheng.mr_horse.utils.Constants;
import com.jscheng.mr_horse.model.QuestionJsonModel;
import com.jscheng.mr_horse.model.QuestionModel;
import com.jscheng.mr_horse.model.PatternStatus;
import com.jscheng.mr_horse.presenter.AnswerPresenter;
import com.jscheng.mr_horse.utils.JsonUtil;
import com.jscheng.mr_horse.view.AnswerView;
import com.jscheng.mr_horse.view.MvpView;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by cheng on 17-1-7.
 */
public class AnswerPresenterImpl implements AnswerPresenter{

    private PatternStatus patternStatus;
    private AnswerView mAnswerView;
    private Context mContext;

    public AnswerPresenterImpl(Context context){
        this.mContext = context;
        this.patternStatus = PatternStatus.DATI_PATTERN;//默认是答题模式，测试使用背题模式
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
        Observable.create(new Observable.OnSubscribe<List<QuestionJsonModel>>() {
            @Override
            public void call(Subscriber<? super List<QuestionJsonModel>> subscriber) {
                subscriber.onStart();
                try {
                    String filename = Constants.FLJC_JSON_NAME;
                    InputStream inputstream = mContext.getClass().getClassLoader().getResourceAsStream("assets/"+filename);
                    if(inputstream!=null) {
                        String jsonString = JsonUtil.inputStreamToStr(inputstream);
                        ArrayList<QuestionJsonModel> jsonModelList = JsonUtil.jsonToArrayList(QuestionJsonModel.class,jsonString);

                        subscriber.onNext(jsonModelList);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
                subscriber.onCompleted();
            }
        }).map(new Func1<List<QuestionJsonModel>, List<QuestionModel>>() {
            @Override
            public List<QuestionModel> call(List<QuestionJsonModel> questionJsonModel) {
                List<QuestionModel> list = new ArrayList();

                for(QuestionJsonModel model : questionJsonModel){
                    list.add(model.toQuestionModel());
                }
                return list;
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Subscriber<List<QuestionModel>>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        mAnswerView.showProcessing();
                    }

                    @Override
                    public void onCompleted() {
                        mAnswerView.hideProcessing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.toString());
                        mAnswerView.showError(e.toString());
                    }

                    @Override
                    public void onNext(List<QuestionModel> questionModelList) {
                        mAnswerView.initPaperAdapter(questionModelList,patternStatus);
                    }
                });
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
    public void onPageSelected(int position, QuestionModel model) {

    }

    public void onClickDatiPattern() {
        if(patternStatus==PatternStatus.DATI_PATTERN)
            return;
        patternStatus=PatternStatus.DATI_PATTERN;
        mAnswerView.changeToDatiView();
    }

    public void onClickBeitiPattern() {
        if(patternStatus==PatternStatus.BETI_PATTERN)
            return;
        patternStatus=PatternStatus.BETI_PATTERN;
        mAnswerView.changeToBeitiView();
    }
}
