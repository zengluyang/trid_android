package com.xicheng.trid.login;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xicheng.trid.R;
import com.xicheng.trid.applib.controller.HXSDKHelper;
import com.xicheng.trid.hx.activity.BaseActivity;
import com.xicheng.trid.json.PfPictureRequest;
import com.xicheng.trid.json.PreferAnswer;
import com.xicheng.trid.json.PreferAnswerUpload;
import com.xicheng.trid.main.MainActivity;
import com.xicheng.trid.utils.HttpUtil;
import com.xicheng.trid.utils.PicUtil;
import com.xicheng.trid.value.RequestUrlValue;
import com.xicheng.trid.value.ResponseTypeValue;

/**
 * 二选一（偏好）界面
 * 
 * @author DengRenbin
 *
 */
public class PreferActivity extends BaseActivity {
	private static String TAG = "PreferActivity";

	public static int countDown = 1;// 二选一计数器
	private FrameLayout choose0;// 点击选择图片0
	private FrameLayout choose1;// 点击选择图片1
	private ImageView imv_0;// 显示图片0
	private ImageView imv_1;// 显示图片1
	private TextView tv0_cn;// 图片0中文名
	private TextView tv0_en;// 图片0英文名
	private TextView tv1_cn;// 图片1中文名
	private TextView tv1_en;// 图片1英文名
	private TextView tv_bottomText;// 底部文字"请选择你偏好的那一个"

	private static SharedPreferences spref;// 连接SharePreference:prefer_data_<tel>
	private static Editor editor;// 编辑SharePreference:prefer_data_<tel>
	private int current;// 记录循环队列当前图片文件夹编号
	private int last;// 记录循环队列末尾下一个图片文件夹编号
	private File parent;// 父文件夹
	private int prefer_id;// 图片文件id号，同时也是文件夹名称

