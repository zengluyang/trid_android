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

public class AlarmActivity extends Activity {
	
   static String chatTo ;
   static String content ;
   static long time ;
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
    
    
}
