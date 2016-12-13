package com.tallty.smart_life_android.fragment.cart;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.tallty.smart_life_android.App;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.Engine.Engine;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.CartAdapter;
import com.tallty.smart_life_android.base.BaseMainFragment;
import com.tallty.smart_life_android.custom.CustomLoadMoreView;
import com.tallty.smart_life_android.event.CartUpdateItem;
import com.tallty.smart_life_android.event.StartBrotherEvent;
import com.tallty.smart_life_android.event.TabReselectedEvent;
import com.tallty.smart_life_android.event.TabSelectedEvent;
import com.tallty.smart_life_android.event.TransferDataEvent;
import com.tallty.smart_life_android.fragment.Common.GlobalAppointFragment;
import com.tallty.smart_life_android.fragment.MainFragment;
import com.tallty.smart_life_android.fragment.home.ProductShowFragment;
import com.tallty.smart_life_android.model.CartItem;
import com.tallty.smart_life_android.model.CartList;
import com.tallty.smart_life_android.model.Product;
import com.tallty.smart_life_android.utils.GlobalUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kang on 16/6/20.
 * 购物车
 */
public class CartFragment extends BaseMainFragment implements BaseQuickAdapter.RequestLoadMoreListener {
    private String shared_token;
    private String shared_phone;

    private TextView pay;
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private CheckBox select_all_btn;
    private TextView total_price_text;
    // 数据
    private ArrayList<CartItem> cartItems = new ArrayList<>();
    private int current_page = 1;
    private int total_pages = 1;
    private int per_page = 10;
    // checked 相关
    private boolean isSelectAll = false;
    private ArrayList<CartItem> selected_commodities = new ArrayList<>();

    public static CartFragment newInstance() {
        Bundle args = new Bundle();
        CartFragment fragment = new CartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_cart;
    }

    @Override
    protected void initToolBar(Toolbar toolbar, TextView title) {
        title.setText("购物车");
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        shared_token = sharedPre.getString(Const.USER_TOKEN, Const.EMPTY_STRING);
        shared_phone = sharedPre.getString(Const.USER_PHONE, Const.EMPTY_STRING);
        pay = getViewById(R.id.pay);
        recyclerView = getViewById(R.id.cart_list);
        select_all_btn = getViewById(R.id.select_all_btn);
        total_price_text = getViewById(R.id.total_price);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        pay.setOnClickListener(this);
        select_all_btn.setChecked(isSelectAll);
        select_all_btn.setOnClickListener(this);
        total_price_text.setText("￥ 0.0");
        initList();
        getCartList();
    }

