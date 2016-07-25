package com.tallty.smart_life_android.fragment.authentication;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 个人中心-注册
 */
public class RegisterFragment extends BaseBackFragment {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private EditText phoneEdit;
    private EditText codeEdit;
    private TextView getCodeBtn;
    private EditText passwordEdit;
    private EditText nicknameEdit;
    private EditText addressEdit;
    private CheckBox acceptBtn;
    private TextView clauseText;
    private Button registerBtn;

    private User user = new User();

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
    protected void initView() {
        toolbar = getViewById(R.id.toolbar);
        toolbar_title = getViewById(R.id.toolbar_title);
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
        acceptBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    registerBtn.setTextColor(context.getResources().getColor(R.color.white));
                    registerBtn.setClickable(true);
                } else {
                    registerBtn.setTextColor(context.getResources().getColor(R.color.alpha_white));
                    registerBtn.setClickable(false);
                }
            }
        });
    }

    @Override
    protected void afterAnimationLogic() {
        initBackToolbar(toolbar);
        toolbar_title.setText("注册");
        registerBtn.setClickable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_code_btn:
                break;
            case R.id.register_address:
                showToast("可以点击");
                break;
            case R.id.register_btn:
                beginRegister();
                break;
            case R.id.clause:
                break;
        }
    }

    // 表单验证
    private void beginRegister() {
        user.setPhone(phoneEdit.getText().toString());
        user.setPhone(passwordEdit.getText().toString());
        user.setNickname(nicknameEdit.getText().toString());
        user.setAddress(addressEdit.getText().toString());
        String code = codeEdit.getText().toString();
        // 初始化
        View focusView = null;
        boolean cancel = false;
        phoneEdit.setError(null);
        codeEdit.setError(null);
        passwordEdit.setError(null);
        nicknameEdit.setError(null);
        addressEdit.setError(null);
        // 判断
        if (user.getAddress().isEmpty()) {
            addressEdit.setError("请填写所在小区");
            cancel = true;
        }
        if (user.getNickname().isEmpty()) {
            nicknameEdit.setError("请填写昵称");
            focusView = nicknameEdit;
            cancel = true;
        }
        if (user.getPassword().isEmpty()) {
            passwordEdit.setError("请填写密码");
            focusView = passwordEdit;
            cancel = true;
        }
        if (!isPasswordValid(user.getPassword())) {
            passwordEdit.setError("密码格式不正确");
            focusView = passwordEdit;
            cancel = true;
        }
        if (code.isEmpty()) {
            codeEdit.setError("请填写验证码");
            focusView = codeEdit;
            cancel = true;
        }
        if (user.getPhone().isEmpty()) {
            phoneEdit.setError("请填写手机号码");
            focusView = phoneEdit;
            cancel = true;
        }
        if (!isPhoneValid(user.getPhone())) {
            phoneEdit.setError("手机号码格式不正确");
            focusView = phoneEdit;
            cancel = true;
        }
        // 注册
        if (cancel) {
            focusView.requestFocus();
        } else {
            // TODO: 16/7/25 注册任务
            registerTask();
        }
    }

    private void registerTask() {
        showToast("注册了");
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
        return password.length() >= 6;
    }
}
