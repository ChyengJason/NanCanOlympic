package com.jscheng.mr_horse.presenter;

import com.jscheng.mr_horse.model.QuestionModel;

/**
 * Created by cheng on 2017/3/21.
 */
public interface SearchPresenter extends MvpPresenter {
    void onClickSearch(String searchText);
    void onClickMoreLayout();
    void onClickItem(int postion, QuestionModel model);
}
