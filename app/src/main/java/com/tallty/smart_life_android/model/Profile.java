package com.tallty.smart_life_android.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by kang on 2017/1/9.
 * 个人资料
 */

public class Profile implements MultiItemEntity {
    public static final int TEXT = 1;
    public static final int IMG = 2;
    private String title;
    private String value;
    private int itemType;
    private boolean hasGap;

    public Profile(String title, String value, int itemType, boolean hasGap) {
        this.title = title;
        this.value = value;
        this.itemType = itemType;
        this.hasGap = hasGap;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public boolean isHasGap() {
        return hasGap;
    }

    public void setHasGap(boolean hasGap) {
        this.hasGap = hasGap;
    }
}
