/**
 * 
 */
package com.liubo.birthdaynotice;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class MyService extends Service {

    private static final String broadcastTag = "android.intent.action.MyBroadcastReceiver"; 
    
    
    @Override
    public void onCreate() {
        super.onCreate();
        registerAlarm();
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
    
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    
    private void cancelAlarm()
    {
        am.cancel(pi);
    }
    
    AlarmManager am;
    PendingIntent pi;
    
    private void registerAlarm()
    {
        String reg = ShPrefUtils.getInstance(this).get(UserValue.registeroKey, null);
        if(!Tool.isTrimEmpty(reg) && reg.equals("t"))
        {
            return;
        }
        am = (AlarmManager)getSystemService(ALARM_SERVICE);  
        
        pi = PendingIntent.getBroadcast(this, 0, new Intent(this, AlarmReceiver.class), Intent.FLAG_ACTIVITY_NEW_TASK);  
        long now = System.currentTimeMillis();  
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, now, 24*60*60*1000, pi); 
        ShPrefUtils.getInstance(this).put(UserValue.registeroKey, "t");
    }
    
}
