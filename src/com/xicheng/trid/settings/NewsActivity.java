package com.xicheng.trid.settings;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.xicheng.trid.R;
import com.xicheng.trid.hx.db.UserDao;
import com.xicheng.trid.value.FinalValue;

/**
 * 
 * @author DengRenbin
 * 
 */
public class NewsActivity extends Activity {
	private ImageButton receiveMessage;
	private ImageButton voice;
	private ImageButton vibrance;
	private ImageButton imabtn_return;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_news);
		receiveMessage = (ImageButton) findViewById(R.id.imabtn_settings_news_receiveMessage);
		voice = (ImageButton) findViewById(R.id.imabtn_settings_news_voice);
		vibrance = (ImageButton) findViewById(R.id.imabtn_settings_news_vibrance);
		imabtn_return = (ImageButton) findViewById(R.id.imabtn_settings_news_return);

		// 获取设置数据
		Setting settingReceiveMessage = Setting
				.getSetting(Setting.SETTINGNAME_RECEIVE_MESSAGE);
		Setting settingVoice = Setting.getSetting(Setting.SETTINGNAME_VOICE);
		Setting settingVibrance = Setting
				.getSetting(Setting.SETTINGNAME_VIBRANCE);
		Setting.load(settingReceiveMessage, receiveMessage);
		Setting.load(settingVoice, voice);
		Setting.load(settingVibrance, vibrance);

		// 接收消息
		receiveMessage.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Setting settingReceiveMessage = Setting
						.getSetting(Setting.SETTINGNAME_RECEIVE_MESSAGE);
				settingReceiveMessage.setStatus(FinalValue.TRUE
						- settingReceiveMessage.getStatus());
				Setting.load(settingReceiveMessage, receiveMessage);
				new UserDao(NewsActivity.this)
						.saveSettings(Setting.settingsList);
			}
		});
		// 声音
		voice.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Setting settingVoice = Setting
						.getSetting(Setting.SETTINGNAME_VOICE);
				settingVoice.setStatus(FinalValue.TRUE
						- settingVoice.getStatus());
				Setting.load(settingVoice, voice);
				new UserDao(NewsActivity.this)
						.saveSettings(Setting.settingsList);
			}
		});
		// 振动
		vibrance.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Setting settingVibrance = Setting
						.getSetting(Setting.SETTINGNAME_RECEIVE_MESSAGE);
				settingVibrance.setStatus(FinalValue.TRUE
						- settingVibrance.getStatus());
				Setting.load(settingVibrance, vibrance);
				new UserDao(NewsActivity.this)
						.saveSettings(Setting.settingsList);
			}
		});
		// 返回键
		imabtn_return.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}
}
