package com.xicheng.trid.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xicheng.trid.R;
import com.xicheng.trid.hx.db.UserDao;
import com.xicheng.trid.value.FinalValue;
/**
 * 
 * @author DengRenbin
 *
 */
public class SettingsActivity extends Activity {
	private ImageButton btn_return;
	private TextView tv_changeTel;
	private TextView diLiWeiZhi;
	private TextView dianHuaShouQuan;
	private TextView tongYong;
	private TextView news;
	private ImageButton receiveCrush;
	private ImageButton immediatelyChat;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		btn_return = (ImageButton) findViewById(R.id.imagebutton_settings_fanhui);
		dianHuaShouQuan = (TextView) findViewById(R.id.dian_hua_shou_quan_textView);
		tv_changeTel = (TextView) findViewById(R.id.geng_huan_hao_ma);
		receiveCrush = (ImageButton) findViewById(R.id.onoff_receive_crush);
		diLiWeiZhi = (TextView) findViewById(R.id.di_li_wei_zhi);
		immediatelyChat = (ImageButton) findViewById(R.id.onoff_immediately_chat);
		
		// 获取设置数据
		Setting settingReceiveCrush = Setting.getSetting(Setting.SETTINGNAME_RECEIVE_CRUSH);
		Setting settingImmediatelyChat = Setting.getSetting(Setting.SETTINGNAME_IMMEDIATELY_CHAT);
		Setting.load(settingReceiveCrush, receiveCrush);
		Setting.load(settingImmediatelyChat, immediatelyChat);
		
		//接受暗恋
		receiveCrush.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Setting settingReceiveCrush = Setting.getSetting(Setting.SETTINGNAME_RECEIVE_CRUSH);
				settingReceiveCrush.setStatus(FinalValue.TRUE-settingReceiveCrush.getStatus());
				Setting.load(settingReceiveCrush, receiveCrush);
				new UserDao(SettingsActivity.this).saveSettings(Setting.settingsList);
			}
		});
		//即时社交
		immediatelyChat.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Setting settingImmediatelyChat = Setting.getSetting(Setting.SETTINGNAME_IMMEDIATELY_CHAT);
				settingImmediatelyChat.setStatus(1-settingImmediatelyChat.getStatus());
				Setting.load(settingImmediatelyChat, immediatelyChat);
				new UserDao(SettingsActivity.this).saveSettings(Setting.settingsList);
			}
		});
		
		//返回键
		btn_return.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		//更换号码
		// ---------------------------------------------------------------------------未完待续-----------------------------------
		//电话授权
		dianHuaShouQuan.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(SettingsActivity.this,DianhuaActivity.class);
				startActivity(intent);
			}
		});
		
		//地理位置
		diLiWeiZhi.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(SettingsActivity.this,DiliActivity.class);
				startActivity(intent);
			}
		});
		
		//消息设置
		news = (TextView) findViewById(R.id.textview_settings_news);
		news.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(SettingsActivity.this,NewsActivity.class);
				startActivity(intent);
			}
		});
		//通用
		tongYong = (TextView) findViewById(R.id.textview_settings_tong_yong);
		tongYong.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(SettingsActivity.this,TongyongActivity.class);
				startActivity(intent);
			}
		});
	}
	
}