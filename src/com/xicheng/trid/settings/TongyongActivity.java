package com.xicheng.trid.settings;

import com.xicheng.trid.R;

import android.app.Activity;
import android.content.Intent;
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
public class TongyongActivity extends Activity {
	private TextView about;
	private ImageButton fanhui;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_tongyong);
		about = (TextView) findViewById(R.id.textview_settings_tongyong_GYST);
		about.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(TongyongActivity.this,AboutActivity.class);
				startActivity(intent);
			}
		});
		fanhui = (ImageButton) findViewById(R.id.imagebutton_settings_tongyong_fanhui);
		fanhui.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}
}
