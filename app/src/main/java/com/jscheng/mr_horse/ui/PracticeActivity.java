package com.jscheng.mr_horse.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.jscheng.mr_horse.R;
import com.jscheng.mr_horse.adapter.AnswerViewPaperAdapter;
import com.jscheng.mr_horse.model.QuestionModel;
import com.jscheng.mr_horse.model.PatternStatus;
import com.jscheng.mr_horse.presenter.PracticePresenter;
import com.jscheng.mr_horse.presenter.impl.PracticePresenterImpl;
import com.jscheng.mr_horse.utils.AppEvent;
import com.jscheng.mr_horse.utils.AppEventAgent;
import com.jscheng.mr_horse.view.PracticeView;
import com.jscheng.mr_horse.wiget.QuestionDailog;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PracticeActivity extends BaseActivity implements PracticeView {

    @BindView(R.id.viewpaper)
    ViewPager answerViewPager;
    @BindView(R.id.dati_pattern_view)
    Button datiPatternView;
    @BindView(R.id.beiti_pattern_view)
    Button beitiPatternView;
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
    @BindView(R.id.collect_iv)
    ImageView collectIamgeView;
    @BindView(R.id.circle_loading_view)
    AnimatedCircleLoadingView loadingView;
    @BindView(R.id.rubbish_layout)
    RelativeLayout rubbish_layout;

    private PracticeHandler changeViewHandler;
    private PracticeHandler progressViewHandler;
    private PracticePresenter practicePresenter;
    private AnswerViewPaperAdapter answerViewPaperAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        ButterKnife.bind(this);
        practicePresenter = new PracticePresenterImpl(this,getIntent());
        practicePresenter.attachView(this);
        changeViewHandler = new PracticeHandler(this, new PracticeHandler.PracticeCallback() {
            @Override
            public void handleMessge(Message msg) {
                if(answerViewPager!=null)
                    answerViewPager.setCurrentItem(msg.what,true);
            }
        });
        progressViewHandler = new PracticeHandler(this, new PracticeHandler.PracticeCallback() {
            @Override
            public void handleMessge(Message msg) {
                loadingView.setPercent(msg.what);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(practicePresenter !=null) {
            practicePresenter.detachView(false);
            if(answerViewPaperAdapter!=null){
                answerViewPaperAdapter.removeAnswerPageListener(practicePresenter);
            }
        }
    }

    @Override
    public void initPaperAdapter(List<QuestionModel> questionModelList, PatternStatus status) {
        answerViewPaperAdapter = new AnswerViewPaperAdapter(this, questionModelList,status);
        answerViewPager.setAdapter(answerViewPaperAdapter);
        answerViewPager.addOnPageChangeListener(answerViewPaperAdapter.new AnswerViewPaperListener());
        answerViewPaperAdapter.addAnswerPageListener(practicePresenter);
    }

    @Override
    public void beginProcessing() {
        loadingView.setVisibility(View.VISIBLE);
        loadingView.startDeterminate();
    }

    @Override
    public void showProcessing(int progress){
        progressViewHandler.sendEmptyMessage(progress);
    }

    @Override
    public void sucessProcessing() {
        loadingView.stopOk();
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void failProcessing(){
        loadingView.stopFailure();
        loadingView.setVisibility(View.GONE);
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

    @Override
    public void showInfo(String s) {
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.dati_pattern_view)
    public void onClickDatiPatternView(){
        practicePresenter.onClickDatiPattern();
        AppEventAgent.onEvent(this,AppEvent.PRACTICE_DATI_PATTERN);
    }

    @OnClick(R.id.beiti_pattern_view)
    public void onClickBeitiPatternView(){
        practicePresenter.onClickBeitiPattern();
        AppEventAgent.onEvent(this,AppEvent.PRACTICE_BEITI_PATTERN);
    }

    @Override
    public void changeAdapterPattern(PatternStatus status){
        if(answerViewPaperAdapter!=null)
            answerViewPaperAdapter.changePatternStatus(status);
    }

    @Override
    public void changePaperView(int pageNum,boolean smooth,int delaytime) {
        answerViewPaperAdapter.notifyDataSetChanged();
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
        practicePresenter.changeTheme();
        AppEventAgent.onEvent(this,AppEvent.PRACTICE_THEME);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @OnClick(R.id.practise_title_back)
    public void TitleBack(){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        practicePresenter.detachView(false);

        finish();
    }

    @OnClick(R.id.questions_layout)
    public void onClickQuestionsLayout(){
        practicePresenter.onClickQuestionsLayout();
        AppEventAgent.onEvent(this,AppEvent.PRACTICE_QUESTION_LAYOUT);
    }

    public void showQuestionDailog(List<QuestionModel> modelList,int currentPosition){
        QuestionDailog dialog = new QuestionDailog(this,modelList,R.style.questionDailog,currentPosition);
        dialog.addQuestionDailogItemListener(practicePresenter);
        Window window = dialog.getWindow();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        params.dimAmount =0.5f;
        window.setAttributes(params);
    }

    @OnClick(R.id.collect_layout)
    public void onClickCollect(){
        practicePresenter.onClickCollect();
        AppEventAgent.onEvent(this,AppEvent.PRACTICE_COLLECT);
    }

    @Override
    public void showCollectView(boolean isShow) {
        if(isShow)
            collectIamgeView.setImageResource(R.mipmap.collect_icon_yes);
        else
            collectIamgeView.setImageResource(R.mipmap.collect_icon_no);
    }

    public void hideRemoveLayout(){
        rubbish_layout.setVisibility(View.GONE);
    }

    public void showRemoveLayout(){
        rubbish_layout.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.rubbish_layout)
    public void onClickRubbish_layout(){
        practicePresenter.onClickRubbishLayout();
        AppEventAgent.onEvent(this,AppEvent.PRACTICE_RUBBISH);
    }

    private static class PracticeHandler extends Handler {
        private final WeakReference<PracticeActivity> mActivity;
        private final PracticeCallback callback;

        public PracticeHandler(PracticeActivity activity,PracticeCallback callback) {
            mActivity = new WeakReference<PracticeActivity>(activity);
            this.callback = callback;
        }

        @Override
        public void handleMessage(Message msg) {
            PracticeActivity activity = mActivity.get();
            if (activity != null && callback!=null) {
                callback.handleMessge(msg);
            }
        }

        interface PracticeCallback{
            void handleMessge(Message msg);
        }
    }

    public void finishActivity(){
        super.finish();
    }

    @Override
    public void finish() {
        practicePresenter.detachView(false);
        this.setResult(1,new Intent());
        super.finish();
    }

}
