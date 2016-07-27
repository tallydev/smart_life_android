package com.tallty.smart_life_android.fragment.me;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;

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
    private Toolbar toolbar;
    private TextView toolbar_title;
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
    protected void initView() {
        toolbar = getViewById(R.id.toolbar);
        toolbar_title = getViewById(R.id.toolbar_title);
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
        initBackToolbar(toolbar);
        toolbar_title.setText(""+ key);

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
                String text = change_input.getText().toString();
                // TODO: 16/7/27 调用接口修改数据
                Bundle bundle = new Bundle();
                bundle.putString(RESULT_DATA, text);
                bundle.putInt(RESULT_POSITION, position);
                setFramgentResult(RESULT_OK, bundle);
                // 隐藏软键盘
                hideSoftInput();
                pop();
                break;
        }
    }
}
