package com.eju.ejpropertysdkdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.eju.housekeeper.commonres.widget.CleanEditText;
import com.eju.housekeeper.sdk.ThirdPartyManager;


/**
 * @author : zengmei
 * @date : 2019/10/17
 * @description :测试activity
 */
public class MainActivity extends AppCompatActivity {
    CleanEditText etToken;
    CleanEditText etMemberId;
    CleanEditText etCommunityId;
    CleanEditText etTestToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etToken = (CleanEditText) findViewById(R.id.et_token);
        etMemberId = (CleanEditText) findViewById(R.id.et_member_id);
        etCommunityId = (CleanEditText) findViewById(R.id.et_community_id);
        etTestToken = (CleanEditText) findViewById(R.id.et_test_token);
        etTestToken.setText("eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxN3NoaWh1aS5jb20iLCJzdWIiOiJBVVRIRU5USUNBVElPTl9KV1QiLCJpc3MiOiJBVVRIX1NFUlZFUiIsImlhdCI6MTU3MTY0Mzg4NiwiZXhwIjoxNTc0MzIyMjg2LCJqdGkiOiJiZWYzYjZjYS1iNGFiLTRlOGMtYWJjNC05OWZkOTAwYjFhYjAiLCJ1aWQiOjQ1MDV9.mPFonW5GQy54THbViOVSF1oMwlSlLuDO-hAg9w2P8Sw");
        etCommunityId.setText("65a3a176b6ab8c3d57cce31038e78ba2");
        etMemberId.setText("12000ebb1b697400");
        etToken.setText("MjFBNzBDRjg0MDg5MzhDNDRGRTgzNUM1RENBRkE2NEM2NzI3NzY2OUFEMjFFRUE0Q0QxMDdEQTQ5NUE1RTlCRg==");
        findViewById(R.id.btn_start).setOnClickListener(onClickListener);
        findViewById(R.id.btn_test_token).setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_test_token:
                    onTestStart();
                    break;
                case R.id.btn_start:
                    navigation();
                    break;
            }
        }
    };

    private void onTestStart() {
        ThirdPartyManager.getInstance()
                //进行测试的token 无需进行测试请不用调用次方法
                .test(etTestToken.getText().toString())
                .navigation();//跳转
    }

    private void navigation() {
        ThirdPartyManager.getInstance()
                //第三方accessToken
                .setAccessToken(etToken.getText().toString())
                .setCommunityId(etCommunityId.getText().toString())//第三方小区id
                .setMemberId(etMemberId.getText().toString())//第三方memberId
                .navigation();//跳转
    }
}
