package com.jscheng.mr_horse.ui;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
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
    @BindView(R.id.app_email)
    TextView app_email;
    @BindView(R.id.app_image)
    ImageView app_image;
    @BindView(R.id.app_resource)
    TextView app_resource;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        app_resource.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
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

    @OnClick(R.id.app_email)
    public void onClickEmail(){
        copyToBoard();
    }

    public void copyToBoard(){
        ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("email",getResources().getString(R.string.person_email));
        cmb.setPrimaryClip(data);
        Toast.makeText(this,R.string.copied_to_board, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.app_resource)
    public void onClickResource(){
        Uri uri = Uri.parse(getResources().getString(R.string.resource_address));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
