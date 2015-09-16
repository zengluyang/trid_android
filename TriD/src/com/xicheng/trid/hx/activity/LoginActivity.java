/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xicheng.trid.hx.activity;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;

import com.xicheng.trid.DemoApplication;
import com.xicheng.trid.DemoHXSDKHelper;
import com.xicheng.trid.R;
import com.xicheng.trid.hx.db.UserDao;
import com.xicheng.trid.hx.domain.User;
import com.xicheng.trid.hx.utils.CommonUtils;
import com.xicheng.trid.hx.utils.UserUtils;
import com.xicheng.trid.main.Constant;
import com.xicheng.trid.main.MainActivity;
import com.xicheng.trid.utils.HttpUtil;

/**
 * 登陆页面
 * 
 */
public class LoginActivity extends BaseActivity {
	private static final String TAG = "LoginActivity";
	public static final int REQUEST_CODE_SETNICK = 1;
	private EditText usernameEditText;
	private EditText passwordEditText;

	private boolean progressShow;
	private boolean autoLogin = false;

	private String currentUsername;
	private String currentPassword;
	ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HttpUtil.setHandler(handler);
		// 如果用户名密码都有，直接进入主页面
		if (DemoHXSDKHelper.getInstance().isLogined()) {
			autoLogin = true;
			startActivity(new Intent(LoginActivity.this, MainActivity.class));

			return;
		}
		setContentView(R.layout.activity_login);

		usernameEditText = (EditText) findViewById(R.id.username);
		passwordEditText = (EditText) findViewById(R.id.password);

		// 如果用户名改变，清空密码
		usernameEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				passwordEditText.setText(null);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		if (DemoApplication.getInstance().getUserName() != null) {
			usernameEditText.setText(DemoApplication.getInstance().getUserName());
		}
	}

	/**
	 * 登录
	 * 
	 * @param view
	 */
	public void login(View view) {
		if (!CommonUtils.isNetWorkConnected(this)) {
			Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
			return;
		}
		currentUsername = usernameEditText.getText().toString().trim();
		currentPassword = passwordEditText.getText().toString().trim();

		if (TextUtils.isEmpty(currentUsername)) {
			Toast.makeText(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(currentPassword)) {
			Toast.makeText(this, R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
			return;
		}

		progressShow = true;
		//显示正在登陆
	    pd = new ProgressDialog(LoginActivity.this);
		pd.setCanceledOnTouchOutside(false);
		pd.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				progressShow = false;
			}
		});
		pd.setMessage(getString(R.string.Is_landing));
		pd.show();

		final long start = System.currentTimeMillis();
		// 调用sdk登陆方法登陆聊天服务器
		EMChatManager.getInstance().login(currentUsername, currentPassword, new EMCallBack() {

			@Override
			public void onSuccess() {
				if (!progressShow) {
					return;
				}
				// 登陆成功，保存用户名密码
				DemoApplication.getInstance().setUserName(currentUsername);
				DemoApplication.getInstance().setPassword(currentPassword);

				try {
					// ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
					// ** manually load all local groups and
				    EMGroupManager.getInstance().loadAllGroups();
					EMChatManager.getInstance().loadAllConversations();
					// 处理好友和群组
				    initializeContacts();
				   // this.sleep();
				} catch (Exception e) {
					e.printStackTrace();
					// 取好友或者群聊失败，不让进入主页面
					runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
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
				if (!progressShow) {
					return;
				}
				runOnUiThread(new Runnable() {
					public void run() {
						pd.dismiss();
						Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}
	
	/**
	 * 查看数据库中是否有联系人信息，没有则请求并保存到本地数据库*/
	private void initializeContacts() {
		Map<String, User> userlist = new HashMap<String, User>();
		UserDao dao = new UserDao(LoginActivity.this);
		if(dao.getContactList().size() == 0)
		{
			
		    String url = "http://101.200.89.240/index.php?r=contact/get-friend-list";
		    JSONObject obj =  new JSONObject();
		    try {
				obj.put("token", "MU5OYjRaODN4dmRkaXEweiszV0VYZWRad0YrdHIrUXZOVm1WMHhQRWY4MTdtM0ww");
				obj.put("tel", "15008271111");
				obj.put("type","get_friend_list");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
		    System.out.println(obj.toString());
			//向服务器请求
			HttpUtil.postRequest(url, obj.toString());
			
		}
		
		
		
	}
	
//	private void initializeContacts() {
//		Map<String, User> userlist = new HashMap<String, User>();
//		// 添加user"申请与通知"
//		User newFriends = new User();
//		newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
//		String strChat = getResources().getString(
//				R.string.Application_and_notify);
//		newFriends.setNick(strChat);
//
//		userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
//		// 添加"群聊"
//		User groupUser = new User();
//		String strGroup = getResources().getString(R.string.group_chat);
//		groupUser.setUsername(Constant.GROUP_USERNAME);
//		groupUser.setNick(strGroup);
//		groupUser.setHeader("");
//		userlist.put(Constant.GROUP_USERNAME, groupUser);
//		
//		// 添加"Robot"
//		User robotUser = new User();
//		String strRobot = getResources().getString(R.string.robot_chat);
//		robotUser.setUsername(Constant.CHAT_ROBOT);
//		robotUser.setNick(strRobot);
//		robotUser.setHeader("");
//		userlist.put(Constant.CHAT_ROBOT, robotUser);
//		
//		// 存入内存
//		DemoApplication.getInstance().setContactList(userlist);
//		// 存入db
//		UserDao dao = new UserDao(LoginActivity.this);
//		List<User> users = new ArrayList<User>(userlist.values());
//		dao.saveContactList(users);
//	}
//	
	/**
	 * 注册
	 * 
	 * @param view
	 */
	public void register(View view) {
		startActivityForResult(new Intent(this, RegisterActivity.class), 0);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (autoLogin) {
			return;
		}
	}

	@Override
	protected void handleResult(JSONObject obj) {
		// TODO Auto-generated method stub
		try {
			if(obj.getString("type").equals(Constant.FRIEND_LIST) && obj.getBoolean("success") )	
			{
				JSONArray friendList = obj.getJSONArray("friend_list");
				List<User> users = UserUtils.createUserList(friendList);
			    UserDao dao = new UserDao(LoginActivity.this);
				// 存入内存
				//DemoApplication.getInstance().setContactList(userlist);
				// 存入db
				dao.saveContactList(users);
				//List<User> size = dao.getContactList() ;
				int size1 = dao.getContactList().size() ;
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
			pd.dismiss();
		}
		// 进入主页面
		Intent intent = new Intent(LoginActivity.this,
				MainActivity.class);
		startActivity(intent);
		
		finish();
		
	}
}
