package com.tallty.smart_life_android.fragment.authentication;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.Engine.Engine;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.model.Errors;
import com.tallty.smart_life_android.model.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 找回密码
 */
public class ResetFragment extends BaseBackFragment {
    private EditText phone_edit;
    private EditText code_edit;
    private EditText password_edit;
    private TextView code_btn;
    private Button reset_password_btn;
    // 验证码相关
    private CountDownTimer timer;
    private boolean hasGot = false;
    // 用户信息
    private User user = new User();

    public static ResetFragment newInstance() {
        Bundle args = new Bundle();

        ResetFragment fragment = new ResetFragment();
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
        return R.layout.fragment_reset;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText("密码重置");
    }

    @Override
    protected void initView() {
        phone_edit = getViewById(R.id.reset_phone);
        code_btn = getViewById(R.id.reset_code_btn);
        code_edit = getViewById(R.id.reset_code);
        password_edit = getViewById(R.id.reset_password);
        reset_password_btn = getViewById(R.id.reset_password_btn);
    }

    @Override
    protected void setListener() {
        code_btn.setOnClickListener(this);
        reset_password_btn.setOnClickListener(this);
    }

    @Override
    protected void afterAnimationLogic() {
        String phone = sharedPre.getString(Const.USER_PHONE, Const.EMPTY_STRING);
        if (Const.EMPTY_STRING.equals(phone)) {
            phone_edit.requestFocus();
        } else {
            phone_edit.setText(phone);
            code_edit.requestFocus();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset_code_btn:
                getSmsTask();
                break;
            case R.id.reset_password_btn:
                beginReset();
                break;
        }
    }

    public void getSmsTask() {
        code_edit.requestFocus();
        phone_edit.setError(null);
        boolean begin = true;

        String phone = phone_edit.getText().toString();
        if (phone.isEmpty()) {
            phone_edit.setError("手机号码不能为空");
            phone_edit.requestFocus();
            begin = false;
        } else if (!isPhoneValid(phone)) {
            phone_edit.requestFocus();
            phone_edit.setError("手机号码格式不正确");
            begin = false;
        }

        // 设置获取验证码计时器
        timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                code_btn.setText(millisUntilFinished/1000 + "秒");
            }

            @Override
            public void onFinish() {
                code_btn.setClickable(true);
                code_btn.setText("重新获取");
            }
        };

        if (begin) {
            hasGot = true;
            code_btn.setClickable(false);
            timer.start();
            // 获取
            Engine.noAuthService().getSms(phone).enqueue(new Callback<HashMap<String, String>>() {
                @Override
                public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                    if (response.isSuccessful()) {
                        showToast("验证码已发送");
                    } else {
                        timer.cancel();
                        code_btn.setClickable(true);
                        code_btn.setText("重新获取");
                        showToast("获取失败,请重新获取");
                    }
                }

                @Override
                public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
                    code_btn.setClickable(true);
                    code_btn.setText("重新获取");
                    showToast(_mActivity.getString(R.string.network_error));
                }
            });
        }
    }

    // 表单验证
    private void beginReset() {
        String phone = phone_edit.getText().toString();
        String password = password_edit.getText().toString();
        String sms = code_edit.getText().toString();
        // 初始化
        View focusView = null;
        boolean cancel = false;
        phone_edit.setError(null);
        code_edit.setError(null);
        password_edit.setError(null);
        // 判断
        if (password.isEmpty()) {
            password_edit.setError("请填写密码");
            focusView = password_edit;
            cancel = true;
        }
        if (!isPasswordValid(password)) {
            password_edit.setError("密码格式不正确");
            focusView = password_edit;
            cancel = true;
        }
        if (sms.isEmpty()) {
            code_edit.setError("请填写验证码");
            focusView = code_edit;
            cancel = true;
        }
        if (!hasGot) {
            code_edit.setError("请点击按钮获取验证码");
            focusView = code_edit;
            cancel = true;
        }
        if (phone.isEmpty()) {
            phone_edit.setError("请填写手机号码");
            focusView = phone_edit;
            cancel = true;
        }
        if (!isPhoneValid(phone)) {
            phone_edit.setError("手机号码格式不正确");
            focusView = phone_edit;
            cancel = true;
        }
        // 注册
        if (cancel) {
            assert focusView != null;
            focusView.requestFocus();
        } else {
            resetTask(phone, password, sms);
        }
    }

    // 密码重置
    private void resetTask(final String phone, final String password, String sms) {
        showProgress(getString(R.string.progress_reset));

        Engine.noAuthService()
                .resetPassword(phone, password, sms)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            hideProgress();
                            pop();
                        } else if (response.code() == 422){
                            Gson gson = new Gson();
                            try {
                                String error = null;
                                View focusView = null;
                                password_edit.setError(null);
                                code_edit.setError(null);
                                phone_edit.setError(null);

                                Errors errors = gson.fromJson(response.errorBody().string(), User.class).getErrors();
                                if (errors.getPassword().size() > 0) {
                                    error = errors.getPassword().get(0);
                                    password_edit.setError(error);
                                    focusView = password_edit;
                                }
                                if (errors.getSms_token().size() > 0) {
                                    error = errors.getSms_token().get(0);
                                    code_edit.setError(error);
                                    focusView = code_edit;
                                }
                                if (errors.getPhone().size() > 0) {
                                    error = errors.getPhone().get(0);
                                    phone_edit.setError(error);
                                    focusView = phone_edit;
                                }

                                if (error != null) {
                                    focusView.requestFocus();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            reset_password_btn.setClickable(true);
                            hideProgress();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        reset_password_btn.setClickable(true);
                        hideProgress();
                        showToast(_mActivity.getString(R.string.network_error));
                    }
                });
    }

    /**
     * 验证手机号格式
     */
    private boolean isPhoneValid(String phone) {
        boolean flag;
        try{
            Pattern pattern = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
            Matcher matcher = pattern.matcher(phone);
            flag = matcher.matches();
        }catch(Exception e){
            flag = false;
        }
        return flag;
    }

    /**
     * 验证密码长度
     */
    private boolean isPasswordValid(String password) {
        return password.length() >= 8;
    }
}
