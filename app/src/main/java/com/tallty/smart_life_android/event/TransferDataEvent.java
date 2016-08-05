package com.tallty.smart_life_android.event;

import android.os.Bundle;

/**
 * Created by kang on 16/8/3.
 * 通用事件
 * 传递数据
 */

public class TransferDataEvent {
    public Bundle bundle;
    public String tag;

    public TransferDataEvent(Bundle bundle, String tag) {
        this.bundle = bundle;
        this.tag = tag;
    }
}
