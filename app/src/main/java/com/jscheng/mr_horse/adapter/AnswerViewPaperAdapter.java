package com.jscheng.mr_horse.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jscheng.mr_horse.R;
import com.jscheng.mr_horse.model.QuestionDoneType;
import com.jscheng.mr_horse.model.QuestionModel;
import com.jscheng.mr_horse.model.PatternStatus;
import com.jscheng.mr_horse.model.QuestionType;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cheng on 17-1-7.
 */
public class AnswerViewPaperAdapter extends PagerAdapter {
    private static PatternStatus patternStatus;//看题模式，做题模式
    private Context mContext;
    private List<QuestionModel> mData;
    private LinkedList<View> mViewCache = null;
    private LayoutInflater mLayoutInflater = null;
    private List<AnswerPageChangeListener> listenerList = null;

    public AnswerViewPaperAdapter(Context context,List data,PatternStatus patternStatus){
        this.mContext = context;
        this.mData = data;
        this.mViewCache = new LinkedList<>();
        this.mLayoutInflater = LayoutInflater.from(mContext) ;
        this.listenerList = new ArrayList<>();
        this.patternStatus = patternStatus;
    }

    public void addAnswerPageListener(AnswerPageChangeListener listener){
        if(listenerList!=null){
            listenerList.add(listener);
        }
    }

    public void removeAnswerPageListener(AnswerPageChangeListener listener){
        if(listenerList!=null && listenerList.contains(listener)){
            listenerList.remove(listener);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        SingleViewHolder viewHolder = null;
        View convertView = null;
        final QuestionModel model = mData.get(position);

        if(mViewCache.size() == 0){
            convertView = this.mLayoutInflater.inflate(R.layout.pageview_item,
                    null ,false);
            TextView textView = (TextView)convertView.findViewById(R.id.question_text);
            ListView listView = (ListView)convertView.findViewById(R.id.option_listview);
            Button confirmBtn = (Button)convertView.findViewById(R.id.confirm);

            viewHolder = new SingleViewHolder();
            viewHolder.textView = textView;
            viewHolder.listView = listView;
            viewHolder.confirmBtn = confirmBtn;
            convertView.setTag(viewHolder);
        }else {
            convertView = mViewCache.removeFirst();
            viewHolder = (SingleViewHolder)convertView.getTag();
        }
        viewHolder.textView.setText("("+model.getQuestionType().toString()+") "+model.getQuestion());
        final AnswerListViewAdapter listViewAdapter = new AnswerListViewAdapter(mContext,model);
        viewHolder.listView.setAdapter(listViewAdapter);

        if (patternStatus == PatternStatus.DATI_PATTERN) {//答题模式
            ((AnswerListViewAdapter) viewHolder.listView.getAdapter()).hideAnswer();

            if(model.getDone()== QuestionDoneType.NOT_DONE) {
                viewHolder.listView.setOnItemClickListener(new OptionItemListener(position,model));
                if (model.getQuestionType() == QuestionType.MULTIPLE ) {//多选模式
                    viewHolder.confirmBtn.setVisibility(View.VISIBLE);
                    viewHolder.confirmBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            confirmAnswer(position,listViewAdapter, model);
                        }
                    });
                } else {//单选模式、判断题
                    viewHolder.confirmBtn.setVisibility(View.GONE);
                }
            }else {//已做过
                viewHolder.confirmBtn.setVisibility(View.GONE);
            }
        }else {//看题模式
            viewHolder.confirmBtn.setVisibility(View.GONE);
            ((AnswerListViewAdapter)viewHolder.listView.getAdapter()).showAnswer(false);
        }

        container.addView(convertView ,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT );
        return convertView;
    }

    public int getCount() {
        return this.mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public int getItemPosition(Object object) {
        // TODO Auto-generated method stub
        return POSITION_NONE;
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public Parcelable saveState() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View contentView = (View) object;
        container.removeView(contentView);
        this.mViewCache.add(contentView);
    }

    public QuestionType getItemType(int postion){
        return mData.get(postion).getQuestionType();
    }

    public final class SingleViewHolder{
        public TextView textView ;
        public ListView listView;
        public Button confirmBtn;
    }

    public class AnswerViewPaperListener implements ViewPager.OnPageChangeListener{

        public AnswerViewPaperListener(){
            super();
        }
        /**
         * @param position 当前页面，及你点击滑动的页面
         * @param positionOffset 当前页面偏移的百分比
         * @param positionOffsetPixels 当前页面偏移的像素位置
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * @param position (int) 判断当前是哪个view
         */
        @Override
        public void onPageSelected(int position) {
            if(listenerList!=null && !listenerList.isEmpty()){
                for(AnswerPageChangeListener listener:listenerList){
                    listener.onPageSelected(position);
                }
            }
        }

        /**
         * @param state 1正在滑动，2默示滑动完毕了，0什么都没做。
         */
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public interface AnswerPageChangeListener{
        void onPageSelected(int position);
        void onAnswerRight(int postion);
        void onAnswerWrong(int postion);
    }

    private class OptionItemListener implements AdapterView.OnItemClickListener{
        private QuestionModel questionModel;
        private int position;

        public OptionItemListener(int position,QuestionModel questionModel){
            this.questionModel = questionModel;
            this.position = position;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int optionPostion, long id) {

            if (questionModel.getDone()!=QuestionDoneType.NOT_DONE)return;

            if(questionModel.getUserAnswerList().contains(optionPostion)){
                questionModel.getUserAnswerList().remove((Integer)(optionPostion));
            }else {
                questionModel.getUserAnswerList().add(optionPostion);
            }
            if(questionModel.getQuestionType()==QuestionType.MULTIPLE)
                ((AnswerListViewAdapter)parent.getAdapter()).clickOption(optionPostion);
            else {
                confirmAnswer(position,(AnswerListViewAdapter)parent.getAdapter(),questionModel);
            }
        }
    }

    private void confirmAnswer(int position,AnswerListViewAdapter adapter,QuestionModel questionModel){

        List list1 = questionModel.getAnswerList();
        Collections.sort(list1);
        List list2 = questionModel.getUserAnswerList();
        Collections.sort(list2);
        if(list1.equals(list2)) {//答案正确
            questionModel.setDone(QuestionDoneType.DONE_RIGHT);
            adapter.showAnswer(true);
            if(listenerList!=null && !listenerList.isEmpty()){
                for(AnswerPageChangeListener listener:listenerList){
                    listener.onAnswerRight(position);
                }
            }
        }else {
            questionModel.setDone(QuestionDoneType.DONE_WRONG);
            adapter.showAnswer(true);
            if(listenerList!=null && !listenerList.isEmpty()){
                for(AnswerPageChangeListener listener:listenerList){
                    listener.onAnswerWrong(position);
                }
            }
        }
    }

    public void changePatternStatus(PatternStatus status){
        patternStatus = status;
        this.notifyDataSetChanged();
    }

}
