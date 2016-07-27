package com.tallty.smart_life_android.fragment.me;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class BindPhoneFragment extends BaseBackFragment {
    // 修改项
    private String key;
    private String value;
    private int position;
    // UI
    private Toolbar toolbar;
    private TextView toolbar_title;
    private EditText phoneEdit;
    private EditText codeEdit;
    private TextView getCodeBtn;
    private Button bindBtn;
    private TextView callService;

    public static BindPhoneFragment newInstance(String key, String value, int position) {
        Bundle args = new Bundle();
        args.putString("修改项", key);
        args.putString("值", value);
        args.putInt("位置", position);
        BindPhoneFragment fragment = new BindPhoneFragment();
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
        return R.layout.fragment_bind_phone;
    }

    @Override
    protected void initView() {
        toolbar = getViewById(R.id.toolbar);
        toolbar_title = getViewById(R.id.toolbar_title);
        phoneEdit = getViewById(R.id.bind_phone);
        codeEdit = getViewById(R.id.bind_code);
        getCodeBtn = getViewById(R.id.get_code_btn);
        bindBtn = getViewById(R.id.bind_btn);
        callService = getViewById(R.id.service);
    }

    @Override
    protected void setListener() {
        getCodeBtn.setOnClickListener(this);
        bindBtn.setOnClickListener(this);
        callService.setOnClickListener(this);
    }

    @Override
    protected void afterAnimationLogic() {
        initBackToolbar(toolbar);
        toolbar_title.setText(""+key);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.service:

                break;
            case R.id.get_code_btn:
                break;
            case R.id.bind_btn:
                break;
        }
    }
}
