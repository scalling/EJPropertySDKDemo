package com.eju.ejpropertysdkdemo.di.module;

import com.eju.ejpropertysdkdemo.mvp.contract.LoginContract;
import com.eju.ejpropertysdkdemo.mvp.model.LoginModel;

import dagger.Binds;
import dagger.Module;



@Module
public abstract class LoginModule {

    @Binds
    abstract LoginContract.Model bindLoginModel(LoginModel model);
}