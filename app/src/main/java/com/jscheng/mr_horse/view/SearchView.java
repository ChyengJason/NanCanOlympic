package com.jscheng.mr_horse.view;

import com.jscheng.mr_horse.model.QuestionModel;

import java.util.List;

/**
 * Created by cheng on 2017/3/21.
 */
public interface SearchView extends MvpView {
    void showProcessing(int i);
    void beginProcess();
    void showError(String s);
    void failProcessing();
    void sucessProcessing();
    void showInfo(String info);
    void showSearchResult(String searchWord,List<QuestionModel> results);
    void showLastPageView();
    void showMorePageView();
    void addSearchResults(List<QuestionModel> results);
    void clearSearchResult();
}
