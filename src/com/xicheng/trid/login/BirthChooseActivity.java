package com.xicheng.trid.login;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.xicheng.trid.R;
import com.xicheng.trid.hx.activity.BaseActivity;
import com.xicheng.trid.json.BasicInfoUpload;
import com.xicheng.trid.json.Birthdate;
import com.xicheng.trid.main.MainActivity;
import com.xicheng.trid.utils.HttpUtil;
import com.xicheng.trid.value.RequestUrlValue;
import com.xicheng.trid.value.ResponseTypeValue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;
/**
 * 生日选择界面
 * @author DengRenbin
 *
 */
public class BirthChooseActivity extends BaseActivity {

	private DatePicker datePicker;//日期选择器
	private Button confirm;//确认按钮
	private static Birthdate birthdate;//保存生日信息
	private static int sex = 0;//保存性别信息

	public static void setSex(int sex){
		BirthChooseActivity.sex = sex;
	}
	
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_birthchoose);
		datePicker = (DatePicker) findViewById(R.id.datePicker_birthchoose);
		confirm = (Button) findViewById(R.id.btn_birthchoose);

		//为确认按钮设置动作
		confirm.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				int year = datePicker.getYear();
				int month = datePicker.getMonth()+1;
				int date = datePicker.getDayOfMonth();
				birthdate = new Birthdate(year,month,date);//保存生日信息
				HttpUtil.postRequest(RequestUrlValue.BASIC_INFO_UPLOAD,
						new Gson().toJson(new BasicInfoUpload(sex,birthdate)));//上传个人信息，包括性别和生日
				
			}
		});

	}

	/**
	 * 处理服务器返回数据
	 */
	protected void handleResult(JSONObject obj) {
		try {
			if(obj.getString("type").equals(ResponseTypeValue.BASIC_INFO_UPLOAD_RESULT)){
				if(obj.getBoolean("success")){
					//基本信息上传成功
					Intent intent = new Intent(BirthChooseActivity.this, MainActivity.class);
					startActivity(intent);
				} else {
					//基本信息上传失败
					Toast.makeText(getApplicationContext(), "上传失败", Toast.LENGTH_LONG).show();
				}
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
