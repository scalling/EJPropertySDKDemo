package com.eju.ejpropertysdkdemo.mvp.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.eju.ejpropertysdkdemo.R;
import com.eju.ejpropertysdkdemo.mvp.model.bean.MainBean;
import com.eju.housekeeper.commonres.utils.ViewUtils;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.zm.rb.view.RoundButton;

import java.util.List;

import butterknife.BindView;

public class MainAdapter extends DefaultAdapter<MainBean> {
    public MainAdapter(List<MainBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<MainBean> getHolder(View v, int viewType) {
        return new ViewHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item;
    }

    class ViewHolder extends BaseHolder<MainBean> {
        @BindView(R.id.btn_next)
        RoundButton btnNext;

        public ViewHolder(View itemView) {
            super(itemView);
            ViewUtils.setSolidColor(itemView.getContext(),true,btnNext);
        }

        @Override
        public void setData(MainBean data, int position) {
            btnNext.setText(data.name);
        }
    }

}