    private void initList() {
        // 初始化列表
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new CartAdapter(R.layout.item_cart_list, cartItems);
        adapter.setLoadMoreView(new CustomLoadMoreView());
        adapter.setOnLoadMoreListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemLongClickListener() {
            @Override
            public void onSimpleItemLongClick(BaseQuickAdapter baseQuickAdapter, View view, final int i) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(_mActivity, R.style.CustomAlertDialogTheme);
                builder.setMessage("确认删除吗")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteCartItem(i);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
    }

    private void getCartList() {
        Engine.authService(shared_token, shared_phone)
            .getCartList(current_page, per_page)
            .enqueue(new Callback<CartList>() {
                @Override
                public void onResponse(Call<CartList> call, Response<CartList> response) {
                    if (response.isSuccessful()) {
                        CartList cartList = response.body();
                        current_page = response.body().getCurrentPage();
                        total_pages = response.body().getTotalPages();
                        if (cartItems.size() != cartList.getCartItems().size()) {
                            Log.d(App.TAG, "更新了, 现在的:"+cartItems.size()+"获取的"+cartList.getCartItems().size());
                            cartItems.clear();
                            cartItems.addAll(cartList.getCartItems());
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        showToast(showString(R.string.response_error));
                    }
                }

                @Override
                public void onFailure(Call<CartList> call, Throwable t) {
                    showToast(showString(R.string.network_error));
                }
            });
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (current_page >= total_pages) {
                    adapter.loadMoreEnd();
                } else {
                    current_page++;
                    Engine.noAuthService().getCartList(current_page, per_page).enqueue(new Callback<CartList>() {
                        @Override
                        public void onResponse(Call<CartList> call, Response<CartList> response) {
                            if (response.isSuccessful()) {
                                current_page = response.body().getCurrentPage();
                                total_pages = response.body().getTotalPages();
                                // 商品列表
                                adapter.addData(response.body().getCartItems());
                                adapter.loadMoreComplete();
                            } else {
                                adapter.loadMoreFail();
                            }
                        }

                        @Override
                        public void onFailure(Call<CartList> call, Throwable t) {
                            adapter.loadMoreFail();
                        }
                    });
                }
            }
        }, 1000);
    }

    /**
     * 删除一个购物车条目
     */
    private void deleteCartItem(final int position) {
        showProgress("正在删除...");
        Engine.authService(shared_token, shared_phone)
            .deleteCartItem(cartItems.get(position).getId())
            .enqueue(new Callback<CartItem>() {
                @Override
                public void onResponse(Call<CartItem> call, Response<CartItem> response) {
                    hideProgress();
                    if (response.isSuccessful()) {
                        // 从数据源删除数据
                        cartItems.remove(position);
                        // 通知列表变化
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, cartItems.size()-position);
                        // 重新计算总价
                        setCartListState(cartItems);
                    } else {
                        showToast("删除失败");
                    }
                }

                @Override
                public void onFailure(Call<CartItem> call, Throwable t) {
                    hideProgress();
                    showToast(showString(R.string.network_error));
                }
            });
    }

    /**
     * 购物车列表数据发生改变
     */
    private void setCartListState(ArrayList<CartItem> cartItems){
        // 重新计算总价
        float total = 0.0f;
        if (cartItems.size() > 0){
            for (CartItem cartItem : cartItems){
                if (cartItem.isChecked()){
                    float item_total = cartItem.getCount() * cartItem.getPrice();
                    total += item_total;
                }
            }
        } else {
            select_all_btn.setChecked(false);
        }
        total_price_text.setText("￥ "+ GlobalUtils.floatRound(total));
    }

    // 获取商品详情
    private void getProduct(int product_id) {
        Engine.noAuthService().getProduct(product_id)
            .enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    if (response.isSuccessful()) {
                        EventBus.getDefault().post(
                                new StartBrotherEvent(ProductShowFragment.newInstance(response.body()))
                        );
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay:
                processAccount();
                break;
            case R.id.select_all_btn:
                processSelectAll();
                break;
        }
    }

    // 结算
    private void processAccount() {
        float total = 0.0f;
        selected_commodities.clear();
        // 结算时,保存选中商品
        for (CartItem cartItem : cartItems){
            if (cartItem.isChecked()){
                total += cartItem.getCount() * cartItem.getPrice();
                selected_commodities.add(cartItem);
            }
        }
        if (selected_commodities.size() > 0){
            EventBus.getDefault().post(new StartBrotherEvent(
                    ConfirmOrder.newInstance(selected_commodities, GlobalUtils.floatRound(total)))
            );
        }else{
            showToast("您还未选择任何商品");
        }
    }

    // 全选
    private void processSelectAll() {
        float total = 0.0f;
        if (cartItems.size() == 0) {
            select_all_btn.setChecked(false);
            showToast("购物车空空如也");
            return;
        }
        for (CartItem cartItem : cartItems){
            if (Const.SALE_OFF.equals(cartItem.getState())) continue;
            if (select_all_btn.isChecked()){
                cartItem.setChecked(true);
                total += cartItem.getCount() * cartItem.getPrice();
            }else{
                cartItem.setChecked(false);
            }
        }
        adapter.notifyDataSetChanged();
        total_price_text.setText("￥ " + GlobalUtils.floatRound(total));
    }

    /**
     * 订阅事件: CartUpdateItem(int position, CartItem cartItem)
     */
    @Subscribe
    public void onCartUpdateItem(CartUpdateItem event){
        cartItems.set(event.position, event.cartItem);
        adapter.notifyItemChanged(event.position);
        // 处理【合计】【全选】逻辑
        float total = 0.0f;
        isSelectAll = true;
        for (CartItem cartItem : cartItems){
            if (Const.SALE_OFF.equals(cartItem.getState())) continue;
            if (cartItem.isChecked()){
                float item_total = cartItem.getCount() * cartItem.getPrice();
                total += item_total;
            }else{
                isSelectAll = false;
            }
        }
        // 设置【合计】【全选】
        total_price_text.setText("￥ " + GlobalUtils.floatRound(total));
        select_all_btn.setChecked(isSelectAll);
    }

    // Tab被重复点击时执行的操作
    @Subscribe
    public void onTabReselectedEvent(TabReselectedEvent event) {
        if (event.position == MainFragment.CART)
            Log.d(App.TAG, "购物车被重复点击了");
    }

    // tab页面被选中
    @Subscribe
    public void onTabSelectedEvent(TabSelectedEvent event) {
        if (event.getPosition() == 3) {
            getCartList();
        }
    }

    // 查看商品详情
    @Subscribe
    public void onTransferDataEvent(TransferDataEvent event) {
        if ("cart_product".equals(event.tag)) {
            getProduct(event.bundle.getInt("product_id"));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
