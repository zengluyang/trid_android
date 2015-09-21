package com.xicheng.trid.crush;

import com.xicheng.trid.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * 
 * @author DengRenbin
 *
 */
public class StartCrushActivity extends Activity{
	private Button btn_start;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crush_start);
		btn_start = (Button) findViewById(R.id.btn_anlian_start);
		btn_start.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(StartCrushActivity.this,CrushActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
}
