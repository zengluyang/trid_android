package com.xicheng.trid.settings;

import com.xicheng.trid.R;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
/**
 * 
 * @author DengRenbin
 *
 */
public class AboutActivity extends Activity {
	private TextView versionName;
	private ImageButton fanhui;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_tongyong_about);
		versionName = (TextView) findViewById(R.id.version_name);
		try {
			versionName.setText(AboutActivity.this.getPackageManager().
					getPackageInfo(AboutActivity.this.getPackageName(), 0).versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fanhui = (ImageButton) findViewById(R.id.imabtn_settings_tongyong_about_fanhui);
		fanhui.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}
}
