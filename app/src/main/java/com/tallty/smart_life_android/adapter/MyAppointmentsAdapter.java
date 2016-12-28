package com.tallty.smart_life_android.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.model.Appointment;

import java.util.List;

/**
 * Created by kang on 16/7/28.
 * 账户管理-我的预约
 */
public class MyAppointmentsAdapter extends BaseQuickAdapter<Appointment, BaseViewHolder> {
    public MyAppointmentsAdapter(int layoutResId, List<Appointment> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Appointment appointment) {
        baseViewHolder.setText(R.id.appointment_time, "预约时间: "+appointment.getDate())
                .setText(R.id.appointment_content, "预约内容: "+ Const.APPOINTMENT_TYPES.get(appointment.getAppointmentType()))
                .setText(R.id.appointment_state, appointment.getStateAlias());
    }
}