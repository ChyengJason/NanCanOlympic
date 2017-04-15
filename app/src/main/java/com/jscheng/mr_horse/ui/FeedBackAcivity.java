package com.jscheng.mr_horse.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jscheng.mr_horse.R;
import com.jscheng.mr_horse.http.FeedBackService;
import com.jscheng.mr_horse.utils.Constants;
import com.jscheng.mr_horse.utils.OSUtil;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cheng on 17-2-7.
 */
public class FeedBackAcivity extends BaseActivity{
    @BindView(R.id.title_text)
    TextView titleView;
    @BindView(R.id.feed_back_edit)
    EditText feedBackEdit;
    @BindView(R.id.feed_back_contact)
    EditText feedBackContact;
    @BindView(R.id.confirm)
    Button confirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        titleView.setText(getString(R.string.feed_back));
        feedBackContact.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
    }

    @OnClick(R.id.title_back)
    public void onTitleBack(){
        finish();
    }

    @OnClick(R.id.confirm)
    public void onClickConfirm(){
        if(feedBackEdit.getText().toString().isEmpty() || feedBackEdit.getText().toString().equals("")){
            Toast.makeText(this,getString(R.string.feed_back_content_not_null),Toast.LENGTH_SHORT).show();
            return;
        }
        String contact = feedBackContact.getText().toString();
        if (contact == null && contact.isEmpty())
            contact = "";
        String content = feedBackEdit.getText().toString();
        addFeedback(contact,content);
    }

    private void addFeedback(String contact , String content){
        Logger.w(content);
        try{
            String pkName = this.getPackageName();
            String versionName = this.getPackageManager().getPackageInfo(pkName,0).versionName;
            String versionCode = this.getPackageManager().getPackageInfo(pkName,0).versionCode+"";
            FeedBackService.getFeedBackApi().addFeedBack(OSUtil.getMobileInfo() + " " + contact , content,
                    Constants.FEEDBACK_KEY_ID, getString(R.string.app_name), versionName, versionCode)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<String>() {
                        public void onStart(){
                            confirm.setClickable(false);
                        }

                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            Logger.e(e.toString());
                            confirm.setClickable(true);
                        }

                        @Override
                        public void onNext(String s) {
                            Toast.makeText(FeedBackAcivity.this,getString(R.string.thanks_for_your_advise),Toast.LENGTH_SHORT).show();
                            confirm.setClickable(true);
                            feedBackEdit.setText("");
                            feedBackContact.setText("");
                            View v = getCurrentFocus();
                            InputMethodManager imm = (InputMethodManager)
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    });
        }catch (Exception e){
            Logger.e(e.toString());
        }
    }
}
