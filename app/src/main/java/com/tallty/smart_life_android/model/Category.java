package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kang on 2016/12/13.
 * 商品分类
 */

public class Category implements Serializable {
    @SerializedName ("id")
    @Expose
    private int id;

    @SerializedName ("title")
    @Expose
    private String title;

    @SerializedName ("product_sort_icon")
    @Expose
    private String thumb;

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

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
