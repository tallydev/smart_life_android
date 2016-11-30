package com.tallty.smart_life_android.fragment.Common;


import android.content.Context;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.tallty.smart_life_android.App;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.event.ConfirmDialogEvent;
import com.tallty.smart_life_android.fragment.Pop.HintDialogFragment;
import com.tallty.smart_life_android.model.Appointment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

import static com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP;

/**
 * 首页-社区活动-活动详情
 * 首页-社区活动-新品上市
 */
public class GlobalAppointFragment extends BaseBackFragment {
    private String fragmentTitle;
    private String imageUrl;
    private String appointType;
    private Boolean isSingle;
    private String btn_text;
    private Boolean hasAction;
    // 详情图
    private SubsamplingScaleImageView detail_image;
    private ImageView small_detail_image;
    // 多人的操作
    private View countLayout;
    private TextView add;
    private TextView reduce;
    private TextView number;
    private TextView countAppointBtn;
    // 单人的操作
    private View singleLayout;
    private TextView singleAppointBtn;
    // 计数
    private int count = 1;

    // 带有预约操作的详情展示
    public static GlobalAppointFragment newInstance(String title,
                                                    String imageUrl,
                                                    String appointType,
                                                    String btn_text,
                                                    Boolean isSingle) {
        Bundle args = new Bundle();
        args.putString(Const.FRAGMENT_NAME, title);
        args.putString("详情图", imageUrl);
        args.putString(Const.STRING, appointType);
        args.putBoolean("是否是单人", isSingle);
        args.putString("按钮文本", btn_text);
        args.putBoolean("是否有按钮", true);
        GlobalAppointFragment fragment = new GlobalAppointFragment();
        fragment.setArguments(args);
        return fragment;
    }
    // 没有操作的详情展示
    public static GlobalAppointFragment newInstance(String title, String imageUrl) {
        Bundle args = new Bundle();
        args.putString(Const.FRAGMENT_NAME, title);
        args.putString("详情图", imageUrl);
        args.putBoolean("是否有按钮", false);
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
            imageUrl = args.getString("详情图");
            hasAction = args.getBoolean("是否有按钮");
            appointType = args.getString(Const.STRING);
            isSingle = args.getBoolean("是否是单人");
            btn_text = args.getString("按钮文本");
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
        EventBus.getDefault().register(this);
        // 详情图
        small_detail_image = getViewById(R.id.small_detail_image);
        detail_image = getViewById(R.id.detail_image);
        detail_image.setZoomEnabled(false);
        detail_image.setMinimumScaleType(SCALE_TYPE_CENTER_CROP);
        // 单人操作按钮
        singleLayout = getViewById(R.id.single_action_include);
        singleAppointBtn = (TextView) singleLayout.findViewById(R.id.appoint_btn);
        // 多人操作按钮
        countLayout = getViewById(R.id.count_action_include);
        add = (TextView) countLayout.findViewById(R.id.count_add);
        reduce = (TextView) countLayout.findViewById(R.id.count_reduce);
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
        showProgress("正在加载...");
        Glide
            .with(context).load(imageUrl)
            .downloadOnly(new SimpleTarget<File>() {
                @Override
                public void onResourceReady(final File resource, GlideAnimation<? super File> glideAnimation) {
                    detail_image.setImage(ImageSource.uri(Uri.fromFile(resource)), new ImageViewState(1.0f, new PointF(0, 0), 0));
                    detail_image.setOnImageEventListener(new SubsamplingScaleImageView.OnImageEventListener() {
                        @Override
                        public void onReady() {
                            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                            int width = wm.getDefaultDisplay().getWidth();//屏幕宽度
                            int image_w = detail_image.getSWidth(); // 图片宽度
                            float width_ratio = (float) (width * 1.0 / image_w);
                            Log.d(App.TAG, detail_image.getScale()+"缩放比例");
                            Log.d(App.TAG, width_ratio+"宽度比例");

                            if (detail_image.getScale() > width_ratio) {
                                Log.d(App.TAG, "宽图");
                                detail_image.recycle();
                                detail_image.setVisibility(View.GONE);
                                small_detail_image.setVisibility(View.VISIBLE);
                                Glide.with(context).load(resource).into(small_detail_image);
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
                            hideProgress();
                        }

                        @Override
                        public void onTileLoadError(Exception e) {

                        }

                        @Override
                        public void onPreviewReleased() {

                        }
                    });
                }
            });
        if (hasAction) {
            if (isSingle) {
                singleLayout.setVisibility(View.VISIBLE);
                singleAppointBtn.setText(btn_text);
            } else {
                countLayout.setVisibility(View.VISIBLE);
                countAppointBtn.setText(btn_text);
            }
        }
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
                HintDialogFragment fragment_one = HintDialogFragment.newInstance(
                        "报名后由<慧生活>服务专员和您电话联系,请保持手机畅通.", appointType);
                fragment_one.show(getActivity().getFragmentManager(), "HintDialog");
                break;
            case R.id.appoint_btn:
                HintDialogFragment fragment_two = HintDialogFragment.newInstance(
                        "报名后由<慧生活>服务专员和您电话联系,请保持手机畅通.", appointType);
                fragment_two.show(getActivity().getFragmentManager(), "HintDialog");
                break;
        }
    }

    /**
     * 接收预约确认事件
     * @param event
     */
    @Subscribe
    public void onConfirmDialogEvnet(ConfirmDialogEvent event) {
        event.dialog.dismiss();
        if (event.caller != null) {
            submitAppointmentListener(event.caller, count, new OnAppointListener() {
                @Override
                public void onSuccess(Appointment appointment) {
                    showToast("提交成功");
                }

                @Override
                public void onFail(String errorMsg) {
                    showToast("提交失败");
                }

                @Override
                public void onError() {
                    showToast("链接错误");
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        detail_image.recycle();
    }
}
