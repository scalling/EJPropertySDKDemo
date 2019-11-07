package com.eju.ejpropertysdkdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.eju.housekeeper.app.ResponseErrorListenerImpl;
import com.eju.housekeeper.app.utils.RxUtils;
import com.eju.housekeeper.app.utils.ViewUtils;
import com.eju.housekeeper.app.widget.CleanEditText;
import com.eju.housekeeper.housekeeper.common.ui.dialog.LoadingDialog;
import com.eju.housekeeper.net.bean.BaseResp;
import com.eju.housekeeper.net.bean.LoginBean;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.IView;
import com.jess.arms.utils.ArmsUtils;
import com.zm.rb.view.RoundButton;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * @author : zengmei
 * @date : 2019/10/31
 * @description : 测试登录
 */
public class LoginActivity extends BaseActivity implements IView {
    private LoadingDialog loadingDialog;
    @BindView(R.id.et_pwd)
    CleanEditText etPwd;
    @BindView(R.id.et_account)
    CleanEditText etAccount;
    @BindView(R.id.btn_login)
    RoundButton btnLogin;
    @BindView(R.id.btn_test)
    RoundButton btnTest;
    private RxErrorHandler rxErrorHandler;

    private IRepositoryManager repositoryManager;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        repositoryManager = appComponent.repositoryManager();
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_login; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        loadingDialog = new LoadingDialog(this);
        ViewUtils.setSolidColor(this, true, btnLogin, btnTest);
        rxErrorHandler = RxErrorHandler.builder()
                .with(this)
                .responseErrorListener(new ResponseErrorListenerImpl()).build();
        //测试账号
//        etAccount.setText("13693198391");
//        etPwd.setText("11111111");


        //第三方测试账号
        //etAccount.setText("18814188118");
        //etPwd.setText("Test1234");
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
        ArmsUtils.makeText(this, message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @OnClick(R.id.btn_login)
    public void onClick() {
        Map<String, String> loginBody = new HashMap<>();
        loginBody.put("account", etAccount.getText().toString());
        loginBody.put("password", etPwd.getText().toString());
        Observable.just(repositoryManager.obtainRetrofitService(ApiService.class).loginThrid(loginBody))
                .flatMap((Function<Observable<LoginThirdBean>, ObservableSource<LoginThirdBean>>) loginThirdBeanObservable -> loginThirdBeanObservable)
                .compose(RxUtils.applySchedulers(this))
                .subscribe(new ErrorHandleSubscriber<LoginThirdBean>(rxErrorHandler) {
                    @Override
                    public void onNext(LoginThirdBean bean) {
                        if (bean == null || TextUtils.isEmpty(bean.access_token)) {
                            ArmsUtils.snackbarText("登录失败");
                            return;
                        }
                        navMain("", bean.access_token, bean.member_id);
                    }
                });
    }

    @OnClick(R.id.btn_test)
    public void onClickTest() {
        Map<String, String> loginBody = new HashMap<>();
        loginBody.put("loginPhone", etAccount.getText().toString());
        loginBody.put("password", etPwd.getText().toString());
        RxUtils.loadData(repositoryManager.obtainRetrofitService(ApiService.class).login(loginBody))
                .compose(RxUtils.applySchedulers(this))
                .subscribe(new ErrorHandleSubscriber<BaseResp<LoginBean>>(rxErrorHandler) {
                    @Override
                    public void onNext(BaseResp<LoginBean> loginBeanBaseResp) {
                        navMain(loginBeanBaseResp.data.token, "", "");
                    }
                });
    }

    private void navMain(String testToken, String access_token, String member_id) {
        ArmsUtils.snackbarText("登录成功");
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        if (TextUtils.isEmpty(testToken)) {
            intent.putExtra("access_token", access_token);
            intent.putExtra("community_id", "65a3a176b6ab8c3d57cce31038e78ba2");
            intent.putExtra("member_id", member_id);
        } else {
            intent.putExtra("test_token", testToken);
        }
        launchActivity(intent);
        killMyself();
    }

    //登录
    interface ApiService {
        @POST("/user/manage/login")
        Observable<BaseResp<LoginBean>> login(@Body Map<String, String> body);

        @Headers({"Domain-Name: test"})
        @POST("/v2/corp_auth")
        Observable<LoginThirdBean> loginThrid(@Body Map<String, String> body);
    }
}
