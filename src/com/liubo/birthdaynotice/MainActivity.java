package com.liubo.birthdaynotice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.liubo.birthdaynotice.Dialog1.DialogPassinterface;

@SuppressLint("HandlerLeak")
public class MainActivity extends Activity
{
    
    private int changeView = 999;
    private ListView listView;
    private MyBroadcastReceiver receiver;
    private Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        if(!Tool.isServiceRunning(this))
        {
            Intent intent = new Intent(this, MyService.class);  
            startService(intent);
        }
        
        receiver = new MyBroadcastReceiver();
        
        IntentFilter filter = new IntentFilter();
        filter.addAction(UserValue.broadcastTag);
        registerReceiver(receiver, filter);
        
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    Handler handler = new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            if(msg.what == changeView)
            {
                if(msg.obj!=null)
                {
                    String message = msg.obj.toString();
                    showNotice(message);
                }
            }
        }
        
    };
    
    private void showNotice(String msg)
    {
      AlertDialog.Builder dialog = new AlertDialog.Builder(this);
      dialog.setTitle("生日提醒");
      dialog.setMessage(msg);
      dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog,int which) {
              dialog.dismiss();
          }
      });
      dialog.show();
    }
    
    public class MyBroadcastReceiver extends BroadcastReceiver
    {

        /**
         * 
         */
        public MyBroadcastReceiver()
        {
            // TODO Auto-generated constructor stub
        }

        /* (non-Javadoc)
         * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
         */
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if(intent.getAction().equals(UserValue.broadcastTag))
            {
                Message msg = Message.obtain();
                msg.what = changeView;
                msg.obj = intent.getStringExtra(UserValue.serviceMsgTag);
                handler.sendMessage(msg);
            }
        }
    }

    @Override
    protected void onDestroy()
    {
        unregisterReceiver(receiver);
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
