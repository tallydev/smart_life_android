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
        if (!key.equals("昵称")) {
            change_tips.setText(null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_btn:
                change_input.setError(null);
                String text = change_input.getText().toString();
                if (text.isEmpty()) {
                    change_input.setError("请填写信息");
                    change_input.requestFocus();
                } else {
                    // 调用接口修改数据
                    updateUser(text);
                }
                break;
        }
    }

    private void updateUser(final String text) {
        change_btn.setClickable(false);
        showProgress("修改中...");
        // user: phone,token,email
        Map<String, String> fields = new HashMap<>();
        if (key.equals("昵称")) {
            fields.put("user_info[nickname]", text);
            Logger.d("nickname"+text);
        } else if (key.equals("身份证号")) {
            fields.put("user_info[identity_card]", text);
            Logger.d("identity_card"+text);
        }

        mApp.noHeaderEngine().updateUser(
                sharedPre.getString("user_token", Const.EMPTY_STRING),
                sharedPre.getString("user_phone", Const.EMPTY_STRING),
                fields)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.code() == 200) {
                            Bundle bundle = new Bundle();
                            bundle.putString(RESULT_DATA, text);
                            bundle.putInt(RESULT_POSITION, position);
                            setFramgentResult(RESULT_OK, bundle);
                            // 隐藏软键盘
                            hideSoftInput();
                            hideProgress();
                            pop();
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
