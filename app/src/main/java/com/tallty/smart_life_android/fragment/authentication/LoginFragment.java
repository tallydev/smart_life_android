package com.tallty.smart_life_android.fragment.authentication;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.base.BaseLazyMainFragment;

/**
 * 个人中心-登录
 */
public class LoginFragment extends BaseLazyMainFragment {
    private AutoCompleteTextView phone_edit;
    private EditText password_edit;
    private Button login_btn;
    private Button link_to_register;

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
        phone_edit = getViewById(R.id.login_phone);
        password_edit = getViewById(R.id.login_password);
        login_btn = getViewById(R.id.login_btn);
        link_to_register = getViewById(R.id.free_register);
    }

    @Override
    protected void initLazyView(@Nullable Bundle savedInstanceState) {
        login_btn.setOnClickListener(this);
        link_to_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn:

                break;
            case R.id.free_register:
                start(RegisterFragment.newInstance());
                break;
        }
    }
}
