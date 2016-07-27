package com.tallty.smart_life_android.fragment.me;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.ProfileListAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.custom.RecyclerVIewItemTouchListener;
import com.tallty.smart_life_android.event.StartBrotherEvent;
import com.tallty.smart_life_android.fragment.cart.MyAddress;
import com.tallty.smart_life_android.utils.ImageUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * 个人中心-个人资料
 */
public class ProfileFragment extends BaseBackFragment {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private RecyclerView recyclerView;
    private ProfileListAdapter adapter;
    // 数据
    private ArrayList<String> keys = new ArrayList<String>(){
        {
            add("头像");add("昵称");add("登录手机号");add("出生日期");add("性别");add("个性签名");
            add("身份证号");add("收货地址");add("变更绑定手机号");add("设置支付密码");
        }
    };
    private ArrayList<String> values = new ArrayList<String>(){
        {
            add("http://img0.pconline.com.cn/pconline/1312/27/4072897_01_thumb.gif");add("Stark");
            add("13816000000");add("1992-05-03");add("男");add("未设置");add("310112199205031234");
            add("");add("15316788888");add("");
        }
    };
    // 弹框
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    // 上传头像相关
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;

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
    protected void initView() {
        toolbar = getViewById(R.id.toolbar);
        toolbar_title = getViewById(R.id.toolbar_title);
        recyclerView = getViewById(R.id.profile_list);
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void afterAnimationLogic() {
        initBackToolbar(toolbar);
        toolbar_title.setText("账户管理");
        processRecyclerView();
    }

    @Override
    public void onClick(View v) {

    }

    private void processRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ProfileListAdapter(context, keys, values);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerVIewItemTouchListener(recyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh, final int position) {
                if (position == 0){
                    // 修改头像
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
                } else if (position == 2) {
                    // 登录手机号
                    showToast("可通过绑定手机号修改");
                } else if (position == 3) {
                    // 修改生日
                    Calendar calendar = Calendar.getInstance();
                    Dialog dateDialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    String date = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                                    values.set(position, date);
                                    adapter.notifyItemChanged(position);
                                }
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                    );
                    dateDialog.show();
                } else if (position == 4) {
                    // 修改性别
                    final String[] sex = new String[]{"男", "女"};
                    alert = null;
                    builder = new AlertDialog.Builder(getActivity());
                    alert = builder.setTitle("修改性别")
                            .setItems(sex, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    values.set(position, sex[which]);
                                    adapter.notifyItemChanged(position);
                                }
                            }).create();
                    alert.show();
                } else if (position == 7) {
                    EventBus.getDefault().post(new StartBrotherEvent(MyAddress.newInstance(FROM_PROFILE)));
                } else if (position == 8) {
                    // 跳转修改页面
                    startForResult(BindPhoneFragment.newInstance(
                            keys.get(position), values.get(position), position), REQ_CODE);
                } else {
                    // 跳转修改页面
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
     *
     * @param uri
     */
    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
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
     *
     * @param data
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            // Bitmap转化为file, 获取file 路径
            String imagePath = ImageUtils.savaBitmap(String.valueOf(System.currentTimeMillis()), photo);
            // 更新账户管理图片
            values.set(0, imagePath);
            adapter.notifyItemChanged(0);
            uploadPic(imagePath);
        }
    }

    /**
     * 上传到服务器
     * @param imagePath
     */
    private void uploadPic(String imagePath) {
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // bitmap是已经被裁剪了
        Log.e("imagePath", imagePath+"");
        if(imagePath != null){
            // TODO: 16/7/27 使用imagePath 上传头像
            showToast("上传了");
        }
    }

    /**
     * 处理ChangeProfileFragment返回的数据
     * @param requestCode
     * @param resultCode
     * @param data
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
}
