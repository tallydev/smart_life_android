package com.tallty.smart_life_android.fragment.me;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.ProfileListAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * 个人中心-个人资料
 */
public class ProfileFragment extends BaseBackFragment {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private RecyclerView recyclerView;
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

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ProfileListAdapter adapter = new ProfileListAdapter(context, keys, values);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }
}
