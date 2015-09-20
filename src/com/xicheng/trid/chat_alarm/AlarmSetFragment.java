package com.xicheng.trid.chat_alarm;


import java.util.Calendar;

import com.xicheng.trid.R;
import com.xicheng.trid.timepicker.TimePicker;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class AlarmSetFragment  extends Fragment{
	TimePicker timePicker;
    EditText contentText ;
    Button alarm_sure;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		Log.i("fragmnet","onCreateView");
	
		View v = inflater.inflate(R.layout.fragment_chat_alarm_set, container, false);
	   
	    timePicker = (TimePicker) v.findViewById(R.id.timePicker);
	    contentText = (EditText) v.findViewById(R.id.alarm_content);
	    alarm_sure = (Button) v.findViewById(R.id.btn_sure_alarm);
	    
	    return v;
		                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
	}
	@Override
	public void  onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		alarm_sure.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String temp_time = timePicker.getTime();
				Calendar c = AlarmUtils.stringToLongTime(temp_time);
				AlarmActivity.time= c.getTimeInMillis();
				AlarmActivity.content = contentText.getText().toString();
				if(AlarmActivity.content .equals(""))
				{
					Toast.makeText(getActivity(), "请输入提醒事项！", Toast.LENGTH_SHORT).show();
					return;
				}
				//跳转
				AlarmConfirmFragment alarmConfirmFragment = new AlarmConfirmFragment();
				getActivity().getFragmentManager().beginTransaction().replace(R.id.alarm_fragment, alarmConfirmFragment).commit();
			}});
		
		
		
	}

}
