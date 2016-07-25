package com.tallty.smart_life_android.fragment.authentication;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.activity.MainActivity;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.base.BaseLazyMainFragment;
import com.tallty.smart_life_android.event.StartBrotherEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 个人中心-登录
 */
public class LoginFragment extends BaseLazyMainFragment {
    private AutoCompleteTextView phone_edit;
    private EditText password_edit;
    private Button login_btn;
    private Button link_to_register;
    private ScrollView login_form_layout;

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
        link_to_register = getViewById(R.id.free_register);
        login_form_layout = getViewById(R.id.login_form);
    }

    @Override
    protected void initLazyView(@Nullable Bundle savedInstanceState) {
        login_btn.setOnClickListener(this);
        link_to_register.setOnClickListener(this);

        initFormEdit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn:
                beginLogin();
                break;
            case R.id.free_register:
                start(RegisterFragment.newInstance());
                break;
        }
    }

    // 表单初始化
    private void initFormEdit() {
        String phone = sharedPre.getString(PHONE, EMPTY_STRING);
        if (EMPTY_STRING.equals(phone)) {
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
            password_edit.setError("登录密码错误");
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
            // 隐藏软键盘
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            // TODO: 16/7/25 开始登录功能
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
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
        return password.length() >= 5 && password.length() <= 8;
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
