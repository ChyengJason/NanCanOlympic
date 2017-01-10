package com.jscheng.mr_horse.adapter;

import android.content.Context;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jscheng.mr_horse.App;
import com.jscheng.mr_horse.R;
import com.jscheng.mr_horse.model.QuestionDoneType;
import com.jscheng.mr_horse.model.QuestionModel;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by cheng on 17-1-10.
 */
public class DailogGirdviewAdapter extends BaseAdapter {
    private List<QuestionModel> modelList;
    private Context mContext;
    private int currentPosition;

    public DailogGirdviewAdapter(Context mContext,List<QuestionModel> modelList,int currentPosition){
        this.modelList = modelList;
        this.mContext = mContext;
        this.currentPosition = currentPosition;
    }

    @Override
    public int getCount() {
        return modelList==null ? 0 :modelList.size();
    }

    @Override
    public QuestionModel getItem(int position) {
        return modelList==null ? null :modelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.question_dailog_item, null);
            viewHolder.idview = (TextView) convertView.findViewById(R.id.question_id_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String text = new String((position+1)+"");
        viewHolder.idview.setText(text);
        if(position==currentPosition){
            viewHolder.idview.setBackgroundResource(R.drawable.gray_solid_circle);
            return convertView;
        }

        if(getItem(position).getDone()== QuestionDoneType.DONE_RIGHT){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                viewHolder.idview.setTextColor(mContext.getColor(R.color.blue));
            }else {
                viewHolder.idview.setTextColor(mContext.getResources().getColor(R.color.blue));
            }
            viewHolder.idview.setBackgroundResource(R.drawable.blue_circle);
        }else if(getItem(position).getDone() == QuestionDoneType.DONE_WRONG){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                viewHolder.idview.setTextColor(mContext.getColor(R.color.pink_red));
            }else {
                viewHolder.idview.setTextColor(mContext.getResources().getColor(R.color.pink_red));
            }
            viewHolder.idview.setBackgroundResource(R.drawable.red_circle);
        }else {
            TypedValue typedValue = new TypedValue();
            mContext.getTheme().resolveAttribute(R.attr.text_color, typedValue, true);
            int resouce =  typedValue.resourceId;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                viewHolder.idview.setTextColor(mContext.getColor(resouce));
            }else {
                viewHolder.idview.setTextColor(mContext.getResources().getColor(resouce));
            }
            viewHolder.idview.setBackgroundResource(R.drawable.gray_circle);
        }
        return convertView;
    }

    private class ViewHolder {
        public TextView idview;
    }
}
