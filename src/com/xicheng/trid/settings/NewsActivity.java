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
public class NewsActivity extends Activity{
	private ImageButton jieShou;
	private ImageButton voice;
	private ImageButton vibrance;
	private ImageButton fanhui;

	private SharedPreferences spref;
	private Editor editor;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_news);
		spref = getSharedPreferences("settings_data"+ConnInfo.TEL, MODE_PRIVATE);
		editor = spref.edit();
		//接收消息通知
		jieShou = (ImageButton) findViewById(R.id.imagebutton_settings_news_jieshou);
		boolean data = spref.getBoolean("jie_shou_tong_zhi", true);
		if(data){
			jieShou.setImageResource(R.drawable.kaiguan_on);
		} else {
			jieShou.setImageResource(R.drawable.kaiguan_off);
		}
		jieShou.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				boolean data = spref.getBoolean("jie_shou_tong_zhi", true);
				if(data){
					editor.putBoolean("jie_shou_tong_zhi", false);
					editor.commit();
					jieShou.setImageResource(R.drawable.kaiguan_off);
				} else {
					editor.putBoolean("jie_shou_tong_zhi", true);
					editor.commit();
					jieShou.setImageResource(R.drawable.kaiguan_on);
				}
			}
		});
		//声音
		voice = (ImageButton) findViewById(R.id.imagebutton_settings_news_voice);
		data = spref.getBoolean("jie_shou_voice", true);
		if(data){
			voice.setImageResource(R.drawable.kaiguan_on);
		} else {
			voice.setImageResource(R.drawable.kaiguan_off);
		}
		voice.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				boolean data = spref.getBoolean("jie_shou_voice", true);
				if(data){
					editor.putBoolean("jie_shou_voice", false);
					editor.commit();
					voice.setImageResource(R.drawable.kaiguan_off);
				} else {
					editor.putBoolean("jie_shou_voice", true);
					editor.commit();
					voice.setImageResource(R.drawable.kaiguan_on);
				}
			}
		});
		//振动
		vibrance = (ImageButton) findViewById(R.id.imagebutton_settings_news_vibrance);
		data = spref.getBoolean("jie_shou_vibrance", true);
		if(data){
			vibrance.setImageResource(R.drawable.kaiguan_on);
		} else {
			vibrance.setImageResource(R.drawable.kaiguan_off);
		}
		vibrance.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				boolean data = spref.getBoolean("jie_shou_vibrance", true);
				if(data){
					editor.putBoolean("jie_shou_vibrance", false);
					editor.commit();
					vibrance.setImageResource(R.drawable.kaiguan_off);
				} else {
					editor.putBoolean("jie_shou_vibrance", true);
					editor.commit();
					vibrance.setImageResource(R.drawable.kaiguan_on);
				}
			}
		});
		//返回键
		fanhui = (ImageButton) findViewById(R.id.imagebutton_settings_news_fanhui);
		fanhui.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}
}
