package com.tallty.smart_life_android.fragment.home;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
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
import com.tallty.smart_life_android.event.StartBrotherEvent;
import com.tallty.smart_life_android.fragment.cart.ConfirmOrderFragment;
import com.tallty.smart_life_android.holder.NetworkImageBannerHolder;
import com.tallty.smart_life_android.model.CartItem;
import com.tallty.smart_life_android.model.Product;
import com.tallty.smart_life_android.model.ProductBanner;
import com.tallty.smart_life_android.utils.ArithUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP;

/**
 * Created by kang on 2017/1/13.
 * 【限量销售】商品详情
 * 和 【product】不是同一个表
 */

public class PromotionShowFragment  extends BaseBackFragment implements OnItemClickListener {
    // 商品
    private Product product;
    private int productId = 0;
    private ArrayList<CartItem> buy_commodities = new ArrayList<>();
    // 修改数量
    private int count = 1;
    // UI
    private TextView product_title;
    private TextView product_price;
    private TextView product_original_price;
    private TextView inventory_and_sales;
    private TextView product_description;
    private Button add;
    private Button reduce;
    private TextView number;
    private TextView buy_now;
    private ConvenientBanner<String> banner;
    private SubsamplingScaleImageView detail_image;
    private ImageView small_detail_image;

    public static PromotionShowFragment newInstance(Product product) {
        Bundle args = new Bundle();
        args.putSerializable(Const.OBJECT, product);
        PromotionShowFragment fragment = new PromotionShowFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static PromotionShowFragment newInstance(int productId) {
        Bundle args = new Bundle();
        args.putSerializable(Const.INT, productId);
        PromotionShowFragment fragment = new PromotionShowFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            product = (Product) args.getSerializable(Const.OBJECT);
            productId = args.getInt(Const.INT);
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_promotion_show;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText("团购商品详情");
    }

    @Override
    protected void initView() {
        product_title = getViewById(R.id.product_detail_title);
        product_price = getViewById(R.id.product_detail_price);
        product_description = getViewById(R.id.product_detail_description);
        product_original_price = getViewById(R.id.product_detail_original_price);
        inventory_and_sales = getViewById(R.id.product_detail_inventory_and_sales);

        add = getViewById(R.id.add);
        reduce = getViewById(R.id.reduce);
        number = getViewById(R.id.product_count);
        buy_now = getViewById(R.id.buy_now);
        banner = getViewById(R.id.product_detail_banner);
        detail_image = getViewById(R.id.product_detail_image);
        small_detail_image = getViewById(R.id.small_product_detail_image);
    }

    @Override
    protected void setListener() {
        add.setOnClickListener(this);
        reduce.setOnClickListener(this);
        buy_now.setOnClickListener(this);
    }

    @Override
    protected void afterAnimationLogic() {
        if (productId != 0) {
            getPromotionById();
            return;
        }
        if (product != null){
            // 显示商品信息
            showProduct();
            // 设置banner
            setBanner();
            // 库存处理
            processCount();
        }
    }

    private void processCount() {
        if (product.getCount() == 0) {
            buy_now.setClickable(false);
            buy_now.setBackgroundColor(showColor(R.color.disable_orange));
            buy_now.setText("库存不足");
            add.setClickable(false);
            number.setText("0");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add:
                if (count < product.getCount()) {
                    number.setText(String.valueOf(++count));
                } else {
                    showToast("库存不足");
                }
                break;
            case R.id.reduce:
                if (count > 1){
                    number.setText(String.valueOf(--count));
                }else{
                    number.setText(String.valueOf(count));
                }
                break;
            case R.id.buy_now:
                if (product.getCount() <= 0) {
                    showToast("库存不足，努力备货中");
                } else {
                    buyNow();
                }
                break;
        }
    }

    /**
     * 我要参团
     */
    private void buyNow() {
        buy_commodities.clear();
        // 把商品封装成一个购物车条目对象
        // 此处的 id 使用 促销商品的 id, 创建团购订单需要
        CartItem cartItem = new CartItem();
        cartItem.setId(product.getId());
        cartItem.setChecked(true);
        cartItem.setName(product.getTitle());
        cartItem.setCount(count);
        cartItem.setPrice(product.getPrice());
        cartItem.setThumb(product.getThumb());
        // 加入列表
        buy_commodities.add(cartItem);
        float total = count * product.getPrice();
        // 显示确认订单, 类型为promotion, 使用促销的创建订单接口
        EventBus.getDefault().post(new StartBrotherEvent(
                ConfirmOrderFragment.newInstance(buy_commodities, ArithUtils.round(total), Const.TYPE_PROMOTION))
        );
    }

    // 获取商品详情
    private void getPromotionById() {
        Engine.noAuthService().getPromotion(productId)
                .enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        if (response.isSuccessful()) {
                            product = response.body();
                            // 显示商品信息
                            showProduct();
                            // 设置banner
                            setBanner();
                            // 库存处理
                            processCount();
                        } else {
                            Log.d(App.TAG, response.message());
                            showToast("获取团购详情失败");
                        }
                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {
                        showToast("网络连接错误");
                    }
                });
    }

    private void showProduct() {
        product_title.setText(product.getTitle());
        product_price.setText("￥ "+product.getPrice());
        product_description.setText(product.getDetail());
        inventory_and_sales.setText("库存量："+product.getCount()+"    销量："+product.getSales());

        String original_price_str = "￥ " + product.getOriginalPrice();
        SpannableString spannableString = new SpannableString(original_price_str);
        spannableString.setSpan(new StrikethroughSpan(), 0, original_price_str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(_mActivity, R.color.gray_text)), 0, original_price_str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        product_original_price.setText(spannableString);

        // 加载详情图
        detail_image.setZoomEnabled(false);
        detail_image.setMinimumScaleType(SCALE_TYPE_CENTER_CROP);
        detail_image.setFocusable(false);
        Glide.with(_mActivity).load(product.getDetailImage())
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
                        showToast("加载商品详情失败");
                    }
                });
    }

    private void setBanner() {
        List<String> networkImages = new ArrayList<>();
        ArrayList<ProductBanner> productBanners = product.getProductBanners();
        for(ProductBanner productBanner : productBanners) {
            networkImages.add(productBanner.getUrl());
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
                    small_detail_image.setVisibility(View.VISIBLE);
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
    public void onDestroy() {
        super.onDestroy();
        detail_image.recycle();
        releaseImageViewResouce(small_detail_image);
    }
}
