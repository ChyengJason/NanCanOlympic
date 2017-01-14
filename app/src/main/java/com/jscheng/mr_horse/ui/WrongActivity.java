package com.jscheng.mr_horse.ui;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jscheng.mr_horse.R;
import com.jscheng.mr_horse.presenter.WrongPresenter;
import com.jscheng.mr_horse.presenter.impl.WrongPresenterImpl;
import com.jscheng.mr_horse.view.WrongView;
import com.orhanobut.logger.Logger;
import com.pnikosis.materialishprogress.ProgressWheel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cheng on 17-1-11.
 */
public class WrongActivity extends BaseActivity implements WrongView {
    @BindView(R.id.title_text)
    TextView title_text;
    @BindView(R.id.rubbish)
    ImageButton rubbish_btn;
    @BindView(R.id.cancel)
    TextView cancel_view;
    @BindView(R.id.progress_wheel)
    ProgressWheel progressWheel;

    @BindView(R.id.fljc_num_tv)
    TextView fljc_num_tv;
    @BindView(R.id.fljc_delete_tv)
    TextView fljc_delete_tv;
    @BindView(R.id.fljc_cardview)
    FrameLayout fljc_cardview;

    @BindView(R.id.mkszy_num_tv)
    TextView mkszy_num_tv;
    @BindView(R.id.mkszy_delete_tv)
    TextView mkszy_delete_tv;
    @BindView(R.id.mkszy_cardview)
    FrameLayout mkszy_cardview;

    @BindView(R.id.mzdsx_num_tv)
    TextView mzdsx_num_tv;
    @BindView(R.id.mzdsx_delete_tv)
    TextView mzdsx_delete_tv;
    @BindView(R.id.mzdsx_cardview)
    FrameLayout mzdsx_cardview;

    @BindView(R.id.sxdd_num_tv)
    TextView sxdd_num_tv;
    @BindView(R.id.sxdd_delete_tv)
    TextView sxdd_delete_tv;
    @BindView(R.id.sxdd_cardview)
    FrameLayout sxdd_cardview;

    @BindView(R.id.zgjds_num_tv)
    TextView zgjds_num_tv;
    @BindView(R.id.zgjds_delete_tv)
    TextView zgjds_delete_tv;
    @BindView(R.id.zgjds_cardview)
    FrameLayout zgjds_cardview;

