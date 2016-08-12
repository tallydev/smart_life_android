package com.tallty.smart_life_android.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.activity.MainActivity;
import com.tallty.smart_life_android.model.Step;
import com.tallty.smart_life_android.utils.CountDownTimer;
import com.tallty.smart_life_android.utils.DbUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 计步服务
 * 监听器: OnSensorChangeListener
 */
// TODO: 16/8/8 存在问题

/**
 * 1、当手机处于裤子口袋时,步数为实际步数的一半
 * 2、当手机处于手中时,步数等于实际步数
 * 3、当手机处于上半身口袋时,步数待测试
 */
public class StepService extends Service implements SensorEventListener {
    private static final String TAG = "step_service";
    //默认为30秒进行一次存储
    private static int duration = 30000;
    private SensorManager sensorManager;
    private StepCreator stepDetector;
    private Messenger messenger = new Messenger(new MessengerHandler());
    private BroadcastReceiver phoneStatusReceiver;
    private WakeLock mWakeLock;
    private TimeCount time;
    // 计步传感器类型 0-counter 1-detector
    private static int stepSensor = -1;


    /**
     * 传递步数消息
     * 通知接收者,更新页面
     */
    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Const.MSG_FROM_CLIENT:
                    try {
                        Messenger messenger = msg.replyTo;
                        Message replyMsg = Message.obtain(null, Const.MSG_FROM_SERVER);
                        Bundle bundle = new Bundle();
                        bundle.putInt("step", StepCreator.CURRENT_STEP);
                        replyMsg.setData(bundle);
                        messenger.send(replyMsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * 计时器: 保存步数
     */
    private class TimeCount extends CountDownTimer {
        TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            // 如果计时器正常结束，则开始计步
            time.cancel();
            save();
            startTimeCount();
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }
    }


    @Override
    public void onCreate() {
        // 注册广播接收器, 监听手机状态, 并做相应处理
        initBroadcastReceiver();
        // 启动计步器
        new Thread(new Runnable() {
            public void run() {
                startStepDetector();
            }
        }).start();
        // 启动倒计时
        startTimeCount();
        // 初始化今天的数据
        initTodayData();
        // 更新通知
        updateNotification("今日步数：" + StepCreator.CURRENT_STEP + " 步");
        Log.d(TAG, "创建了计步器服务");
    }

    private void initTodayData() {
        DbUtils.createDb(this, "smart_life");
        String current_date = getTodayDate();
        //获取当天的数据，用于展示
        List<Step> list = DbUtils.getQueryByWhere(Step.class, "date", new String[]{current_date});
        if (list.size() == 0 || list.isEmpty()) {
            StepCreator.CURRENT_STEP = 0;
        } else if (list.size() == 1) {
            StepCreator.CURRENT_STEP = Integer.parseInt(list.get(0).getCount());
        }
    }

    private void initBroadcastReceiver() {
        final IntentFilter filter = new IntentFilter();
        // 屏幕灭屏广播
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        // 关机广播
        filter.addAction(Intent.ACTION_SHUTDOWN);
        // 屏幕亮屏广播
        filter.addAction(Intent.ACTION_SCREEN_ON);
        // 屏幕解锁广播
        filter.addAction(Intent.ACTION_USER_PRESENT);
        // 当长按电源键弹出“关机”对话或者锁屏时系统会发出这个广播
        // example：有时候会用到系统对话框，权限可能很高，会覆盖在锁屏界面或者“关机”对话框之上，
        // 所以监听这个广播，当收到时就隐藏自己的对话，如点击pad右下角部分弹出的对话框
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

        phoneStatusReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                String action = intent.getAction();

                if (Intent.ACTION_SCREEN_ON.equals(action)) {
                    Log.d("计步器,Intent状态", "手机屏幕开启");
                } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                    //改为60秒一存储
                    duration = 60000;

                    Log.d("计步器,Intent状态", "手机屏幕关闭");
                } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                    save();
                    //改为30秒一存储
                    duration = 30000;

                    Log.d("计步器,Intent状态", "手机解锁");
                } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
                    // 例如点击Home按钮,保存一次
                    save();

