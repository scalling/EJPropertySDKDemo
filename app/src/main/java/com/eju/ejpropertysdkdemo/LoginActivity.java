package com.eju.ejpropertysdkdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.eju.ejpropertysdkdemo.bean.LoginBody;
import com.eju.ejpropertysdkdemo.bean.LoginThirdBean;
import com.eju.housekeeper.app.ResponseErrorListenerImpl;
import com.eju.housekeeper.app.utils.RxUtils;
import com.eju.housekeeper.app.utils.ViewUtils;
import com.eju.housekeeper.app.widget.CleanEditText;
import com.eju.housekeeper.housekeeper.common.ui.dialog.LoadingDialog;
import com.eju.housekeeper.net.bean.BaseResp;
import com.eju.housekeeper.net.bean.LoginBean;
import com.eju.housekeeper.net.local.AppPreferences;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.IView;
import com.jess.arms.utils.ArmsUtils;
import com.zm.rb.view.RoundButton;

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
    CleanEditText etPwd;
    CleanEditText etAccount;
    RoundButton btnLogin;
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
        init();
        loadingDialog = new LoadingDialog(this);
        ViewUtils.setSolidColor(this, true, btnLogin, btnTest);
        rxErrorHandler = RxErrorHandler.builder()
                .with(this)
                .responseErrorListener(new ResponseErrorListenerImpl()).build();
        etAccount.setText(AppPreferences.getInstance().getLoginPhone());
        etPwd.setText(AppPreferences.getInstance().getLoginPassword());
        //测试账号
//        etAccount.setText("13693198391");
//        etPwd.setText("11111111");
        //第三方测试账号
//        etAccount.setText("18814188118");
////        etPwd.setText("Test1234");
    }

    private void init(){
        etPwd=findViewById(R.id.et_pwd);
        etAccount=findViewById(R.id.et_account);
        btnLogin=findViewById(R.id.btn_login);
        btnTest=findViewById(R.id.btn_test);
        btnLogin.setOnClickListener(v -> onLoginClick());
        btnTest.setOnClickListener(v -> onTestLoginClick());
    }
    private void onLoginClick(){
        LoginBody loginBean = new LoginBody();
        loginBean.account = etAccount.getText().toString();
        loginBean.password = etPwd.getText().toString();

        Observable.just(repositoryManager.obtainRetrofitService(ApiService.class).loginThrid(loginBean))
                .flatMap((Function<Observable<LoginThirdBean>, ObservableSource<LoginThirdBean>>) loginThirdBeanObservable -> loginThirdBeanObservable)
                .compose(RxUtils.applySchedulers(this))
                .subscribe(new ErrorHandleSubscriber<LoginThirdBean>(rxErrorHandler) {
                    @Override
                    public void onNext(LoginThirdBean bean) {
                        if (bean == null || TextUtils.isEmpty(bean.access_token)) {
                            ArmsUtils.snackbarText("登录失败");
                            return;
                        }
                        AppPreferences.getInstance().setLoginPhone(etAccount.getText().toString());
                        AppPreferences.getInstance().setLoginPassword(etPwd.getText().toString());
                        navMain("", bean.access_token, bean.member_id);
                    }
                });
    }

    private void onTestLoginClick(){
        LoginBody loginBean = new LoginBody();
        loginBean.loginPhone = etAccount.getText().toString();
        loginBean.password = etPwd.getText().toString();
        RxUtils.loadData(repositoryManager.obtainRetrofitService(ApiService.class).login(loginBean))
                .compose(RxUtils.applySchedulers(this))
                .subscribe(new ErrorHandleSubscriber<BaseResp<LoginBean>>(rxErrorHandler) {
                    @Override
                    public void onNext(BaseResp<LoginBean> loginBeanBaseResp) {
                        AppPreferences.getInstance().setLoginPhone(etAccount.getText().toString());
                        AppPreferences.getInstance().setLoginPassword(etPwd.getText().toString());
                        navMain(loginBeanBaseResp.data.token, "", "");
                    }
                });
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
        Observable<BaseResp<LoginBean>> login(@Body LoginBody body);

        @Headers({"Domain-Name: test"})
        @POST("/v2/corp_auth")
        Observable<LoginThirdBean> loginThrid(@Body LoginBody body);
    }
}
