package com.tallty.smart_life_android;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.AndroidLogTool;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by kang on 16/6/14.
 * Application
 */
public class App extends Application{
    private static App sInstance;
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        context = getApplicationContext();
        Logger.init();
    }

    // 判断是否是主线程
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

    public static App getInstance() {
        return sInstance;
    }
}
