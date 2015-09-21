package com.xicheng.trid.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.google.gson.Gson;
import com.xicheng.trid.DemoApplication;
import com.xicheng.trid.DemoHXSDKHelper;
import com.xicheng.trid.R;
import com.xicheng.trid.hx.activity.BaseActivity;
import com.xicheng.trid.hx.db.UserDao;
import com.xicheng.trid.hx.domain.User;
import com.xicheng.trid.json.DeleteFriends;
import com.xicheng.trid.json.FriendListRequest;
import com.xicheng.trid.json.SmsValidationCode;
import com.xicheng.trid.json.SmsValidationRequest;
import com.xicheng.trid.utils.HttpUtil;
import com.xicheng.trid.utils.MyCountDownTimer;
import com.xicheng.trid.value.RequestUrlValue;
import com.xicheng.trid.value.ResponseTypeValue;

/**
 * 登陆界面
 * 
 * @author DengRenbin
 *
 */
public class LoginActivity extends BaseActivity {

	private static final String TAG = "LoginActivity";

	private List<String> list = new ArrayList<String>();// 电话号码区域下拉框内容
	private Spinner spinner;// 电话号码区域下拉框
	private ImageView imv_spinnerRight;// 下拉框右边小箭头
	private ArrayAdapter<String> adapter;// 下拉框adapter

	private Button btn_login;// 登陆按钮

	private EditText edt_phoneNum;// 电话号码输入框
	private EditText edt_smsValidationCode;// 短信验证码输入框
	private TextView tv_getSmsValidationCode;// 获取验证码TextView

	private static MyCountDownTimer myCountDownTimer;// 倒计时计时器

