package com.jscheng.mr_horse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jscheng.mr_horse.R;
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

    public AnswerListViewAdapter(Context context, QuestionModel questionModel){
        this.mContext = context;
        this.mQuestionModel = questionModel;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.isShowAnswer = false;
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

    public int getChooseDrawable(int position){
        if(mQuestionModel!=null && mQuestionModel.getUserAnswerList().contains(position)){
            switch (position) {
                case 0:
                    return R.mipmap.practise_a_s_day;
                case 1:
                    return R.mipmap.practise_b_s_day;
                case 2:
                    return R.mipmap.practise_c_s_day;
                case 3:
                    return R.mipmap.practise_d_s_day;
                case 4:
                    return R.mipmap.practise_e_s_day;
                case 5:
                    return R.mipmap.practise_f_s_day;
                case 6:
                    return R.mipmap.practise_g_s_day;
            }
        }else {
            switch (position) {
                case 0:
                    return R.mipmap.practise_a_n_day;
                case 1:
                    return R.mipmap.practise_b_n_day;
                case 2:
                    return R.mipmap.practise_c_n_day;
                case 3:
                    return R.mipmap.practise_d_n_day;
                case 4:
                    return R.mipmap.practise_e_n_day;
                case 5:
                    return R.mipmap.practise_f_n_day;
                case 6:
                    return R.mipmap.practise_g_n_day;
            }
        }
        return 0;
    }

    public int getAnswerDrawable(int position) {
        if(mQuestionModel.getAnswerList().contains(position)){
            return R.mipmap.practise_true_day;
        }
        if(mQuestionModel.getDone() && !mQuestionModel.getAnswerList().contains(position)){
            return R.mipmap.practise_false_day;
        }
        return getChooseDrawable(position);
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

        if(isShowAnswer || mQuestionModel.getDone()){//显示答案 或者 已做过题目
            viewHolder.answerChoose.setImageResource(getAnswerDrawable(position));
        }else {
            viewHolder.answerChoose.setImageResource(getChooseDrawable(position));
        }

        viewHolder.optionText.setText(mQuestionModel.getOptionList().get(position));
        return convertView;
    }

    public void showAnswer() {
        this.isShowAnswer = true;
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
}
