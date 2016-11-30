package com.tallty.smart_life_android.fragment.me;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.Engine.Engine;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.model.User;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * 账户管理-修改信息
 */
public class ChangeProfileFragment extends BaseBackFragment {
    // 修改项
    private String key;
    private String value;
    private int position;
    // UI
    private EditText change_input;
    private TextView change_tips;
    private Button change_btn;

    public static ChangeProfileFragment newInstance(String key, String value, int position) {
        Bundle args = new Bundle();
        args.putString("修改项", key);
        args.putString("值", value);
        args.putInt("位置", position);
        ChangeProfileFragment fragment = new ChangeProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            key = args.getString("修改项");
            value = args.getString("值");
            position = args.getInt("位置");
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_change_profile;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText(""+ key);
    }

    @Override
    protected void initView() {
        change_input = getViewById(R.id.change_input);
        change_tips = getViewById(R.id.change_tips);
        change_btn = getViewById(R.id.change_btn);
    }

    @Override
    protected void setListener() {
        change_btn.setOnClickListener(this);
    }

    @Override
    protected void afterAnimationLogic() {
        change_input.setText(value);
        change_input.setHint("请输入"+key);
        if (!"昵称".equals(key)) {
            change_tips.setText(null);
        } else if ("设置支付密码".equals(key)) {
            change_tips.setText("密码长度不小于8位");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_btn:
                change_input.setError(null);
                String text = change_input.getText().toString();
                boolean begin = true;

                if (text.isEmpty()) {
                    change_input.setError("请填写信息");
                    change_input.requestFocus();
                    begin = false;
                } else {
                    if ("身份证号".equals(key) && text.length() != 18) {
                        change_input.setError("身份证号码格式错误");
                        change_input.requestFocus();
                        begin = false;
                    } else if ("设置支付密码".equals(key) && text.length() < 8) {
                        change_input.setError("密码长度小于8位");
                        change_input.requestFocus();
                        begin = false;
                    }
                }

                if (begin) {
                    // 调用接口修改数据
                    updateUser(text);
                }

                break;
        }
    }

    private void updateUser(final String text) {
        change_btn.setClickable(false);
        showProgress("修改中...");

        Map<String, String> fields = new HashMap<>();

        if ("昵称".equals(key)) {
            fields.put("user_info[nickname]", text);
        } else if ("身份证号".equals(key)) {
            fields.put("user_info[identity_card]", text);
        } else if ("个性签名".equals(key)) {
            fields.put("user_info[slogan]", text);
        } else if ("设置支付密码".equals(key)) {
            fields.put("user_info[pay_password]", text);
        }

        Engine.noAuthService().updateUser(
                sharedPre.getString("user_token", Const.EMPTY_STRING),
                sharedPre.getString("user_phone", Const.EMPTY_STRING),
                fields)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            Bundle bundle = new Bundle();
                            bundle.putString(RESULT_DATA, text);
                            bundle.putInt(RESULT_POSITION, position);
                            setFramgentResult(RESULT_OK, bundle);
                            // 隐藏软键盘
                            hideSoftInput();
                            hideProgress();
                            pop();
                            showToast("修改成功");
                        } else {
                            hideProgress();
                            change_btn.setClickable(true);
                            showToast("修改失败,请重试");
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        hideProgress();
                        change_btn.setClickable(true);
                        showToast(context.getString(R.string.network_error));
                    }
                });
    }


}
