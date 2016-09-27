package com.tallty.smart_life_android.event;

/**
 * Created by kang on 2016/9/27.
 * 清空保存的天的逐小时步数
 */

public class ClearDayStepEvent {
    public boolean to_clear;

    public ClearDayStepEvent(boolean to_clear) {
        this.to_clear = to_clear;
    }
}
