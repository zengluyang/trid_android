package com.xicheng.trid.settings;

import com.xicheng.trid.R;
import com.xicheng.trid.value.ConnInfo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
/**
 * 
 * @author DengRenbin
 *
 */
public class DiliActivity extends Activity {
	private ImageButton fanhui;
	private ImageButton dilishouquan;

	private SharedPreferences spref;
	private Editor editor;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_dlsq);
		spref = getSharedPreferences("settings_data"+ConnInfo.TEL, MODE_PRIVATE);
		editor = spref.edit();
		//地理位置授权按钮
		dilishouquan = (ImageButton) findViewById(R.id.imagebutton_dlsq);
		boolean data = spref.getBoolean("di_li_shou_quan", true);
		if(data){
			dilishouquan.setImageResource(R.drawable.kaiguan_on);
		} else {
			dilishouquan.setImageResource(R.drawable.kaiguan_off);
		}
		dilishouquan.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				boolean data = spref.getBoolean("di_li_shou_quan", true);
				if(data){
					editor.putBoolean("di_li_shou_quan", false);
					editor.commit();
					dilishouquan.setImageResource(R.drawable.kaiguan_off);
				} else {
					editor.putBoolean("di_li_shou_quan", true);
					editor.commit();
					dilishouquan.setImageResource(R.drawable.kaiguan_on);
				}
			}
		});
		//返回键
		fanhui = (ImageButton) findViewById(R.id.di_li_return);
		
		fanhui.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}
}
