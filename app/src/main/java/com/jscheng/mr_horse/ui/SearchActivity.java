package com.jscheng.mr_horse.ui;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.jscheng.mr_horse.R;
import com.jscheng.mr_horse.presenter.SearchPresenter;
import com.jscheng.mr_horse.presenter.impl.SearchPresenterImpl;
import com.jscheng.mr_horse.view.MvpView;
import com.jscheng.mr_horse.view.SearchView;
import com.pnikosis.materialishprogress.ProgressWheel;

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

    private SearchPresenter presenter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        presenter = new SearchPresenterImpl(this);
        presenter.attachView(this);
        initSearchEditText();
    }

    private void initSearchEditText() {
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
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
}
