package com.tallty.smart_life_android.event;

/**
 * Created by kang on 2016/12/18.
 * 支付回调事件
 */

public class PayEvent {
    private String result;

    public PayEvent(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
