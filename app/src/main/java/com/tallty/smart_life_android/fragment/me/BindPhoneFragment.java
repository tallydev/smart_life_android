package com.tallty.smart_life_android.fragment.me;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class BindPhoneFragment extends BaseBackFragment {
    // 修改项
    private String key;
    private String value;
    private int position;
    // UI
    private EditText phoneEdit;
    private EditText codeEdit;
    private TextView getCodeBtn;
    private Button bindBtn;
    private TextView callService;
    // 控制
    private boolean isGot = false;

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
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText(""+key);
    }

    @Override
    protected void initView() {
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
        // 密码输入监听【enter】键
        codeEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL) {
                    beginBindPhone();
                    return true;
            }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.service:
                PackageManager pm = context.getPackageManager();
                boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.CALL_PHONE","com.tallty.smart_life_android"));
                if (permission) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+"087164589208"));
                    startActivity(intent);
                } else {
                    setSnackBar(callService,
                            "应用无拨打电话权限,请设置应用权限后尝试",
                            100000, R.layout.snackbar_icon, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                }
                break;
            case R.id.get_code_btn:
                beginGetCode();
                break;
            case R.id.bind_btn:
                if (isGot) {
                    beginBindPhone();
                } else {
                    showToast("请先获取验证码");
                }
                break;
        }
    }

    private void beginBindPhone() {
        boolean begin = true;
        View focusView = null;

        phoneEdit.setError(null);
        codeEdit.setError(null);

        String phone = phoneEdit.getText().toString();
        String code = codeEdit.getText().toString();

        if (code.isEmpty()) {
            codeEdit.setError("验证码不能为空");
            focusView = codeEdit;
            begin = false;
        }

        if (phone.isEmpty()) {
            phoneEdit.setError("手机号码不能为空");
            focusView = phoneEdit;
            begin = false;
        } else if (!isPhoneValid(phone)) {
            phoneEdit.setError("手机号码格式不正确");
            focusView = phoneEdit;
            begin = false;
        }

        if (begin) {
            // TODO: 16/7/27 开始绑定手机号码
            // if 绑定成功
            Bundle bundle = new Bundle();
            bundle.putString(RESULT_DATA, phone);
            bundle.putInt(RESULT_POSITION, position);
            setFramgentResult(RESULT_OK, bundle);
            // 隐藏软键盘
            hideSoftInput();
            // 弹出fragment
            pop();

        } else {
            if (focusView != null)
                focusView.requestFocus();
        }
    }

    private void beginGetCode() {
        String phone = phoneEdit.getText().toString();
        boolean begin = true;
        phoneEdit.setError(null);

        if (phone.isEmpty()) {
            phoneEdit.requestFocus();
            phoneEdit.setError("手机号码不能为空");
            begin = false;
        } else if (!isPhoneValid(phone)) {
            phoneEdit.requestFocus();
            phoneEdit.setError("手机号码格式不正确");
            begin = false;
        }

        if (begin) {
            // TODO: 16/7/27 获取验证码操作
            getCodeBtn.setText("重新发送");
            isGot = true;
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
}
