package com.tallty.smart_life_android.event;

import android.app.Dialog;

/**
 * Created by kang on 16/7/30.
 * 底部弹出框, 确认事件
 */

public class ConfirmDialogEvent {
    public Dialog dialog;
    private String caller;

    public ConfirmDialogEvent(Dialog dialog, String caller) {
        this.dialog = dialog;
        this.caller = caller;
    }

}
