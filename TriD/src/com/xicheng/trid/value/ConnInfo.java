package com.xicheng.trid.value;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 联网数据
 * 
 * @author DengRenbin
 *
 */
public class ConnInfo {
	public static String TOKEN;
	public static String TEL;
	public static String HUANXIN_ID;
	public static String HUANXIN_PWD;
	public static Boolean ALREADY_LOGIN;

	/**
	 * 从conn_data.xml中读取联网数据
	 * 
	 * @param context
	 */
	public static void load(Context context) {
		SharedPreferences spref = context.getSharedPreferences("conn_data", Activity.MODE_PRIVATE);
		ConnInfo.TOKEN = spref.getString("token", "");
		ConnInfo.HUANXIN_ID = spref.getString("huanxin_id", "");
		ConnInfo.HUANXIN_PWD = spref.getString("haunxin_pwd", "");
		ConnInfo.TEL = spref.getString("tel", "");
		ConnInfo.ALREADY_LOGIN = spref.getBoolean("already_login", false);
	}

	/**
	 * 将联网数据保存到conn_data.xml中
	 * 
	 * @param context
	 * @param tel
	 * @param token
	 * @param huanxin_id
	 * @param huanxin_pwd
	 */
	public static void write(Context context, String tel, String token, String huanxin_id, String huanxin_pwd) {
		SharedPreferences spref = context.getSharedPreferences("conn_data", Activity.MODE_PRIVATE);
		Editor editor = spref.edit();
		editor.putString("tel", tel);
		editor.putString("token", token);
		editor.putString("huanxin_id", huanxin_id);
		editor.putString("huanxin_pwd", huanxin_pwd);
		editor.commit();
	}

	/**
	 * 设置登陆情况信息
	 * 
	 * @param context
	 * @param bool
	 */
	public static void setAlreadyLogin(Context context, boolean bool) {
		SharedPreferences spref = context.getSharedPreferences("conn_data", Activity.MODE_PRIVATE);
		Editor editor = spref.edit();
		editor.putBoolean("already_login", bool);
		editor.commit();
	}
}
