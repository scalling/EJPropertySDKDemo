package com.eju.ejpropertysdkdemo.di.component;

import com.eju.ejpropertysdkdemo.di.module.LoginModule;
import com.eju.ejpropertysdkdemo.mvp.contract.LoginContract;
import com.eju.ejpropertysdkdemo.mvp.ui.activity.LoginActivity;
import com.eju.ejpropertysdkdemo.mvp.ui.activity.LoginTestActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.BindsInstance;
import dagger.Component;



@ActivityScope
@Component(modules = LoginModule.class, dependencies = AppComponent.class)
public interface LoginComponent {
    void inject(LoginTestActivity activity);
    void inject(LoginActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        LoginComponent.Builder view(LoginContract.View view);

        LoginComponent.Builder appComponent(AppComponent appComponent);

        LoginComponent build();
    }
}