package com.jscheng.mr_horse.wiget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.jscheng.mr_horse.R;
import com.jscheng.mr_horse.adapter.DailogGirdviewAdapter;
import com.jscheng.mr_horse.model.QuestionModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cheng on 17-1-10.
 */
public class QuestionDailog extends Dialog implements View.OnClickListener {
    private final static int Animation_Time = 550;
    private Context mContext;
    private List<QuestionModel> modelList;
    private DailogGirdviewAdapter girdviewAdapter;
    private int currentPosition;
    private List<QuestionDailogListener> listenerList;

    @BindView(R.id.dailog_layout)
    LinearLayout dailogLayout;
    @BindView(R.id.dailog_gridview)
    GridView dailogGridview;

    public QuestionDailog(Context mContext, List<QuestionModel> modelList,int themeResId,int currentPosition){
        super(mContext,themeResId);
        this.mContext = mContext;
        this.modelList = modelList;
        this.currentPosition = currentPosition;
        this.listenerList = new ArrayList<>();
    }

    public void addQuestionDailogItemListener(QuestionDailogListener listener){
        if(listenerList!=null){
            listenerList.add(listener);
        }
    }

    public void removeQuestionDailogItemListener(QuestionDailogListener listener){
        if(listenerList!=null){
            listenerList.remove(listener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_dailog);
        ButterKnife.bind(this);
        girdviewAdapter = new DailogGirdviewAdapter(mContext,modelList,currentPosition);
        dailogGridview.setAdapter(girdviewAdapter);
        dailogGridview.setSelection(currentPosition);
        dailogGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentPosition==position)
                    return;
                if(listenerList!=null && !listenerList.isEmpty()){
                    for(QuestionDailogListener listener:listenerList){
                        listener.onItemSelected(position);
                    }
                }
                dismiss();
            }
        });
    }

    @Override
    public void show() {
        super.show();
        animationShow(Animation_Time);
    }

    @Override
    public void dismiss() {
        animationHide(Animation_Time);
    }

    @Override
    public void onClick(View v) {

    }

    private void animationShow(int mDuration) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(dailogLayout, "translationY",1200, 0).setDuration(mDuration)
        );
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();
    }

    private void animationHide(int mDuration) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(dailogLayout, "translationY",0,1200).setDuration(mDuration)
        );
        animatorSet.start();

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                QuestionDailog.super.dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    public interface QuestionDailogListener{
        void onItemSelected(int position);
    }
}
