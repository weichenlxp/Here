package edu.hebut.here.service;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import edu.hebut.here.R;
import edu.hebut.here.WelcomeActivity;
import edu.hebut.here.utils.DateUtils;

import static edu.hebut.here.data.MyContentResolver.queryGoods;
import static edu.hebut.here.data.MyContentResolver.queryUser;
import static edu.hebut.here.data.MyContentResolver.updateGoods;

public class LongRunningService extends Service {
    SharedPreferences sharedPreferences;
    int userID, houseID, over = 0, close = 0;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(() -> {
            //此处写要进行的操作
            over = 0;
            close = 0;
            sharedPreferences = getApplicationContext().getSharedPreferences("here", Context.MODE_PRIVATE);
            userID = sharedPreferences.getInt("userID", -1);
            houseID = sharedPreferences.getInt("houseID", -1);
            Cursor user = queryUser(getApplicationContext(), new String[]{"reminderTime"}, "userID=?", new String[]{String.valueOf(userID)});
            if (user.moveToNext()) {
                int reminderTime = user.getInt(0);
                Date nowDate = new Date();
                String now = DateUtils.date2Str(nowDate);
                Cursor goodsHome = queryGoods(getApplicationContext(), new String[]{"manufactureDate", "qualityGuaranteePeriod","qualityGuaranteePeriodType", "overtime", "goodsID"}, "userID=? AND houseID=? AND packed=0", new String[]{String.valueOf(userID),String.valueOf(houseID)});
                Cursor goodsContainer = queryGoods(getApplicationContext(), new String[]{"manufactureDate", "qualityGuaranteePeriod","qualityGuaranteePeriodType", "overtime", "goodsID"}, "userID=? AND packed=1", new String[]{String.valueOf(userID)});
                int length = goodsHome.getCount()+goodsContainer.getCount();
                int[] goodsID = new int[length];
                String[] manufactureDate = new String[length];
                Date[] productDate = new Date[length];
                String[] qualityGuaranteePeriod = new String[length];
                String[] qualityGuaranteePeriodType = new String[length];
                String[] overtime = new String[length];

                for (int i=0; goodsHome.moveToNext(); i++) {
                    manufactureDate[i] = goodsHome.getString(0);
                    qualityGuaranteePeriod[i] = goodsHome.getString(1);
                    qualityGuaranteePeriodType[i] = goodsHome.getString(2);
                    overtime[i] = goodsHome.getString(3);
                    goodsID[i] = goodsHome.getInt(4);
                    if (qualityGuaranteePeriod[i]!=null && !qualityGuaranteePeriod[i].equals("")  && !manufactureDate[i].equals("选择日期")) {
                        productDate[i] = DateUtils.string2Date(manufactureDate[i]);
                        Date overtimeDate = DateUtils.addValue(productDate[i], Integer.parseInt(qualityGuaranteePeriod[i]), qualityGuaranteePeriodType[i]);
                        overtime[i] = DateUtils.date2Str(overtimeDate);
                        int subDate = DateUtils.getDaysIntervalStr(now, overtime[i]);
                        if (subDate < 0) {
                            updateGoods(getApplicationContext(), goodsID[i], manufactureDate[i], qualityGuaranteePeriod[i], qualityGuaranteePeriodType[i], "已过期" + (-subDate) + "天", true, false);
                            over++;
                        } else{
                            if (subDate <= reminderTime) {
                                updateGoods(getApplicationContext(), goodsID[i], manufactureDate[i], qualityGuaranteePeriod[i], qualityGuaranteePeriodType[i], "还有" + subDate + "天过期", false, true);
                                close++;
                            }
                            else {
                                updateGoods(getApplicationContext(), goodsID[i], manufactureDate[i], qualityGuaranteePeriod[i], qualityGuaranteePeriodType[i], "还有" + subDate + "天过期", false, false);
                            }
                        }
                    }
                }
                for (int i=0; goodsContainer.moveToNext(); i++) {
                    manufactureDate[i] = goodsContainer.getString(0);
                    qualityGuaranteePeriod[i] = goodsContainer.getString(1);
                    qualityGuaranteePeriodType[i] = goodsContainer.getString(2);
                    overtime[i] = goodsContainer.getString(3);
                    goodsID[i] = goodsContainer.getInt(4);
                    if (qualityGuaranteePeriod[i]!=null && !qualityGuaranteePeriod[i].equals("")  && !manufactureDate[i].equals("选择日期")) {
                        productDate[i] = DateUtils.string2Date(manufactureDate[i]);
                        Date overtimeDate = DateUtils.addValue(productDate[i], Integer.parseInt(qualityGuaranteePeriod[i]), qualityGuaranteePeriodType[i]);
                        overtime[i] = DateUtils.date2Str(overtimeDate);
                        int subDate = DateUtils.getDaysIntervalStr(now, overtime[i]);
                        if (subDate < 0) {
                            updateGoods(getApplicationContext(), goodsID[i], manufactureDate[i], qualityGuaranteePeriod[i], qualityGuaranteePeriodType[i], "已过期" + (-subDate) + "天", true, false);
                            over++;
                        } else{
                            if (subDate <= reminderTime) {
                                updateGoods(getApplicationContext(), goodsID[i], manufactureDate[i], qualityGuaranteePeriod[i], qualityGuaranteePeriodType[i], "还有" + subDate + "天过期", false, true);
                                close++;
                            }
                            else {
                                updateGoods(getApplicationContext(), goodsID[i], manufactureDate[i], qualityGuaranteePeriod[i], qualityGuaranteePeriodType[i], "还有" + subDate + "天过期", false, false);
                            }
                        }
                    }
                }

               if (over!=0||close!=0) {
                    String id ="channel_1";//channel的id
                    int importance = NotificationManager.IMPORTANCE_LOW;//channel的重要性
                    Intent resultIntent = new Intent(this, WelcomeActivity.class);

                    PendingIntent pendingIntent = PendingIntent.getActivity(this,0,resultIntent,0);
                    NotificationChannel channel = new NotificationChannel(id, "123", importance);//生成channel
                    //为channel添加属性
                    channel.enableVibration(true); //震动
                    channel.enableLights(true); //提示灯
                    NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                    manager.createNotificationChannel(channel);//添加channel
                    Notification notification = new Notification.Builder(this,id)
                            //注意这里多了一个参数id，指配置的NotificationChannel的id
                            //你可以自己去试一下 运行一次后 即配置完后 将这行代码以上的代
                            //码注释掉 将参数id直接改成“channel_1”也可以成功运行
                            //但改成别的如“channel_2”就不行了
                            .setCategory(Notification.CATEGORY_MESSAGE)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("您有物品处于过期和临期状态")
                            .setContentText(close+"个物品临期，"+over+"个物品过期")
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .build();
                    manager.notify(1,notification);
                }
            }
            Log.e("LongRunningService", "executed at " + new Date().toString());
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 1000*60*60*6; // 6小时毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_IMMUTABLE);
        manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    public static boolean isServiceRunning(Context context,
                                           Class<? extends Service> runService) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) am
                .getRunningServices(1024);
        for (int i = 0; i < runningService.size(); ++i) {
            if (runService.getName().equals(
                    runningService.get(i).service.getClassName().toString())) {
                return true;
            }
        }
        return false;
    }
}