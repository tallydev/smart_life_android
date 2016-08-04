package com.tallty.smart_life_android.fragment.authentication;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.event.ConfirmDialogEvent;
import com.tallty.smart_life_android.event.StartBrotherEvent;
import com.tallty.smart_life_android.fragment.Pop.AddressDialogFragment;
import com.tallty.smart_life_android.model.Errors;
import com.tallty.smart_life_android.model.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 个人中心-注册
 */
public class RegisterFragment extends BaseBackFragment {
    private EditText phoneEdit;
    private EditText codeEdit;
    private TextView getCodeBtn;
    private EditText passwordEdit;
    private EditText nicknameEdit;
    private EditText addressEdit;
    private CheckBox acceptBtn;
    private TextView clauseText;
    private Button registerBtn;
    // 用户信息
    private User user_edit = new User();
    // 验证码相关
    private CountDownTimer timer;
    private boolean hasGot = false;

    public static RegisterFragment newInstance() {
        Bundle args = new Bundle();

        RegisterFragment fragment = new RegisterFragment();
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
        return R.layout.fragment_register;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText("注册");
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);

        phoneEdit = getViewById(R.id.register_phone);
        codeEdit = getViewById(R.id.register_code);
        getCodeBtn = getViewById(R.id.get_code_btn);
        passwordEdit = getViewById(R.id.register_password);
        nicknameEdit = getViewById(R.id.register_nickname);
        addressEdit = getViewById(R.id.register_address);
        acceptBtn = getViewById(R.id.register_accept_clause);
        clauseText = getViewById(R.id.clause);
        registerBtn = getViewById(R.id.register_btn);
    }

    @Override
    protected void setListener() {
        getCodeBtn.setOnClickListener(this);
        addressEdit.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        clauseText.setOnClickListener(this);
        acceptBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    registerBtn.setTextColor(showColor(R.color.white));
                    registerBtn.setClickable(true);
                } else {
                    registerBtn.setTextColor(showColor(R.color.alpha_white));
                    registerBtn.setClickable(false);
                }
            }
        });
    }

    @Override
    protected void afterAnimationLogic() {
        registerBtn.setClickable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_code_btn:
                getSmsTask();
                break;
            case R.id.register_address:
                AddressDialogFragment fragment = AddressDialogFragment.newInstance("注册");
                fragment.show(getActivity().getFragmentManager(), "HintDialog");
                break;
            case R.id.register_btn:
                beginRegister();
                break;
            case R.id.clause:
                EventBus.getDefault().post(new StartBrotherEvent(ClauseFragment.newInstance()));
                break;
        }
    }

    private void getSmsTask() {
        String phone = phoneEdit.getText().toString();

        codeEdit.requestFocus();
        phoneEdit.setError(null);
        boolean begin = true;

        if (phone.isEmpty()) {
            phoneEdit.setError("手机号不能为空");
            phoneEdit.requestFocus();
            begin = false;
        } else if (!isPhoneValid(phone)) {
            phoneEdit.requestFocus();
            phoneEdit.setError("手机号码格式不正确");
            begin = false;
        }

        // 获取验证码计时器
        timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                getCodeBtn.setText(millisUntilFinished/1000 + "秒");
            }

            @Override
            public void onFinish() {
                getCodeBtn.setClickable(true);
                getCodeBtn.setText("重新获取");
            }
        };

        if (begin) {
            hasGot = true;
            getCodeBtn.setClickable(false);
            timer.start();
            // 获取
            mApp.noHeaderEngine().getSms(phone).enqueue(new Callback<HashMap<String, String>>() {
                @Override
                public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                    if (response.code() == 201) {
                        Log.d("验证码", response.body().get("token"));
                        showToast("验证码已发送");
                    } else {
                        timer.cancel();
                        getCodeBtn.setClickable(true);
                        getCodeBtn.setText("重新获取");
                        showToast("获取失败,请重新获取");
                    }
                }

                @Override
                public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
                    getCodeBtn.setClickable(true);
                    getCodeBtn.setText("重新获取");
                    showToast(context.getString(R.string.network_error));
                }
            });
        }
    }

    // 表单验证
    private void beginRegister() {
        user_edit.setPhone(phoneEdit.getText().toString());
        user_edit.setPassword(passwordEdit.getText().toString());
        user_edit.setNickname(nicknameEdit.getText().toString());
        user_edit.setAddress(addressEdit.getText().toString());
        String sms = codeEdit.getText().toString();
        // 初始化
        View focusView = null;
        boolean cancel = false;
        phoneEdit.setError(null);
        codeEdit.setError(null);
        passwordEdit.setError(null);
        nicknameEdit.setError(null);
        addressEdit.setError(null);
        // 判断
        if (user_edit.getAddress().isEmpty()) {
            addressEdit.setError("请填写所在小区");
            cancel = true;
        }
        if (user_edit.getNickname().isEmpty()) {
            nicknameEdit.setError("请填写昵称");
            focusView = nicknameEdit;
            cancel = true;
        }
        if (user_edit.getPassword().isEmpty()) {
            passwordEdit.setError("请填写密码");
            focusView = passwordEdit;
            cancel = true;
        }
        if (!isPasswordValid(user_edit.getPassword())) {
            passwordEdit.setError("密码格式不正确");
            focusView = passwordEdit;
            cancel = true;
        }
        if (sms.isEmpty()) {
            codeEdit.setError("请填写验证码");
            focusView = codeEdit;
            cancel = true;
        }
        if (!hasGot) {
            codeEdit.setError("请点击按钮获取验证码");
            focusView = codeEdit;
            cancel = true;
        }
        if (user_edit.getPhone().isEmpty()) {
            phoneEdit.setError("请填写手机号码");
            focusView = phoneEdit;
            cancel = true;
        }
        if (!isPhoneValid(user_edit.getPhone())) {
            phoneEdit.setError("手机号码格式不正确");
            focusView = phoneEdit;
            cancel = true;
        }
        // 注册
        if (cancel) {
            assert focusView != null;
            focusView.requestFocus();
        } else {
            registerTask(user_edit, sms);
        }
    }

    private void registerTask(User user_edit, String sms) {
        registerBtn.setClickable(false);
        showProgress(showString(R.string.progress_register));

        mApp.noHeaderEngine().registerUser(
                user_edit.getPhone(),
                user_edit.getPassword(),
                sms).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 201) {
                    User user = response.body();
                    // 保存手机号,方便登陆页自动显示
                    SharedPreferences.Editor editor = sharedPre.edit();
                    editor.putString(Const.USER_PHONE, user.getPhone());
                    editor.commit();
                    // 更新用户信息
                    updateUser(user);
                } else if (response.code() == 422) {
                    Gson gson = new Gson();
                    try {
                        String error = null;
                        View focusView = null;
                        passwordEdit.setError(null);
                        codeEdit.setError(null);
                        phoneEdit.setError(null);

                        Errors errors = gson.fromJson(response.errorBody().string(), User.class).getErrors();
                        if (errors.getPassword().size() > 0) {
                            error = errors.getPassword().get(0);
                            passwordEdit.setError(error);
                            focusView = passwordEdit;
                        }
                        if (errors.getSms_token().size() > 0) {
                            error = errors.getSms_token().get(0);
                            codeEdit.setError(error);
                            focusView = codeEdit;
                        }
                        if (errors.getPhone().size() > 0) {
                            error = errors.getPhone().get(0);
                            phoneEdit.setError(error);
                            focusView = phoneEdit;
                        }

                        if (error != null) {
                            focusView.requestFocus();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    registerBtn.setClickable(true);
                    hideProgress();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                registerBtn.setClickable(true);
                hideProgress();
                showToast(context.getString(R.string.network_error));
            }
        });
    }

    private void updateUser(User user) {
        // user: phone,token,email
        Map<String, String> fields = new HashMap<>();
        fields.put("user_info[nickname]", user_edit.getNickname());

        mApp.noHeaderEngine().updateUser(
                user.getToken(),
                user.getPhone(),
                fields)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        // 更新信息成功与否,都跳转登录(注册页处理不了修改信息失败的问题, profile也可处理)
                        hideProgress();
                        pop();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        registerBtn.setClickable(true);
                        hideProgress();
                        showToast(context.getString(R.string.network_error));
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timer != null) {
            timer.cancel();
        }
        EventBus.getDefault().unregister(this);
    }

    /**
     * 接收事件: 选择小区地址
     */
    @Subscribe
    public void onConfirmDialogEvnet(ConfirmDialogEvent event) {
        event.dialog.dismiss();
        addressEdit.setText(event.data.getString("小区"));
    }
}
