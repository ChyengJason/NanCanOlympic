package com.jscheng.mr_horse.ui;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jscheng.mr_horse.R;
import com.jscheng.mr_horse.adapter.SearchRecyclerAdapter;
import com.jscheng.mr_horse.model.QuestionModel;
import com.jscheng.mr_horse.presenter.SearchPresenter;
import com.jscheng.mr_horse.presenter.impl.SearchPresenterImpl;
import com.jscheng.mr_horse.view.MvpView;
import com.jscheng.mr_horse.view.SearchView;
import com.jscheng.mr_horse.wiget.RecyclerViewDivider;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cheng on 2017/3/21.
 */
public class SearchActivity extends BaseActivity implements SearchView {

    @BindView(R.id.title_search_edit)
    EditText searchEditText;
    @BindView(R.id.progress_wheel)
    ProgressWheel progressWheel;
    @BindView(R.id.search_content_recycleview)
    RecyclerView recyclerView;
    @BindView(R.id.more_content_layout)
    FrameLayout moreLayout;
    @BindView(R.id.out_content_layout)
    FrameLayout outLayout;

    private SearchRecyclerAdapter adapter = null;
    private SearchPresenter presenter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        presenter = new SearchPresenterImpl(this);
        presenter.attachView(this);
        initSearchEditText();
        initRecycleView();
    }

    private void initRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SearchRecyclerAdapter();
        recyclerView.setAdapter(adapter);
        RecyclerViewDivider dividerItemDecoration = new RecyclerViewDivider(this, RecyclerViewDivider.VERTICAL_LIST);
        dividerItemDecoration.setDivider(R.drawable.item_divider);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void initSearchEditText() {
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0); //强制隐藏键盘
                    String searchText = searchEditText.getText().toString();
                    presenter.onClickSearch(searchText);
                }
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView(false);
    }

    @OnClick(R.id.title_back)
    public void onTitleBack(){
        finish();
    }

    @Override
    public void showProcessing(int i) {
        progressWheel.setProgress(i);
    }

    @Override
    public void beginProcess() {
        progressWheel.setVisibility(View.VISIBLE);
        ValueAnimator progressFadeInAnim = ObjectAnimator.ofFloat(progressWheel, "alpha", 0, 1, 1);
        progressFadeInAnim.start();
    }

    @Override
    public void showError(String s) {

    }

    @Override
    public void failProcessing() {
        ValueAnimator progressFadeInAnim = ObjectAnimator.ofFloat(progressWheel, "alpha", 1, 0, 0);
        progressFadeInAnim.start();
        progressWheel.setVisibility(View.GONE);
    }

    @Override
    public void sucessProcessing() {
        ValueAnimator progressFadeInAnim = ObjectAnimator.ofFloat(progressWheel, "alpha", 1, 0, 0);
        progressFadeInAnim.start();
        progressWheel.setVisibility(View.GONE);
    }

    @Override
    public void showInfo(String info) {
        Toast.makeText(this,info,Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSearchResult(String searchWord,List<QuestionModel> results) {
        adapter.setSearchContent(searchWord,results);
    }

    @Override
    public void showLastPageView() {
        moreLayout.setVisibility(View.GONE);
        outLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMorePageView() {
        moreLayout.setVisibility(View.VISIBLE);
        outLayout.setVisibility(View.GONE);
    }

    @Override
    public void addSearchResults(List<QuestionModel> results) {
        adapter.addSearchContent(results);
    }

    @Override
    public void clearSearchResult() {
        adapter.clear();
    }

    @OnClick(R.id.more_content_layout)
    public void clickMoreLayout(){
        presenter.onClickMoreLayout();
    }
}
