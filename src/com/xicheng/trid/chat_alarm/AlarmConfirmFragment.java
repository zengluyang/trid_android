package com.xicheng.trid.chat_alarm;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.xicheng.trid.DemoApplication;
import com.xicheng.trid.R;
import com.xicheng.trid.hx.activity.ChatActivity;
import com.xicheng.trid.hx.activity.LoginActivity;
import com.xicheng.trid.hx.db.UserDao;

public class AlarmConfirmFragment extends Fragment {
	private static final View View = null;
	TextView alarmContent;
	TextView alarmTime;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.i("fragmnet","onCreateView");
	    View v = inflater.inflate(R.layout.fragment_chat_alarm_confirm, container, false);
	    
	    alarmContent = (TextView) v.findViewById(R.id.text_con_12);
	    alarmTime = (TextView) v.findViewById(R.id.text_con_22);
	    return v;
		
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		alarmContent.setText(AlarmActivity.content);
		alarmTime.setText(AlarmActivity.time);
		
		
		
	}
		
	
	
		
	//	Button but_cancel  = (Button) findViewById(R.id.btn_con_cancel);

	
	

}
