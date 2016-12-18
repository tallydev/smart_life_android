package com.tallty.smart_life_android.fragment.me;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.tallty.smart_life_android.App;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.Engine.Engine;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.activity.LoginActivity;
import com.tallty.smart_life_android.adapter.ProfileListAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.custom.RecyclerVIewItemTouchListener;
import com.tallty.smart_life_android.event.StartBrotherEvent;
import com.tallty.smart_life_android.event.TransferDataEvent;
import com.tallty.smart_life_android.model.User;
import com.tallty.smart_life_android.utils.ImageUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 个人中心-个人资料
 */
public class ProfileFragment extends BaseBackFragment {
    // 弹框
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    // 上传头像相关
    private static final int CHOOSE_PICTURE = 0;
    private static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    private static Uri tempUri;
    // 数据
    private RecyclerView recyclerView;
    private ProfileListAdapter adapter;

    private User user = new User();
    private ArrayList<String> keys = new ArrayList<String>(){
        {
            add("头像");add("昵称");add("登录手机号");
            add("出生日期");add("性别");add("个性签名");
            add("身份证号");add("收货地址");add("变更绑定手机号");
            add("设置支付密码");add("版本更新");add("退出当前账号");
        }
    };
    private ArrayList<String> values = new ArrayList<String>(){
        {
            add("");add("");add("");
            add("未设置");add("未设置");add("未设置");
            add("");add("");add("");
            add("");add("");add("");
        }
    };


    public static ProfileFragment newInstance() {
        Bundle args = new Bundle();

        ProfileFragment fragment = new ProfileFragment();
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
        return R.layout.fragment_profile;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText("账户管理");
    }

