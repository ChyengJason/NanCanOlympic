package com.jscheng.mr_horse.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jscheng.mr_horse.App;
import com.jscheng.mr_horse.R;
import com.jscheng.mr_horse.utils.Constants;
import com.jscheng.mr_horse.utils.SharedPreferencesUtil;
import com.kyleduo.switchbutton.SwitchButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cheng on 17-2-7.
 */
public class SettingActivity extends BaseActivity {
    @BindView(R.id.title_text)
    TextView titleView;
    @BindView(R.id.auto_check_switchButton)
    SwitchButton switchBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        switchBtn.setCheckedImmediately((boolean) SharedPreferencesUtil.getParam(this, Constants.AUTO_CHECK_UPGRADE,true));
        titleView.setText(getString(R.string.setting));
        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchBtn.setChecked(isChecked);
                SharedPreferencesUtil.setParam(SettingActivity.this,Constants.AUTO_CHECK_UPGRADE,isChecked);
            }
        });
    }

    @OnClick(R.id.title_back)
    public void onTitleBack(){
        finish();
    }

    @OnClick(R.id.feedback)
    public void onFeedBack(){
        startActivity(new Intent(this,FeedBackAcivity.class));
    }

    @OnClick(R.id.check_update)
    public void checkUpdate(){
        App.getInstance().checkUpgrade();
    }

    @OnClick(R.id.about)
    public void onClickAbout(){
        startActivity(new Intent(this,AboutActivity.class));
    }
}
