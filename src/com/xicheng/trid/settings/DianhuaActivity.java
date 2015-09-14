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
public class DianhuaActivity extends Activity {
	private ImageButton telPermission;
	private ImageButton btn_return;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_dhsq);
		//电话授权
		telPermission = (ImageButton) findViewById(R.id.imabtn_dhsq);
		Setting settingTelPermission = Setting.getSetting(Setting.SETTINGNAME_TEL_PERMISSION);
		Setting.load(settingTelPermission, telPermission);
		telPermission.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Setting settingTelPermission = Setting.getSetting(Setting.SETTINGNAME_TEL_PERMISSION);
				settingTelPermission.setStatus(FinalValue.TRUE-settingTelPermission.getStatus());
				Setting.load(settingTelPermission, telPermission);
				new UserDao(DianhuaActivity.this).saveSettings(Setting.settingsList);
			}
		});
		//返回键
		btn_return = (ImageButton) findViewById(R.id.dian_hua_return);
		btn_return.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}
}
