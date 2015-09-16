package com.xicheng.trid.chat_alarm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.easemob.EMCallBack;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.xicheng.trid.Constant;
import com.xicheng.trid.DemoApplication;
import com.xicheng.trid.R;
import com.xicheng.trid.hx.activity.ChatActivity;
import com.xicheng.trid.hx.db.UserDao;
import com.xicheng.trid.login.LoginActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class AlarmActivity extends Activity {
	
   static String chatTo ;
   static String content ;
   static String time ;
   Intent i ;

   FragmentTransaction fragmentTransaction ;
	
    @Override
	protected void  onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_alarm);
		i = getIntent();
	    chatTo = i.getStringExtra("chatTo");
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("提醒");
		ImageButton right_but = (ImageButton) findViewById(R.id.right_button);
		right_but.setImageResource(R.color.transparent);
		ImageButton left_but = (ImageButton) findViewById(R.id.left_menu_button);
		left_but.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlarmActivity.this.finish();
			}
			
		});
		right_but.setImageResource(R.color.transparent);
		
		FragmentManager fragmentManager = getFragmentManager();
	    fragmentTransaction = fragmentManager.beginTransaction();

		AlarmSetFragment alarmSetFragment = new AlarmSetFragment();
		
		// 添加显示第一个fragment
		fragmentTransaction.add(R.id.alarm_fragment, alarmSetFragment)
						.show(alarmSetFragment)
						.commit();

    }
    
    public void requestSetAlarm(View view)
	{
		
		EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);
		String action=Constant.ACTION_SET_ALARM;//action可以自定义，在广播接收时可以收到
		CmdMessageBody cmdBody=new CmdMessageBody(action);
		cmdMsg.setReceipt(AlarmActivity.chatTo);
		cmdMsg.setAttribute("time", AlarmActivity.time);//支持自定义扩展
		cmdMsg.setAttribute("content",AlarmActivity.content );
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
				UserDao dao = new UserDao(AlarmActivity.this);
				dao.SaveAlarm(AlarmActivity.chatTo, DemoApplication.getInstance().getUserName(), content, time);
			//	Toast.makeText(AlarmActivity.this , "闹钟设定已发送", Toast.LENGTH_LONG).show();
				
				System.out.println("alarm set!");
				//通知chatactivity更新alarmpreview
				AlarmActivity.this.setResult(ChatActivity.RESULT_CODE_SET_ALARM_SUCCESS);
			    AlarmActivity.this.finish();
			}
			
		});
	}
    
    
    public void requestCancelAlarm(String  tel)
   	{
   		
   		EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);
   		String action=Constant.ACTION_CANCEL_ALARM;//action可以自定义，在广播接收时可以收到
   		CmdMessageBody cmdBody=new CmdMessageBody(action);
   		cmdMsg.setReceipt(AlarmActivity.chatTo);
   		cmdMsg.setAttribute("alarmID", tel);//支持自定义扩展
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
   				UserDao dao = new UserDao(AlarmActivity.this);
   				dao.SaveAlarm(AlarmActivity.chatTo, DemoApplication.getInstance().getUserName(), content, time);
   			//	Toast.makeText(AlarmActivity.this , "闹钟设定已发送", Toast.LENGTH_LONG).show();
   				
   				System.out.println("alarm cancel!");
   		
   			}
   			
   		});
   	}
    
    public static void setAlarm(Context context,String writer, String content, String time)
    {
    	// 指定启动AlarmActivity组件
		Intent intent = new Intent(context,LoginActivity.class);
		Bundle bd  = new Bundle();
//		int viewId = v.getId();
//		bd.putInt("viewId",viewId);
//		intent.putExtra("vplan",bd);
		// 创建PendingIntent对象			
		PendingIntent pi = PendingIntent.getActivity(context, 111, intent, PendingIntent.FLAG_UPDATE_CURRENT);		
    	AlarmManager aManager = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE);
    	Calendar c = stringToLongTime(time);
    	long selectTime = c.getTimeInMillis();
    	Date date = new Date(selectTime);
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	System.out.println(sdf.format(date));
		//设置alarmmanager 在calendar对应启动时间
		aManager.set(AlarmManager.RTC_WAKEUP,selectTime,pi);//AlarmManager.INTERVAL_DAY
		// 显示闹铃设置成功的提示信息
	//	Toast.makeText(context, "闹铃设置成功啦"+sdf.format(date), Toast.LENGTH_SHORT).show();

		
    }
    
    private static Calendar stringToLongTime(String time){
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
    
    private void cancelAlarm(Context context ,int id){
		Intent intent = new Intent(AlarmActivity.this,AlarmActivity.class);
		PendingIntent pi = PendingIntent.getActivity(context, id, intent, 0);		
		AlarmManager aManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
		aManager.cancel(pi);
//		Toast.makeText(V_PlanActivity.this, "闹铃取消啦"
//				, Toast.LENGTH_SHORT).show();
		
	}

    
}
