package com.tallty.smart_life_android.fragment.cart;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tallty.smart_life_android.App;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.Engine.Engine;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.CartListAdapter;
import com.tallty.smart_life_android.base.BaseLazyMainFragment;
import com.tallty.smart_life_android.custom.RecyclerVIewItemTouchListener;
import com.tallty.smart_life_android.event.CartUpdateItem;
import com.tallty.smart_life_android.event.StartBrotherEvent;
import com.tallty.smart_life_android.event.TabReselectedEvent;
import com.tallty.smart_life_android.event.TabSelectedEvent;
import com.tallty.smart_life_android.fragment.MainFragment;
import com.tallty.smart_life_android.model.CartItem;
import com.tallty.smart_life_android.model.CartList;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kang on 16/6/20.
 * 购物车
 */
public class CartFragment extends BaseLazyMainFragment {
    private String shared_token;
    private String shared_phone;

    private TextView pay;
    private RecyclerView recyclerView;
    private CartListAdapter adapter;
    private CheckBox select_all_btn;
    private TextView total_price_text;
    // 数据
    private ArrayList<CartItem> cartItems = new ArrayList<>();
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
    protected void initLazyView(@Nullable Bundle savedInstanceState) {
        pay.setOnClickListener(this);
        select_all_btn.setChecked(isSelectAll);
        select_all_btn.setOnClickListener(this);
        total_price_text.setText("￥ 0.0");

        getCartList();
    }

    @Override
    public void onClick(View v) {
        float total;
        switch (v.getId()) {
            case R.id.pay:
                // 初始化selected_commodities
                selected_commodities.clear();
                total = 0.0f;
                // 结算时,保存选中商品
                for (CartItem cartItem : cartItems){
                    if (cartItem.isChecked()){
                        total += cartItem.getCount() * cartItem.getPrice();
                        selected_commodities.add(cartItem);
                    }
                }
                if (selected_commodities.size() > 0){
                    EventBus.getDefault().post(new StartBrotherEvent(
                            ConfirmOrder.newInstance(selected_commodities, total))
                    );
                }else{
                    showToast("您还未选择任何商品");
                }
                break;
            case R.id.select_all_btn:
                total = 0.0f;
                if (cartItems.size() > 0){
                    for (CartItem cartItem : cartItems){
                        if (select_all_btn.isChecked()){
                            cartItem.setChecked(true);
                            total += cartItem.getCount() * cartItem.getPrice();
                        }else{
                            cartItem.setChecked(false);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    total_price_text.setText("￥ "+formatTotalPrice(total));
                }else{
                    select_all_btn.setChecked(false);
                    showToast("购物车空空如也");
                }
                break;
        }
    }

    private void getCartList() {
        showProgress(showString(R.string.progress_normal));
        Engine.authService(shared_token, shared_phone)
            .getCartList(1, 100)
            .enqueue(new Callback<CartList>() {
                @Override
                public void onResponse(Call<CartList> call, Response<CartList> response) {
                    if (response.isSuccessful()) {
                        CartList cartList = response.body();
                        cartItems.clear();
                        cartItems.addAll(cartList.getCartItems());
                        // 设置业务参数Checked
                        for (CartItem item : cartItems) {
                            item.setChecked(false);
                        }
                        processRecyclerVIew();
                    } else {
                        showToast(showString(R.string.response_error));
                    }
                    hideProgress();
                }

                @Override
                public void onFailure(Call<CartList> call, Throwable t) {
                    hideProgress();
                    showToast(showString(R.string.network_error));
                }
            });
    }

    private void processRecyclerVIew(){
        // 载入列表
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new CartListAdapter(_mActivity, cartItems);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerVIewItemTouchListener(recyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh, int position) {

            }

            @Override
            public void onItemLongPress(RecyclerView.ViewHolder vh, final int position) {
//                final AlertDialog.Builder builder = new AlertDialog.Builder(_mActivity, R.style.CustomAlertDialogTheme);
//                builder.setMessage("确认删除吗")
//                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                deleteCartItem(position);
//                            }
//                        })
//                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        }).create().show();
            }
        });
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
        total_price_text.setText("￥ "+ formatTotalPrice(total));
    }

    /**
     * format total price
     */
    private String formatTotalPrice(float total) {
        //构造方法的字符格式这里如果小数不足2位,会以0补足.
        DecimalFormat decimalFormat=new DecimalFormat(".00");
        //format 返回的是字符串
        return decimalFormat.format(total);
    }


    /**
     * 订阅事件: CartUpdateItem(int position, CartItem cartItem)
     */
    @Subscribe
    public void onCartUpdateItem(CartUpdateItem event){
        cartItems.set(event.position, event.cartItem);
        adapter.notifyItemChanged(event.position, event.cartItem);
        // 处理【合计】【全选】逻辑
        float total = 0.0f;
        isSelectAll = true;
        for (CartItem cartItem : cartItems){
            if (cartItem.isChecked()){
                float item_total = cartItem.getCount() * cartItem.getPrice();
                total += item_total;
            }else{
                isSelectAll = false;
            }
        }
        // 设置【合计】【全选】
        total_price_text.setText("￥ "+total);
        select_all_btn.setChecked(isSelectAll);
    }

    // 订阅事件:
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
