package com.jscheng.mr_horse.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.jscheng.mr_horse.R;
import com.jscheng.mr_horse.presenter.WrongPresenter;
import com.jscheng.mr_horse.presenter.impl.WrongPresenterImpl;
import com.jscheng.mr_horse.view.MvpView;
import com.jscheng.mr_horse.view.WrongView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cheng on 17-1-11.
 */
public class WrongActivity extends BaseActivity implements WrongView {
    @BindView(R.id.title_text)
    TextView title_text;
    @BindView(R.id.fljc_num_tv)
    TextView fljc_num_tv;
    @BindView(R.id.fljc_delete_tv)
    TextView fljc_delete_tv;
    @BindView(R.id.mkszy_num_tv)
    TextView mkszy_num_tv;
    @BindView(R.id.mkszy_delete_tv)
    TextView mkszy_delete_tv;
    @BindView(R.id.mzdsx_num_tv)
    TextView mzdsx_num_tv;
    @BindView(R.id.mzdsx_delete_tv)
    TextView mzdsx_delete_tv;
    @BindView(R.id.sxdd_num_tv)
    TextView sxdd_num_tv;
    @BindView(R.id.sxdd_delete_tv)
    TextView sxdd_delete_tv;
    @BindView(R.id.zgjds_num_tv)
    TextView zgjds_num_tv;
    @BindView(R.id.zgjds_delete_tv)
    TextView zgjds_delete_tv;

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
    public void onResume() {
        super.onResume();
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
        if(num<=0)
            fljc_num_tv.setVisibility(View.GONE);
        else {
            fljc_num_tv.setVisibility(View.VISIBLE);
            fljc_num_tv.setText(num+"");
        }
    }

    public void showMkszyNumTv(int num){
        if(num<=0)
            mkszy_num_tv.setVisibility(View.GONE);
        else {
            mkszy_num_tv.setVisibility(View.VISIBLE);
            mkszy_num_tv.setText(num+"");
        }
    }

    @Override
    public void showMzdsxNumTv(int num) {
        if(num<=0)
            mzdsx_num_tv.setVisibility(View.GONE);
        else {
            mzdsx_num_tv.setVisibility(View.VISIBLE);
            mzdsx_num_tv.setText(num+"");
        }
    }

    @Override
    public void showSxddNumTv(int num) {
        if(num<=0)
            sxdd_num_tv.setVisibility(View.GONE);
        else {
            sxdd_num_tv.setVisibility(View.VISIBLE);
            sxdd_num_tv.setText(num+"");
        }
    }

    @Override
    public void showZgjdsNumTv(int num) {
        if(num<=0)
            zgjds_num_tv.setVisibility(View.GONE);
        else {
            zgjds_num_tv.setVisibility(View.VISIBLE);
            zgjds_num_tv.setText(num+"");
        }
    }

    @Override
    public void showDeleteView() {
        fljc_delete_tv.setVisibility(View.VISIBLE);
        mkszy_delete_tv.setVisibility(View.VISIBLE);
        mzdsx_delete_tv.setVisibility(View.VISIBLE);
        sxdd_delete_tv.setVisibility(View.VISIBLE);
        zgjds_delete_tv.setVisibility(View.VISIBLE);

        fljc_delete_tv.setClickable(true);
        mkszy_delete_tv.setClickable(true);
        mzdsx_delete_tv.setClickable(true);
        sxdd_delete_tv.setClickable(true);
        zgjds_delete_tv.setClickable(true);
    }

    @Override
    public void hideDeleteView() {
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

    @OnClick(R.id.fljc_delete_tv)
    public void onClickFljcNumTv(){

    }

    @OnClick(R.id.mkszy_delete_tv)
    public void onClickMkszyNumTv(){

    }

    @OnClick(R.id.mzdsx_delete_tv)
    public void onClickMzdsxNumTv(){

    }

    @OnClick(R.id.sxdd_delete_tv)
    public void onClickSxddNumTv(){

    }

    @OnClick(R.id.zgjds_delete_tv)
    public void onClickZgjdsNumTv(){

    }
}