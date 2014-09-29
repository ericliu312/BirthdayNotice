package com.liubo.birthdaynotice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TimePicker;

import com.liubo.birthdaynotice.Dialog1.DialogPassinterface;

@SuppressLint("HandlerLeak")
public class MainActivity extends Activity
{
    
    private ListView listView;
    private Button addBtn;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
//        if(!Tool.isServiceRunning(this))
//        {
//            Intent intent = new Intent(this, MyService.class);  
//            startService(intent);
//        }
        
//        receiver = new MyBroadcastReceiver();
//        
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(UserValue.broadcastTag);
//        registerReceiver(receiver, filter);
        
        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.my_list);
        addBtn= (Button)findViewById(R.id.add_btn);
        addBtn.setOnClickListener(new android.view.View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                openDialog();
            }
        });
        
        registerBtn = (Button)findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(new android.view.View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                showChangeDialog();
            }
        });
        
        registerAlarm();
        
        String info = ShPrefUtils.getInstance(this).get(UserValue.infoKey, "");
        List<String> data = new ArrayList<String>();
        if(!Tool.isTrimEmpty(info))
        {
            data = Arrays.asList(info.split(";"));
        }
        adapter = new MyListAdapter(data, this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                
                
                
            }
        });

    }
    
    private void registerAlarm()
    {
        String reg = ShPrefUtils.getInstance(this).get(UserValue.registeroKey, null);
        if(!Tool.isTrimEmpty(reg) && reg.equals("t") && Tool.isServiceRunning(this))
        {
            return;
        }
        
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);  
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(this, AlarmReceiver.class), Intent.FLAG_ACTIVITY_NEW_TASK);  
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMinute = c.get(Calendar.MINUTE);
        int hourOfDay = 7;
        int minute = 0;

        if (hourOfDay < nowHour  ||
                hourOfDay == nowHour && minute <= nowMinute) {
            c.add(Calendar.DAY_OF_YEAR, 1);
        }
        long time = c.getTimeInMillis(); 
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, time, UserValue.dayTimeToMillis, pi); 
        ShPrefUtils.getInstance(this).put(UserValue.alarmTime, time+"");
        ShPrefUtils.getInstance(this).put(UserValue.registeroKey, "t");
    }
    
    private void cancelAlarm()
    {
        ShPrefUtils.getInstance(MainActivity.this).put(UserValue.registeroKey, "");
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);  
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(this, AlarmReceiver.class), Intent.FLAG_ACTIVITY_NEW_TASK);  
        am.cancel(pi);
    }
    
    
    private void showChangeDialog()
    {
        Calendar c= Calendar.getInstance();
        TimePickerDialog dialog=new TimePickerDialog(
                this, 
                new TimePickerDialog.OnTimeSetListener(){
                    public void onTimeSet(TimePicker timePicker, int hourOfDay,int minute) {
                      cancelAlarm();
                      Calendar c = Calendar.getInstance();
                      c.setTimeInMillis(System.currentTimeMillis());

                      int nowHour = c.get(Calendar.HOUR_OF_DAY);
                      int nowMinute = c.get(Calendar.MINUTE);

                      if (hourOfDay < nowHour  ||
                              hourOfDay == nowHour && minute <= nowMinute) {
                          c.add(Calendar.DAY_OF_YEAR, 1);
                      }
                      c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                      c.set(Calendar.MINUTE, minute);
                      c.set(Calendar.SECOND, 0);
                      c.set(Calendar.MILLISECOND, 0);
                      
                      AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);  
                      PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, 0, new Intent(MainActivity.this, AlarmReceiver.class), Intent.FLAG_ACTIVITY_NEW_TASK);  
                      long time = c.getTimeInMillis(); 
                      am.setInexactRepeating(AlarmManager.RTC_WAKEUP, time, UserValue.dayTimeToMillis, pi); 
                      
//                      System.out.println("now0:"+Tool.getDateByCalendar(c)+"---"+Tool.getTimeByCalendar(c));
//                      System.out.println("now1:"+now);
//                      System.out.println("now2:"+System.currentTimeMillis());
                      ShPrefUtils.getInstance(MainActivity.this).put(UserValue.alarmTime, time+"");
                      ShPrefUtils.getInstance(MainActivity.this).put(UserValue.registeroKey, "t");
                    }
                }, 
                c.get(Calendar.HOUR_OF_DAY), 
                c.get(Calendar.MINUTE),
                true);
        dialog.show();
    }
    
    MyListAdapter adapter;
    
    Dialog1 dialog1;
    
    private void openDialog()
    {
          dialog1 = new Dialog1(this,R.layout.dialog1layout);
          dialog1.show();
          dialog1.setPositiveClickListener();
          dialog1.setDialogPassinterface(new DialogPassinterface()
        {
            
            @Override
            public void pass()
            {
                reload();
            }
        });
    }

    private void reload()
    {
        String info = ShPrefUtils.getInstance(this).get(UserValue.infoKey, "");
        List<String> data = new ArrayList<String>();
        if(!Tool.isTrimEmpty(info))
        {
            data = Arrays.asList(info.split(";"));
        }
        adapter = new MyListAdapter(data, this);
        listView.setAdapter(adapter);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) 
        {
            new AlertDialog.Builder(this).setTitle("退出").setMessage("确定退出" + getResources().getString(R.string.app_name) + "?").setPositiveButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    exitApplication();
                }
            }).setNegativeButton(getResources().getString(android.R.string.cancel), null).create().show();   
            return true;
        }
        return false;
            
    }
    public void exitApplication(){
        System.runFinalizersOnExit(true);   
        System.exit(0);  
        android.os.Process.killProcess(android.os.Process.myPid());
    }   

}
