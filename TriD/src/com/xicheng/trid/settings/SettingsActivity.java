package com.xicheng.trid.settings;

import com.xicheng.trid.R;
import com.xicheng.trid.value.ConnInfo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author DengRenbin
 *
 */
public class SettingsActivity extends Activity {
	private ImageButton fanhui;
	private TextView huanHao;
	private TextView diLiWeiZhi;
	private TextView dianHuaShouQuan;
	private TextView tongYong;
	private TextView news;
	private ImageButton jieShouAnLian;
	private ImageButton jiShiSheJiao;
	private SharedPreferences spref;
	private Editor editor;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		spref = getSharedPreferences("settings_data_"+ConnInfo.TEL, MODE_PRIVATE);
		editor = spref.edit();
		//返回键
		fanhui = (ImageButton) findViewById(R.id.imagebutton_settings_fanhui);
		fanhui.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		//更换号码
		huanHao = (TextView) findViewById(R.id.geng_huan_hao_ma);
		
		//电话授权
		dianHuaShouQuan = (TextView) findViewById(R.id.dian_hua_shou_quan_textView);
		dianHuaShouQuan.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(SettingsActivity.this,DianhuaActivity.class);
				startActivity(intent);
			}
		});
		
		//地理位置
		diLiWeiZhi = (TextView) findViewById(R.id.di_li_wei_zhi);
		diLiWeiZhi.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(SettingsActivity.this,DiliActivity.class);
				startActivity(intent);
			}
		});
		
		//接受暗恋
		jieShouAnLian = (ImageButton) findViewById(R.id.jie_shou_an_lian);
		boolean data = spref.getBoolean("jie_shou_an_lian", true);
		if(data){
			jieShouAnLian.setImageResource(R.drawable.kaiguan_on);
		} else {
			jieShouAnLian.setImageResource(R.drawable.kaiguan_off);
		}
		jieShouAnLian.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				boolean data = spref.getBoolean("jie_shou_an_lian", true);
				if(data){
					editor.putBoolean("jie_shou_an_lian", false);
					editor.commit();
					jieShouAnLian.setImageResource(R.drawable.kaiguan_off);
				} else {
					editor.putBoolean("jie_shou_an_lian", true);
					editor.commit();
					jieShouAnLian.setImageResource(R.drawable.kaiguan_on);
				}
			}
		});
		
		//即时社交
		jiShiSheJiao = (ImageButton) findViewById(R.id.ji_shi_she_jiao);
		data = spref.getBoolean("ji_shi_she_jiao", true);
		if(data){
			jiShiSheJiao.setImageResource(R.drawable.kaiguan_on);
		} else {
			jiShiSheJiao.setImageResource(R.drawable.kaiguan_off);
		}
		jiShiSheJiao.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				boolean data = spref.getBoolean("ji_shi_she_jiao", true);
				if(data){
					editor.putBoolean("ji_shi_she_jiao", false);
					editor.commit();
					jiShiSheJiao.setImageResource(R.drawable.kaiguan_off);
				} else {
					editor.putBoolean("ji_shi_she_jiao", true);
					editor.commit();
					jiShiSheJiao.setImageResource(R.drawable.kaiguan_on);
				}
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