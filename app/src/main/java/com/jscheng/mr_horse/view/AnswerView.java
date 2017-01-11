package com.jscheng.mr_horse.view;

import com.jscheng.mr_horse.model.QuestionModel;
import com.jscheng.mr_horse.model.PatternStatus;

import java.util.List;

/**
 * Created by cheng on 17-1-7.
 */
public interface AnswerView extends MvpView {
    void initPaperAdapter(List<QuestionModel> questionModelList, PatternStatus status);
    void changeToBeitiView();
    void changeToDatiView();
    void showError(String s);
    void showInfo(String s);
    void changeAdapterPattern(PatternStatus status);
    void changePaperView(int pageNum,boolean smooth,int delaytime);
    void showRightNumView(int rightNum);
    void showWrongNumView(int wrongNum);
    void showPageNumView(int pageNum,int totalNum);
    void changeToNightTheme();
    void changeToSunTheme();
    void showQuestionDailog(List<QuestionModel> modelList,int currentPosition);
    void showCollectView(boolean isShow);
    void beginProcessing();
    void showProcessing(int progress);
    void sucessProcessing();
    void failProcessing();
}
