package com.tallty.smart_life_android.model;

/**
 * Created by kang on 2016/12/16.
 * 健康建议
 */

public class Advise {
    public Advise(String title, String advise) {
        this.title = title;
        this.advise = advise;
    }

    private String title;
    private String advise;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAdvise() {
        return advise;
    }

    public void setAdvise(String advise) {
        this.advise = advise;
    }
}
