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
public class DiliActivity extends Activity {
	private ImageButton fanhui;
	private ImageButton locationPermission;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_dlsq);
		//地理位置授权按钮
		locationPermission = (ImageButton) findViewById(R.id.imagebutton_dlsq);
		Setting settingLocationPermission = Setting.getSetting(Setting.SETTINGNAME_LOCATION_PERMISSION);
		Setting.load(settingLocationPermission, locationPermission);
		locationPermission.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Setting settingLocationPermission = Setting.getSetting(Setting.SETTINGNAME_LOCATION_PERMISSION);
				settingLocationPermission.setStatus(FinalValue.TRUE-settingLocationPermission.getStatus());
				Setting.load(settingLocationPermission, locationPermission);
				new UserDao(DiliActivity.this).saveSettings(Setting.settingsList);
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
