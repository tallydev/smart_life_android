package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kang on 2016/12/27.
 * 首页轮播图
 */

public class Banner implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("position")
    @Expose
    private int position;

    @SerializedName("banner_type")
    @Expose
    private String bannerType;

    @SerializedName("type_id")
    @Expose
    private int typeId;

    @SerializedName("banner_cover")
    @Expose
    private String bannerImage;

    @SerializedName("banner_detail")
    @Expose
    private String bannerDetailImage;


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

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getBannerDetailImage() {
        return bannerDetailImage;
    }

    public void setBannerDetailImage(String bannerDetailImage) {
        this.bannerDetailImage = bannerDetailImage;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getBannerType() {
        return bannerType;
    }

    public void setBannerType(String bannerType) {
        this.bannerType = bannerType;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
}
