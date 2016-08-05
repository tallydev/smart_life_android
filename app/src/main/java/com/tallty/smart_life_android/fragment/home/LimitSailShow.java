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
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.event.SwitchTabFragment;
import com.tallty.smart_life_android.event.TransferDataEvent;
import com.tallty.smart_life_android.fragment.MainFragment;
import com.tallty.smart_life_android.holder.BannerHolderView;
import com.tallty.smart_life_android.model.Product;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.List;

/**
 * 首页-限量销售-商品详情
 */
public class LimitSailShow extends BaseBackFragment implements OnItemClickListener{
    private static final String TAG = "LimitSailShow";
    // 商品
    private Product product;
    // 修改数量
    private int count = 1;
    // UI
    private TextView product_title;
    private TextView product_price;
    private TextView product_description;
    private TextView add;
    private TextView reduce;
    private TextView number;
    private TextView add_to_cart;
    private ConvenientBanner<Integer> banner;
    // banner图数据
    private Integer[] firstImages = { R.drawable.product_pineapple_one, R.drawable.product_pineapple_two, R.drawable.product_pineapplie_three };
    private Integer[] secondImages = { R.drawable.product_honey_one, R.drawable.product_honey_two, R.drawable.product_honey_three };
    private Integer[] thirdImages = { R.drawable.product_egg_one, R.drawable.product_egg_two, R.drawable.product_egg_three };

    public static LimitSailShow newInstance(Product product) {
        Bundle args = new Bundle();
        args.putSerializable(Const.OBJECT, product);
        LimitSailShow fragment = new LimitSailShow();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            product = (Product) args.getSerializable(Const.OBJECT);
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_limit_sail_show;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText("商品详情");
    }

    @Override
    protected void initView() {
        product_title = getViewById(R.id.product_detail_title);
        product_price = getViewById(R.id.product_detail_price);
        product_description = getViewById(R.id.product_detail_description);
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
        setToolbarMenu(toolbar);
        // 显示商品信息
        showProduct();
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
                // TODO: 16/8/4 添加商品到购物车
                addProductToCart();
                break;
        }
    }

    private void addProductToCart() {
        // TODO: 16/8/5 暂时使用事件传递, 后来要调用接口
        Bundle bundle = new Bundle();
        bundle.putSerializable(Const.OBJECT, product);
        bundle.putInt(Const.INT, count);
        EventBus.getDefault().post(new TransferDataEvent(bundle, "LimitSailShow"));

        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.cart_has);
        showToast("加入成功");
    }

    /**
     * 判断购物车是否为空
     * 修改图标
     * 设置toolbar的菜单按钮
     */
    private void setToolbarMenu(Toolbar toolbar) {
        boolean blank = true;
        // TODO: 16/8/1 调用接口判断购物车是否为空

        if (blank) {
            toolbar.inflateMenu(R.menu.cart_blank);
        } else {
            toolbar.inflateMenu(R.menu.cart_has);
        }

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.toolbar_cart:
                        // 调到MainFragment
                        popTo(MainFragment.class, false, new Runnable() {
                            @Override
                            public void run() {
                                // 通知MainFragment切换CartFragment
                                EventBus.getDefault().post(new SwitchTabFragment(3));
                            }
                        });
                }
                return true;
            }
        });
    }

    private void showProduct() {
        product_title.setText(product.getTitle());
        product_price.setText("￥ "+product.getPrice()+"0 (包邮)");
        product_description.setText(" "+showString(product.getStringId()));
    }

    private void setBanner() {
        // TODO: 16/8/4 假数据
        List<Integer> networkImages;
        if (product.getTag() == 0) {
            networkImages = Arrays.asList(firstImages);
        } else if (product.getTag() == 1) {
            networkImages = Arrays.asList(secondImages);
        } else {
            networkImages = Arrays.asList(thirdImages);
        }

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
