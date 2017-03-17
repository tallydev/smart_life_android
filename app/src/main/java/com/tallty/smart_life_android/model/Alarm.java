package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kang on 2017/3/15.
 * 电子猫眼告警
 */

public class Alarm implements Serializable {

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("time")
    @Expose
    private String time;

    private boolean unread = false;

    @SerializedName("pics")
    @Expose
    private ArrayList<HashMap<String, String>> images = new ArrayList<>();


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

    public ArrayList<HashMap<String, String>> getImages() {
        return images;
    }

    public void setImages(ArrayList<HashMap<String, String>> images) {
        this.images = images;
    }

    public boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }
}
