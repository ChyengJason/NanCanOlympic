package com.jscheng.mr_horse.wiget;

import android.view.View;
import android.widget.RelativeLayout;

import com.jscheng.mr_horse.R;

/**
 * Created by cheng on 17-1-15.
 */
public class ShareDialog extends BottomDialog implements View.OnClickListener {

    private RelativeLayout mShareCircleLayout;
    private RelativeLayout mShareFriendLayout;
    private RelativeLayout mCopyBoardLayout;

    public final static int COPY_TO_BOARD = 100;
    public final static int CIRCLE = 101;
    public final static int WEIXIN =102;

    @Override
    public void onClick(View v) {
        if(mOnShareSelectCallBack==null)
            return;
        switch (v.getId()){
            case R.id.shareCircleLayout:
                mOnShareSelectCallBack.onShareSelect(CIRCLE);
                break;
            case R.id.shareFriendLayout:
                mOnShareSelectCallBack.onShareSelect(WEIXIN);
                break;
            case R.id.copyLayout:
                mOnShareSelectCallBack.onShareSelect(COPY_TO_BOARD);
                break;
            default:
                break;
        }
        this.dismiss();
    }

    public interface OnShareSelectCallBack {
        void onShareSelect(int position);
    }

    OnShareSelectCallBack mOnShareSelectCallBack;

    public OnShareSelectCallBack getOnShareSelectCallBack() {
        return mOnShareSelectCallBack;
    }

    public void setOnShareSelectCallBack(OnShareSelectCallBack onShareSelectCallBack) {
        mOnShareSelectCallBack = onShareSelectCallBack;
    }


    @Override
    public int getLayoutRes() {
        return R.layout.dialog_share;
    }

    @Override
    public void bindView(View v) {
        mShareCircleLayout = (RelativeLayout) v.findViewById(R.id.shareCircleLayout);
        mShareFriendLayout = (RelativeLayout) v.findViewById(R.id.shareFriendLayout);
        mCopyBoardLayout = (RelativeLayout) v.findViewById(R.id.copyLayout);

        mShareCircleLayout.setOnClickListener(this);
        mShareFriendLayout.setOnClickListener(this);
        mCopyBoardLayout.setOnClickListener(this);
    }
}