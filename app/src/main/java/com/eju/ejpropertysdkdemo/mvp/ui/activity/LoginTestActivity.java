package com.eju.ejpropertysdkdemo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.eju.ejpropertysdkdemo.R;
import com.eju.ejpropertysdkdemo.di.component.DaggerLoginComponent;
import com.eju.ejpropertysdkdemo.mvp.contract.LoginContract;
import com.eju.ejpropertysdkdemo.mvp.presenter.LoginPresenter;
import com.eju.housekeeper.commonres.mvp.ui.dialog.LoadingDialog;
import com.eju.housekeeper.commonres.utils.ViewUtils;
import com.eju.housekeeper.commonres.widget.CleanEditText;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.AppManager;
import com.jess.arms.utils.ArmsUtils;
import com.zm.rb.view.RoundButton;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * @author : zengmei
 * @date : 2019/10/31
 * @description : 测试登录
 */
public class LoginTestActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {
    private LoadingDialog loadingDialog;
    @BindView(R.id.et_pwd)
    CleanEditText etPwd;
    @BindView(R.id.et_account)
    CleanEditText etAccount;
    @BindView(R.id.btn_login)
    RoundButton btnLogin;

    @BindView(R.id.view_community)
    View viewCommunity;
    @BindView(R.id.btn_test)
    View btnTest;


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLoginComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_login; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("测试登录");
        etAccount.setText("13693198391");
        etPwd.setText("11111111");
        loadingDialog = new LoadingDialog(this);
        ViewUtils.setSolidColor(this, true, btnLogin);
        ViewUtils.setViewsGone(viewCommunity, btnTest);
    }

    @Override
    public void showLoading() {
        loadingDialog.show();
    }

    @Override
    public void hideLoading() {
        loadingDialog.dismiss();
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
        AppManager.getAppManager().killActivity(LoginActivity.class);
    }

    @OnClick(R.id.btn_login)
    public void onClick() {
        mPresenter.login(etAccount.getText().toString(), etPwd.getText().toString());
    }
}
