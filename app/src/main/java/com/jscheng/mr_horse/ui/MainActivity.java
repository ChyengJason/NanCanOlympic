package com.jscheng.mr_horse.ui;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jscheng.mr_horse.App;
import com.jscheng.mr_horse.R;
import com.jscheng.mr_horse.adapter.HeadViewPaperAdapter;
import com.jscheng.mr_horse.presenter.MainPresenter;
import com.jscheng.mr_horse.presenter.impl.MainPresenterImpl;
import com.jscheng.mr_horse.utils.Configs;
import com.jscheng.mr_horse.utils.Constants;
import com.jscheng.mr_horse.utils.ShareUtil;
import com.jscheng.mr_horse.view.MainView;
import com.jscheng.mr_horse.wiget.ShareDialog;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cheng on 17-1-8.
 */
public class MainActivity extends BaseActivity implements MainView,View.OnClickListener,ShareDialog.OnShareSelectCallBack{
    @BindView(R.id.head_viewpaper)
    ViewPager headViewPager;
    @BindView(R.id.sun_night)
    ImageButton sunNight;
    @BindView(R.id.date_tv)
    TextView dateTextView;
    @BindView(R.id.punch_day_num_text)
    TextView puchDayView;
    @BindView(R.id.today_done_num_text)
    TextView todayDoneNumView;
    @BindView(R.id.head_circle_left)
    ImageView headCircleLeftView;
    @BindView(R.id.head_circle_right)
    ImageView headCircleRightView;
    private View headview_1;
    private View headview_2;
    private MainPresenter mainPresenter;
    private IWXAPI wxApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        regToWx();
        initViewPaper();
        initPresenter();
    }

    private void regToWx() {
        wxApi = WXAPIFactory.createWXAPI(this, Configs.WX_APP_ID,true);
        wxApi.registerApp(Configs.WX_APP_ID);
    }

    private void initPresenter() {
        mainPresenter = new MainPresenterImpl(this);
        mainPresenter.attachView(this);
    }

    private void initViewPaper() {
        LayoutInflater inflater=getLayoutInflater();
        headview_1 = inflater.inflate(R.layout.headview_item_1, null);
        headview_2 = inflater.inflate(R.layout.headview_item_2,null);
        List<View> viewList = new ArrayList<>();
        viewList.add(headview_1);
        viewList.add(headview_2);
        headViewPager.setAdapter(new HeadViewPaperAdapter(viewList));
        setHeadViewPagerListener();
        (headview_1.findViewById(R.id.fljc_book)).setOnClickListener(this);
        (headview_1.findViewById(R.id.mkszy_book)).setOnClickListener(this);
        (headview_1.findViewById(R.id.mzdsx_book)).setOnClickListener(this);
        (headview_2.findViewById(R.id.sxdd_book)).setOnClickListener(this);
        (headview_2.findViewById(R.id.zgjds_book)).setOnClickListener(this);
    }

    private void setHeadViewPagerListener() {
        headViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    headCircleLeftView.setImageResource(R.drawable.solid_circle);
                    headCircleRightView.setImageResource(R.drawable.hollow_circle);
                }else {
                    headCircleLeftView.setImageResource(R.drawable.hollow_circle);
                    headCircleRightView.setImageResource(R.drawable.solid_circle);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fljc_book:
                mainPresenter.onClickFljc();
                break;
            case R.id.mkszy_book:
                mainPresenter.onClickMkszy();
                break;
            case R.id.mzdsx_book:
                mainPresenter.onClickMzdsx();
                break;
            case R.id.sxdd_book:
                mainPresenter.onClickSxdd();
                break;
            case R.id.zgjds_book:
                mainPresenter.onClickZgjds();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mainPresenter!=null){
            mainPresenter.detachView(false);
        }
    }

    @Override
    public void changeToNightTheme() {
        recreate();
    }

    @Override
    public void changeToSunTheme(){
        recreate();
    }

    @OnClick(R.id.sun_night)
    public void onClickSunNight() {
        mainPresenter.changeTheme();
    }

    public void showDate(String date){
        dateTextView.setText(date);
    }

    @Override
    public void showHaveDoneNum(String s) {
        todayDoneNumView.setText(s);
    }

    @Override
    public void showPunchDayNum(String s) {
        puchDayView.setText(s);
    }



    @OnClick(R.id.about_layout)
    public void onClickAboutLayout(){
            mainPresenter.onClickAbout();
    }

    @OnClick(R.id.setting_layout)
    public void onClickSettingLayout(){
        mainPresenter.onClickSetting();
    }

    @OnClick(R.id.collect_layout)
    public void onClickCollectLayout(){
       mainPresenter.onClickCollect();
    }

    @OnClick(R.id.cuoti_layout)
    public void onClickWrongLayout(){
        mainPresenter.onClickWrong();
    }

    @OnClick(R.id.share_layout)
    public void onClickShareLayout(){
        mainPresenter.onClickShareLayout();
    }

    @OnClick(R.id.search_layout)
    public void onClickSearchLayout() {
        mainPresenter.onClickSearchLayout();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mainPresenter.onActivityForResult();
    }

    public void startActivityForResult(Intent intent){
        this.startActivityForResult(intent,1);
    }

    @Override
    public void finish() {
        mainPresenter.detachView(false);
        super.finish();
    }

    public void showShareDialog() {
        ShareDialog dialog = new ShareDialog();
        dialog.setOnShareSelectCallBack(this);
        dialog.show(getSupportFragmentManager());
    }

    @Override
    public void onShareSelect(int position) {
        switch (position){
            case ShareDialog.CIRCLE:
                ShareUtil.sendToCircle(wxApi,Constants.DOWNLOAD_URL, getString(R.string.app_name), getString(R.string.app_descripe),
                        BitmapFactory.decodeResource(getResources(), R.mipmap.icon_night));
                break;
            case ShareDialog.WEIXIN:
                ShareUtil.sendToWeiXin(wxApi,Constants.DOWNLOAD_URL, getString(R.string.app_name), getString(R.string.app_descripe),
                        BitmapFactory.decodeResource(getResources(), R.mipmap.icon_night));
                break;
            default:
                break;
        }
    }
}
