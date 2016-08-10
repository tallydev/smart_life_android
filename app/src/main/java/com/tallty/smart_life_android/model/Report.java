package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kang on 16/8/10.
 * 健康报告-item
 */

public class Report implements Serializable {
    @SerializedName ("name")
    @Expose
    private String height;

    @SerializedName("alias")
    @Expose
    private String alias;

    @SerializedName("hint")
    @Expose
    private String hint;

    @SerializedName("value")
    @Expose
    private float value;

    @SerializedName("state")
    @Expose
    private String state;


    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
