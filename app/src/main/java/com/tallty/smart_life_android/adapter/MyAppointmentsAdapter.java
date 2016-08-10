package com.tallty.smart_life_android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.model.Appointment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kang on 16/7/28.
 * 账户管理-我的预约
 */

public class MyAppointmentsAdapter extends RecyclerView.Adapter<MyAppointmentsAdapter.MyAppointmentsViewHolder> {
    private Context context;
    private List<Appointment> appointments = new ArrayList<>();

    public MyAppointmentsAdapter(Context context, List<Appointment> appointments){
        this.context = context;
        this.appointments = appointments;
    }

    @Override
    public MyAppointmentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyAppointmentsViewHolder(LayoutInflater
                .from(context).inflate(R.layout.item_my_appointments, parent, false));
    }

    @Override
    public void onBindViewHolder(MyAppointmentsViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);
        holder.dateTime.setText("预约时间: "+appointment.getDate());
        holder.content.setText("预约内容: "+ Const.APPOINTMENT_TYPES.get(appointment.getAppointmentType()));
        holder.state.setText(appointment.getStateAlias());
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    class MyAppointmentsViewHolder extends RecyclerView.ViewHolder {
        private TextView dateTime;
        private TextView content;
        private TextView state;

        MyAppointmentsViewHolder(View itemView) {
            super(itemView);
            dateTime = (TextView) itemView.findViewById(R.id.appointment_time);
            content = (TextView) itemView.findViewById(R.id.appointment_content);
            state = (TextView) itemView.findViewById(R.id.appointment_state);
        }
    }
}
