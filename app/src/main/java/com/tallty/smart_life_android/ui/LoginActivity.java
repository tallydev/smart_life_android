package com.tallty.smart_life_android.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseActivity;
import com.tallty.smart_life_android.presenter.ILoginPresenter;
import com.tallty.smart_life_android.presenter.LoginPresenter;
import com.tallty.smart_life_android.view.ILoginView;

public class LoginActivity extends BaseActivity implements ILoginView {
    ILoginPresenter iLoginPresenter;

    Button button;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);

        button = getViewById(R.id.button);
    }

    @Override
    protected void setListener() {
        button.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        showActionBar(false);
        // 获取presenter的引用,执行后续操作
        iLoginPresenter = new LoginPresenter(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.button:
                iLoginPresenter.processName();
                break;
        }
    }

    @Override
    public void showName(String name) {
        showToast(name);
    }
}
