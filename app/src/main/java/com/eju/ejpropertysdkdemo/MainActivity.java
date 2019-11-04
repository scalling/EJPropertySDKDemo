package com.eju.ejpropertysdkdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

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


    String testToken;

    String accessToken;
    String communityId;
    String memberId;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        testToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxN3NoaWh1aS5jb20iLCJzdWIiOiJBVVRIRU5USUNBVElPTl9KV1QiLCJpc3MiOiJBVVRIX1NFUlZFUiIsImlhdCI6MTU3MTY0Mzg4NiwiZXhwIjoxNTc0MzIyMjg2LCJqdGkiOiJiZWYzYjZjYS1iNGFiLTRlOGMtYWJjNC05OWZkOTAwYjFhYjAiLCJ1aWQiOjQ1MDV9.mPFonW5GQy54THbViOVSF1oMwlSlLuDO-hAg9w2P8Sw";
        accessToken = "";
        communityId = "";
        memberId = "";
        rvList.setLayoutManager(new LinearLayoutManager(this));
        MainAdapter adapter = new MainAdapter(getData());
        adapter.setOnItemClickListener((DefaultAdapter.OnRecyclerViewItemClickListener<MainBean>) (view, viewType, data, position) -> {
            nav(data.ruterHub);
        });
        rvList.setAdapter(adapter);
    }


    //跳转
    private void nav(String ruterHub) {
        if (!TextUtils.isEmpty(accessToken)) {
            ThirdPartyManager.getInstance()
                    //第三方accessToken
                    .setAccessToken(accessToken)
                    .setCommunityId(communityId)//第三方小区id
                    .setMemberId(memberId);
        } else {
            ThirdPartyManager.getInstance().test(testToken);
        }
        ThirdPartyManager.getInstance().navigation(ruterHub);//跳转

    }

    private List<MainBean> getData() {
        List<MainBean> items = new ArrayList<>();
        items.add(new MainBean("工单管理", RouterHub.WORK_ORDER_MAIN));
        items.add(new MainBean("投诉表扬", RouterHub.COMPLAINT_PRAISE_MAIN));
        return items;
    }


}
