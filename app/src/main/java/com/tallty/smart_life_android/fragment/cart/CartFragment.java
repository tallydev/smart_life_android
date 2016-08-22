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

import com.orhanobut.logger.Logger;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.CartListAdapter;
import com.tallty.smart_life_android.base.BaseLazyMainFragment;
import com.tallty.smart_life_android.custom.RecyclerVIewItemTouchListener;
import com.tallty.smart_life_android.event.CartUpdateItem;
import com.tallty.smart_life_android.event.StartBrotherEvent;
import com.tallty.smart_life_android.event.TabSelectedEvent;
import com.tallty.smart_life_android.event.TransferDataEvent;
import com.tallty.smart_life_android.fragment.MainFragment;
import com.tallty.smart_life_android.model.CartItem;
import com.tallty.smart_life_android.model.CartList;
import com.tallty.smart_life_android.model.Product;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kang on 16/6/20.
 * 购物车
 */
public class CartFragment extends BaseLazyMainFragment {
    private TextView pay;
    private RecyclerView recyclerView;
    private CartListAdapter adapter;
    private CheckBox select_all_btn;
    private TextView total_price_text;
    private AlertDialog.Builder builder = null;
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
                    total_price_text.setText("￥ "+total);
                }else{
                    select_all_btn.setChecked(false);
                    showToast("购物车空空如也");
                }
                break;
        }
    }

    private void getCartList() {
        // TODO: 16/8/5 LimitSailShow传来的死数据,待处理
//        showProgress(showString(R.string.progress_normal));
//        // 获取网络数据
//        Engine.authService().getCartList(1, 10).enqueue(new Callback<CartList>() {
//            @Override
//            public void onResponse(Call<CartList> call, Response<CartList> response) {
//                if (response.code() == 200) {
//                    CartList cartList = response.body();
//                    cartItems = cartList.getCartItems();
//                    // 设置业务参数Checked
//                    for (CartItem item : cartItems) {
//                        item.setChecked(false);
//                    }
//
//                    processRecyclerVIew();
//
//                    hideProgress();
//                } else {
//                    hideProgress();
//                    showToast(showString(R.string.response_error));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CartList> call, Throwable t) {
//                hideProgress();
//                showToast(showString(R.string.network_error));
//            }
//        });

        processRecyclerVIew();
    }

    private void processRecyclerVIew(){
        // 载入列表
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new CartListAdapter(context, cartItems);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerVIewItemTouchListener(recyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh, int position) {

            }

            @Override
            public void onItemLongPress(RecyclerView.ViewHolder vh, final int position) {
                // Use the Builder class for convenient dialog construction
                builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialogTheme);
                builder.setMessage("确定删除该商品吗?")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // 从数据源删除数据
                                cartItems.remove(position);
                                // 通知列表变化
                                adapter.notifyItemRemoved(position);
                                adapter.notifyItemRangeChanged(position, cartItems.size()-position);

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
                                total_price_text.setText("￥ "+total);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }).create();
                builder.show();
            }
        });
    }

    /**
     * 订阅事件: CartUpdateItem(int position, CartItem cartItem)
     */
    @Subscribe
    public void onCartUpdateItem(CartUpdateItem event){
        Log.d("接受了事件", "===》");
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

    /**
     * 订阅事件:
     * Tab Cart按钮被重复点击时执行的操作
     */
    @Subscribe
    public void onTabSelectedEvent(TabSelectedEvent event) {
        if (event.position == MainFragment.CART)
            Log.d("tab-reselected", "购物车被重复点击了");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 订阅事件: TransferDataEvent
     * 接收商品详情加入购物车事件
     */
    @Subscribe
    public void onTransferDataEvent(TransferDataEvent event) {
        if ("LimitSailShow".equals(event.tag)) {
            Bundle bundle = event.bundle;
            int count = bundle.getInt(Const.INT);
            Product product = (Product) bundle.getSerializable(Const.OBJECT);

            CartItem cartItem = new CartItem();
            cartItem.setChecked(false);
            // TODO: 16/8/5 记得修改,
            cartItem.setThumbID(product.getThumbId());
            cartItem.setPrice(product.getPrice());
            cartItem.setCount(count);
            cartItem.setName(product.getTitle());

            cartItems.add(cartItem);

            adapter.notifyDataSetChanged();

            Logger.d("接受数据");
        }
    }
}