                    Log.d("计步器,Intent状态", "关闭系统对话框");
                } else if (Intent.ACTION_SHUTDOWN.equals(intent.getAction())) {
                    save();

                    Log.d("计步器,Intent状态", "手机关机");
                }
            }
        };
        registerReceiver(phoneStatusReceiver, filter);
    }

    private void startTimeCount() {
        Log.d(TAG, "启动了定时器");
        time = new TimeCount(duration, 1000);
        time.start();
    }

    /**
     * 更新通知栏信息
     */
    @TargetApi (Build.VERSION_CODES.JELLY_BEAN)
    private void updateNotification(String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setPriority(Notification.PRIORITY_MIN);

        //Notification.Builder builder = new Notification.Builder(this);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        builder.setContentIntent(contentIntent);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker("慧生活");
        builder.setContentTitle("慧生活");
        //设置不可清除
        builder.setOngoing(true);
        builder.setContentText(content);
        Notification notification = builder.build();

        startForeground(0, notification);

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(R.string.app_name, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private void startStepDetector() {
        if (sensorManager != null && stepDetector != null) {
            sensorManager.unregisterListener(stepDetector);
            sensorManager = null;
            stepDetector = null;
        }
        getLock(this);
        // 获取传感器管理器的实例
        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        //android4.4以后可以使用计步传感器
        int VERSION_CODES = android.os.Build.VERSION.SDK_INT;

        if (VERSION_CODES >= 19) {
            addCountStepListener();
        } else {
            addBasePedoListener();
        }
    }

    // 使用系统计步器
    @TargetApi (Build.VERSION_CODES.KITKAT)
    private void addCountStepListener() {
        Sensor detectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            stepSensor = 0;
            Log.d(TAG, "countSensor");
            sensorManager.registerListener(StepService.this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else if (detectorSensor != null) {
            stepSensor = 1;
            Log.d(TAG, "detector");
            sensorManager.registerListener(StepService.this, detectorSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Log.d(TAG, "Count sensor not available!");
            addBasePedoListener();
        }
    }

    // 使用BasePedo计步器
    private void addBasePedoListener() {
        stepDetector = new StepCreator(this);
        // 获得传感器的类型，这里获得的类型是加速度传感器
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // 此方法用来注册，只有注册过才会生效，参数：SensorEventListener的实例，Sensor的实例，更新速率
        sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_UI);
        // 反注册计步器
        // sensorManager.unregisterListener(stepDetector);
        // 计步器事件监听器
        stepDetector.setOnSensorChangeListener(new StepCreator.OnSensorChangeListener() {
                    @Override
                    public void onChange() {
                        updateNotification("今日步数：" + StepCreator.CURRENT_STEP + " 步");
                    }
                });
    }

    // 系统计步器监听
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(stepSensor == 0){
            StepCreator.CURRENT_STEP = (int)event.values[0];
        }else if(stepSensor == 1){
            StepCreator.CURRENT_STEP++;
        }
        updateNotification("今日步数：" + StepCreator.CURRENT_STEP + " 步");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * 保存步数到数据库中
     */
    private void save() {
        int tempStep = StepCreator.CURRENT_STEP;
        String current_date = getTodayDate();
        // 保存到数据库
        List<Step> list = DbUtils.getQueryByWhere(Step.class, "date", new String[]{current_date});
        if (list.size() == 0 || list.isEmpty()) {
            /**
             * 自己补充: 如果保存时,数据库没有今天的记录,说明今天已经结束,重置步数,更新通知
             */
            StepCreator.CURRENT_STEP = 0;
            updateNotification("今日步数：" + StepCreator.CURRENT_STEP + " 步");
            // 插入新的一天的步数记录
            Step data = new Step();
            data.setDate(current_date);
            data.setCount(tempStep + "");
            DbUtils.insert(data);

            Log.d(TAG, "插入"+current_date+"新步数记录,总记录数:"+DbUtils.getQueryAll(Step.class).size());
        } else if (list.size() == 1) {
            Step data = list.get(0);
            data.setCount(tempStep + "");
            DbUtils.update(data);
            Log.d(TAG, "更新"+current_date+"今天步数记录:"+list.get(0).getCount()+", 总记录数:"+DbUtils.getQueryAll(Step.class).size());
        }
    }

    private String getTodayDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }


    @Override
    public void onDestroy() {
        //取消前台进程
        stopForeground(true);
        DbUtils.closeDb();
        unregisterReceiver(phoneStatusReceiver);
        Intent intent = new Intent(this, StepService.class);
        startService(intent);
        Log.d("OnDestroy", "重启了计步器服务");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private  void unlock(){
        setLockPatternEnabled(android.provider.Settings.Secure.LOCK_PATTERN_ENABLED,false);
    }

    private void setLockPatternEnabled(String systemSettingKey, boolean enabled) {
        //推荐使用
        android.provider.Settings.Secure.putInt(getContentResolver(), systemSettingKey,enabled ? 1 : 0);
    }

    synchronized private PowerManager.WakeLock getLock(Context context) {
        if (mWakeLock != null) {
            if (mWakeLock.isHeld())
                mWakeLock.release();
            mWakeLock = null;
        }

        if (mWakeLock == null) {
            PowerManager mgr = (PowerManager) context
                    .getSystemService(Context.POWER_SERVICE);
            mWakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    StepService.class.getName());
            mWakeLock.setReferenceCounted(true);
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
            int hour = c.get(Calendar.HOUR_OF_DAY);
            if (hour >= 23 || hour <= 6) {
                mWakeLock.acquire(5000);
            } else {
                mWakeLock.acquire(300000);
            }
        }

        return (mWakeLock);
    }
}
