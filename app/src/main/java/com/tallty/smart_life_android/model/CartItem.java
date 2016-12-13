package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kang on 16/7/23.
 * 购物车列表——条目
 */

public class CartItem implements Serializable{

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

    @SerializedName ("amount")
    @Expose
    private float amount;

    @SerializedName ("state")
    @Expose
    private String state;

    @SerializedName ("state_alias")
    @Expose
    private String stateAlias;

    @SerializedName ("product_sort")
    @Expose
    private String category;

    @SerializedName ("created_at")
    @Expose
    private String createdAt;

    @SerializedName ("thumb")
    @Expose
    private String thumb;

    // 业务参数
    @SerializedName ("checked")
    @Expose
    private boolean checked;

    public int getId() {
        return id;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateAlias() {
        return stateAlias;
    }

    public void setStateAlias(String stateAlias) {
        this.stateAlias = stateAlias;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
