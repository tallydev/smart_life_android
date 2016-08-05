package com.tallty.smart_life_android.fragment.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.event.ConfirmDialogEvent;
import com.tallty.smart_life_android.fragment.Pop.HintDialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 首页-社区it-IT学堂
 */
public class CommunityIt extends BaseBackFragment {
    private String mName;

    private ImageView detail_image;
    private TextView do_event;

    public static CommunityIt newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(Const.TOOLBAR_TITLE, title);
        CommunityIt fragment = new CommunityIt();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mName = args.getString(Const.TOOLBAR_TITLE);
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_community_it;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText(mName);
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);

        detail_image = getViewById(R.id.it_image);
        do_event = getViewById(R.id.do_event);
    }

    @Override
    protected void setListener() {
        do_event.setOnClickListener(this);
    }

    @Override
    protected void afterAnimationLogic() {
        switch (mName){
            case "IT学堂":
                Glide.with(context).load(R.drawable.it_class).into(detail_image);
                do_event.setText("我要预约");
                break;
            case "在线冲印":
                Glide.with(context).load(R.drawable.print_online).into(detail_image);
                do_event.setText("微 我");
                break;
            case "IT服务":
                Glide.with(context).load(R.drawable.it_service).into(detail_image);
                do_event.setText("我要预约");
                break;
            case "更多服务":
                do_event.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.do_event:
                if (!mName.equals("在线冲印")) {
                    HintDialogFragment fragment = HintDialogFragment.newInstance(
                            "预约后由<慧生活>服务专员和您电话联系,请保持手机畅通.", mName);
                    fragment.show(getActivity().getFragmentManager(), "HintDialog");
                }
                break;
        }
    }


    @Subscribe
    public void onConfirmDialogEvnet(ConfirmDialogEvent event) {
        event.dialog.dismiss();
        showToast("预约成功");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
