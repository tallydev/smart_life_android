package com.tallty.smart_life_android.fragment.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;

/**
 * 首页-限量销售-商品详情
 */
public class LimitSailShow extends BaseBackFragment {
    private String mName;
    private int count = 1;

    private Toolbar toolbar;
    private TextView toolbar_title;
    private TextView add;
    private TextView reduce;
    private TextView number;
    private TextView add_to_cart;
    private ImageView product_detail;

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
        product_detail = getViewById(R.id.product_detail_image);
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
        Glide.with(context).load(R.drawable.product_detail).into(product_detail);
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
}
