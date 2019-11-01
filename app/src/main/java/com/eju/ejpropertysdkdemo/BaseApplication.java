package com.eju.ejpropertysdkdemo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.eju.ejpropertysdkdemo.mvp.ui.activity.LoginTimeOutActivity;
import com.eju.housekeeper.sdk.SdkAppDelegate;
import com.eju.housekeeper.sdk.ThirdPartyManager;
import com.jess.arms.base.App;
import com.jess.arms.di.component.AppComponent;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import timber.log.Timber;

/**
 * @author : zengmei
 * @date : 2019/10/17
 * @description : 继承Application
 */
public class BaseApplication extends Application implements App {
    private SdkAppDelegate mAppDelegate;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (mAppDelegate == null)
            this.mAppDelegate = new SdkAppDelegate(this);
        this.mAppDelegate.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ThirdPartyManager.openLog();//打开日志 正式环境下可以不打开
        if (mAppDelegate != null)
            this.mAppDelegate.onCreate();
        initThirdPartyManager();


        RetrofitUrlManager.getInstance().putDomain("test", "http://39.98.98.227");
    }


    private void initThirdPartyManager() {
        //工具进行初始化
        ThirdPartyManager.init(this, "10000000")
                .setThemeColor("#009d8d")//主题颜色
                .setTimeOutInterface(() -> {
                    Timber.e("APP登录过期相关操作");
                    timeOut();
                });
    }

    private void timeOut() {
        Intent intent = new Intent(this, LoginTimeOutActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 在模拟环境中程序终止时会被调用
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mAppDelegate != null)
            this.mAppDelegate.onTerminate();
    }

    @NonNull
    @Override
    public AppComponent getAppComponent() {
        return mAppDelegate.getAppComponent();
    }
}
