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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
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
import com.tallty.smart_life_android.event.StartBrotherEvent;
import com.tallty.smart_life_android.event.TransferDataEvent;
import com.tallty.smart_life_android.model.Profile;
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
    // 列表
    private RecyclerView recyclerView;
    private ProfileListAdapter adapter;
    // 数据
    private User user = new User();
    private ArrayList<Profile> profiles = new ArrayList<>();
    private String[] titles = {"头像", "昵称", "登录手机号", "绑定社区", "小区名称", "出生日期", "性别", "个性签名", "身份证号", "收货地址", "版本更新", "切换账号"};
    private boolean[] hasGaps = {true, false,  true,       false,     true,      false,      false,  false,    false,     true,      false,     false};
    private int[] itemTypes = {Profile.IMG, Profile.TEXT, Profile.TEXT, Profile.TEXT, Profile.TEXT,
                                Profile.TEXT, Profile.TEXT, Profile.TEXT, Profile.TEXT, Profile.TEXT,
                                Profile.TEXT, Profile.TEXT};

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
        return R.layout.fragment_common_list;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText("账户管理");
    }

    @Override
    protected void initView() {
        recyclerView = getViewById(R.id.common_list);
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void afterAnimationLogic() {
        tidyData();
        initList();
        getUserInfo();
    }

    private void tidyData() {
        for (int i = 0; i < titles.length; i++) {
            Profile profile = new Profile(titles[i], "", itemTypes[i], hasGaps[i]);
            profiles.add(profile);
        }
    }

    private void initList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new ProfileListAdapter(profiles);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                switch (profiles.get(i).getTitle()) {
                    case "头像":
                        processPhoto();
                        break;
                    case "登录手机号":
                        startForResult(BindPhoneFragment.newInstance(profiles.get(i), i), REQ_CODE);
                        break;
                    case "出生日期":
                        processBirth(i);
                        break;
                    case "性别":
                        processSex(i);
                        break;
                    case "收货地址":
                        EventBus.getDefault().post(new StartBrotherEvent(ManageAddresses.newInstance()));
                        break;
                    case "版本更新":
                        updateVersion();
                        break;
                    case "切换账号":
                        processSignOut();
                        break;
                    case "绑定社区":
                        startForResult(BindCommunityFragment.newInstance(i), REQ_CODE);
                        break;
                    default:
                        // 跳转修改页面(昵称、个性签名、身份证号、小区名称)
                        startForResult(ChangeProfileFragment.newInstance(profiles.get(i), i), REQ_CODE);
                        break;
                }
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
                    profiles.get(0).setValue(user.getAvatar());
                    profiles.get(1).setValue(user.getNickname());
                    profiles.get(2).setValue(user.getPhone());
                    profiles.get(3).setValue(user.getCommunity());
                    profiles.get(4).setValue(user.getVillage());
                    profiles.get(5).setValue(user.getBirth());
                    profiles.get(6).setValue(user.getSex());
                    profiles.get(7).setValue(user.getSlogan());
                    profiles.get(8).setValue(user.getIdCard());
                    profiles.get(10).setValue(getVersion());
                    adapter.notifyDataSetChanged();
                    setVersionInfo();
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


    // 显示版本信息
    private void setVersionInfo() {
        PgyUpdateManager.register(getActivity(), new UpdateManagerListener() {
            @Override
            public void onNoUpdateAvailable() {
            }

            @Override
            public void onUpdateAvailable(String s) {
                profiles.get(8).setValue("发现新版本");
                adapter.notifyItemChanged(8);
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
        bundle.putString(Const.USER_AVATAR, profiles.get(0).getValue());
        bundle.putString(Const.USER_NICKNAME, profiles.get(1).getValue());
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
                    profiles.get(0).setValue(file.getAbsolutePath());
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
     * 修改用户信息模态框:
     * 出生日期、性别
     */
    private void updateUserInfoDialog(final String value, final int position, String tag) {
        showProgress("修改中...");
        // 更新字段
        Map<String, String> fields = new HashMap<>();
        if ("生日".equals(tag)) {
            fields.put("user_info[birth]", value);
        } else if ("性别".equals(tag)){
            String parse_sex = "男".equals(value) ? "male" : "female";
            fields.put("user_info[sex]", parse_sex);
        }


        Engine.noAuthService().updateUser(shared_token, shared_phone, fields)
            .enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.code() == 200) {
                        profiles.get(position).setValue(value);
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
            profiles.get(position).setValue(text);
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
