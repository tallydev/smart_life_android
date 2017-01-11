package com.tallty.smart_life_android.fragment.me;


import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.Engine.Engine;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.event.ConfirmDialogEvent;
import com.tallty.smart_life_android.fragment.Pop.AddressDialogFragment;
import com.tallty.smart_life_android.model.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 绑定社区页面
 */
public class BindCommunityFragment extends BaseBackFragment {
    private int position;
    // UI
    private EditText area_edit;
    private EditText detail_edit;
    private TextView save_text;

    public static BindCommunityFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(Const.INT, position);
        BindCommunityFragment fragment = new BindCommunityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            position = args.getInt(Const.INT);
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_bind_community;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText("绑定社区");
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        area_edit = getViewById(R.id.community_area);
        detail_edit = getViewById(R.id.community_detail);
        save_text = getViewById(R.id.save_community);
    }

    @Override
    protected void setListener() {
        area_edit.setOnClickListener(this);
        detail_edit.setOnClickListener(this);
        save_text.setOnClickListener(this);
    }

    @Override
    protected void afterAnimationLogic() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_community:
                beginSave();
                break;
            case R.id.community_area:
                AddressDialogFragment fragment = AddressDialogFragment.newInstance("新建收货地址");
                fragment.show(getActivity().getFragmentManager(), "HintDialog");
                break;
            case R.id.community_detail:
                AddressDialogFragment fragment_one = AddressDialogFragment.newInstance("新建收货地址");
                fragment_one.show(getActivity().getFragmentManager(), "HintDialog");
                break;
        }
    }

    private void beginSave() {
        area_edit.setError(null);
        detail_edit.setError(null);

        String area = area_edit.getText().toString();
        String detail = detail_edit.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (detail.isEmpty()) {
            cancel = true;
            focusView = detail_edit;
            detail_edit.setError("请选择所在小区地址");
        }
        if (area.isEmpty()) {
            cancel = true;
            focusView = area_edit;
            area_edit.setError("请选择所在省市地区");
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            // 表单数据
            Map<String, String> fields = new HashMap<>();
            fields.put("user_info[community]", detail);
            // 更新用户信息
            updateUserCommunity(fields);
        }
    }

    private void updateUserCommunity(Map<String, String> fields) {
        showProgress("正在绑定...");
        Engine.noAuthService().updateUser(shared_token, shared_phone, fields)
            .enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    hideProgress();
                    if (response.isSuccessful()) {
                        Bundle bundle = new Bundle();
                        bundle.putString(RESULT_DATA, "修改了地区和地址");
                        bundle.putInt(RESULT_POSITION, position);
                        setFragmentResult(RESULT_OK, bundle);
                        // 隐藏软键盘
                        hideSoftInput();
                        pop();
                        showToast("绑定成功");
                    } else {
                        showToast("绑定失败,请重试");
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    hideProgress();
                    showToast(_mActivity.getString(R.string.network_error));
                }
            });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }


    /**
     * 接收事件: 选择小区地址
     * @param event
     */
    @Subscribe
    public void onConfirmDialogEvnet(ConfirmDialogEvent event) {
        event.dialog.dismiss();
//        edit_area.setText(event.data.getString("小区"));
//        contact.setArea(event.data.getString(Const.CONTACT_AREA));
//        contact.setStreet(event.data.getString(Const.CONTACT_STREET));
//        contact.setCommunity(event.data.getString(Const.CONTACT_COMMUNITY));
    }
}
