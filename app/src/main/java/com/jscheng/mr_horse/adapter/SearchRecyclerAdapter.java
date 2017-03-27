package com.jscheng.mr_horse.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jscheng.mr_horse.App;
import com.jscheng.mr_horse.R;
import com.jscheng.mr_horse.model.QuestionModel;
import com.jscheng.mr_horse.utils.QuestionCatagoryUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cheng on 2017/3/27.
 */
public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.SearchRecyclerViewHolder> {

    private List<QuestionModel> searchList;
    private String searchWord;

    public SearchRecyclerAdapter(){
        searchList = new ArrayList<>();
        searchWord = "";
    }

    @Override
    public SearchRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_content_item,parent,false);
        SearchRecyclerViewHolder viewHolder = new SearchRecyclerViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    @Override
    public void onBindViewHolder(SearchRecyclerViewHolder holder, final int position) {
        QuestionModel model = searchList.get(position);
        if (model == null)
            throw new RuntimeException(" NO." + position + "is null");

        String numString = String.valueOf(position+1);
        String catagoryString  = ". [" + QuestionCatagoryUtil.getShortName(model.getCatogory()) + "] ";
        StringBuilder content = new StringBuilder(numString + catagoryString);
        if (model.getQuestion().contains(searchWord)) {
            content.append(model.getQuestion().replaceAll("\\s*", ""));
        }else {
            content.append(model.getQuestion());
            for (String answer : model.getOptionList()){
                if (answer.contains(searchWord)){
                    content.append(answer.replaceAll("\\s*", ""));
                    break;
                }
            }
        }

        int index = content.indexOf(searchWord,numString.length() + catagoryString.length());
        SpannableStringBuilder style = new SpannableStringBuilder(content);

        if ( index != -1){
            int highLightColor = App.getInstance().getResources().getColor(R.color.pink_red);
            style.setSpan(new ForegroundColorSpan(highLightColor), index, index + searchWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        holder.searchContentTextview.setText(style);
        holder.searchContentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickItemListener!=null)
                    clickItemListener.onClickItem(position,searchList.get(position));
            }
        });
    }

    public void clear() {
        searchList.clear();
        notifyDataSetChanged();
    }

    protected static class SearchRecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView searchContentTextview;
        LinearLayout searchContentLayout;
        public SearchRecyclerViewHolder(View itemView) {
            super(itemView);
            searchContentTextview = (TextView) itemView.findViewById(R.id.search_content_item_textview);
            searchContentLayout = (LinearLayout) itemView.findViewById(R.id.search_content_item_layout);
        }
    }

    public void setSearchContent(String searchWord, List<QuestionModel> searchList){
        this.searchList.clear();
        this.searchList.addAll(searchList);
        this.searchWord = searchWord;
        this.notifyDataSetChanged();
    }

    public void addSearchContent( List<QuestionModel> searchList){
        this.searchList.addAll(searchList);
        this.notifyDataSetChanged();
    }

    public OnClickItemListener getClickItemListener() {
        return clickItemListener;
    }

    public void setClickItemListener(OnClickItemListener clickItemListener) {
        this.clickItemListener = clickItemListener;
    }

    private OnClickItemListener clickItemListener = null;

    public interface OnClickItemListener{
        void onClickItem(int postion,QuestionModel model);
    }
}
