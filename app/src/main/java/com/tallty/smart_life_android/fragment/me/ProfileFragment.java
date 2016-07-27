package com.tallty.smart_life_android.fragment.me;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.ProfileListAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.custom.RecyclerVIewItemTouchListener;
import com.tallty.smart_life_android.event.StartBrotherEvent;
import com.tallty.smart_life_android.fragment.cart.MyAddress;
import com.tallty.smart_life_android.model.Address;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 个人中心-个人资料
 */
public class ProfileFragment extends BaseBackFragment {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private RecyclerView recyclerView;
    private ProfileListAdapter adapter;
    // 数据
    private ArrayList<String> keys = new ArrayList<String>(){
        {
            add("头像");add("昵称");add("登录手机号");add("出生日期");add("性别");add("个性签名");
            add("身份证号");add("收货地址");add("变更绑定手机号");add("设置支付密码");
        }
    };
    private ArrayList<String> values = new ArrayList<String>(){
        {
            add("http://img0.pconline.com.cn/pconline/1312/27/4072897_01_thumb.gif");add("Stark");
            add("13816000000");add("1992-05-03");add("男");add("未设置");add("310112199205031234");
            add("");add("15316788888");add("");
        }
    };
    // 弹框
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;

    public static ProfileFragment newInstance() {
        Bundle args = new Bundle();

        ProfileFragment fragment = new ProfileFragment();
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
        return R.layout.fragment_profile;
    }

    @Override
    protected void initView() {
        toolbar = getViewById(R.id.toolbar);
        toolbar_title = getViewById(R.id.toolbar_title);
        recyclerView = getViewById(R.id.profile_list);
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void afterAnimationLogic() {
        initBackToolbar(toolbar);
        toolbar_title.setText("账户管理");
        processRecyclerView();
    }

    @Override
    public void onClick(View v) {

    }

    private void processRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ProfileListAdapter(context, keys, values);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerVIewItemTouchListener(recyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh, final int position) {
                if (position == 0){
                    // 修改头像
                    final String[] kind = new String[]{"拍照", "相册"};
                    alert = null;
                    builder = new AlertDialog.Builder(getActivity());
                    alert = builder.setTitle("修改头像")
                            .setItems(kind, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    showToast("选择"+kind[which]);
                                }
                            }).create();
                    alert.show();
                } else if (position == 2) {
                    // 登录手机号
                    showToast("可通过绑定手机号修改");
                } else if (position == 3) {
                    // 修改生日
                    // monthOfYear: 0-11
                    // SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    // Date date = simpleDateFormat.parse(values.get(position));

                    Calendar calendar = Calendar.getInstance();

                    Dialog dateDialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    String date = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                                    values.set(position, date);
                                    adapter.notifyItemChanged(position);
                                }
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)

                    );
                    dateDialog.show();
                } else if (position == 4) {
                    // 修改性别
                    final String[] sex = new String[]{"男", "女"};
                    alert = null;
                    builder = new AlertDialog.Builder(getActivity());
                    alert = builder.setTitle("修改性别")
                            .setItems(sex, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    values.set(position, sex[which]);
                                    adapter.notifyItemChanged(position);
                                }
                            }).create();
                    alert.show();
                } else if (position == 7) {
                  EventBus.getDefault().post(new StartBrotherEvent(MyAddress.newInstance(FROM_PROFILE)));
                } else if (position == 8) {
                    // 跳转修改页面
                    startForResult(BindPhoneFragment.newInstance(
                            keys.get(position), values.get(position), position), REQ_CODE);
                } else {
                    // 跳转修改页面
                    startForResult(ChangeProfileFragment.newInstance(
                            keys.get(position), values.get(position), position), REQ_CODE);
                }
            }

            @Override
            public void onItemLongPress(RecyclerView.ViewHolder vh, int position) {

            }
        });
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == RESULT_YES) {
            // 显示
            String text = data.getString(RESULT_DATA);
            int position = data.getInt(RESULT_POSITION);
            values.set(position, text);
            adapter.notifyItemChanged(position);
        }
    }
}
