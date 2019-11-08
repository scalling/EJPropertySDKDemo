package com.eju.ejpropertysdkdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.eju.housekeeper.sdk.Navigation;
import com.eju.housekeeper.sdk.ThirdPartyManager;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnItemClick;


/**
 * @author : zengmei
 * @date : 2019/10/17
 * @description :测试activity
 */
public class MainActivity extends BaseActivity {
    @BindView(R.id.list)
    ListView list;
    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        String testToken = intent.getStringExtra("test_token");
        if (!TextUtils.isEmpty(testToken)) {
            //仅测试
            ThirdPartyManager.getInstance().test(testToken);
        } else {
            String accessToken = intent.getStringExtra("access_token");
            String communityId = intent.getStringExtra("community_id");
            String memberId = intent.getStringExtra("member_id");
            //正常调用
            ThirdPartyManager.getInstance()
                    .setAccessToken(accessToken) //第三方accessToken
                    .setCommunityId(communityId)//第三方小区id
                    .setMemberId(memberId);//第三方memberId
        }
        initAdapter();
    }

    private void initAdapter() {
        List<Map<String, Object>> datas = new ArrayList<>();
        datas.add(getItem("跳转工单管理", Navigation.WORK_ORDER_MAN));
        datas.add(getItem("跳转投诉表扬", Navigation.COMPLAINT_PRAISE_MAIN));
        datas.add(getItem("返回登录", 0));
        list.setAdapter(new SimpleAdapter(this, datas, R.layout.item, new String[]{"title"}, new int[]{R.id.btn_next}));
    }
    @OnItemClick(R.id.list)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, Object> infoMap = (Map<String, Object>) parent.getItemAtPosition(position);
        int nav = Integer.parseInt(Objects.requireNonNull(infoMap.get("nav")).toString());
        if (nav >0) {
            ThirdPartyManager.getInstance().navigation(nav);//跳转
        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private Map<String, Object> getItem(String title, int nav) {
        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("nav", nav);
        return map;
    }


}
