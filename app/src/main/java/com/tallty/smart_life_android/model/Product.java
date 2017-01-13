package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kang on 16/8/4.
 * 模型 - 商品
 * 模型 — 限量销售
 */

public class Product implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("title")
    @Expose
    private String title;

    // 折扣
    @SerializedName("discount_rate")
    @Expose
    private float discountRate;

    // 原价
    @SerializedName("price")
    @Expose
    private float originalPrice;

    // 折后价
    @SerializedName("after_discount")
    @Expose
    private float price;

    @SerializedName("count")
    @Expose
    private int count;

    @SerializedName("sales")
    @Expose
    private int sales;

    @SerializedName("detail")
    @Expose
    private String detail;

    @SerializedName("product_cover")
    @Expose
    private String thumb;

    @SerializedName("product_detail")
    @Expose
    private String detailImage;

    @SerializedName("product_banners")
    @Expose
    private ArrayList<ProductBanner> productBanners;

    // 限量销售
    @SerializedName("expiration_time")
    @Expose
    private String endTime;
    // 限量销售商品状态
    @SerializedName("promotion_state")
    @Expose
    private boolean promotionEnable;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public ArrayList<ProductBanner> getProductBanners() {
        return productBanners;
    }

    public void setProductBanners(ArrayList<ProductBanner> productBanners) {
        this.productBanners = productBanners;
    }

    public String getDetailImage() {
        return detailImage;
    }

    public void setDetailImage(String detailImage) {
        this.detailImage = detailImage;
    }

    public float getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(float originalPrice) {
        this.originalPrice = originalPrice;
    }

    public float getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(float discountRate) {
        this.discountRate = discountRate;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isPromotionEnable() {
        return promotionEnable;
    }

    public void setPromotionEnable(boolean promotionEnable) {
        this.promotionEnable = promotionEnable;
    }
}
