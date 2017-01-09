package com.jscheng.mr_horse.ui;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.jscheng.mr_horse.R;
import com.jscheng.mr_horse.adapter.AnswerViewPaperAdapter;
import com.jscheng.mr_horse.model.QuestionModel;
import com.jscheng.mr_horse.model.PatternStatus;
import com.jscheng.mr_horse.presenter.AnswerPresenter;
import com.jscheng.mr_horse.presenter.impl.AnswerPresenterImpl;
import com.jscheng.mr_horse.view.AnswerView;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PracticeActivity extends BaseActivity implements AnswerView {

    @BindView(R.id.viewpaper)
    ViewPager answerViewPager;
    @BindView(R.id.dati_pattern_view)
    Button datiPatternView;
    @BindView(R.id.beiti_pattern_view)
    Button beitiPatternView;
    @BindView(R.id.progress_wheel)
    ProgressWheel progressWheel;
    @BindView(R.id.practice_wrong_num_text)
    TextView wrongNumTextView;
    @BindView(R.id.practice_right_num_text)
    TextView rightNUmTextView;
    @BindView(R.id.practice_page_num)
    TextView PageNumTextView;
    @BindView(R.id.practise_title_back)
    ImageButton titleBack;
    @BindView(R.id.sun_night)
    ImageButton sunNight;

    private Handler changeViewHandler;
    private AnswerPresenter answerPresenter;
    private AnswerViewPaperAdapter answerViewPaperAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        ButterKnife.bind(this);
        answerPresenter = new AnswerPresenterImpl(this,getIntent());
        answerPresenter.attachView(this);
        changeViewHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                answerViewPager.setCurrentItem(msg.what,true);
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(answerPresenter !=null) {
            answerPresenter.detachView(false);
            if(answerViewPaperAdapter!=null){
                answerViewPaperAdapter.removeAnswerPageListener(answerPresenter);
            }
        }
    }

    @Override
    public void initPaperAdapter(List<QuestionModel> questionModelList, PatternStatus status) {
        answerViewPaperAdapter = new AnswerViewPaperAdapter(this, questionModelList,status);
        answerViewPager.setAdapter(answerViewPaperAdapter);
        answerViewPager.addOnPageChangeListener(answerViewPaperAdapter.new AnswerViewPaperListener());
        answerViewPaperAdapter.addAnswerPageListener(answerPresenter);
    }

    @Override
    public void showProcessing() {
        progressWheel.setVisibility(View.VISIBLE);
        ValueAnimator progressFadeInAnim = ObjectAnimator.ofFloat(progressWheel, "alpha", 0, 1, 1);
        progressFadeInAnim.start();
    }

    @Override
    public void hideProcessing() {
        progressWheel.setVisibility(View.GONE);
        ValueAnimator progressFadeInAnim = ObjectAnimator.ofFloat(progressWheel, "alpha", 1, 0, 0);
        progressFadeInAnim.start();
    }

    @Override
    public void changeToBeitiView() {
        datiPatternView.setSelected(false);
        beitiPatternView.setSelected(true);
    }

    @Override
    public void changeToDatiView() {
        datiPatternView.setSelected(true);
        beitiPatternView.setSelected(false);
    }

    @Override
    public void showError(String s) {

    }

    @OnClick(R.id.dati_pattern_view)
    public void onClickDatiPatternView(){
        answerPresenter.onClickDatiPattern();
    }

    @OnClick(R.id.beiti_pattern_view)
    public void onClickBeitiPatternView(){
        answerPresenter.onClickBeitiPattern();
    }

    @Override
    public void changeAdapterPattern(PatternStatus status){
        if(answerViewPaperAdapter!=null)
            answerViewPaperAdapter.changePatternStatus(status);
    }

    @Override
    public void changePaperView(int pageNum,boolean smooth,int delaytime) {
        if (delaytime > 0) {
            changeViewHandler.sendEmptyMessageDelayed(pageNum, delaytime);
        } else {
            answerViewPager.setCurrentItem(pageNum, smooth);
        }
    }

    @Override
    public void showRightNumView(int rightNum) {
        rightNUmTextView.setText(rightNum+"");
    }

    @Override
    public void showWrongNumView(int wrongNum) {
        wrongNumTextView.setText(wrongNum+"");
    }

    @Override
    public void showPageNumView(int pageNum,int totalNum){
        PageNumTextView.setText(pageNum+"/"+totalNum);
    }

    @Override
    public void changeToNightTheme() {
        recreate();
    }

    public void changeToSunTheme(){
        recreate();
    }

    @OnClick(R.id.sun_night)
    public void onClickSunNight() {
        answerPresenter.changeTheme();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @OnClick(R.id.practise_title_back)
    public void TitleBack(){
        super.finish();
    }


}
