/**
 * 
 */
package com.liubo.birthdaynotice;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompleteReceiver extends BroadcastReceiver {
    
    @Override
    public void onReceive(Context context, Intent intent) {
        if(!Tool.isServiceRunning(context))
        {
            Intent service = new Intent(context, MyService.class);
            context.startService(service);            
        }
    }

}
