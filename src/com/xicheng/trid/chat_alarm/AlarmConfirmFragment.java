package com.xicheng.trid.chat_alarm;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.xicheng.trid.R;

public class AlarmConfirmFragment extends Fragment {
	private static final View View = null;
	TextView alarmContent;
	TextView alarmTime;
	Button but_alarm_confirm;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.i("fragmnet","onCreateView");
	    View v = inflater.inflate(R.layout.fragment_chat_alarm_confirm, container, false);
	    
	    alarmContent = (TextView) v.findViewById(R.id.text_con_12);
	    alarmTime = (TextView) v.findViewById(R.id.text_con_22);
	    but_alarm_confirm  = (Button) v.findViewById(R.id.btn_con_sure);
	    but_alarm_confirm.setOnClickListener(new  OnClickListener(){

			@Override
			public void onClick(android.view.View v) {
				// TODO Auto-generated method stub	
				AlarmUtils.requestSetAlarm(getActivity(),AlarmActivity.chatTo,AlarmActivity.content,AlarmActivity.time);
			}
	    	
	    });
	    return v;
		
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		alarmContent.setText(AlarmActivity.content);
		alarmTime.setText(AlarmUtils.LongToStringTime(AlarmActivity.time));
	}
		
	
	
		
	//	Button but_cancel  = (Button) findViewById(R.id.btn_con_cancel);

	
	

}
