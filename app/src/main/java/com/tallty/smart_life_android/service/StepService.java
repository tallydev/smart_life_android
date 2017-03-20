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
import android.content.pm.PackageManager;
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

import com.tallty.smart_life_android.App;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.Engine.Engine;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.activity.MainActivity;
import com.tallty.smart_life_android.model.SportData;
import com.tallty.smart_life_android.model.Step;
import com.tallty.smart_life_android.utils.GlobalUtils;
import com.tallty.smart_life_android.custom.CountDownTimer;
import com.tallty.smart_life_android.utils.DbUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 计步服务
 * 监听器: OnSensorChangeListener
 *
 * 流程: 1、注册广播器; 2、启动计步器; 3、启动计时器; 4、初始化当天数据; 5、更新通知栏
 * 计时器: 定时save();
 *
 * 结果,产出唯一对外数据: ==> StepCreator.current_step
 */

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class StepService extends Service implements SensorEventListener {
    private final String TAG = "StepService";
    private String shared_token;
    private String shared_phone;
    private SharedPreferences sharedPre;
    //默认为30秒进行一次存储
    private static int duration = 30000;
    private static String CURRENT_DATE = "";
    private SensorManager sensorManager;
    private StepCreator stepCreator;
    private NotificationManager nm;
    private NotificationCompat.Builder builder;
    private Messenger messenger = new Messenger(new MessengerHandler());
    private BroadcastReceiver phoneStatusReceiver;
    private WakeLock mWakeLock;
    private TimeCount time;
    private String DB_NAME = "smart_life";
    // 系统计步传感器类型 0-counter 1-detector
    private static int stepSensor = -1;

    /**
     * 消息处理:
     * 1、MSG_FROM_CLIENT, 来自业务部分的步数请求, -> 发送: 步数 + MSG_FROM_SERVER
     * 2、SET_STEP, 来自业务部分的计步器重置请求, -> 重置计步器、发送: 步数 + MSG_FROM_SERVER
     */
    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Const.MSG_FROM_CLIENT:
                    sendStepAndMsg(msg);
                    break;
                case Const.SET_STEP:
                    StepCreator.CURRENT_STEP = msg.getData().getInt("step");
                    sendStepAndMsg(msg);
                    Log.i(App.TAG, "StepService 设置了计步器步数:  "+StepCreator.CURRENT_STEP);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }

        // 发送step 和 msg
        private void sendStepAndMsg(Message msg) {
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
        }
    }

    @Override
    public void onCreate() {
        sharedPre = this.getSharedPreferences("SmartLife", Context.MODE_PRIVATE);
        shared_token = sharedPre.getString(Const.USER_TOKEN, Const.EMPTY_STRING);
        shared_phone = sharedPre.getString(Const.USER_PHONE, Const.EMPTY_STRING);
        // 注册广播接收器, 监听手机状态, 并做相应处理
        initBroadcastReceiver();
        // 开启一个新的线程启动计步器
        new Thread(new Runnable() {
            public void run() {
                startStepDetector();
            }
        }).start();
        // 启动倒计时, 每个周期结束, 执行save() 创建或更新数据
        startTimeCount();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 初始化今天的数据
        initTodayData();
        // 初始化第一次安装APP的计步器数据
        initFirstStart();
        // 更新通知
        updateNotification("今日步数：" + StepCreator.CURRENT_STEP + " 步");
        Log.d(App.TAG, "创建了计步器服务");
        return START_STICKY;
    }

    /**
     * 初次安装时的逻辑, 初始化计步器, 解决重装步数重置的问题
     */
    private void initFirstStart() {
        if (sharedPre.getBoolean("first_start", true)) {
            Engine.authService(shared_token, shared_phone).getSportsData("daily").enqueue(new Callback<SportData>() {
                @Override
                public void onResponse(Call<SportData> call, Response<SportData> response) {
                    if (response.code() == 200) {
                        int count = response.body().getSelf().getCount();
                        String current_date = GlobalUtils.getTodayDate();
                        DbUtils.createDb(StepService.this, Const.DB_NAME);
                        // 保存到数据库 (不保存, stepService中无记录是会重置 current_step);
                        Step data = new Step();
                        data.setDate(current_date);
                        data.setCount(count + "");
                        DbUtils.insert(data);
                        Log.d(App.TAG, "插入"+current_date+"新步数记录, 总记录数:"+DbUtils.getQueryAll(Step.class).size());
                        // 初始化计步器
                        StepCreator.CURRENT_STEP = count;
                        Log.i(App.TAG, "初次安装并登录, 更新本地步数");
                        // 将登录标志位设置为false，下次登录时初始化计步器
                        SharedPreferences.Editor editor = sharedPre.edit();
                        editor.putBoolean("first_start", false);
                        editor.apply();
                    } else {
                        Log.d(App.TAG, "首次安装, 获取步数信息失败");
                    }
                }

                @Override
                public void onFailure(Call<SportData> call, Throwable t) {
                    Log.d(App.TAG, "上传步数链接服务器失败");
                }
            });
            // 将登录标志位设置为false，下次登录时初始化计步器
            SharedPreferences.Editor editor = sharedPre.edit();
            editor.putBoolean("first_start", false);
            editor.apply();
        }
    }

    /**
     * 初始化当天数据:
     * 查询到值: 赋值给step
     * 未查询到: step = 0
     */
    private void initTodayData() {
        CURRENT_DATE = getTodayDate();
        DbUtils.createDb(this, DB_NAME);
        //获取当天的数据，用于展示
        ArrayList list = DbUtils.getQueryByWhere(Step.class, "date", new String[]{CURRENT_DATE});

        if (list.size() == 0 || list.isEmpty()) {
            // 查不到今天的记录 => 新的一天: step = 0
            StepCreator.CURRENT_STEP = 0;
        } else if (list.size() == 1) {
            // 查询到当天数据, step = data
            Step step = (Step) list.get(0);
            StepCreator.CURRENT_STEP = Integer.parseInt(step.getCount());
        } else {
            Log.v(TAG, "出错了！");
        }
    }

    private void initBroadcastReceiver() {
        final IntentFilter filter = new IntentFilter();
        // 屏幕灭屏广播
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        //日期修改
        filter.addAction(Intent.ACTION_TIME_CHANGED);
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
                    Log.d(App.TAG, "手机屏幕开启");
                } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                    //改为60秒一存储
                    duration = 60000;
                    Log.d(App.TAG, "手机屏幕关闭");
                } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                    save();
                    //改为30秒一存储
                    duration = 30000;
                    Log.d(App.TAG, "手机解锁");
                } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
                    // 例如点击Home按钮,保存一次
                    save();
                    Log.d(App.TAG, "关闭系统对话框");
                } else if (Intent.ACTION_SHUTDOWN.equals(intent.getAction())) {
                    save();
                    Log.d(App.TAG, "手机关机");
                } else if (Intent.ACTION_TIME_CHANGED.equals(intent.getAction())) {
                    Log.v(TAG, "系统时间被设置");
                    initTodayData();
                }
            }
        };
        registerReceiver(phoneStatusReceiver, filter);
    }

    private void startTimeCount() {
        Log.d(App.TAG, "启动了定时器");
        time = new TimeCount(duration, 1000);
        time.start();
    }

    /**
     * 更新通知栏信息
     */
    private void updateNotification(String content) {
        builder = new NotificationCompat.Builder(this);
        builder.setPriority(Notification.PRIORITY_MIN);
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

        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(R.string.app_name, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    private void startStepDetector() {
        if (sensorManager != null && stepCreator != null) {
            sensorManager.unregisterListener(stepCreator);
            sensorManager = null;
            stepCreator = null;
        }
        getLock(this);
        // 获取传感器管理器的实例
        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        // android4.4以后可以使用计步传感器
        int VERSION_CODES = android.os.Build.VERSION.SDK_INT;
        Log.i(App.TAG, "系统版本号: "+VERSION_CODES);
        // 判断手机有没有传感器
        boolean hasSystemDetector = getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_DETECTOR);
        boolean hasSystemCounter = getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER);
        // 使用 Counter 系统计步器
        if (hasSystemCounter) {
            Log.d(App.TAG, "手机有counter传感器");
            addCounterSensorListener();
            return;
        }
        // 使用 Detector 系统步数探测器
        if (hasSystemDetector) {
            Log.d(App.TAG, "手机有Detector传感器");
            addDetectorSensorListener();
        }
    }

    /**
     * 使用 Counter 系统计步器
     */
    private void addCounterSensorListener() {
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            stepSensor = 0;
            Log.d(App.TAG, "使用了countSensor");
            sensorManager.registerListener(StepService.this, countSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    /**
     * 使用 Detector 系统步数探测器
     */
    private void addDetectorSensorListener() {
        Sensor detectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (detectorSensor != null) {
            stepSensor = 1;
            Log.d(App.TAG, "使用了detector");
            sensorManager.registerListener(StepService.this, detectorSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    /**
     * 系统计步器监听, 步数发生变化调用接口
     * @param event
     */
    public void onSensorChanged(SensorEvent event) {
        StepCreator.CURRENT_STEP++;
        updateNotification("今日步数：" + StepCreator.CURRENT_STEP + " 步");
    }


    // 使用BasePedo计步器
    private void addBasePedoListener() {
        stepCreator = new StepCreator(this);
        // 获得传感器的类型，这里获得的类型是加速度传感器
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // 此方法用来注册，只有注册过才会生效，参数：SensorEventListener的实例，Sensor的实例，更新速率
        sensorManager.registerListener(stepCreator, sensor, SensorManager.SENSOR_DELAY_UI);
        // BasePedo计步器事件监听器
        stepCreator.setOnSensorChangeListener(new StepCreator.OnSensorChangeListener() {
                    @Override
                    public void onChange() {
                        updateNotification("今日步数：" + StepCreator.CURRENT_STEP + " 步");
                        Log.i(App.TAG, "BasePedo计步器改变" + StepCreator.CURRENT_STEP);
                    }
                });
    }

    /**
     * 系统传感器精度
     * @param sensor
     * @param accuracy
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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

    /**
     * 保存步数到数据库中
     */
    private void save() {
        /**
         * 每次保存步数的时候, 都获取一下日期, 做成跨天的时候, StepCreator.current_step 未重置。
         */
        CURRENT_DATE = getTodayDate();
        // 保存到数据库
        DbUtils.createDb(this, Const.DB_NAME);
        ArrayList list = DbUtils.getQueryByWhere(Step.class, "date", new String[]{CURRENT_DATE});

        if (list.size() == 0 || list.isEmpty()) {
            /**
             * 自己补充:
             * 如果保存时,数据库没有今天的记录,说明今天已经结束, 执行如下操作:
             */
            // 1、重置步数
            StepCreator.CURRENT_STEP = 0;
            // 2、更新通知
            updateNotification("今日步数：" + StepCreator.CURRENT_STEP + " 步");
            // 3、插入新的一天的步数记录
            Step data = new Step();
            data.setDate(CURRENT_DATE);
            data.setCount(StepCreator.CURRENT_STEP + "");
            DbUtils.insert(data);
            Log.d(App.TAG, "插入"+CURRENT_DATE+"新步数记录, 总记录数:"+DbUtils.getQueryAll(Step.class).size());
        } else if (list.size() == 1) {
            Step data = (Step) list.get(0);
            data.setCount(StepCreator.CURRENT_STEP + "");
            DbUtils.update(data);
            Log.d(App.TAG, "更新"+CURRENT_DATE+"步数记录:"+ ((Step) list.get(0)).getCount());
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

        return (mWakeLock);
    }
}
