package com.eju.ejpropertysdkdemo.mvp.model;

import android.app.Application;

import com.eju.ejpropertysdkdemo.mvp.contract.LoginContract;
import com.eju.ejpropertysdkdemo.mvp.model.bean.LoginBody;
import com.eju.ejpropertysdkdemo.mvp.model.bean.LoginThirdBean;
import com.eju.ejpropertysdkdemo.mvp.model.bean.LoginThirdBody;
import com.eju.housekeeper.commonsdk.net.bean.BaseResp;
import com.eju.housekeeper.commonsdk.net.bean.LoginBean;
import com.eju.housekeeper.commonsdk.utils.RxUtils;
import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;


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
public class LoginModel extends BaseModel implements LoginContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public LoginModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<BaseResp<LoginBean>> login(String loginPhone, String password) {
        LoginBody loginBody = new LoginBody();
        loginBody.loginPhone = loginPhone;
        loginBody.password = password;
        return RxUtils.loadData(mRepositoryManager.obtainRetrofitService(ApiService.class).login(loginBody));
    }

    @Override
    public Observable<LoginThirdBean> loginThird(String loginPhone, String password) {
        LoginThirdBody loginBody = new LoginThirdBody();
        loginBody.account = loginPhone;
        loginBody.password = password;
        return Observable.just(mRepositoryManager.obtainRetrofitService(ApiService.class).loginThrid(loginBody))
                .flatMap((Function<Observable<LoginThirdBean>, ObservableSource<LoginThirdBean>>) loginThirdBeanObservable -> loginThirdBeanObservable);
    }
}