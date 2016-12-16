package com.tallty.smart_life_android.event;

import com.tallty.smart_life_android.model.Order;

/**
 * Created by kang on 2016/12/16.
 * 管理我的订单事件
 */

public class ManageOrderEvent {
    private int position;
    private Order order;
    private String action;

    public ManageOrderEvent(int position, Order order, String action) {
        this.position = position;
        this.order = order;
        this.action = action;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
