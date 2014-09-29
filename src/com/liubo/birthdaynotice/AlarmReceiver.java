/**
 * 
 */
package com.liubo.birthdaynotice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 *<一句话功能简述>
 *<功能详细描述>
 *@author liubo
 *@version
*/
public class AlarmReceiver extends BroadcastReceiver
{

//    private String tag = "com.liubo.birthdaynotice.AlarmReceiver";
    
    /* (non-Javadoc)
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
     */
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String msg = getMsg(context);
        if(!Tool.isTrimEmpty(msg))
        {
            showNotification(msg, context);
        }
    }
    
    private String getMsg(Context context)
    {
        String info = ShPrefUtils.getInstance(context).get(UserValue.infoKey, "");
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
            return msg;
        }
        return null;

    }
    
    private void showNotification(String msg,Context context)
    {
        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);               
        Notification notification = new Notification(R.drawable.app_icon, msg, System.currentTimeMillis());             
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        notification.defaults = Notification.DEFAULT_SOUND;
        notification.ledOnMS = 1000;
        notification.ledOffMS = 1000;                
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);           
        PendingIntent contentIntent = PendingIntent.getActivity(
                context, 
                R.string.app_name, 
                i, 
                PendingIntent.FLAG_UPDATE_CURRENT);
                         
        notification.setLatestEventInfo(
                context,
                "生日提醒", 
                msg, 
                contentIntent);
        nm.notify(R.string.app_name, notification);
        
    }

}
