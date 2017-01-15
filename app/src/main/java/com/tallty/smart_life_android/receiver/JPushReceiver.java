package com.tallty.smart_life_android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.tallty.smart_life_android.App;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.activity.MainActivity;
import com.tallty.smart_life_android.event.TransferDataEvent;
import com.tallty.smart_life_android.model.Push;
import com.tallty.smart_life_android.model.PushExtra;
import com.tallty.smart_life_android.utils.GlobalUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 极光推送接收器
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPushReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "收到的所有数据 - " + intent.getAction() + ", extras: " + printBundle(bundle));
        Log.d(TAG, "-----------------------------------------------------------");
        Log.d(TAG, "接收到推送下来的Extra: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
        Log.d(TAG, "============================================================");

        // 获取推送 Registration Id
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            Log.d(TAG, "接收Registration Id : " + bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID));
        }
        // 接收自定义消息
        else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            handleExtraMessage(bundle);
        }
        // 接收到推送下来的通知
        else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            handleReceive(bundle);
        }
        // 用户点击打开了通知
        else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            handleUserClick(context, bundle);
        }
        // 接收到【消息命令】
        else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            handlePushCallback(bundle);
        }
        // 推送链接状态改变
        else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            handleConnectChange(intent);
        }
        // 其他未定义的广播
        else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }

    private void handleReceive(Bundle bundle) {
        int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
        Log.d(TAG, "接收到推送下来的通知的ID: " + notificationId);
        if (MainActivity.isForeground) {
            Log.d(TAG, "首页 =》 接收到推送下来的通知的ID: " + notificationId);
            Bundle data = getPushData(bundle);
            data.putInt(Const.NOTIFICATION_ID, notificationId);
            EventBus.getDefault().post(new TransferDataEvent(data, Const.JPUSH));
        }
    }

    private void handleUserClick(Context context, Bundle bundle) {
        Log.d(TAG, "用户点击打开了通知");
        // 通知首页, 打开通知显示页面
        Bundle data = getPushData(bundle);
        // APP在前台
        if (MainActivity.isForeground) {
            EventBus.getDefault().post(new TransferDataEvent(data, Const.JPUSH));
        } else {
            if (GlobalUtils.isAppRunning(context)) {
                // APP 后台运行
                Intent mainIntent = new Intent(context, MainActivity.class);
                // 将MainAtivity的launchMode设置成SingleTask, 或者在下面flag中加上Intent.FLAG_CLEAR_TOP,
                // 如果Task栈中有MainActivity的实例，就会把它移到栈顶，把在它之上的Activity都清理出栈，
                // 如果Task栈不存在MainActivity实例，则在栈顶创建
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mainIntent.putExtra(Const.HOME_BUNDLE, data);
                context.startActivity(mainIntent);
            } else {
                // APP 已结束
                // 如果app进程已经被杀死，先重新启动app，将DetailActivity的启动参数传入Intent中，参数经过
                // SplashActivity传入MainActivity，此时app的初始化已经完成，在MainActivity中就可以根据传入
                // 参数跳转到DetailActivity中去了
                Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
                launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                launchIntent.putExtra(Const.HOME_BUNDLE, data);
                context.startActivity(launchIntent);
            }
        }

    }

    private void handleExtraMessage(Bundle bundle) {
        Log.d(TAG, "接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
    }

    private void handlePushCallback(Bundle bundle) {
        // 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        Log.d(TAG, "用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
    }

    private void handleConnectChange(Intent intent) {
        boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        Log.w(TAG, intent.getAction() +" connected state change to "+connected);
    }


    /**
     * 封装收到的推送数据
     * @param bundle 推送返回的 bundle 数据
     * @return
     */
    private Bundle getPushData(Bundle bundle) {
        // 接收 => 推送数据包
        String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        // 解析 => 获取告警数据
        Gson gson = new Gson();
        PushExtra pushExtra = gson.fromJson(extra, PushExtra.class);
        String data = pushExtra.getMessage();
        // 解析 => Push 对象
        Push message = gson.fromJson(data, Push.class);
        Log.i(TAG, message.getTitle() + message.getTime());
        // 提取 => 图片
        ArrayList<String> images = new ArrayList<>();
        for (HashMap<String, String> cache : message.getPics()) {
            images.add(cache.get("url"));
        }
        // 封装为数据包
        Bundle args = new Bundle();
        args.putString(Const.PUSH_TITLE, message.getTitle());
        args.putString(Const.PUSH_TIME, message.getTime());
        args.putStringArrayList(Const.PUSH_IMAGES, images);

        Log.d(TAG, args.toString());
        return args;
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
