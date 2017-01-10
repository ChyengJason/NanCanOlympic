package com.jscheng.mr_horse.presenter.impl;

import android.content.Context;
import android.content.Intent;

import com.jscheng.mr_horse.App;
import com.jscheng.mr_horse.R;
import com.jscheng.mr_horse.presenter.MainPresenter;
import com.jscheng.mr_horse.ui.PracticeActivity;
import com.jscheng.mr_horse.utils.Constants;
import com.jscheng.mr_horse.utils.SharedPreferencesUtil;
import com.jscheng.mr_horse.view.MainView;
import com.jscheng.mr_horse.view.MvpView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cheng on 17-1-8.
 */
public class MainPresenterImpl implements MainPresenter {
    private MainView mainView;
    private Context context;

    public MainPresenterImpl(Context context){
        this.context = context;
    }

    @Override
    public void attachView(MvpView view) {
        this.mainView = (MainView)view;
        init();
    }

    private void init() {
        DateFormat format = new SimpleDateFormat("yyyy.mm.dd");
        Date date = new Date(System.currentTimeMillis());
        String dateStr = format.format(date);
        if(mainView!=null)
            mainView.showDate(dateStr);
    }

    @Override
    public void detachView(boolean retainInstance) {
        if(retainInstance==false)
            this.mainView = null;
    }

    @Override
    public void onClickFljc() {
        Intent intent = new Intent(context, PracticeActivity.class);
        intent.putExtra("filename", Constants.FLJC_JSON_NAME);
        intent.putExtra("catogory",Constants.FLJC);
        context.startActivity(intent);
    }

    @Override
    public void onClickMkszy() {
        Intent intent = new Intent(context, PracticeActivity.class);
        intent.putExtra("filename", Constants.MKSZY_JSON_NAME);
        intent.putExtra("catogory",Constants.MKSZY);
        context.startActivity(intent);
    }

    @Override
    public void onClickMzdsx() {
        Intent intent = new Intent(context, PracticeActivity.class);
        intent.putExtra("filename", Constants.MZDSX_JSON_NAME);
        intent.putExtra("catogory",Constants.MZDSX);
        context.startActivity(intent);

    }

    @Override
    public void onClickSxdd() {
        Intent intent = new Intent(context, PracticeActivity.class);
        intent.putExtra("filename", Constants.SXDD_JSON_NAME);
        intent.putExtra("catogory",Constants.SXDD);
        context.startActivity(intent);
    }

    @Override
    public void onClickZgjds() {
        Intent intent = new Intent(context, PracticeActivity.class);
        intent.putExtra("filename", Constants.ZGJDS_JSON_NAME);
        intent.putExtra("catogory",Constants.MKSZY);
        context.startActivity(intent);
    }
    @Override
    public void changeTheme() {
        int dayNightTheme = App.getDayNightTheme();
        if (dayNightTheme == R.style.SunAppTheme) {
            App.setDayNightTheme(R.style.NightAppTheme);
            mainView.changeToNightTheme();
        } else {
            App.setDayNightTheme(R.style.SunAppTheme);
            mainView.changeToSunTheme();
        }
        SharedPreferencesUtil.setParam(context,Constants.THEME,App.getDayNightTheme());
    }
}
