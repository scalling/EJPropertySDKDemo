package com.eju.ejpropertysdkdemo.mvp.model;

import com.eju.ejpropertysdkdemo.mvp.model.bean.LoginBody;
import com.eju.ejpropertysdkdemo.mvp.model.bean.LoginThirdBean;
import com.eju.ejpropertysdkdemo.mvp.model.bean.LoginThirdBody;
import com.eju.housekeeper.commonsdk.net.bean.BaseResp;
import com.eju.housekeeper.commonsdk.net.bean.LoginBean;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/user/manage/login")
    Observable<BaseResp<LoginBean>> login(@Body LoginBody body);

    @Headers({"Domain-Name: test"})
    @POST("/v2/corp_auth")
    Observable<LoginThirdBean> loginThrid(@Body LoginThirdBody body);
}
