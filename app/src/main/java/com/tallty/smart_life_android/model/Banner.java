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
}
