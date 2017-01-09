package com.jscheng.mr_horse.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import com.jscheng.mr_horse.R;
import com.jscheng.mr_horse.adapter.HeadViewPaperAdapter;
import com.jscheng.mr_horse.presenter.MainPresenter;
import com.jscheng.mr_horse.presenter.impl.MainPresenterImpl;
import com.jscheng.mr_horse.view.MainView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cheng on 17-1-8.
 */
public class MainActivity extends BaseActivity implements MainView,View.OnClickListener{
    @BindView(R.id.head_viewpaper)
    ViewPager headViewPager;

    @BindView(R.id.sun_night)
    ImageButton sunNight;

    private View headview_1;
    private View headview_2;
    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViewPaper();
        initPresenter();
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
        (headview_1.findViewById(R.id.fljc_book)).setOnClickListener(this);
        (headview_1.findViewById(R.id.mkszy_book)).setOnClickListener(this);
        (headview_1.findViewById(R.id.mzdsx_book)).setOnClickListener(this);
        (headview_2.findViewById(R.id.sxdd_book)).setOnClickListener(this);
        (headview_2.findViewById(R.id.zgjds_book)).setOnClickListener(this);
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

}
