package com.tallty.smart_life_android.fragment.me;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.MyAppointmentsAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.custom.RecyclerVIewItemTouchListener;
import com.tallty.smart_life_android.model.Appointment;
import com.tallty.smart_life_android.model.AppointmentList;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 个人中心-我的预约
 */
public class MyAppointments extends BaseBackFragment {
    private RecyclerView recyclerView;
    private MyAppointmentsAdapter adapter;
    // 临时数据
    private String[] date = {"2016年7月1日", "2016年7月2日"};
    private String[] content = {"IT学堂", "社区活动"};
    private String[] state = {"未接受", "已接受"};
    // 数据
    private AppointmentList appointmentList = new AppointmentList();
    private List<Appointment> appointments = new ArrayList<>();

    public static MyAppointments newInstance() {
        Bundle args = new Bundle();

        MyAppointments fragment = new MyAppointments();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {

        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_my_appointments;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText("我的预约");
    }

    @Override
    protected void initView() {
        recyclerView = getViewById(R.id.appointments_list);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void afterAnimationLogic() {
        // 获取数据
        appointmentList.setCurrent_page(1);
        appointmentList.setTotal_pages(1);

        for (int i=0;i<date.length;i++){
            Appointment appointment = new Appointment();
            appointment.setDate(date[i]);
            appointment.setContent(content[i]);
            appointment.setState(state[i]);
            appointments.add(appointment);
        }
        appointmentList.setAppointments(appointments);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new MyAppointmentsAdapter(context, appointments);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerVIewItemTouchListener(recyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh, int position) throws ParseException {

            }

            @Override
            public void onItemLongPress(RecyclerView.ViewHolder vh, final int position) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(_mActivity, R.style.CustomAlertDialogTheme);
                builder.setMessage("确认删除吗")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showToast(""+position);
                                appointments.remove(position);
                                adapter.notifyItemRemoved(position);
                                adapter.notifyItemRangeChanged(position, appointments.size()-position);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
