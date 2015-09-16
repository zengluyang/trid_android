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
public class DianhuaActivity extends Activity {
	private ImageButton dianHuaShouQuan;
	private ImageButton fanhui;
	
	private SharedPreferences spref;
	private Editor editor;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_dhsq);
		spref = getSharedPreferences("settings_data"+ConnInfo.TEL, MODE_PRIVATE);
		editor = spref.edit();
		//电话授权
		dianHuaShouQuan = (ImageButton) findViewById(R.id.dian_hua_shou_quan);
		boolean data = spref.getBoolean("dian_hua_shou_quan", true);
		if(data){
			dianHuaShouQuan.setImageResource(R.drawable.kaiguan_on);
		} else {
			dianHuaShouQuan.setImageResource(R.drawable.kaiguan_off);
		}
		dianHuaShouQuan.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				boolean data = spref.getBoolean("dian_hua_shou_quan", true);
				if(data){
					editor.putBoolean("dian_hua_shou_quan", false);
					editor.commit();
					dianHuaShouQuan.setImageResource(R.drawable.kaiguan_off);
				} else {
					editor.putBoolean("dian_hua_shou_quan", true);
					editor.commit();
					dianHuaShouQuan.setImageResource(R.drawable.kaiguan_on);
				}
			}
		});
		//返回键
		fanhui = (ImageButton) findViewById(R.id.dian_hua_return);
		fanhui.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}
}
