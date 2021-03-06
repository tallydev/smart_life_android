package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kang on 2017/3/15.
 * 告警列表
 */

public class Alarms implements Serializable {
    @SerializedName ("total")
    @Expose
    private int totalPages;

    @SerializedName("status")
    @Expose
    private boolean present;

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("result")
    @Expose
    private ArrayList<Alarm> alarms = new ArrayList<>();

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<Alarm> getAlarms() {
        return alarms;
    }

    public void setAlarms(ArrayList<Alarm> alarms) {
        this.alarms = alarms;
    }
}
