package com.jscheng.mr_horse.presenter.impl;

import android.content.Context;
import android.content.Intent;
import com.jscheng.mr_horse.App;
import com.jscheng.mr_horse.R;
import com.jscheng.mr_horse.presenter.MainPresenter;
import com.jscheng.mr_horse.ui.AboutActivity;
import com.jscheng.mr_horse.ui.PracticeActivity;
import com.jscheng.mr_horse.ui.SearchActivity;
import com.jscheng.mr_horse.ui.SettingActivity;
import com.jscheng.mr_horse.ui.WrongActivity;
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
        DateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        Date date = new Date(System.currentTimeMillis());
        String dateStr = format.format(date);

        String lastdayStr = (String) SharedPreferencesUtil.getParam(context, Constants.LAST_DAY, "");
        int punchday = (int) SharedPreferencesUtil.getParam(context, Constants.PUNCH_DAY_NUM, 0);
        if (!lastdayStr.equals(dateStr)) {
            SharedPreferencesUtil.setParam(context, Constants.LAST_DAY, dateStr);
            SharedPreferencesUtil.setParam(context, Constants.HAVE_DONE_NUM, 0);
            punchday++;
            SharedPreferencesUtil.setParam(context, Constants.PUNCH_DAY_NUM, punchday);
        }
        showDateAndDoneNum();
    }

    @Override
    public void detachView(boolean retainInstance) {
        if(retainInstance==false) {
            this.mainView = null;
            this.context = null;
        }
    }

    @Override
    public void onClickFljc() {
        Intent intent = new Intent(context, PracticeActivity.class);
        intent.putExtra(Constants.FILENAME, Constants.FLJC_JSON_NAME);
        intent.putExtra(Constants.CATOGORY,Constants.FLJC);
        mainView.startActivityForResult(intent);
    }

    @Override
    public void onClickMkszy() {
        Intent intent = new Intent(context, PracticeActivity.class);
        intent.putExtra(Constants.FILENAME, Constants.MKSZY_JSON_NAME);
        intent.putExtra(Constants.CATOGORY,Constants.MKSZY);
        mainView.startActivityForResult(intent);
    }

    @Override
    public void onClickMzdsx() {
        Intent intent = new Intent(context, PracticeActivity.class);
        intent.putExtra(Constants.FILENAME, Constants.MZDSX_JSON_NAME);
        intent.putExtra(Constants.CATOGORY,Constants.MZDSX);
        mainView.startActivityForResult(intent);

    }

    @Override
    public void onClickSxdd() {
        Intent intent = new Intent(context, PracticeActivity.class);
        intent.putExtra(Constants.FILENAME, Constants.SXDD_JSON_NAME);
        intent.putExtra(Constants.CATOGORY,Constants.SXDD);
        mainView.startActivityForResult(intent);
    }

    @Override
    public void onClickZgjds() {
        Intent intent = new Intent(context, PracticeActivity.class);
        intent.putExtra(Constants.FILENAME, Constants.ZGJDS_JSON_NAME);
        intent.putExtra(Constants.CATOGORY,Constants.ZGJDS);
        mainView.startActivityForResult(intent);
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

    @Override
    public void onActivityForResult() {
        showDateAndDoneNum();
    }

    @Override
    public void onClickCollect() {
        Intent intent = new Intent(context,WrongActivity.class);
        intent.putExtra(Constants.CATOGORY, Constants.COLLECT);
        mainView.startActivityForResult(intent);
    }

    @Override
    public void onClickWrong() {
        Intent intent = new Intent(context,WrongActivity.class);
        intent.putExtra(Constants.CATOGORY, Constants.WRONG);
        mainView.startActivityForResult(intent);
    }

    @Override
    public void onClickAbout() {
        context.startActivity(new Intent(context, AboutActivity.class));
    }

    @Override
    public void onClickShareLayout() {
        mainView.showShareDialog();
    }

    @Override
    public void onClickSetting() {
        context.startActivity(new Intent(context, SettingActivity.class));
    }

    @Override
    public void onClickSearchLayout() {
        context.startActivity(new Intent(context, SearchActivity.class));
    }

    private void showDateAndDoneNum(){
        DateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        Date date = new Date(System.currentTimeMillis());
        String dateStr = format.format(date);
        int haveDoneNum = (int) SharedPreferencesUtil.getParam(context, Constants.HAVE_DONE_NUM, 0);
        int punchday = (int) SharedPreferencesUtil.getParam(context, Constants.PUNCH_DAY_NUM, 0);
        if (mainView != null) {
            mainView.showDate(dateStr);
            mainView.showHaveDoneNum(haveDoneNum+"");
            mainView.showPunchDayNum(punchday+"");
        }
    }
}
