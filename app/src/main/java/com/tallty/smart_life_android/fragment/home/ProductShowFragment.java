package com.tallty.smart_life_android.fragment.home;


import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
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
import com.tallty.smart_life_android.event.SwitchTabFragment;
import com.tallty.smart_life_android.event.TransferDataEvent;
import com.tallty.smart_life_android.fragment.MainFragment;
import com.tallty.smart_life_android.holder.NetworkImageBannerHolder;
import com.tallty.smart_life_android.model.Banner;
import com.tallty.smart_life_android.model.CartItem;
import com.tallty.smart_life_android.model.Product;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP;

/**
 * 首页-限量销售-商品详情
 */
public class ProductShowFragment extends BaseBackFragment implements OnItemClickListener{
    // 商品
    private Product product;
    // 修改数量
    private int count = 1;
    // UI
    private TextView product_title;
    private TextView product_price;
    private TextView product_description;
    private Button add;
    private Button reduce;
    private TextView number;
    private TextView add_to_cart;
    private ConvenientBanner<String> banner;
    private SubsamplingScaleImageView detail_image;
    private ImageView small_detail_image;

    public static ProductShowFragment newInstance(Product product) {
        Bundle args = new Bundle();
        args.putSerializable(Const.OBJECT, product);
        ProductShowFragment fragment = new ProductShowFragment();
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
        return R.layout.fragment_product_show;
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
        detail_image = getViewById(R.id.product_detail_image);
        small_detail_image = getViewById(R.id.small_product_detail_image);
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
                addProductToCart();
                break;
        }
    }

    private void addProductToCart() {
        showProgress("正在添加...");
        Engine.authService(shared_token, shared_phone)
                .addProductToCart(product.getId(), count)
                .enqueue(new Callback<CartItem>() {
                    @Override
                    public void onResponse(Call<CartItem> call, Response<CartItem> response) {
                        hideProgress();
                        if (response.isSuccessful()) {
                            toolbar.getMenu().clear();
                            toolbar.inflateMenu(R.menu.cart_has);
                            showToast("已加入购物车");
                        } else {
                            showToast("加入购物车失败");
                        }
                    }

                    @Override
                    public void onFailure(Call<CartItem> call, Throwable t) {
                        hideProgress();
                        showToast("网络链接错误");
                    }
                });
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
        product_price.setText("￥ "+product.getPrice());
        product_description.setText(product.getDetail());
        // 加载详情图
        detail_image.setZoomEnabled(false);
        detail_image.setMinimumScaleType(SCALE_TYPE_CENTER_CROP);
        detail_image.setFocusable(false);
        Glide.with(context).load(product.getDetailImage())
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

    private void setBanner() {
        List<String> networkImages = new ArrayList<>();
        ArrayList<Banner> banners = product.getBanners();
        for(Banner banner : banners) {
            networkImages.add(banner.getUrl());
        }
        banner.setPages(new CBViewHolderCreator<NetworkImageBannerHolder>() {
                @Override
                public NetworkImageBannerHolder createHolder() {
                    return new NetworkImageBannerHolder();
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

    // 图片载入监听
    private void onImageLoadListener(final File resource) {
        detail_image.setOnImageEventListener(new SubsamplingScaleImageView.OnImageEventListener() {
            @Override
            public void onReady() {
                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
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
            }

            @Override
            public void onTileLoadError(Exception e) {
            }

            @Override
            public void onPreviewReleased() {
            }
        });
    }
}
