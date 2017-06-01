package com.jscheng.mr_horse.wiget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.jscheng.mr_horse.R;
import com.jscheng.mr_horse.presenter.SearchPresenter;
import com.jscheng.mr_horse.utils.Constants;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 橙俊森 on 2017/6/1.
 */

public class SearchPopupView {
    private PopupWindow popupWindow = null;
    private SearchPresenter searchPresenter;

    public SearchPopupView(Context context, SearchPresenter searchPresenter) {
        View popupView = LayoutInflater.from(context).inflate(R.layout.search_popupview, null);
        ButterKnife.bind(this,popupView);
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        this.searchPresenter = searchPresenter;
    }

    public void showAsDropDown(View belowView) {
//        popupWindow.showAsDropDown(belowView,-belowView.getWidth()-25,30);
        popupWindow.showAsDropDown(belowView);
    }

    public boolean isShowing() {
        return popupWindow != null && popupWindow.isShowing();
    }

    public void dismiss() {
        if (popupWindow != null)
            popupWindow.dismiss();
    }

    @OnClick(R.id.search_popup_default)
    public void onClickDefaultLayout(){
        searchPresenter.onClickChooseLayout(Constants.DEFAULT_CHOOSE);
        dismiss();
    }

    @OnClick(R.id.search_popup_mzdsx)
    public void onClickMzdsxLayout(){
        searchPresenter.onClickChooseLayout(Constants.MZDSX);
        dismiss();
    }

    @OnClick(R.id.search_popup_sxdd)
    public void onClickSxddLayout(){
        searchPresenter.onClickChooseLayout(Constants.SXDD);
        dismiss();
    }

    @OnClick(R.id.search_popup_fljc)
    public void onClickFljcLayout(){
        searchPresenter.onClickChooseLayout(Constants.FLJC);
        dismiss();
    }

    @OnClick(R.id.search_popup_mkszy)
    public void onClickMkszyLayout(){
        searchPresenter.onClickChooseLayout(Constants.MKSZY);
        dismiss();
    }

    @OnClick(R.id.search_popup_zgjds)
    public void onClickZgjdsLayout(){
        searchPresenter.onClickChooseLayout(Constants.ZGJDS);
        dismiss();
    }
}
