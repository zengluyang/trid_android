package com.xicheng.trid.match;

import com.xicheng.trid.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
/**
 * 
 * @author DengRenbin
 *
 */
public class MatchActivity extends Activity{
	private Button jion;
	private Button fanhui;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match);
		//加入
		jion = (Button) findViewById(R.id.btn_match_join);
		jion.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "加入成功，敬请期待", Toast.LENGTH_LONG).show();
				finish();
			}
		});
		//取消
		fanhui = (Button) findViewById(R.id.btn_match_return);
		fanhui.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}
}
