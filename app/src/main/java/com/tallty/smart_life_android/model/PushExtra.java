package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kang on 2017/1/14.
 * 接收到推送 - EXTRA 数据包
 */

public class PushExtra {
    @SerializedName("hshmsg")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
