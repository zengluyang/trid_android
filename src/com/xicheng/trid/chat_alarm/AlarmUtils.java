package com.xicheng.trid.chat_alarm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.easemob.EMCallBack;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.xicheng.trid.Constant;
import com.xicheng.trid.DemoApplication;
import com.xicheng.trid.hx.activity.ChatActivity;
import com.xicheng.trid.hx.db.UserDao;
import com.xicheng.trid.login.LoginActivity;

public class AlarmUtils {
	
	 //请求为对方设置闹钟(发送激活、取消请求)
    public static void requestSetAlarm(final Context context ,final String chatTo , final String content, final long time)
	{
		
		EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);
		String action=Constant.ACTION_SET_ALARM;//action可以自定义，在广播接收时可以收到
		CmdMessageBody cmdBody=new CmdMessageBody(action);
		cmdMsg.setReceipt(chatTo);
		cmdMsg.setAttribute("time", String.valueOf(time));//支持自定义扩展
		cmdMsg.setAttribute("content", content);
		cmdMsg.addBody(cmdBody); 
		EMChatManager.getInstance().sendMessage(cmdMsg, new EMCallBack(){

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProgress(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				//保存至数据库
				UserDao dao = new UserDao(context);
				dao.SaveAlarmForHe(chatTo, content, time, true);
			//	Toast.makeText(AlarmActivity.this , "闹钟设定已发送", Toast.LENGTH_LONG).show();
				System.out.println("alarm set!");
				if(context.getClass().equals(AlarmActivity.class))
				{
					//通知chatactivity更新alarmpreview
					((AlarmActivity)context).setResult(ChatActivity.RESULT_CODE_SET_ALARM_SUCCESS);
					((AlarmActivity)context).finish();
				}
				
			}
			
		});
	}
    
  //取消我为别人设的闹钟
  	public static void requestCancelAlarm(final Context context, final String chatTo, final String content ,final long time)
     	{
     		EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);
     		String action=Constant.ACTION_CANCEL_ALARM;//action可以自定义，在广播接收时可以收到
     		CmdMessageBody cmdBody=new CmdMessageBody(action);
     		cmdMsg.setReceipt(chatTo);
     		cmdMsg.setAttribute("content", content);
     		cmdMsg.setAttribute("time", String.valueOf(time));
     		cmdMsg.addBody(cmdBody); 
     		EMChatManager.getInstance().sendMessage(cmdMsg, new EMCallBack(){

     			@Override
     			public void onError(int arg0, String arg1) {
     				// TODO Auto-generated method stub
     				
     			}

     			@Override
     			public void onProgress(int arg0, String arg1) {
     				// TODO Auto-generated method stub
     				
     			}

     			@Override
     			public void onSuccess() {
     				// TODO Auto-generated method stub
     				//修改数据库
     				UserDao dao = new UserDao(context);
     				dao.SaveAlarmForHe(chatTo, content, time, false);
     			//	Toast.makeText(ChatActivity.this , "闹钟取消已发送", Toast.LENGTH_LONG).show();
     				System.out.println("alarm cancel!");
     			}
     			
     		});
     	}
    /**
     *设置本地闹钟开关
     * */
    public static void setAlarm(Context context,String writer, String content, Long time, Boolean state)
    {
    	if(state){
    		// 指定启动AlarmActivity组件
    		Intent intent = new Intent(context,LoginActivity.class);
    		Bundle bd  = new Bundle();
    		long id = Long.parseLong(writer);
    		int alarm_id =  (int) (id%10000000);
    		// 创建PendingIntent对象			
    		PendingIntent pi = PendingIntent.getActivity(context, alarm_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);		
        	AlarmManager aManager = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE);
    		//设置alarmmanager 在calendar对应启动时间
    		aManager.set(AlarmManager.RTC_WAKEUP,time,pi);//AlarmManager.INTERVAL_DAY
    		// 显示闹铃设置成功的提示信息
    	//	Toast.makeText(context, "闹铃设置成功啦"+sdf.format(date), Toast.LENGTH_SHORT).show();
    		//修改数据库
    		UserDao dao = new UserDao(DemoApplication.getInstance().getApplicationContext());
			dao.SaveAlarmForMe( writer ,content, time, true);
    		
    	}
    	else {
    		//取消闹钟
    		Long id = Long.parseLong(writer);
    		int alarm_id =  (int) (id%10000000);
    		Intent intent = new Intent(context,LoginActivity.class);
    		PendingIntent pi = PendingIntent.getActivity(context, alarm_id, intent, 0);		
    		AlarmManager aManager = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE);
    		aManager.cancel(pi);
    		//修改数据库(总操作是别人为我设的闹钟)
    		UserDao dao = new UserDao(DemoApplication.getInstance().getApplicationContext());
			dao.SaveAlarmForMe( writer ,content, time, false);
    	}
    	
		
    }
    
    public  static Calendar stringToLongTime(String time){
    	String[] timeString= new String[2];
    	timeString = time.split(" ");
    	Calendar c = Calendar.getInstance(Locale.getDefault());
    	c.setTimeInMillis(System.currentTimeMillis());
    	if(timeString[0].equals("今天"))
    	{
    		
    	}
    	else if(timeString[0].equals("明天"))
    	{
    		c.add(Calendar.DAY_OF_MONTH, 1);  
    	}
    	else{
    		
    		c.add(Calendar.DAY_OF_MONTH, 2);  
    	}
    	
		String[] timeString2 = new String[2];
		timeString2 = timeString[1].split(":");
		c.set(Calendar.HOUR_OF_DAY,Integer.parseInt(timeString2[0]) );
		c.set(Calendar.MINUTE,Integer.parseInt(timeString2[1]));
    	return c;
    			
    } 
    
    
    public  static String LongToStringTime(Long time){
    	Date date = new Date(time);
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String current_time  = sdf.format(date);
    	String[]  s1 = new String[2];
    	s1 = current_time.split(" ");
    	String[]  s2 = new String[3];
    	s2 = s1[1].split(":");
  	
        Calendar current = Calendar.getInstance();
		
		Calendar todayBegain = Calendar.getInstance();	//今天
		
		todayBegain.set(Calendar.YEAR, current.get(Calendar.YEAR));
		todayBegain.set(Calendar.MONTH, current.get(Calendar.MONTH));
		todayBegain.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH));
		//  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
		todayBegain.set( Calendar.HOUR_OF_DAY, 0);
		todayBegain.set( Calendar.MINUTE, 0);
		todayBegain.set(Calendar.SECOND, 0);
		
        long longTodayBegain = todayBegain.getTimeInMillis();
		
		long interval = time -longTodayBegain;
		if(interval <24*60*60*1000 ){
			return "今天 "+s2[0]+":"+s2[1];
		}else if(interval>2*24*60*60*1000){
			return "后天 "+s2[0]+":"+s2[1];
		}else{
			return "明天 "+s2[0]+":"+s2[1];
		}
    } 
    

}
