package com.eju.ejpropertysdkdemo.mvp.presenter;

import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;

import com.eju.ejpropertysdkdemo.MainActivity;
import com.eju.ejpropertysdkdemo.mvp.contract.LoginContract;
import com.eju.ejpropertysdkdemo.mvp.model.bean.LoginThirdBean;
import com.eju.ejpropertysdkdemo.mvp.ui.activity.LoginTestActivity;
import com.eju.housekeeper.commonsdk.net.bean.BaseResp;
import com.eju.housekeeper.commonsdk.net.bean.LoginBean;
import com.eju.housekeeper.commonsdk.utils.RxUtils;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 10/31/2019 15:58
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class LoginPresenter extends BasePresenter<LoginContract.Model, LoginContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public LoginPresenter(LoginContract.Model model, LoginContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    public void login(String account, String pwd) {
        mModel.login(account, pwd)
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseResp<LoginBean>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResp<LoginBean> loginBeanBaseResp) {
                        ArmsUtils.snackbarText("登录成功");
                        Intent intent = new Intent(mApplication, MainActivity.class);
                        intent.putExtra("test_token", loginBeanBaseResp.data.token);
                        mRootView.launchActivity(intent);
                        mRootView.killMyself();
                    }
                });
    }

    public void loginThird(String account, String pwd, String communityId) {
        mModel.loginThird(account, pwd)
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<LoginThirdBean>(mErrorHandler) {
                    @Override
                    public void onNext(LoginThirdBean bean) {
                        if (bean == null || TextUtils.isEmpty(bean.access_token)) {
                            ArmsUtils.snackbarText("登录失败");
                            return;
                        }
                        Intent intent = new Intent(mApplication, MainActivity.class);
                        intent.putExtra("access_token", bean.access_token);
                        intent.putExtra("community_id", communityId);
                        intent.putExtra("member_id", bean.member_id);
                        mRootView.launchActivity(intent);
                        mRootView.killMyself();


                    }
                });
    }


    public void onTestLogin() {
        Intent intent = new Intent(mApplication, LoginTestActivity.class);
        mRootView.launchActivity(intent);
    }

}
