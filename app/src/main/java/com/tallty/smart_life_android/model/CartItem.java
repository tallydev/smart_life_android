package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kang on 16/7/23.
 * 购物车列表——条目
 */

public class CartItem implements Serializable{

    public CartItem(){

    }

    @SerializedName ("id")
    @Expose
    private int id;

    @SerializedName ("product_id")
    @Expose
    private int productId;

    @SerializedName ("title")
    @Expose
    private String name;

    @SerializedName ("price")
    @Expose
    private float price;

    @SerializedName ("count")
    @Expose
    private int count;

    @SerializedName ("state")
    @Expose
    private String state;

    @SerializedName ("created_at")
    @Expose
    private String createdAt;

    @SerializedName ("thumb")
    @Expose
    private String thumb;

    // 前端已计算,可不使用
    @SerializedName ("amount")
    @Expose
    private float amount;


    // 业务参数
    @SerializedName ("checked")
    @Expose
    private boolean checked;

    private int thumbID;


    public int getId() {
        return id;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getThumbID() {
        return thumbID;
    }

    public void setThumbID(int thumbID) {
        this.thumbID = thumbID;
    }
}
