package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kang on 16/8/8.
 * 运动列表- 条目
 */

public class SportRankItem implements Serializable {
    @SerializedName("index")
    @Expose
    private int index;

    @SerializedName("avatar")
    @Expose
    private String avatar;

    @SerializedName("nickname")
    @Expose
    private String nickname;

    @SerializedName("count")
    @Expose
    private int count;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
