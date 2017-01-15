package com.tallty.smart_life_android;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.content.Context;

import com.pgyersdk.crash.PgyCrashManager;
import com.squareup.leakcanary.LeakCanary;
import com.tallty.smart_life_android.activity.MainActivity;

import java.util.List;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by kang on 16/6/14.
 * Application
 */
public class App extends Application{
    private static App sInstance;
    public static final String TAG = "smart_life";

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        // 极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        setPushNotificationBuilder();
        // 内存泄露测试
        LeakCanary.install(this);
        // 注册蒲公英Crash接口
        PgyCrashManager.register(this);
    }

    public static App getInstance() {
        return sInstance;
    }

    /**
     * 判断是否是主线程
     */
    public boolean isMainProcess() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 设置推送通知的样式
     */
    private void setPushNotificationBuilder() {
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(this);
        builder.statusBarDrawable = R.mipmap.ic_launcher;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
                | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
        builder.notificationDefaults = Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE
                | Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
        JPushInterface.setPushNotificationBuilder(3, builder);
    }
}
