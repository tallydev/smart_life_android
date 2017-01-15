package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kang on 2017/1/14.
 * 推送 Extra 数据模型
 */

public class Push {
    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("pics")
    @Expose
    private ArrayList<HashMap<String, String>> pics = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<HashMap<String, String>> getPics() {
        return pics;
    }

    public void setPics(ArrayList<HashMap<String, String>> pics) {
        this.pics = pics;
    }
}
