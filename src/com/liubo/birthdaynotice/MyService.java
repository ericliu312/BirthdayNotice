/**
 * 
 */
package com.liubo.birthdaynotice;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyService extends Service {

    private static final String broadcastTag = "android.intent.action.MyBroadcastReceiver"; 
    
    
    @Override
    public void onCreate() {
        super.onCreate();
        Timer timer = new Timer();  
        timer.scheduleAtFixedRate(new MyTime(), 1000, UserValue.dayTimeToMillis);  
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
    
    private class MyTime extends TimerTask{  
        public void run() {  
            String info = ShPrefUtils.getInstance(getApplicationContext()).get(UserValue.infoKey, "");
            if(!Tool.isTrimEmpty(info))
            {
                String infos[] = info.split(";");
                StringBuffer buffer = new StringBuffer();
                for(int i=0;i<infos.length;i++)
                {
                    String uerstr = infos[i];
                    String uerstrs[] = uerstr.split(":");
                    String name = uerstrs[0];
                    String date = uerstrs[1];
                    String date1 = date;
                    String date2 = Tool.getDate();
                    long days = Tool.getQuot(date1,date2);
                    if(days>0 && days<=3)
                    {
                        buffer.append("姓名:");
                        buffer.append(name);
                        buffer.append("         ");
                        buffer.append("距生日还有:");
                        buffer.append(days);
                        buffer.append("天!");
                        buffer.append("\n");
                    }else if(days==0){
                        buffer.append("姓名:");
                        buffer.append(name);
                        buffer.append("         ");
                        buffer.append("今天生日!");
                        buffer.append("\n");
                    }
                }
                String msg = buffer.toString();
                if(!Tool.isTrimEmpty(msg))
                {
                    Intent intent = new Intent(broadcastTag);
                    intent.putExtra(UserValue.serviceMsgTag, msg);
                    sendBroadcast(intent); 
                }
            }
        }  
    }
}
