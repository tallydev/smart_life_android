package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kang on 2016/11/23.
 * 活动列表
 */

public class Activities implements Serializable {
    @SerializedName ("activity_sqhds")
    @Expose
    private ArrayList<Activity> activities = new ArrayList<>();

    public ArrayList<Activity> getActivities() {
        return activities;
    }
}
