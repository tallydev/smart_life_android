package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kang on 16/8/8.
 * 运动数据 - 个人运动统计信息
 */

public class SportInfo implements Serializable{
    @SerializedName("today_count")
    @Expose
    private int todayCount;

    @SerializedName("avg_count")
    @Expose
    private int avgCount;

    @SerializedName("count")
    @Expose
    private int count;

    @SerializedName("rank")
    @Expose
    private int rank;

    @SerializedName("rank_percent")
    @Expose
    private float rankPercent;

    public int getTodayCount() {
        return todayCount;
    }

    public void setTodayCount(int todayCount) {
        this.todayCount = todayCount;
    }

    public int getAvgCount() {
        return avgCount;
    }

    public void setAvgCount(int avgCount) {
        this.avgCount = avgCount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public float getRankPercent() {
        return rankPercent;
    }

    public void setRankPercent(float rankPercent) {
        this.rankPercent = rankPercent;
    }
}