    @Override
    protected void initView() {
        recyclerView = getViewById(R.id.profile_list);
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void afterAnimationLogic() {
        values.set(10, getVersion());
        initList();
        getUserInfo();
    }

    private void initList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new ProfileListAdapter(_mActivity, keys, values);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerVIewItemTouchListener(recyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh, final int position) {
                if (position == 0){
                    // 修改头像
                    processPhoto();
                } else if (position == 2) {
                    // 登录手机号
                    showToast("暂不支持修改");
                } else if (position == 3) {
                    // 修改生日
                    processBirth(position);
                } else if (position == 4) {
                    // 修改性别
                    processSex(position);
                } else if (position == 7) {
                    EventBus.getDefault().post(
                            new StartBrotherEvent(ManageAddresses.newInstance()));
                } else if (position == 8) {
                    // 跳转绑定手机页面
                    showToast("暂不支持修改");
//                    startForResult(BindPhoneFragment.newInstance(
//                            keys.get(position), values.get(position), position), REQ_CODE);
                } else if (position == 10) {
                    updateVersion();
                } else if (position == 11) {
                    // 用户退出
                    processSignOut();
                } else {
                    // 跳转修改页面(昵称、个性签名、身份证号、支付密码)
                    startForResult(ChangeProfileFragment.newInstance(
                            keys.get(position), values.get(position), position), REQ_CODE);
                }
            }

            @Override
            public void onItemLongPress(RecyclerView.ViewHolder vh, int position) {

            }
        });
    }


    /**
     * 获取用户信息,并显示
     */
    private void getUserInfo() {
        // 查询用户信息, 更新列表
        Engine.authService(shared_token, shared_phone).getUser().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    values.set(0, user.getAvatar());
                    values.set(1, user.getNickname());
                    values.set(2, user.getPhone());
                    values.set(3, user.getBirth());
                    values.set(4, user.getSex());
                    values.set(5, user.getSlogan());
                    values.set(6, user.getIdCard());
                    values.set(8, user.getPhone());
                    adapter.notifyDataSetChanged();
                } else {
                    showToast(showString(R.string.response_error));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showToast(showString(R.string.network_error));
            }
        });
    }

    /**
     * 退出事件
     */
    @Override
    protected void onFragmentPop() {
        super.onFragmentPop();
        // 给<账户管理>传递对象
        Bundle bundle = new Bundle();
        bundle.putString(Const.USER_AVATAR, values.get(0));
        bundle.putString(Const.USER_NICKNAME, values.get(1));
        EventBus.getDefault().post(new TransferDataEvent(bundle, "ProfileFragment"));
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 版本更新
     */
    private void updateVersion() {
        PgyUpdateManager.register(getActivity(), new UpdateManagerListener() {
            @Override
            public void onNoUpdateAvailable() {
                showToast("已经是最新版本了");
            }

            @Override
            public void onUpdateAvailable(String s) {
                final AppBean appBean = getAppBeanFromString(s);
                new AlertDialog.Builder(_mActivity)
                    .setTitle("更新提醒")
                    .setMessage(appBean.getReleaseNote())
                    .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startDownloadTask(getActivity(), appBean.getDownloadURL());
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
            }
        });
    }

    /**
     * 处理头像
     */
    private void processPhoto() {
        final String[] kind = new String[]{"相册", "拍照"};
        alert = null;
        builder = new AlertDialog.Builder(getActivity());
        builder.setNegativeButton("取消", null);
        alert = builder.setTitle("修改头像")
                .setItems(kind, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        uploadPhoto(which);
                    }
                }).create();
        alert.show();
    }

    /**
     * 处理上传头像任务
     * @param which
     */
    private void uploadPhoto(int which) {
        if (which == 0) {
            // 选择本地图片
            Intent OpenLocalIntent = new Intent(Intent.ACTION_GET_CONTENT);
            OpenLocalIntent.setType("image/*");
            startActivityForResult(OpenLocalIntent, CHOOSE_PICTURE);
        } else {
            // 拍照
            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            tempUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image.jpg"));
            // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
            startActivityForResult(openCameraIntent, TAKE_PICTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    /**
     * 裁剪图片方法实现
     */
    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i(App.TAG, "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     */
    private void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            // Bitmap转化为file, 获取file 路径
            File file = ImageUtils.saveBitmapToFile(String.valueOf(System.currentTimeMillis()), photo);
            // 更新账户管理图片
            if (file != null) {
                // 上传
                Log.d(App.TAG, "开始上传");
                uploadPic(file);
            } else {
                showToast("");
            }
        }
    }

    /**
     * 上传头像到服务器
     * @param file
     */
    private void uploadPic(final File file) {
        showProgress("正在上传...");
        // 构建上传的文件
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Part body = MultipartBody.Part
                .createFormData("user_info[avatar_attributes][photo]", file.getName(), requestBody);
        // 上传
        Engine.authService(shared_token, shared_phone).updateUserPhoto(body).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    // 更新头像地址
                    SharedPreferences.Editor editor = sharedPre.edit();
                    editor.putString(Const.USER_AVATAR, response.body().getAvatar());
                    editor.apply();
                    // 更新UI
                    values.set(0, file.getAbsolutePath());
                    adapter.notifyItemChanged(0);
                    hideProgress();
                    showToast("上传成功");
                } else {
                    hideProgress();
                    showToast("上传失败");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                hideProgress();
                showToast(_mActivity.getString(R.string.network_error));
            }
        });
    }

    /**
     * 处理生日
     */
    private void processBirth(final int position) {
        Calendar calendar = Calendar.getInstance();
        Dialog dateDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                        updateUserInfoDialog(date, position, "生日");
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dateDialog.show();
    }

    /**
     * 处理性别
     */
    private void processSex(final int position) {
        final String[] sex = new String[]{"男", "女"};
        alert = null;
        builder = new AlertDialog.Builder(getActivity());
        alert = builder.setTitle("修改性别")
                .setItems(sex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateUserInfoDialog(sex[which], position, "性别");
                    }
                }).create();
        alert.show();
    }

    /**
     * 处理退出登录
     */
    private void processSignOut() {
        confirmDialog("确定退出当前账号吗?", new OnConfirmDialogListener() {
            @Override
            public void onConfirm(DialogInterface dialog, int which) {
                // 从数据源删除数
                SharedPreferences.Editor editor = sharedPre.edit();
                editor.putString(Const.USER_TOKEN, Const.EMPTY_STRING);
                editor.apply();
                // 重置网络请求, 否则退出账号,authService 还保留着上一账号的phone 和 token
                Engine.resetEngine();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onCancel(DialogInterface dialog, int which) {

            }
        });
    }

    /**
     * 修改用户信息:
     * 出生日期、性别
     */
    private void updateUserInfoDialog(final String value, final int position, String tag) {
        showProgress("修改中...");
        // TODO: 16/8/3 后台暂无字段 ,模拟更新
        // 更新字段
        Map<String, String> fields = new HashMap<>();
        if ("生日".equals(tag)) {
            fields.put("user_info[birth]", value);
        } else if ("性别".equals(tag)){
            String parse_sex = "男".equals(value) ? "male" : "female";
            fields.put("user_info[sex]", parse_sex);
        }

        Engine.noAuthService().updateUser(
                sharedPre.getString("user_token", Const.EMPTY_STRING),
                sharedPre.getString("user_phone", Const.EMPTY_STRING),
                fields
        ).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    values.set(position, value);
                    adapter.notifyItemChanged(position);
                    hideProgress();
                } else {
                    showToast("修改失败,请重试");
                    hideProgress();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                hideProgress();
                showToast(_mActivity.getString(R.string.network_error));
            }
        });
    }

    /**
     * 处理ChangeProfileFragment返回的数据
     */
    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
            // 显示
            String text = data.getString(RESULT_DATA);
            int position = data.getInt(RESULT_POSITION);
            values.set(position, text);
            adapter.notifyItemChanged(position);
        }
    }

    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = _mActivity.getPackageManager();
            PackageInfo info = manager.getPackageInfo(_mActivity.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