    private WrongPresenter wrongPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong);
        ButterKnife.bind(this);
        wrongPresenter = new WrongPresenterImpl(this,getIntent());
        wrongPresenter.attachView(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        wrongPresenter.detachView(false);
    }

    public void setBarTitle(String title){
        title_text.setText(title);
    }

    public void showFljcNumTv(int num){
        if(num<=0) {
            fljc_num_tv.setVisibility(View.GONE);
            fljc_cardview.setVisibility(View.GONE);
        }else {
            fljc_num_tv.setVisibility(View.VISIBLE);
            fljc_num_tv.setText(num+"");
        }
    }

    public void showMkszyNumTv(int num){
        if(num<=0) {
            mkszy_num_tv.setVisibility(View.GONE);
            mkszy_cardview.setVisibility(View.GONE);
        }else {
            mkszy_num_tv.setVisibility(View.VISIBLE);
            mkszy_num_tv.setText(num+"");
        }
    }

    @Override
    public void showMzdsxNumTv(int num) {
        if (num <= 0){
            mzdsx_num_tv.setVisibility(View.GONE);
            mzdsx_cardview.setVisibility(View.GONE);
        }else {
            mzdsx_num_tv.setVisibility(View.VISIBLE);
            mzdsx_num_tv.setText(num+"");
        }
    }

    @Override
    public void showSxddNumTv(int num) {
        if(num<=0) {
            sxdd_num_tv.setVisibility(View.GONE);
            sxdd_cardview.setVisibility(View.GONE);
        }else {
            sxdd_num_tv.setVisibility(View.VISIBLE);
            sxdd_num_tv.setText(num+"");
        }
    }

    @Override
    public void showZgjdsNumTv(int num) {
        if(num<=0) {
            zgjds_num_tv.setVisibility(View.GONE);
            zgjds_cardview.setVisibility(View.GONE);
        }else {
            zgjds_num_tv.setVisibility(View.VISIBLE);
            zgjds_num_tv.setText(num+"");
        }
    }

    @Override
    public void showDeleteView() {
        rubbish_btn.setVisibility(View.GONE);
        cancel_view.setVisibility(View.VISIBLE);

        if (fljc_cardview.getVisibility() == View.VISIBLE) {
            fljc_delete_tv.setVisibility(View.VISIBLE);
            fljc_delete_tv.setClickable(true);
        }
        if (mkszy_cardview.getVisibility() == View.VISIBLE) {
            mkszy_delete_tv.setVisibility(View.VISIBLE);
            mkszy_delete_tv.setClickable(true);
        }
        if (mzdsx_cardview.getVisibility() == View.VISIBLE) {
            mzdsx_delete_tv.setVisibility(View.VISIBLE);
            mzdsx_delete_tv.setClickable(true);
        }
        if (sxdd_cardview.getVisibility() == View.VISIBLE) {
            sxdd_delete_tv.setVisibility(View.VISIBLE);
            sxdd_delete_tv.setClickable(true);
        }
        if (zgjds_cardview.getVisibility() == View.VISIBLE) {
            zgjds_delete_tv.setVisibility(View.VISIBLE);
            zgjds_delete_tv.setClickable(true);
        }
    }

    @Override
    public void hideDeleteView() {
        rubbish_btn.setVisibility(View.VISIBLE);
        cancel_view.setVisibility(View.GONE);

        fljc_delete_tv.setVisibility(View.GONE);
        mkszy_delete_tv.setVisibility(View.GONE);
        mzdsx_delete_tv.setVisibility(View.GONE);
        sxdd_delete_tv.setVisibility(View.GONE);
        zgjds_delete_tv.setVisibility(View.GONE);

        fljc_delete_tv.setClickable(false);
        mkszy_delete_tv.setClickable(false);
        mzdsx_delete_tv.setClickable(false);
        sxdd_delete_tv.setClickable(false);
        zgjds_delete_tv.setClickable(false);
    }

    @Override
    public void showProcessing() {
        progressWheel.setVisibility(View.VISIBLE);
        ValueAnimator progressFadeInAnim = ObjectAnimator.ofFloat(progressWheel, "alpha", 0, 1, 1);
        progressFadeInAnim.start();
    }

    @Override
    public void showError() {

    }

    @Override
    public void hideProcessing() {
        progressWheel.setVisibility(View.GONE);
        ValueAnimator progressFadeInAnim = ObjectAnimator.ofFloat(progressWheel, "alpha", 1, 0, 0);
        progressFadeInAnim.start();
    }


    @OnClick(R.id.fljc_delete_tv)
    public void onClickFljcNumTv(){
        showConfirmDailog(new DailogCallback() {
            @Override
            public void confirm() {
                Logger.e("clickFljc");
                wrongPresenter.deleteFljc();
            }
        });
    }

    @OnClick(R.id.mkszy_delete_tv)
    public void onClickMkszyNumTv(){
        showConfirmDailog(new DailogCallback() {
            @Override
            public void confirm() {
                wrongPresenter.deleteMkszy();
            }
        });
    }

    @OnClick(R.id.mzdsx_delete_tv)
    public void onClickMzdsxNumTv(){
        showConfirmDailog(new DailogCallback() {
            @Override
            public void confirm() {
                wrongPresenter.deleteMzdsx();
            }
        });
    }

    @OnClick(R.id.sxdd_delete_tv)
    public void onClickSxddNumTv(){
        showConfirmDailog(new DailogCallback() {
            @Override
            public void confirm() {
                wrongPresenter.deleteSxdd();
            }
        });
    }

    @OnClick(R.id.zgjds_delete_tv)
    public void onClickZgjdsNumTv(){
        showConfirmDailog(new DailogCallback() {
            @Override
            public void confirm() {
                wrongPresenter.deleteZgjsds();
            }
        });
    }

    @OnClick(R.id.rubbish)
    public void onClickRubbish(){
        wrongPresenter.onClickRubbish();
    }

    @OnClick(R.id.cancel)
    public void onClickCancel(){
        wrongPresenter.onClickCancel();
    }

    private void showConfirmDailog(final DailogCallback callback){
        AlertDialog.Builder builder = new AlertDialog.Builder(WrongActivity.this);
        builder.setMessage("确定删除？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.confirm();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private interface DailogCallback{
        void confirm();
    }

    @OnClick(R.id.fljc_cardview)
    public void onClickFljcLayout(){
        wrongPresenter.onClickFljcLayout();
    }

    @OnClick(R.id.mkszy_cardview)
    public void onClickMkszyLayout(){
        wrongPresenter.onClickMkszyLayout();
    }

    @OnClick(R.id.mzdsx_cardview)
    public void onClickMzdsxLayout(){
        wrongPresenter.onClickMzdsxLayout();
    }

    @OnClick(R.id.sxdd_cardview)
    public void onClickSxddLayout(){
        wrongPresenter.onClickSxddLayout();
    }

    @OnClick(R.id.zgjds_cardview)
    public void onClickZgjdsLayout(){
        wrongPresenter.onClickZgjdsLayout();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void startActivityForResult(Intent intent) {
        this.startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        wrongPresenter.onActivityForResult();
    }
}