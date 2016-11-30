package com.tallty.smart_life_android.event;

/**
 * Created by kang on 2016/11/30.
 * tab页被选中事件
 */

public class TabSelectedEvent {
    private int position;

    public TabSelectedEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
