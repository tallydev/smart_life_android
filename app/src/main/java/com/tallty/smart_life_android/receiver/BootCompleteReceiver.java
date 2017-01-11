package com.tallty.smart_life_android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tallty.smart_life_android.service.StepService;

/**
 * Created by kang on 16/9/14.
 * 开机完成广播
 */

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, StepService.class);
        context.startService(i);
    }
}
