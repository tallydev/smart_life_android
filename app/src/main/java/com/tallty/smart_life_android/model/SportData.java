package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kang on 16/8/8.
 * 运动数据: 个人统计 & 时间数据列表
 */

public class SportData implements Serializable{
    @SerializedName("self")
    @Expose
    private SportInfo self;

    @SerializedName("detail")
    @Expose
    private ArrayList<SportDetail> detail = new ArrayList<>();


    public SportInfo getSelf() {
        return self;
    }

    public void setSelf(SportInfo self) {
        this.self = self;
    }

    public ArrayList<SportDetail> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<SportDetail> detail) {
        this.detail = detail;
    }
}
