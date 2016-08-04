package com.tallty.smart_life_android.fragment.me;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private boolean hasGot = false;
    private CountDownTimer timer;

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
                if (hasGot) {
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

        final String phone = phoneEdit.getText().toString();
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
            bindBtn.setClickable(false);
            // user包含属性: phone,token,email
            Map<String, String> fields = new HashMap<>();
            fields.put("user_info[phone]", phone);

            mApp.noHeaderEngine().updateUser(
                    sharedPre.getString("user_token", EMPTY_STRING),
                    sharedPre.getString("user_phone", EMPTY_STRING),
                    fields)
                    .enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.code() == 200) {
                                // TODO: 16/8/3 暂时还不能绑定新手机号
                                // 更新本地信息
                                SharedPreferences.Editor editor = sharedPre.edit();
                                editor.putString("use_phone", phone);
                                editor.commit();
                                // 回传给上个fragment
                                Bundle bundle = new Bundle();
                                bundle.putString(RESULT_DATA, phone);
                                bundle.putInt(RESULT_POSITION, position);
                                setFramgentResult(RESULT_OK, bundle);
                                // 隐藏软键盘
                                hideSoftInput();
                                hideProgress();
                                pop();
                            } else {
                                hideProgress();
                                bindBtn.setClickable(true);
                                showToast("修改失败,请重试");
                            }

                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            hideProgress();
                            bindBtn.setClickable(true);
                            showToast(context.getString(R.string.network_error));
                        }
                    });

        } else {
            if (focusView != null)
                focusView.requestFocus();
        }
    }

    private void beginGetCode() {
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
