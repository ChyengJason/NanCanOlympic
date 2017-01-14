package com.jscheng.mr_horse.presenter;

import com.jscheng.mr_horse.adapter.AnswerViewPaperAdapter;
import com.jscheng.mr_horse.wiget.QuestionDailog;

/**
 * Created by cheng on 17-1-8.
 */
public interface PracticePresenter extends MvpPresenter, AnswerViewPaperAdapter.AnswerPageChangeListener,QuestionDailog.QuestionDailogListener {
    void onClickDatiPattern();
    void onClickBeitiPattern();
    void changeTheme();
    void onClickQuestionsLayout();
    void onClickCollect();
    void onClickRubbishLayout();
}
