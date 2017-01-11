package com.jscheng.mr_horse.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jscheng.mr_horse.R;
import com.jscheng.mr_horse.model.QuestionDoneType;
import com.jscheng.mr_horse.model.QuestionModel;
import com.jscheng.mr_horse.model.QuestionType;

/**
 * Created by cheng on 17-1-7.
 */
public class AnswerListViewAdapter extends BaseAdapter {

    private Context mContext;
    private QuestionModel mQuestionModel;
    private LayoutInflater mLayoutInflater;
    private boolean isShowAnswer;
    private boolean isShowUserAnswer;//是否显示用户答案

    public AnswerListViewAdapter(Context context, QuestionModel questionModel){
        this.mContext = context;
        this.mQuestionModel = questionModel;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.isShowAnswer = false;
        this.isShowUserAnswer = true;
    }

    @Override
    public int getCount() {
        return mQuestionModel.getOptionCount();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.option_listview_item, null);
            viewHolder.answerChoose = (ImageView) convertView.findViewById(R.id.answer_choose_iv);
            viewHolder.optionText = (TextView) convertView.findViewById(R.id.option_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (isShowAnswer) {//显示答案
            viewHolder.answerChoose.setImageResource(getAnswerDrawable(position));
        }else if ( mQuestionModel.getNewDone()!=QuestionDoneType.NOT_DONE) {
            // 已做过题目
            if (mQuestionModel.getQuestionType() == QuestionType.MULTIPLE &&
                    mQuestionModel.getNewDone()==QuestionDoneType.DONE_WRONG &&
                    mQuestionModel.getUserAnswerList().contains(position) &&
                    mQuestionModel.getAnswerList().contains(position)){
                viewHolder.answerChoose.setImageResource(getTrueDrawable(position));
            } else {
                viewHolder.answerChoose.setImageResource(getAnswerDrawable(position));
            }
        } else {
            //未作过的题目
            viewHolder.answerChoose.setImageResource(getChooseDrawable(position));
        }
        viewHolder.optionText.setText(mQuestionModel.getOptionList().get(position));
        return convertView;
    }

    public void showAnswer(boolean showUserAnswer) {
        this.isShowAnswer = true;
        this.isShowUserAnswer = showUserAnswer;
        notifyDataSetChanged();
    }

    public void hideAnswer(){
        this.isShowAnswer = false;
        notifyDataSetChanged();
    }

    private class ViewHolder{
        public ImageView answerChoose;
        public TextView optionText;
    }

    public void clickOption(int postion){
        notifyDataSetChanged();
    }

    public int getChooseDrawable(int position){

        TypedValue typedValue = new TypedValue();
        int resouce;
        if(mQuestionModel!=null && mQuestionModel.getUserAnswerList().contains(position) && isShowUserAnswer){
            switch (position) {
                case 0:
                    mContext.getTheme().resolveAttribute(R.attr.practise_a_s, typedValue, true);
                    resouce =  typedValue.resourceId;
                    return resouce;
                case 1:
                    mContext.getTheme().resolveAttribute(R.attr.practise_b_s, typedValue, true);
                    resouce =  typedValue.resourceId;
                    return resouce;
                case 2:
                    mContext.getTheme().resolveAttribute(R.attr.practise_c_s, typedValue, true);
                    resouce =  typedValue.resourceId;
                    return resouce;
                case 3:
                    mContext.getTheme().resolveAttribute(R.attr.practise_d_s, typedValue, true);
                    resouce =  typedValue.resourceId;
                    return resouce;
                case 4:
                    mContext.getTheme().resolveAttribute(R.attr.practise_e_s, typedValue, true);
                    resouce =  typedValue.resourceId;
                    return resouce;
                case 5:
                    mContext.getTheme().resolveAttribute(R.attr.practise_f_s, typedValue, true);
                    resouce =  typedValue.resourceId;
                    return resouce;
                case 6:
                    mContext.getTheme().resolveAttribute(R.attr.practise_g_s, typedValue, true);
                    resouce =  typedValue.resourceId;
                    return resouce;
            }
        }else {
            switch (position) {
                case 0:
                    mContext.getTheme().resolveAttribute(R.attr.practise_a_n, typedValue, true);
                    resouce =  typedValue.resourceId;
                    return resouce;
                case 1:
                    mContext.getTheme().resolveAttribute(R.attr.practise_b_n, typedValue, true);
                    resouce =  typedValue.resourceId;
                    return resouce;
                case 2:
                    mContext.getTheme().resolveAttribute(R.attr.practise_c_n, typedValue, true);
                    resouce =  typedValue.resourceId;
                    return resouce;
                case 3:
                    mContext.getTheme().resolveAttribute(R.attr.practise_d_n, typedValue, true);
                    resouce =  typedValue.resourceId;
                    return resouce;
                case 4:
                    mContext.getTheme().resolveAttribute(R.attr.practise_e_n, typedValue, true);
                    resouce =  typedValue.resourceId;
                    return resouce;
                case 5:
                    mContext.getTheme().resolveAttribute(R.attr.practise_f_n, typedValue, true);
                    resouce =  typedValue.resourceId;
                    return resouce;
                case 6:
                    mContext.getTheme().resolveAttribute(R.attr.practise_g_n, typedValue, true);
                    resouce =  typedValue.resourceId;
                    return resouce;
            }
        }
        return 0;
    }

    public int getAnswerDrawable(int position) {

        TypedValue typedValue = new TypedValue();
        int resouce;
        if(mQuestionModel.getAnswerList().contains(position)){
            mContext.getTheme().resolveAttribute(R.attr.practise_true, typedValue, true);
            resouce =  typedValue.resourceId;
            return resouce;
        }
        if(isShowUserAnswer && mQuestionModel.getNewDone()!= QuestionDoneType.NOT_DONE && !mQuestionModel.getAnswerList().contains(position) && mQuestionModel.getUserAnswerList().contains(position)){
            mContext.getTheme().resolveAttribute(R.attr.practise_false, typedValue, true);
            resouce =  typedValue.resourceId;
            return resouce;
        }
        return getChooseDrawable(position);
    }

    public int getTrueDrawable(int position){
        TypedValue typedValue = new TypedValue();
        int resouce;
        switch (position) {
            case 0:
                mContext.getTheme().resolveAttribute(R.attr.practise_a_true, typedValue, true);
                resouce =  typedValue.resourceId;
                return resouce;
            case 1:
                mContext.getTheme().resolveAttribute(R.attr.practise_b_true, typedValue, true);
                resouce =  typedValue.resourceId;
                return resouce;
            case 2:
                mContext.getTheme().resolveAttribute(R.attr.practise_c_true, typedValue, true);
                resouce =  typedValue.resourceId;
                return resouce;
            case 3:
                mContext.getTheme().resolveAttribute(R.attr.practise_d_true, typedValue, true);
                resouce =  typedValue.resourceId;
                return resouce;
            case 4:
                mContext.getTheme().resolveAttribute(R.attr.practise_e_true, typedValue, true);
                resouce =  typedValue.resourceId;
                return resouce;
            case 5:
                mContext.getTheme().resolveAttribute(R.attr.practise_f_true, typedValue, true);
                resouce =  typedValue.resourceId;
                return resouce;
            case 6:
                mContext.getTheme().resolveAttribute(R.attr.practise_g_true, typedValue, true);
                resouce =  typedValue.resourceId;
                return resouce;
        }
        return 0;
    }
}
