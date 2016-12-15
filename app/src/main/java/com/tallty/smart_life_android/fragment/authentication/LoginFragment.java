package com.tallty.smart_life_android.fragment.authentication;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.Engine.Engine;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.activity.MainActivity;
import com.tallty.smart_life_android.base.BaseMainFragment;
import com.tallty.smart_life_android.event.StartBrotherEvent;
import com.tallty.smart_life_android.model.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 个人中心-登录
 */
public class LoginFragment extends BaseMainFragment {
    private AutoCompleteTextView phone_edit;
    private EditText password_edit;
    private Button login_btn;
    private Button free_register;
    private Button find_password;

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
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
    protected void fragmentInterceptor() {
        super.fragmentInterceptor();
        String phone = sharedPre.getString(Const.USER_PHONE, Const.EMPTY_STRING);
        String token = sharedPre.getString(Const.USER_TOKEN, Const.EMPTY_STRING);
        if (!phone.isEmpty() && !token.isEmpty()) {
            // 进入首页, 不再登录
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initToolBar(Toolbar toolbar, TextView title) {
        title.setText("登录");
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        phone_edit = getViewById(R.id.login_phone);
        password_edit = getViewById(R.id.login_password);
        login_btn = getViewById(R.id.login_btn);
        free_register = getViewById(R.id.free_register);
        find_password = getViewById(R.id.reset_password);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        login_btn.setOnClickListener(this);
        free_register.setOnClickListener(this);
        find_password.setOnClickListener(this);
        initFormEdit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn:
                beginLogin();
                break;
            case R.id.free_register:
                hideSoftInput();
                start(RegisterFragment.newInstance());
                break;
            case R.id.reset_password:
                hideSoftInput();
                start(ResetFragment.newInstance());
                break;
        }
    }

    // 表单初始化
    private void initFormEdit() {
        String phone = sharedPre.getString(Const.USER_PHONE, Const.EMPTY_STRING);
        if (Const.EMPTY_STRING.equals(phone)) {
            phone_edit.requestFocus();
        } else {
            phone_edit.setText(phone);
            password_edit.requestFocus();
        }
        // 密码输入监听【enter】键
        password_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL) {
                    beginLogin();
                    return true;
                }
                return false;
            }
        });
    }

    // 开始登录
    private void beginLogin(){
        hideSoftInput();
        // 初始化
        phone_edit.setError(null);
        password_edit.setError(null);
        boolean cancel = false;
        View focusView = null;
        // 取值
        String phone = phone_edit.getText().toString();
        String password = password_edit.getText().toString();
        // 判断
        if (password.isEmpty() || !isPasswordValid(password)) {
            password_edit.setError("密码长度不能小于8位");
            focusView = password_edit;
            cancel = true;
        }

        if (phone.isEmpty() || !isPhoneValid(phone)) {
            phone_edit.setError("手机号码错误");
            focusView = phone_edit;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            loginTask(phone, password);
        }
    }

    private void loginTask(String phone, String password) {
        login_btn.setClickable(false);
        showProgress(showString(R.string.progress_login));

        Engine.noAuthService().login(phone, password).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                if (response.isSuccessful()) {
                    // 保存用户信息
                    SharedPreferences.Editor editor = sharedPre.edit();
                    editor.putInt(Const.USER_ID, user.getId());
                    editor.putString(Const.USER_PHONE, user.getPhone());
                    editor.putString(Const.USER_TOKEN, user.getToken());
                    editor.apply();

                    // 运行一下Auth, 防止第一次使用Auth Engine, 或异步执行初始化服务, 此时调用接口会一直失败
                    Engine.authService(user.getToken(), user.getPhone());
                    // 进入首页
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    hideProgress();
                } else {
                    login_btn.setClickable(true);
                    hideProgress();
                    showToast("登录失败,请检查登录信息");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                login_btn.setClickable(true);
                hideProgress();
                showToast(_mActivity.getString(R.string.network_error));
            }
        });
    }

    // 验证手机号格式
    private boolean isPhoneValid(String phone) {
        boolean flag;
        try{
            Pattern pattern = Pattern.compile(Const.PHONE_PATTEN);
            Matcher matcher = pattern.matcher(phone);
            flag = matcher.matches();
        }catch(Exception e){
            flag = false;
        }
        return flag;
    }

    // 验证密码长度
    private boolean isPasswordValid(String password) {
        return password.length() >= 8;
    }

    /**
     * 接收事件, 启动一个同级的Fragment
     */
    @Subscribe
    public void startBrother(StartBrotherEvent event) {
        start(event.targetFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
