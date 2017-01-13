package com.tallty.smart_life_android.adapter;

import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.custom.GlideCircleTransform;
import com.tallty.smart_life_android.model.Profile;
import com.tallty.smart_life_android.utils.DpUtil;

import java.util.List;

/**
 * Created by kang on 16/7/26.
 * 我的-账户管理
 */
public class ProfileListAdapter extends BaseMultiItemQuickAdapter<Profile, BaseViewHolder> {

    public ProfileListAdapter(List<Profile> data) {
        super(data);
        addItemType(Profile.IMG, R.layout.item_profile_photo);
        addItemType(Profile.TEXT, R.layout.item_profile);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Profile profile) {
        baseViewHolder.setText(R.id.item_profile_key, profile.getTitle());
        // 设置不同布局
        switch (baseViewHolder.getItemViewType()) {
            case Profile.TEXT:
                baseViewHolder.setText(R.id.item_profile_value, profile.getValue());
                if ("发现新版本".equals(profile.getValue())) {
                    baseViewHolder.setTextColor(R.id.item_profile_value, ContextCompat.getColor(mContext, R.color.orange));
                }
                // 设置分组间隙
                if (profile.isHasGap()) {
                    baseViewHolder.setVisible(R.id.profile_line, false);
                    baseViewHolder.setVisible(R.id.profile_gap, true);
                } else {
                    baseViewHolder.setVisible(R.id.profile_line, true);
                    baseViewHolder.setVisible(R.id.profile_gap, false);
                }
                break;
            case Profile.IMG:
                Glide.with(mContext)
                    .load(profile.getValue())
                    .placeholder(R.drawable.user_default)
                    .error(R.drawable.user_default)
                    .transform(new GlideCircleTransform(mContext))
                    .into((ImageView) baseViewHolder.getView(R.id.item_profile_photo));
                break;
        }
    }
}
