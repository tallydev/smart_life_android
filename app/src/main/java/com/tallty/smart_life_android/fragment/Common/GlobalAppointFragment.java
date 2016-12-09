package com.tallty.smart_life_android.fragment.Common;


import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.tallty.smart_life_android.App;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.Engine.Engine;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.fragment.Pop.HintDialogFragment;
import com.tallty.smart_life_android.model.Appointment;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP;

/**
 * 首页-社区活动-活动详情
 * 首页-社区服务
 */
public class GlobalAppointFragment extends BaseBackFragment {
    private String fragmentTitle;
    private String imageUrl;
    private String appointType;
    private Boolean isSingle;
    private String btn_text;
    private Boolean hasButton;
    private int activityId;
    // 详情图
    private SubsamplingScaleImageView detail_image;
    private ImageView small_detail_image;
    private ScrollView scrollView;
    // 多人的操作
    private View countLayout;
    private Button add;
    private Button reduce;
    private TextView number;
    private TextView countAppointBtn;
    // 单人的操作
    private View singleLayout;
    private TextView singleAppointBtn;
    // 计数
    private int count = 1;

    // (1) 首页特定服务预约的详情
    // 预约的类型【ZHJK:智慧健康】【DZMY:电子猫眼】【ITFW:IT服务】【ITXT:IT学堂】【SQHD:社区活动】【XPSS:新品上市】【ZNJJ:智能家居】
    public static GlobalAppointFragment newInstance(String title, String imageUrl, String appointType, String btn_text, Boolean isSingle) {
        Bundle args = new Bundle();
        args.putString(Const.FRAGMENT_NAME, title);
        args.putString("detail_image", imageUrl);
        args.putString("appoint_type", appointType);
        args.putBoolean("is_single", isSingle);
        args.putString("button_text", btn_text);
        args.putBoolean("has_button", true);
        GlobalAppointFragment fragment = new GlobalAppointFragment();
        fragment.setArguments(args);
        return fragment;
    }
    // (2) 社区活动的详情
    public static GlobalAppointFragment newInstance(String title, String imageUrl, int activityId, String btn_text, Boolean isSingle) {
        Bundle args = new Bundle();
        args.putString(Const.FRAGMENT_NAME, title);
        args.putString("detail_image", imageUrl);
        args.putInt("activity_id", activityId);
        args.putBoolean("is_single", isSingle);
        args.putString("button_text", btn_text);
        args.putBoolean("has_button", true);
        GlobalAppointFragment fragment = new GlobalAppointFragment();
        fragment.setArguments(args);
        return fragment;
    }
    // (3) 没有任何操作的详情
    public static GlobalAppointFragment newInstance(String title, String imageUrl) {
        Bundle args = new Bundle();
        args.putString(Const.FRAGMENT_NAME, title);
        args.putString("detail_image", imageUrl);
        args.putBoolean("has_button", false);
        GlobalAppointFragment fragment = new GlobalAppointFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            fragmentTitle = args.getString(Const.FRAGMENT_NAME);
            imageUrl = args.getString("detail_image");
            hasButton = args.getBoolean("has_button");
            appointType = args.getString("appoint_type");
            activityId = args.getInt("activity_id");
            isSingle = args.getBoolean("is_single");
            btn_text = args.getString("button_text");
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_global_appoint;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText(fragmentTitle);
    }

    @Override
    protected void initView() {
        // 详情图
        scrollView = getViewById(R.id.small_product_detail_image_layout);
        small_detail_image = getViewById(R.id.small_detail_image);
        detail_image = getViewById(R.id.detail_image);
        detail_image.setZoomEnabled(false);
        detail_image.setMinimumScaleType(SCALE_TYPE_CENTER_CROP);
        // 单人操作按钮
        singleLayout = getViewById(R.id.single_action_include);
        singleAppointBtn = (TextView) singleLayout.findViewById(R.id.appoint_btn);
        // 多人操作按钮
        countLayout = getViewById(R.id.count_action_include);
        add = (Button) countLayout.findViewById(R.id.count_add);
        reduce = (Button) countLayout.findViewById(R.id.count_reduce);
        number = (TextView) countLayout.findViewById(R.id.number);
        countAppointBtn = (TextView) countLayout.findViewById(R.id.apply_btn);
    }

    @Override
    protected void setListener() {
        add.setOnClickListener(this);
        reduce.setOnClickListener(this);
        countAppointBtn.setOnClickListener(this);
        singleAppointBtn.setOnClickListener(this);
    }

