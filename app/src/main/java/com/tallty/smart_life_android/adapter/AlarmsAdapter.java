package com.tallty.smart_life_android.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tallty.smart_life_android.model.Alarm;

import java.util.List;

/**
 * Created by kang on 2017/3/15.
 * 电子猫眼适配器
 */

public class AlarmsAdapter extends BaseQuickAdapter<Alarm, BaseViewHolder> {

    public AlarmsAdapter(int layoutResId, List<Alarm> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Alarm alarm) {
        // TODO: 2017/3/15 处理适配器
    }
}
