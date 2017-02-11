package com.tallty.smart_life_android.fragment.home;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Button;
import android.widget.ImageButton;
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
import com.tallty.smart_life_android.activity.MainActivity;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.event.StartBrotherEvent;
import com.tallty.smart_life_android.event.SwitchTabFragment;
import com.tallty.smart_life_android.event.TabSelectedEvent;
import com.tallty.smart_life_android.fragment.MainFragment;
import com.tallty.smart_life_android.fragment.cart.CartWithBackFragment;
import com.tallty.smart_life_android.holder.NetworkImageBannerHolder;
import com.tallty.smart_life_android.model.CartItem;
import com.tallty.smart_life_android.model.Product;
import com.tallty.smart_life_android.model.ProductBanner;
import com.tallty.smart_life_android.utils.ImageUtils;

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
    private int productId = 0;
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
    private TextView add_to_cart;
    private ConvenientBanner<String> banner;
    private SubsamplingScaleImageView detail_image;
    private ImageView small_detail_image;
    // toolbar
    private ImageButton productCartBtn;
    private TextView cartCountText;

    public static ProductShowFragment newInstance(Product product) {
        Bundle args = new Bundle();
        args.putSerializable(Const.OBJECT, product);
        ProductShowFragment fragment = new ProductShowFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ProductShowFragment newInstance(int productId) {
        Bundle args = new Bundle();
        args.putSerializable(Const.INT, productId);
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
            productId = args.getInt(Const.INT);
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_product_show;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText("商品详情");
        // 点击事件
        productCartBtn = getViewById(R.id.product_cart_btn);
        productCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(CartWithBackFragment.newInstance());
            }
        });
        // 购物车数量
        cartCountText = getViewById(R.id.product_cart_count);
        setToolbarBadge();
    }

    /**
     * 显示购物车badge
     */
    private void setToolbarBadge() {
        if (HomeFragment.cartCount > 9)
            cartCountText.setText("9+");
        else if (HomeFragment.cartCount == 0)
            cartCountText.setVisibility(View.GONE);
        else
            cartCountText.setText(HomeFragment.cartCount+"");
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
        if (productId != 0) {
            getProductById();
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
            add_to_cart.setClickable(false);
            add_to_cart.setBackgroundColor(showColor(R.color.disable_orange));
            add_to_cart.setText("库存不足");
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
            case R.id.add_to_cart:
                if (product.getCount() <= 0) {
                    showToast("库存不足，努力备货中");
                } else {
                    addProductToCart();
                }
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
                            showToast("已加入购物车");
                            HomeFragment.cartCount++;
                            setToolbarBadge();
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

    // 获取商品详情
    private void getProductById() {
        Engine.authService(shared_token, shared_phone).getProduct(productId)
            .enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    if (response.isSuccessful()) {
                        product = response.body();
                        // 显示商品信息
                        showProduct();
                        // 设置banner
                        setBanner();
                    } else {
                        Log.d(App.TAG, response.message());
                        showToast("获取详情失败");
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
        setLongImage();
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

    private void setLongImage() {
        detail_image.setZoomEnabled(false);
        detail_image.setMinimumScaleType(SCALE_TYPE_CENTER_CROP);
        detail_image.setFocusable(false);
        Glide.with(_mActivity).load(product.getDetailImage())
                .downloadOnly(new SimpleTarget<File>() {
                    @Override
                    public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                        Bitmap bitmap = BitmapFactory.decodeFile(resource.getAbsolutePath(), ImageUtils.getBitmapOption(10));
                        int imageWidth = bitmap.getWidth() * 8;
                        int imageHeight = bitmap.getHeight() * 8;
                        Log.d(App.TAG, MainActivity.windowWidth+"获取设备宽度");
                        Log.d(App.TAG, MainActivity.windowHeight+"获取设备宽度");
                        Log.d(App.TAG, imageWidth+"获取图片的宽度");
                        Log.d(App.TAG, imageHeight+"获取图片的高度");
                        float scale = (float) (MainActivity.windowWidth * 1.0 / imageWidth);
                        Log.d(App.TAG, scale+"获取缩放");
                        // 长图展示
                        detail_image.setImage(ImageSource.uri(Uri.fromFile(resource)), new ImageViewState(scale, new PointF(0, 0), 0));
                        // 监听: 如果图片高度小于设备高度, 使用普通加载方式
                        onImageLoadListener(resource, imageHeight);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        hideProgress();
                        showToast("加载商品详情失败");
                    }
                });
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
    private void onImageLoadListener(final File resource, final int imageHeight) {
        detail_image.setOnImageEventListener(new SubsamplingScaleImageView.OnImageEventListener() {
            @Override
            public void onReady() {
                if (imageHeight < MainActivity.windowHeight) {
                    Log.d(App.TAG, "使用普通ImageView加载宽图");
                    detail_image.recycle();
                    detail_image.setVisibility(View.GONE);
                    small_detail_image.setVisibility(View.VISIBLE);
                    Glide.with(_mActivity)
                        .load(resource)
                        .error(R.drawable.image_error)
                        .placeholder(R.drawable.image_placeholder)
                        .into(small_detail_image);
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
