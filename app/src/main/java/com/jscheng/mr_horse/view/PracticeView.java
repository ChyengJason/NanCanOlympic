package com.jscheng.mr_horse.view;

import android.content.Intent;

import com.jscheng.mr_horse.model.QuestionModel;
import com.jscheng.mr_horse.model.PatternStatus;

import java.util.List;

/**
 * Created by cheng on 17-1-7.
 */
public interface PracticeView extends MvpView {
    void initPaperAdapter(List<QuestionModel> questionModelList, int status);
    void changeToBeitiView();
    void changeToDatiView();
    void showError(String s);
    void showInfo(String s);
    void changeAdapterPattern(int status);
    void changePaperView(int pageNum,boolean smooth,int delaytime);
    void showRightNumView(int rightNum);
    void showWrongNumView(int wrongNum);
    void showPageNumView(int pageNum,int totalNum);
    void changeToNightTheme(Intent intent);
    void changeToSunTheme(Intent intent);
    void showQuestionDailog(List<QuestionModel> modelList,int currentPosition);
    void showCollectView(boolean isShow);
    void beginProcessing();
    void showProcessing(int progress);
    void sucessProcessing();
    void failProcessing();
    void hideRemoveLayout();
    void showRemoveLayout();
    void finishActivity();
}
