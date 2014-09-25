/**
 * 
 */
package com.liubo.birthdaynotice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

/**
 *<一句话功能简述>
 *<功能详细描述>
 *@author liubo
 *@version
*/
@SuppressLint("SimpleDateFormat")
public class Tool
{

    public static String getDate()
    {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        Date dd = new Date();
        return ft.format(dd);
    }
    
    /**
     * 格式化日期
     * 
     * @param cal
     * @return
     */
    public static String getDateByCalendar(Calendar cal)
    {
        int year = cal.get(Calendar.YEAR);
        int monthOfYear = cal.get(Calendar.MONTH);
        int dayOfMonth = cal.get(Calendar.DATE);
        String text = String.valueOf(year);
        monthOfYear += 1;
        if (monthOfYear < 10)
            text += "0";
        text += monthOfYear;
        if (dayOfMonth < 10)
            text += "0";
        text += dayOfMonth;
        return text;
    }
    
    public static boolean deleteInfo(String name,Context context) {
        String allCode = ShPrefUtils.getInstance(context).get(UserValue.infoKey, "");
        int index = allCode.indexOf(name);
        if (index == -1) {
            return false;
        }
        int prevIndex = allCode.lastIndexOf(";", index);
        int nextIndex = allCode.indexOf(";", index);
        String newCode = "";
        if (prevIndex != -1) {
            newCode += allCode.substring(0, prevIndex);
        }
        if (nextIndex != -1) {
            if (prevIndex == -1)
                nextIndex++;
            newCode += allCode.substring(nextIndex);
        }
        ShPrefUtils.getInstance(context).put(UserValue.infoKey, newCode);
        return true;
    }
    
    /**
     * 格式化年月日.
     * @param formatData 日期
     * @return 格式化后的日期
     */
     public static String formatDateYMD(String formatData)
     {
         String returnData= formatData;
         SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
         SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd");
         Date sDt = null;
         try
         {
             sDt = sf1.parse(formatData);
             if(null != sDt)
             {
                 returnData = sf2.format(sDt);
             }
            
             
         }
         catch (Exception  e)
         {
             // TODO Auto-generated catch block
         }
         
         return returnData;
     }
    
    public static long getQuot(String time1, String time2)
    {
        long quot = 0;
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        time1 = formatDateYMD(time1);
        try {
             Date date1 = ft.parse( time1 );
             Date date2 = ft.parse( time2 );
             quot = date1.getTime() - date2.getTime();
             quot = quot / 1000 / 60 / 60 / 24;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return quot;
    }
    
    public static boolean isServiceRunning(Context context)
    {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> serviceList = mActivityManager.getRunningServices(Integer.MAX_VALUE);
        if (null != serviceList && serviceList.size() > 0)
        {
            for (int i = 0; i < serviceList.size(); i++)
            {
                if (MyService.class.getName().equals(serviceList.get(i).service.getClassName()) && context.getPackageName().equals(serviceList.get(i).service.getPackageName()))
                {
                    return true;// 服务已经启动
                }
            }
        }
        return false;
    }
    
    /**
     * 判断字符串是否为空
     * @param trim
     * @return
     */
    public static boolean isEmpty(String trim)
    {
        if (trim == null || trim.equals(""))
            return true;
        ;
        return false;
    }
    
    /**
     * 判断字符串src是不是空的，空返回true否则返回false。 字符串会经过trim操作，就是如果字符串里都是空格也当成是空串
     * 
     * @param str
     * @return
     */
    public static boolean isTrimEmpty(CharSequence str)
    {
        if (str == null || Tool.isEmpty(str.toString().trim()))
        {
            return true;
        }
        return false;
    }

}
