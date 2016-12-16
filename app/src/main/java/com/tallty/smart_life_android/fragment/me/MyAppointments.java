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

import com.tallty.smart_life_android.Engine.Engine;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.MyAppointmentsAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.custom.RecyclerVIewItemTouchListener;
import com.tallty.smart_life_android.model.Appointment;
import com.tallty.smart_life_android.model.AppointmentList;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 个人中心-我的预约
 */
public class MyAppointments extends BaseBackFragment {
    private RecyclerView recyclerView;
    private MyAppointmentsAdapter adapter;
    // 数据
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
        showProgress(showString(R.string.progress_normal));
        Engine.authService(shared_token, shared_phone).getAppointments(1, 10).enqueue(new Callback<AppointmentList>() {
            @Override
            public void onResponse(Call<AppointmentList> call, Response<AppointmentList> response) {
                if (response.isSuccessful()) {
                    appointments.addAll(response.body().getAppointments());
                    // 加载列表
                    setList();
                    hideProgress();
                } else {
                    hideProgress();
                    showToast(showString(R.string.response_error));
                }
            }

            @Override
            public void onFailure(Call<AppointmentList> call, Throwable t) {
                hideProgress();
                showToast(showString(R.string.network_error));
            }
        });
    }

    private void setList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new MyAppointmentsAdapter(_mActivity, appointments);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerVIewItemTouchListener(recyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh, int position) throws ParseException {

            }

            @Override
            public void onItemLongPress(RecyclerView.ViewHolder vh, final int position) {
                confirmDialog("确认删除吗", new OnConfirmDialogListener() {
                    @Override
                    public void onConfirm(DialogInterface dialog, int which) {
                        appointments.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, appointments.size()-position);
                    }

                    @Override
                    public void onCancel(DialogInterface dialog, int which) {

                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