	// 发送请求需要的json对象
	protected SmsValidationCode post_code;
	protected SmsValidationRequest post_request;
	protected String tel;
	private Handler mHandler;
	private List<User> listUser=new ArrayList();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_trid);
		// 已经登陆的情况下，直接跳转二选一
		if (DemoHXSDKHelper.getInstance().getAlreadyLogin()) {
			PreferActivity.countDown = 1;
			Intent intent = new Intent(this, PreferActivity.class);
			startActivity(intent);
			finish();
		}
		
		mHandler=new Handler(Looper.getMainLooper()){
			public void handleMessage(Message msg)
			{
				if(msg.what==ResponseTypeValue.INTENT_ERROR)
					handleError();
				else{
					try {
						JSONObject obj = new JSONObject(msg.obj.toString());
						handleUserResult(obj);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}	
			}
		};

		// 实例化区号下拉栏
		getInstanceOfSpinner();
		// 实例化电话号码输入框
		edt_phoneNum = (EditText) findViewById(R.id.edt_login_phoneNum);
		// 实例化验证码输入框
		edt_smsValidationCode = (EditText) findViewById(R.id.edt_login_smsValidationCode);
		// 实例化验证码获取textView
		tv_getSmsValidationCode = (TextView) findViewById(R.id.textv_login_getIdentifyingCode);
		// 实例化计时器
		myCountDownTimer = new MyCountDownTimer(60000, 1000, tv_getSmsValidationCode);
		// 点击获取验证码，之后后60s倒计时
		tv_getSmsValidationCode.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				tel = edt_phoneNum.getText().toString();
				// 装载要post的对象
				post_request = new SmsValidationRequest(tel);
				HttpUtil.postRequest(RequestUrlValue.SMS_VALIDATION_REQUEST, new Gson().toJson(post_request));
			}
		});

		// 点击进行验证码验证，然后登陆
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				tel = edt_phoneNum.getText().toString();
				String code = edt_smsValidationCode.getText().toString();
				// 装载要post的对象
				post_code = new SmsValidationCode(tel, code);
				HttpUtil.postRequest(RequestUrlValue.SMS_VALIDATION_CODE, new Gson().toJson(post_code));
			}
		});
	}
	/**
	 * 重写父类错误处理方法
	 */
	@Override
	public void handleError(){
		Toast.makeText(this, "联系人列表获取为空", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 保存联系人列表到数据库中
	 * @param obj
	 */
	protected void handleUserResult(JSONObject obj) {
		UserDao dao=new UserDao(this);
		try {
			JSONArray data=obj.getJSONArray("friend_list");
			for(int i=0;i<data.length();i++){
				JSONObject info=data.getJSONObject(i);
				User user=new User(info.getString("peer_tel"),info.getString("huanxin_id"),
						info.getInt("type"),info.getString("chat_title"),info.getLong("expire")*1000);
				listUser.add(user);
			}
			dao.saveContactList(listUser);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 *  实例化区号下拉栏
	 */
	private void getInstanceOfSpinner() {
		spinner = (Spinner) findViewById(R.id.spinner_login);
		// 生成数据源
		list.add("中国大陆（+86）");
		list.add("中国香港（+852）");
		list.add("中国澳门（+853）");
		list.add("中国台湾（+886）");
		// 适配器添加数据源和样式
		adapter = new ArrayAdapter<String>(this, R.layout.layout_spinner_item, list);
		adapter.setDropDownViewResource(R.layout.layout_spinner_drapdown_item);
		// 设置适配器
		spinner.setAdapter(adapter);
		// 小箭头实例化并添加动作
		imv_spinnerRight = (ImageView) findViewById(R.id.imv_login_spinnerRight);
		imv_spinnerRight.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				spinner.performClick();
			}
		});
	}

	/**
	 * 处理服务器返回的JSONObject
	 */
	protected void handleResult(JSONObject obj) {
		try {
			if (obj.getString("type").equals(ResponseTypeValue.SMS_VALIDATION_RESULT)) {
				// 是验证码验证请求的返回值
				if (obj.getBoolean("success")) {
					// 验证成功，保存联网数据并登陆
					longinHX(obj.getString("huanxin_id"),obj.getString("huanxin_pwd"),
							obj.getString("huanxin_id"),obj.getString("token"));
					
					Log.i(TAG, "验证成功");
					// 保存数据
					DemoHXSDKHelper.getInstance().setLoginInfo(obj.getString("huanxin_id"),
							obj.getString("huanxin_pwd"), obj.getString("token"));
					// 需要基础信息否？
					PreferActivity.basicInfoRequired = obj.getBoolean("basic_info_required");
					// 刷新需要上传的二选一结果数组
					PreferActivity.pfAnswerUpload.refresh();
					// 若第一次登陆需要连续9次进行二选一-------------------------此处暂时使用，之后需要修改--------------------------------------------------------
					PreferActivity.countDown = PreferActivity.basicInfoRequired ? 2 : 1;
					Intent intent = new Intent(LoginActivity.this, PreferActivity.class);
					startActivity(intent);
					finish();
				} else {
					Log.i(TAG, "验证失败");
					Toast.makeText(getApplicationContext(), "短信验证码验证失败，请重新验证", Toast.LENGTH_SHORT).show();
				}
			} else if (obj.getString("type").equals(ResponseTypeValue.SMS_VALIDATION_SEND)) {
				// 是请求短信验证码的返回值
				if (obj.getBoolean("success")) {
					// 获取成功
					Log.i(TAG, "验证码获取成功");
					// 开始计时60s
					myCountDownTimer.start();
				} else {
					// 获取失败
					Log.i(TAG, "验证码获取失败");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void longinHX(final String hx_id, final String hx_ps,final String tel,final String token){
		// 调用sdk登陆方法登陆聊天服务器 
		
		System.out.println(hx_id);
		System.out.println(hx_ps);
		EMChatManager.getInstance().login(hx_id, hx_ps, new EMCallBack() {

			@Override
			public void onSuccess() {

				// 登陆成功，保存用户名密码
				DemoApplication.getInstance().setUserName(hx_id);
				DemoApplication.getInstance().setPassword(hx_ps);

				try {
					// ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
					// ** manually load all local groups and
				    EMGroupManager.getInstance().loadAllGroups();
					EMChatManager.getInstance().loadAllConversations();
					// 处理好友和群组
				    initializeContacts(tel,token);
				   // this.sleep();
				} catch (Exception e) {
					e.printStackTrace();
					// 取好友或者群聊失败，不让进入主页面
					runOnUiThread(new Runnable() {
						public void run() {
							
							DemoApplication.getInstance().logout(null);
							Toast.makeText(getApplicationContext(), R.string.login_failure_failed, 1).show();
						}
					});
					return;
				}
				// 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
				boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(
						DemoApplication.currentUserNick.trim());
				if (!updatenick) {
					Log.e("LoginActivity", "update current user nick fail");
				}
				
				
			}

			@Override
			public void onProgress(int progress, String status) {
			}

			@Override
			public void onError(final int code, final String message) {
				
				runOnUiThread(new Runnable() {
					public void run() {
						
						Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}
	
	
	/**
	 * 查看数据库中是否有联系人信息，没有则请求并保存到本地数据库*/
	private void initializeContacts(String tel,String token) {
		Map<String, User> userlist = new HashMap<String, User>();
		UserDao dao = new UserDao(LoginActivity.this);
		if(dao.getContactList().size() == 0)
		{
			
		    String url = "http://101.200.89.240/index.php?r=contact/get-friend-list";
		    JSONObject obj =  new JSONObject();
		    try {
				obj.put("token", token);
				obj.put("tel", tel);
				obj.put("type","get_friend_list");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    System.out.println(obj.toString());
			//向服务器请求
		    HttpUtil.setHandler(mHandler);
			HttpUtil.postRequest(url, obj.toString());
		}
	}
	

}