    @Override
    protected void afterAnimationLogic() {
        showProgress("正在加载详情...");
        Glide
            .with(_mActivity).load(imageUrl)
            .downloadOnly(new SimpleTarget<File>() {
                @Override
                public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                    detail_image.setImage(ImageSource.uri(Uri.fromFile(resource)), new ImageViewState(1.0f, new PointF(0, 0), 0));
                    onImageLoadListener(resource);
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
                    hideProgress();
                    showToast("加载详情失败, 请稍后重试");
                }
            });
    }

    // 图片载入监听
    private void onImageLoadListener(final File resource) {
        detail_image.setOnImageEventListener(new SubsamplingScaleImageView.OnImageEventListener() {
            @Override
            public void onReady() {
                // 图片加载好, 之后显示操作按钮
                if (hasButton) {
                    if (isSingle) {
                        singleLayout.setVisibility(View.VISIBLE);
                        singleAppointBtn.setText(btn_text);
                    } else {
                        countLayout.setVisibility(View.VISIBLE);
                        countAppointBtn.setText(btn_text);
                    }
                }

                WindowManager wm = (WindowManager) _mActivity.getSystemService(Context.WINDOW_SERVICE);
                // 屏幕宽度
                int width = wm.getDefaultDisplay().getWidth();
                // 图片宽度
                int image_w = detail_image.getSWidth();
                // 比例
                float width_ratio = (float) (width * 1.0 / image_w);

                Log.d(App.TAG, detail_image.getScale()+"缩放比例");
                Log.d(App.TAG, width_ratio+"宽度比例");

                if (detail_image.getScale() > width_ratio) {
                    Log.d(App.TAG, "使用普通ImageView加载宽图");
                    detail_image.recycle();
                    detail_image.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                    Glide.with(_mActivity).load(resource).into(small_detail_image);
                }
                hideProgress();
            }

            @Override
            public void onImageLoaded() {

            }

            @Override
            public void onPreviewLoadError(Exception e) {
            }

            @Override
            public void onImageLoadError(Exception e) {
            }

            @Override
            public void onTileLoadError(Exception e) {
            }

            @Override
            public void onPreviewReleased() {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.count_add:
                number.setText(String.valueOf(++count));
                break;
            case R.id.count_reduce:
                if (count > 1){
                    number.setText(String.valueOf(--count));
                }else{
                    number.setText(String.valueOf(count));
                }
                break;
            case R.id.apply_btn:
                handleButtonEvent();
                break;
            case R.id.appoint_btn:
                handleButtonEvent();
                break;
        }
    }

    private void handleButtonEvent() {
        String text = "报名后由<慧生活>服务专员和您电话联系,请保持手机畅通.";
        final HintDialogFragment hintDialog = HintDialogFragment.newInstance(text);
        hintDialog.show(getActivity().getFragmentManager(), "HintDialog");
        hintDialog.setOnHintDialogEventListener(new HintDialogFragment.OnHintDialogEventListener() {
            @Override
            public void onOk(TextView confirm_btn) {
                hintDialog.dismiss();
                if (appointType == null) {
                    // 报名社区活动
                    applyCommunityActivity();
                } else {
                    // 预约首页活动
                    appointHomeActivity();
                }
            }

            @Override
            public void onCancel() {
            }
        });
    }

    /**
     * 社区活动 - 活动报名
     */
    private void applyCommunityActivity() {
        showProgress("正在提交...");
        Engine.authService(shared_token, shared_phone)
                .applyCommunityActivity(activityId, count)
                .enqueue(new Callback<Appointment>() {
                    @Override
                    public void onResponse(Call<Appointment> call, Response<Appointment> response) {
                        if (response.isSuccessful()) {
                            hideProgress();
                            showToast("提交成功");
                        } else {
                            hideProgress();
                            showToast("提交失败");
                        }
                    }

                    @Override
                    public void onFailure(Call<Appointment> call, Throwable t) {
                        hideProgress();
                        showToast("网络错误");
                    }
                });
    }

    /**
     * 首页 - 服务活动预约
     */
    protected void appointHomeActivity() {
        showProgress("正在提交...");
        Engine.authService(shared_token, shared_phone)
            .submitAppointment(appointType, count)
            .enqueue(new Callback<Appointment>() {
                @Override
                public void onResponse(Call<Appointment> call, Response<Appointment> response) {
                    if (response.isSuccessful()) {
                        hideProgress();
                        showToast("提交成功");
                    } else {
                        hideProgress();
                        showToast("提交失败");
                    }
                }

                @Override
                public void onFailure(Call<Appointment> call, Throwable t) {
                    hideProgress();
                    showToast("网络错误");
                }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        detail_image.recycle();
    }
}
