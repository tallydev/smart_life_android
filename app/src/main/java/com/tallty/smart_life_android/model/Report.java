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
    private String name;

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

    @SerializedName("advise")
    @Expose
    private String advise;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAdvise() {
        return advise;
    }

    public void setAdvise(String advise) {
        this.advise = advise;
    }
}
