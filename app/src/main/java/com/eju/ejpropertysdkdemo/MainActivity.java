package com.eju.ejpropertysdkdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.eju.ejpropertysdkdemo.mvp.model.bean.MainBean;
import com.eju.ejpropertysdkdemo.mvp.ui.activity.LoginActivity;
import com.eju.ejpropertysdkdemo.mvp.ui.adapter.MainAdapter;
import com.eju.housekeeper.commonsdk.core.RouterHub;
import com.eju.housekeeper.sdk.ThirdPartyManager;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * @author : zengmei
 * @date : 2019/10/17
 * @description :测试activity
 */
public class MainActivity extends BaseActivity {


    @BindView(R.id.rv_list)
    RecyclerView rvList;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("跳转");
        findViewById(R.id.toolbar_back).setVisibility(View.GONE);

        Intent intent = getIntent();
        String testToken = intent.getStringExtra("test_token");
        if (!TextUtils.isEmpty(testToken)) {
            ThirdPartyManager.getInstance().test(testToken);
        } else {
            String accessToken = intent.getStringExtra("access_token");
            String communityId = intent.getStringExtra("community_id");
            String memberId = intent.getStringExtra("member_id");
            ThirdPartyManager.getInstance()
                    //第三方accessToken
                    .setAccessToken(accessToken)
                    .setCommunityId(communityId)//第三方小区id
                    .setMemberId(memberId);
        }

        initAdapter();
    }

    private void initAdapter() {
        rvList.setLayoutManager(new LinearLayoutManager(this));
        MainAdapter adapter = new MainAdapter(getData());
        adapter.setOnItemClickListener((DefaultAdapter.OnRecyclerViewItemClickListener<MainBean>) (view, viewType, data, position) -> {
            if (TextUtils.isEmpty(data.ruterHub)) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                ThirdPartyManager.getInstance().navigation(data.ruterHub);//跳转
            }

        });
        rvList.setAdapter(new MainAdapter(getData()));
    }

    private List<MainBean> getData() {
        List<MainBean> datas = new ArrayList<>();
        datas.add(new MainBean("工单管理", RouterHub.WORK_ORDER_MAIN));
        datas.add(new MainBean("投诉表扬", RouterHub.COMPLAINT_PRAISE_MAIN));
        datas.add(new MainBean("重新登录", ""));
        return datas;
    }


}
