package com.tallty.smart_life_android.fragment.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.holder.BannerHolderView;

import java.util.Arrays;
import java.util.List;

/**
 * 首页-限量销售-商品详情
 */
public class LimitSailShow extends BaseBackFragment implements OnItemClickListener{
    private String mName;
    private int count = 1;

    private Toolbar toolbar;
    private TextView toolbar_title;
    private TextView add;
    private TextView reduce;
    private TextView number;
    private TextView add_to_cart;
    private ConvenientBanner banner;
    // banner图数据
    private Integer[] imagesUrl = { R.drawable.banner_one, R.drawable.banner_two };

    public static LimitSailShow newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(TOOLBAR_TITLE, title);
        LimitSailShow fragment = new LimitSailShow();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mName = args.getString(TOOLBAR_TITLE);
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_limit_sail_show;
    }

    @Override
    protected void initView() {
        toolbar = getViewById(R.id.toolbar);
        toolbar_title = getViewById(R.id.toolbar_title);
        add = getViewById(R.id.add);
        reduce = getViewById(R.id.reduce);
        number = getViewById(R.id.number);
        add_to_cart = getViewById(R.id.add_to_cart);
        banner = getViewById(R.id.product_detail_banner);
    }

    @Override
    protected void setListener() {
        add.setOnClickListener(this);
        reduce.setOnClickListener(this);
        add_to_cart.setOnClickListener(this);
    }

    @Override
    protected void afterAnimationLogic() {
        initBackToolbar(toolbar);
        toolbar_title.setText(mName);
        setToolbarMenu(toolbar);
        // 设置banner
        setBanner();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add:
                number.setText(String.valueOf(++count));
                break;
            case R.id.reduce:
                if (count > 1){
                    number.setText(String.valueOf(--count));
                }else{
                    number.setText(String.valueOf(count));
                }
                break;
            case R.id.add_to_cart:
                setSnackBar(add_to_cart,
                        "添加成功",
                        100000, R.layout.snackbar_icon, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                add_to_cart.setText("添加成功");
                                add_to_cart.setClickable(false);
                            }
                        });
                break;
        }
    }

    /**
     * 设置toolbar的菜单按钮
     */
    private void setToolbarMenu(Toolbar toolbar) {
        toolbar.inflateMenu(R.menu.cart);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.toolbar_cart:
                        // 进入购物车

                }
                return true;
            }
        });
    }

    private void setBanner() {
        List<Integer> networkImages = Arrays.asList(imagesUrl);
        banner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new BannerHolderView();
            }
        }, networkImages)
                .setPageIndicator(new int[] {R.mipmap.banner_indicator, R.mipmap.banner_indicator_focused})
                .setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onResume() {
        super.onResume();
        banner.startTurning(5000);
    }

    @Override
    public void onPause() {
        super.onPause();
        banner.stopTurning();
    }
}