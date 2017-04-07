package com.jscheng.mr_horse.ui;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jscheng.mr_horse.R;
import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cheng on 17-1-14.
 */
public class AboutActivity extends BaseActivity {
    @BindView(R.id.title_back)
    ImageButton title_text;
    @BindView(R.id.app_info)
    TextView app_info;
//    @BindView(R.id.app_descripe)
//    TextView app_descripe;
    @BindView(R.id.app_email)
    TextView app_email;
    @BindView(R.id.app_image)
    ImageView app_image;

//    private int time;
//    private static final int countTime= 5;
//    private static final int HIDE_MSG = 101;
//    private AboutHandler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
//        handler = new AboutHandler(this);
//        time = 0;
        try{
            String pkName = this.getPackageName();
            String versionName = this.getPackageManager().getPackageInfo(pkName,0).versionName;
            int versionCode = this.getPackageManager().getPackageInfo(pkName,0).versionCode;
            String appname = this.getResources().getString(R.string.app_name);
            app_info.setText(appname + "  " + versionName);
        }catch (Exception e){
            Logger.e(e.toString());
        }
    }

    @OnClick(R.id.title_back)
    public void onClickTitleBack(){
        finish();
    }

//    @OnClick(R.id.app_image)
//    public void onClickAppImage(){
//        time++;
//        time %= countTime;
//        if (time == (countTime-1)){
//            app_email.setVisibility(View.VISIBLE);
//            handler.sendEmptyMessageDelayed(HIDE_MSG,5000);
//        }
//    }

    @OnClick(R.id.app_email)
    public void onClickEmail(){
        copyToBoard();
    }

//    private static class AboutHandler extends Handler{
//        WeakReference<Activity> reference;
//        public AboutHandler(Activity activity){
//            reference = new WeakReference<Activity>(activity);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            final Activity activity = reference.get();
//            if (msg.what == HIDE_MSG && activity!=null){
//                ((AboutActivity)activity).app_email.setVisibility(View.GONE);
//            }
//        }
//    }

    public void copyToBoard(){
        ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("email",getResources().getString(R.string.person_email));
        cmb.setPrimaryClip(data);
        Toast.makeText(this,R.string.copied_to_board, Toast.LENGTH_SHORT).show();
    }

}
