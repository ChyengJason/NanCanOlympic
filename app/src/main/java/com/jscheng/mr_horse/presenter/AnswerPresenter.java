package com.jscheng.mr_horse.presenter;

import com.jscheng.mr_horse.adapter.AnswerViewPaperAdapter;

/**
 * Created by cheng on 17-1-8.
 */
public interface AnswerPresenter extends MvpPresenter, AnswerViewPaperAdapter.AnswerPageChangeListener {
    void onClickDatiPattern();
    void onClickBeitiPattern();
}