	public static PreferAnswerUpload pfAnswerUpload = new PreferAnswerUpload();
	public static Boolean basicInfoRequired = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prefer);
		// 实例化控件
		imv_0 = (ImageView) findViewById(R.id.prefer_pic0);
		imv_1 = (ImageView) findViewById(R.id.prefer_pic1);
		choose0 = (FrameLayout) findViewById(R.id.prefer_choose0);
		choose1 = (FrameLayout) findViewById(R.id.prefer_choose1);
		tv0_cn = (TextView) findViewById(R.id.tv_prefer_pic0_cn);
		tv0_en = (TextView) findViewById(R.id.tv_prefer_pic0_en);
		tv1_cn = (TextView) findViewById(R.id.tv_prefer_pic1_cn);
		tv1_en = (TextView) findViewById(R.id.tv_prefer_pic1_en);
		tv_bottomText = (TextView) findViewById(R.id.tv_prefer_bottom);
		// 加载图片
		showPic();
		// 添加点选动作
		choose0.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				DoChoose(0);
			}
		});
		choose1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				DoChoose(1);
			}
		});
	}

	/**
	 * 选择图片choose,choose可能为0或1
	 * 
	 * @param choose
	 */
	protected void DoChoose(int choose) {
		Intent intent;
		// 二选一倒计时器-1
		// 若countDown==5,则说明此次选择为性别选择，设置性别choose（0为男，1为女）
		if (--countDown == 5) {
			BirthChooseActivity.setSex(choose);
			intent = new Intent(PreferActivity.this, PreferActivity.class);
		} else {
			// 添加二选一结果
			pfAnswerUpload.addPfAnswer(new PreferAnswer(prefer_id, choose));
			// 大于0则继续跳转二选一
			if (countDown > 0) {
				intent = new Intent(PreferActivity.this, PreferActivity.class);
			} else {
				if (HXSDKHelper.getInstance().getNetworkState()) {
					// 上传结果列表，并执行登陆
					HttpUtil.postRequest(RequestUrlValue.PF_ANSWER_UPLOAD, new Gson().toJson(pfAnswerUpload));
					return;
				} else {
					savePreferAnswer();
					if (basicInfoRequired){
						intent = new Intent(PreferActivity.this, BirthChooseActivity.class);
					} else {
						intent = new Intent(PreferActivity.this, MainActivity.class);
					}
				} 
			}
		}
		startActivity(intent);
		finish();
	}

	/**
	 * 加载图片，本地拥有则加载本地图片，否则从服务器获取。
	 */
	private void showPic() {
		// 首先进入文件夹Xicheng/TriD/<tel>/prefer
		parent = new File(Environment.getExternalStorageDirectory() + "/Xicheng/");
		if (!parent.exists()) {
			parent.mkdir();
		}
		parent = new File(parent + "/TriD/");
		if (!parent.exists()) {
			parent.mkdir();
		}
		parent = new File(parent + "/" + HXSDKHelper.getInstance().getHXId() + "/");
		if (!parent.exists()) {
			parent.mkdir();
		}
		parent = new File(parent + "/prefer/");
		if (!parent.exists()) {
			parent.mkdir();
		}
		// 与"prefer_data_<tel>"建立连接
		spref = getSharedPreferences("prefer_data_" + HXSDKHelper.getInstance().getHXId(), MODE_PRIVATE);
		editor = spref.edit();
		// 读取循环队列当前图片文件夹编号
		current = spref.getInt("current", 0);
		// 读取循环队列末尾下一个图片文件夹编号
		last = spref.getInt("last", 0);
		// 相等则说明本地图片队列为空
		if (last == current) {
			if ( HXSDKHelper.getInstance().getNetworkState()) {// 网络连接正常
				// 联网下载图片
				PfPictureRequest post = new PfPictureRequest();
				HttpUtil.postRequest(RequestUrlValue.PF_PICTURE_REQUEST, new Gson().toJson(post));// 请求下载并加载图片				
			} else {//网络断开
				showPicTriD();
			}
		} else {
			// 进入文件夹Xicheng/TriD/<tel>/prefer/prefer_id/
			prefer_id = current;
			parent = new File(parent + "/" + prefer_id + "/");
			// 从本地加载图片
			showPicFromLocal(parent);
		}
	}

	/**
	 * 从本地文件夹中加载图片
	 */
	@SuppressWarnings("resource")
	private void showPicFromLocal(File parent) {
		// 图片文件
		File pic0 = new File(parent, "0.png");
		File pic1 = new File(parent, "1.png");
		// 图片信息文件
		File data0 = new File(parent, "data0.txt");
		File data1 = new File(parent, "data1.txt");
		try {
			// 加载图片0的信息
			// 用Reader读取图片信息文件中数据
			FileReader flReader = new FileReader(data0);
			BufferedReader bfReader = new BufferedReader(flReader);
			if (bfReader.readLine().equals("name_cn 0.png")) {
				tv0_cn.setText(bfReader.readLine());// 中文名
				bfReader.readLine();
				tv0_en.setText(bfReader.readLine());// 英文名
			} else {
				tv0_en.setText(bfReader.readLine());// 英文名
				bfReader.readLine();
				tv0_cn.setText(bfReader.readLine());// 中文名
			}
			// 加载图片1的信息
			// 用Reader读取图片信息文件中数据
			flReader = new FileReader(data1);
			bfReader = new BufferedReader(flReader);
			if (bfReader.readLine().equals("name_cn 1.png")) {
				tv1_cn.setText(bfReader.readLine());// 中文名
				bfReader.readLine();
				tv1_en.setText(bfReader.readLine());// 英文名
			} else {
				tv1_en.setText(bfReader.readLine());// 英文名
				bfReader.readLine();
				tv1_cn.setText(bfReader.readLine());// 中文名
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 加载图片
		imv_0.setImageBitmap(BitmapFactory.decodeFile(pic0.getAbsolutePath()));
		imv_1.setImageBitmap(BitmapFactory.decodeFile(pic1.getAbsolutePath()));
		// 与prefer_data_<tel>建立连接
		spref = getSharedPreferences("prefer_data_" + HXSDKHelper.getInstance().getHXId(), MODE_PRIVATE);
		editor = spref.edit();
		// current图片编号+1
		current = (current + 1) % 11;
		editor.putInt("current", current);
		editor.commit();
	}

	/**
	 * 处理服务器返回数据
	 */
	protected void handleResult(JSONObject obj) {
		try {
			if (obj.getString("type").equals(ResponseTypeValue.PF_PICTURE_RESULT)) {
				// 处理返回的偏好（二选一）图片及相关信息
				if (obj.getBoolean("success")) {
					// 进行图片下载与保存
					PicUtil.handlePfPictureResult(obj, this);
					// 显示图片
					prefer_id = obj.getJSONObject("pf").getInt("pf_id");
					parent = new File(Environment.getExternalStorageDirectory() + "/Xicheng/TriD/" 
							+ HXSDKHelper.getInstance().getHXId()
							+ "/prefer/" + "/" + prefer_id + "/");
					showPicFromLocal(parent);
				} else {
					Log.i(TAG, "图片下载失败");
					showPicTriD();
				}
			} else if (obj.getString("type").equals(ResponseTypeValue.PF_ANSWER_UPLOAD_RESULT)) {
				// 处理偏好（二选一)结果上传返回值
				Intent intent;
				if (!obj.getBoolean("success")){//偏好上传失败
					savePreferAnswer();
				}
				if (basicInfoRequired) {
					// 需要基础信息时跳转生日选择界面
					intent = new Intent(PreferActivity.this, BirthChooseActivity.class);
				} else {
					// 跳转主界面
					intent = new Intent(PreferActivity.this, MainActivity.class);
				}
				startActivity(intent);
				finish();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void savePreferAnswer() {
		// TODO Auto-generated method stub
		
	}

	// ----start------------------------调试用------------------------------调试用-----------------------------------
	private void showPicTriD() {
		// 去掉页面的内容
		choose0.setVisibility(View.GONE);
		choose1.setVisibility(View.GONE);
		tv_bottomText.setVisibility(View.GONE);
		// 更换背景图片
		LinearLayout prefer = (LinearLayout) findViewById(R.id.prefer);
		prefer.setBackgroundResource(R.drawable.tridlogo);
		Timer timer = new Timer();// timer中有一个线程,这个线程不断执行task
		// timertask实现runnable接口,TimerTask类就代表一个在指定时间内执行的task
		TimerTask task = new TimerTask() {
			public void run() {
				Intent intent = new Intent(PreferActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		};
		timer.schedule(task, 1000 * 3);// 设置这个task在延迟三秒之后自动执行
	}
	// ----end------------------------调试用------------------------------调试用-----------------------------------

}
