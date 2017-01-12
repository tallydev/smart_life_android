package com.tallty.smart_life_android.event;

import android.app.Dialog;
import android.os.Bundle;

/**
 * Created by kang on 16/7/30.
 * 底部弹出框, 确认事件
 */

public class ConfirmDialogEvent {
    public Dialog dialog;
    public String tag;
    public Bundle data;

    public ConfirmDialogEvent(Dialog dialog, String tag, Bundle data) {
        this.dialog = dialog;
        this.tag = tag;
        this.data = data;
    }
}