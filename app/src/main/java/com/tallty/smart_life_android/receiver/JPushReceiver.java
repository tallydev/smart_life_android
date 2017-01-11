package com.tallty.smart_life_android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.activity.MainActivity;
import com.tallty.smart_life_android.event.TransferDataEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 极光推送接收器
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = "[JPushReceiver]====>";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.i(TAG, "onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        // 获取推送 Registration Id
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "接收Registration Id : " + regId);
        }
        // 接收自定义消息
        else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            handleExtraMessage(bundle);
        }
        // 接收到推送下来的通知
        else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "接收到推送下来的通知的ID: " + notificationId);
        }
        // 用户点击打开了通知
        else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            handleUserClick(bundle);
        }
        // 接收到【消息命令】
        else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        }
        // 推送链接状态改变
        else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, intent.getAction() +" connected state change to "+connected);
        }
        // 其他未定义的广播
        else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }



    private void handleExtraMessage(Bundle bundle) {
        Log.d(TAG, "接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        if (MainActivity.isForeground)
            EventBus.getDefault().post(new TransferDataEvent(bundle, Const.JPUSH));
    }

    private void handleUserClick(Bundle bundle) {
        Log.d(TAG, "用户点击打开了通知");
        // 通知首页, 打开通知显示页面
        EventBus.getDefault().post(new TransferDataEvent(bundle, Const.JPUSH));
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:").append(key).append(", value:").append(bundle.getInt(key));
            }else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
                sb.append("\nkey:").append(key).append(", value:").append(bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it =  json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:").append(key).append(", value: [").append(myKey).append(" - ").append(json.optString(myKey)).append("]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:").append(key).append(", value:").append(bundle.getString(key));
            }
        }
        return sb.toString();
    }
}
