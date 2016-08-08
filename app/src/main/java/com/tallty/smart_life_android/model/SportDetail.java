package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kang on 16/8/8.
 * 运动数据 - 详情列表
 */

public class SportDetail implements Serializable {
    @SerializedName("tag")
    @Expose
    private String tag;

    @SerializedName("count")
    @Expose
    private int count;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
