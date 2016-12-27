package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kang on 16/7/29.
 * 订单
 */

public class Order implements Serializable {
    @SerializedName ("id")
    @Expose
    private int id;

    @SerializedName ("user_id")
    @Expose
    private int userId;

    @SerializedName ("contactId")
    @Expose
    private int contactId;

    @SerializedName ("seq")
    @Expose
    private String seq;

    // 订单总价 = 商品价格 + 邮费
    @SerializedName ("price")
    @Expose
    private float totalPrice;

    // 商品价格
    @SerializedName ("without_postage")
    @Expose
    private float price;

    // 邮费
    @SerializedName ("postage")
    @Expose
    private float postage;

    @SerializedName ("need_postage")
    @Expose
    private boolean needPostage;

    @SerializedName ("state_alias")
    @Expose
    private String stateAlias;

    @SerializedName ("state")
    @Expose
    private String state;

    @SerializedName ("pay_way")
    @Expose
    private String payWay;

    @SerializedName ("pay_way_alias")
    @Expose
    private String payWayAlias;

    @SerializedName ("created_at")
    @Expose
    private String createdAt;

    @SerializedName ("created_at_output")
    @Expose
    private String created_time;

    @SerializedName ("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName ("updated_at_output")
    @Expose
    private String updated_time;

    @SerializedName("cart_items")
    @Expose
    private List<CartItem> cartItems = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public void setPostage(int postage) {
        this.postage = postage;
    }

    public String getStateAlias() {
        return stateAlias;
    }

    public void setStateAlias(String stateAlias) {
        this.stateAlias = stateAlias;
    }

    public boolean isNeedPostage() {
        return needPostage;
    }

    public void setNeedPostage(boolean needPostage) {
        this.needPostage = needPostage;
    }

    public float getPostage() {
        return postage;
    }

    public void setPostage(float postage) {
        this.postage = postage;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(String updated_time) {
        this.updated_time = updated_time;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getPayWayAlias() {
        return payWayAlias;
    }

    public void setPayWayAlias(String payWayAlias) {
        this.payWayAlias = payWayAlias;
    }
}
