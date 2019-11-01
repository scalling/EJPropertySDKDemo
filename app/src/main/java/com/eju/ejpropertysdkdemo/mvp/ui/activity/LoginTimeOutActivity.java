package com.eju.ejpropertysdkdemo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.eju.ejpropertysdkdemo.R;
import com.eju.housekeeper.commonsdk.net.local.AppPreferences;
import com.eju.housekeeper.commonsdk.utils.StatusBarUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import butterknife.OnClick;


public class LoginTimeOutActivity extends BaseActivity {

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        StatusBarUtils.setFullScreenStyle(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_login_time_out; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @OnClick(R.id.btn_confirm)
    void onClick() {
        onConfirm();
    }

    @OnClick(R.id.btn_cancel)
    void onClickCancel() {
        onCancel();
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        //即设定DecorView在PhoneWindow里的位置
        View view = getWindow().getDecorView();
        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
        lp.gravity = Gravity.LEFT | Gravity.TOP;
        lp.x = 0;
        lp.y = 0;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindowManager().updateViewLayout(view, lp);
    }


    public void onCancel() {
        AppPreferences.getInstance().signOut();
        ArmsUtils.killAll();
        ArmsUtils.exitApp();
    }

    public void onConfirm() {
        AppPreferences.getInstance().signOut();
        ArmsUtils.killAll();
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void onBack() {
        AppPreferences.getInstance().signOut();
        ArmsUtils.killAll();
        ArmsUtils.exitApp();
    }
}
