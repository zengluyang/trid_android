package com.xicheng.trid.main;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.util.EMLog;
import com.easemob.util.NetUtils;
import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.umeng.analytics.MobclickAgent;
import com.xicheng.trid.Constant;
import com.xicheng.trid.DemoApplication;
import com.xicheng.trid.DemoHXSDKHelper;
import com.xicheng.trid.R;
import com.xicheng.trid.applib.controller.HXSDKHelper;
import com.xicheng.trid.crush.CrushActivity;
import com.xicheng.trid.crush.StartCrushActivity;
import com.xicheng.trid.hx.activity.BaseActivity;
import com.xicheng.trid.hx.activity.ChatAllHistoryFragment;
import com.xicheng.trid.hx.activity.LoginActivity;
import com.xicheng.trid.json.PfPictureRequest;
import com.xicheng.trid.match.MatchActivity;
import com.xicheng.trid.settings.SettingsActivity;
import com.xicheng.trid.utils.HttpUtil;
import com.xicheng.trid.utils.PicUtil;
import com.xicheng.trid.value.ConnInfo;
import com.xicheng.trid.value.RequestUrlValue;
import com.xicheng.trid.value.ResponseTypeValue;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @author DengRenbin
 *
 */
public class MainActivity extends BaseActivity implements EMEventListener {
	// 底部导航栏控件实例化
	private LinearLayout chat;
	private LinearLayout views;
	private LinearLayout heart;
	private ImageView chat_icon;
	private ImageView views_icon;
	private ImageView heart_icon;
	// 中部fragment部分实例化
	private ViewPager viewPager;
	private FragmentPagerAdapter fpAdapter;
	private List<Fragment> listData;
	// 顶部ActionBar栏实例化
	private ImageButton left;
	private ImageButton right;
	private OnClickListener left_act;
	private OnClickListener right_act_match;
	private OnClickListener right_act_camera;
	private TextView title;
	// 侧滑菜单实例化
	private SlidingMenu menu;
	private LinearLayout settings;
	private LinearLayout crush;

	public RelativeLayout errorItem;
	public TextView errorText;

	private int currentTabIndex;
	private ChatAllHistoryFragment chatHistoryFragment;
	// 账号在别处登录
	public boolean isConflict = false;
	// 账号被移除
	private boolean isCurrentAccountRemoved = false;
	private MyConnectionListener connectionListener = null;
	protected static final String TAG = "MainActivity";

	/**
	 * 检查当前用户是否被删除
	 */
	public boolean getCurrentAccountRemoved() {
		return isCurrentAccountRemoved;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		errorItem = (RelativeLayout) findViewById(R.id.rl_error_item);
		errorText = (TextView) errorItem.findViewById(R.id.tv_connect_errormsg);
		// 设置为已登录
		ConnInfo.setAlreadyLogin(this, true);
		// 实现状态栏沉浸
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		// 初始化侧滑菜单栏
		initLeftMenu();
		// 初始化三个主页
		initViewPager();
		// 初始化标题栏
		initTitleBar();
		// 初始化联系人变化监听器
		initListener();
		Log.i(TAG, "oncreate");
		// 预下载二选一图片
	    // downloadPreferPic();
	}

	/**
	 * 下载二选一图片
	 */
	private void downloadPreferPic() {
		// 判断是否需要下载(未完成)--------------------------------------------(未完待续)------------------------------------------------------

		// 发送下载二选一图片及信息请求
		PfPictureRequest post = new PfPictureRequest();
		HttpUtil.postRequest(RequestUrlValue.PF_PICTURE_REQUEST, new Gson().toJson(post));
	}

	private void initListener() {
		// setContactListener监听联系人的变化等
		// EMContactManager.getInstance().setContactListener(new
		// MyContactListener());

		// // 注册一个监听连接状态的listener
		// connectionListener = new MyConnectionListener();
		// EMChatManager.getInstance().addConnectionListener(connectionListener);
	}

	/**
	 * 监听事件
	 */
	@Override
	public void onEvent(EMNotifierEvent event) {
		switch (event.getEvent()) {
		case EventNewMessage: // 普通消息
		{
			EMMessage message = (EMMessage) event.getData();

			// 提示新消息
			HXSDKHelper.getInstance().getNotifier().onNewMsg(message);

			refreshUI();
			break;
		}

		case EventOfflineMessage: {
			refreshUI();
			break;
		}

		case EventConversationListChanged: {
			refreshUI();
			break;
		}

		default:
			break;
		}
	}

	private void refreshUI() {
		runOnUiThread(new Runnable() {
			public void run() {
				// 刷新bottom bar消息未读数
				// updateUnreadLabel();
				if (currentTabIndex == 0) {
					// 当前页面如果为聊天历史页面，刷新此页面
					if (chatHistoryFragment != null) {
						chatHistoryFragment.refresh();
					}
				}
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		// umeng
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (connectionListener != null) {
			EMChatManager.getInstance().removeConnectionListener(connectionListener);
		}

	}

	/**
	 * 标题栏右键按钮功能设置
	 * 
	 * @param arg
	 */
	private void setRightButton(int arg) {
		switch (arg) {
		case 0:
			right.setImageResource(R.drawable.match);
			right.setOnClickListener(right_act_match);
			break;
		case 1:
			right.setImageResource(R.drawable.camera);
			right.setOnClickListener(right_act_camera);
			break;
		case 2:
			right.setImageResource(R.color.touming);
			right.setOnClickListener(null);
			break;
		default:
			break;
		}
	}

	/**
	 * 标题栏初始化
	 */
	private void initTitleBar() {
		left = (ImageButton) findViewById(R.id.left_menu_button);
		left_act = new OnClickListener() {
			public void onClick(View v) {
				menu.showMenu();
			}
		};
		left.setOnClickListener(left_act);
		right = (ImageButton) findViewById(R.id.right_button);
		right_act_match = new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, MatchActivity.class);
				startActivity(intent);
			}
		};
		right_act_camera = new OnClickListener() {
			public void onClick(View v) {
				ConnInfo.setAlreadyLogin(getApplicationContext(), false);
			}
		};
		right.setOnClickListener(right_act_match);
		title = (TextView) findViewById(R.id.title);
	}

	/**
	 * 初始化侧滑菜单栏
	 */
	private void initLeftMenu() {
		// Fragment leftMenu = new LeftMenu();
		// setBehindContentView(R.layout.leftmenu_frame);
		// getSupportFragmentManager().beginTransaction()
		// .replace(R.id.leftmenu_frame, leftMenu).commit();
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		// 设置触摸屏幕的模式
		menu.setTouchModeAbove(SlidingMenu.LEFT);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		// menu.setBehindWidth(60);
		// 设置滑动菜单视图的宽度
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置渐入渐出效果的值
		menu.setFadeDegree(0.35f);
		/**
		 * SLIDING_WINDOW will include the Title/ActionBar in the content
		 * section of the SlidingMenu, while SLIDING_CONTENT does not.
		 */
		// 把滑动菜单添加进所有的Activity中，可选值SLIDING_CONTENT ， SLIDING_WINDOW
		menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		// 为侧滑菜单设置布局
		menu.setMenu(R.layout.fragment_slidingmenu);

		// 设置按钮
		settings = (LinearLayout) findViewById(R.id.she_zhi);
		settings.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
				startActivity(intent);
			}
		});

		// 暗恋按钮
		crush = (LinearLayout) findViewById(R.id.an_lian);
		crush.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SharedPreferences spref = getSharedPreferences("settings_data_" + ConnInfo.TEL, Activity.MODE_PRIVATE);
				boolean isFirst = spref.getBoolean("first_to_anlian", true);
				Editor editor = spref.edit();
				if (isFirst) {
					Intent intent = new Intent(MainActivity.this, StartCrushActivity.class);
					editor.putBoolean("first_to_anlian", false);
					editor.commit();
					startActivity(intent);
				} else {
					Intent intent = new Intent(MainActivity.this, CrushActivity.class);
					startActivity(intent);
				}
			}
		});
		// 我的按钮(未完成)--------------------------------------------------未完待续------------------------------------

	}

	/**
	 * 初始化中间fragment
	 */
	private void initViewPager() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		listData = new ArrayList<Fragment>();
		// 初始化三个界面并添加到listData中

		HeartFragment heartfrag = new HeartFragment();
		ViewsFragment viewsfrag = new ViewsFragment();
		chatHistoryFragment = new ChatAllHistoryFragment();
		listData.add(chatHistoryFragment);
		listData.add(heartfrag);
		listData.add(viewsfrag);

		// 初始化底部导航栏
		chat = (LinearLayout) findViewById(R.id.layout_chat);
		heart = (LinearLayout) findViewById(R.id.layout_heart);
		views = (LinearLayout) findViewById(R.id.layout_views);
		chat_icon = (ImageView) findViewById(R.id.layout_chat_icon);
		heart_icon = (ImageView) findViewById(R.id.layout_heart_icon);
		views_icon = (ImageView) findViewById(R.id.layout_views_icon);
		// 设置适配器
		fpAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			public int getCount() {
				return listData.size();
			}

			public Fragment getItem(int arg0) {
				return listData.get(arg0);
			}

		};

		// 为中间fragment页设置动作
		viewPager.setAdapter(fpAdapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageSelected(int arg0) {
				setRightButton(arg0);
				viewPager.setCurrentItem(arg0);
				switch (arg0) {
				case 0:
					chat_icon.setImageResource(R.drawable.chat_on);
					heart_icon.setImageResource(R.drawable.heart_off);
					views_icon.setImageResource(R.drawable.views_off);
					title.setText("聊天");
					break;
				case 1:
					chat_icon.setImageResource(R.drawable.chat_off);
					heart_icon.setImageResource(R.drawable.heart_on);
					views_icon.setImageResource(R.drawable.views_off);
					title.setText("9377雷霆之怒");
					break;
				case 2:
					chat_icon.setImageResource(R.drawable.chat_off);
					heart_icon.setImageResource(R.drawable.heart_off);
					views_icon.setImageResource(R.drawable.views_on);
					title.setText("发现");
					break;
				default:
					break;
				}
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			public void onPageScrollStateChanged(int arg0) {
			}
		});
		// 为底部导航栏设置动作
		chat.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				viewPager.setCurrentItem(0);
			}
		});
		heart.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				viewPager.setCurrentItem(1);
			}
		});
		views.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				viewPager.setCurrentItem(2);
			}
		});
	}

	/**
	 * 连接监听listener
	 * 
	 */
	public class MyConnectionListener implements EMConnectionListener {

		@Override
		public void onConnected() {
			boolean groupSynced = HXSDKHelper.getInstance().isGroupsSyncedWithServer();
			boolean contactSynced = HXSDKHelper.getInstance().isContactsSyncedWithServer();

			// in case group and contact were already synced, we supposed to
			// notify sdk we are ready to receive the events
			if (groupSynced && contactSynced) {
				new Thread() {
					@Override
					public void run() {
						// 通知sdk可以接受广播了
						HXSDKHelper.getInstance().notifyForRecevingEvents();
					}
				}.start();
			} else {
				if (!groupSynced) {
					// asyncFetchGroupsFromServer();
				}

				if (!contactSynced) {
					// asyncFetchContactsFromServer();
				}

			}

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					errorItem.setVisibility(View.GONE);
				}

			});
		}

		// 通知监听器连接断开
		@Override
		public void onDisconnected(final int error) {
			final String st1 = getResources().getString(R.string.can_not_connect_chat_server_connection);
			final String st2 = getResources().getString(R.string.the_current_network);
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (error == EMError.USER_REMOVED) {
						// 显示帐号已经被移除
						showAccountRemovedDialog();
					} else if (error == EMError.CONNECTION_CONFLICT) {
						// 显示帐号在其他设备登陆dialog
						showConflictDialog();
					} else {
						if (errorItem != null && currentTabIndex == 0) {
							errorItem.setVisibility(View.VISIBLE);
							if (NetUtils.hasNetwork(MainActivity.this))
								errorText.setText(st1);
							else
								errorText.setText(st2);
						}

					}
				}

			});
		}
	}

	@Override
	protected void onResume() {
		Log.i("activity", "onResume");
		if (connectionListener == null) {
			// 注册一个监听连接状态的listener
			connectionListener = new MyConnectionListener();
			EMChatManager.getInstance().addConnectionListener(connectionListener);
		}
		super.onResume();
		// onresume时，取消notification显示
		HXSDKHelper.getInstance().getNotifier().reset();

		// umeng
		MobclickAgent.onResume(this);

		// unregister this event listener when this activity enters the
		// background
		DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper.getInstance();
		sdkHelper.pushActivity(this);

		// register the event listener when enter the foreground
		EMChatManager.getInstance().registerEventListener(this,
				new EMNotifierEvent.Event[] { EMNotifierEvent.Event.EventNewMessage,
						EMNotifierEvent.Event.EventOfflineMessage,
						EMNotifierEvent.Event.EventConversationListChanged });
	}

	@Override
	protected void onStop() {
		EMChatManager.getInstance().unregisterEventListener(this);
		DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper.getInstance();
		sdkHelper.popActivity(this);

		super.onStop();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("isConflict", isConflict);
		outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(false);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private android.app.AlertDialog.Builder conflictBuilder;
	private android.app.AlertDialog.Builder accountRemovedBuilder;
	private boolean isConflictDialogShow;
	private boolean isAccountRemovedDialogShow;

	/**
	 * 显示帐号在别处登录dialog
	 */
	private void showConflictDialog() {
		isConflictDialogShow = true;
		DemoApplication.getInstance().logout(null);
		String st = getResources().getString(R.string.Logoff_notification);
		if (!MainActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (conflictBuilder == null)
					conflictBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
				conflictBuilder.setTitle(st);
				conflictBuilder.setMessage(R.string.connect_conflict);
				conflictBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						conflictBuilder = null;
						finish();
						startActivity(new Intent(MainActivity.this, LoginActivity.class));
					}
				});
				conflictBuilder.setCancelable(false);
				conflictBuilder.create().show();
				isConflict = true;
			} catch (Exception e) {
				EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
			}

		}

	}

	/**
	 * 帐号被移除的dialog
	 */
	private void showAccountRemovedDialog() {
		isAccountRemovedDialogShow = true;
		DemoApplication.getInstance().logout(null);
		String st5 = getResources().getString(R.string.Remove_the_notification);
		if (!MainActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (accountRemovedBuilder == null)
					accountRemovedBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
				accountRemovedBuilder.setTitle(st5);
				accountRemovedBuilder.setMessage(R.string.em_user_remove);
				accountRemovedBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						accountRemovedBuilder = null;
						finish();
						startActivity(new Intent(MainActivity.this, LoginActivity.class));
					}
				});
				accountRemovedBuilder.setCancelable(false);
				accountRemovedBuilder.create().show();
				isCurrentAccountRemoved = true;
			} catch (Exception e) {
				EMLog.e(TAG, "---------color userRemovedBuilder error" + e.getMessage());
			}

		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (getIntent().getBooleanExtra("conflict", false) && !isConflictDialogShow) {
			showConflictDialog();
		} else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
			showAccountRemovedDialog();
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.context_tab_contact, menu);
	}

	// 处理服务器返回的JSONObject对象
	protected void handleResult(JSONObject obj) {
		try {
			if (obj.getString("type").equals(ResponseTypeValue.PF_PICTURE_RESULT)) {
				// 处理二选一图片请求返回值
				if (obj.getBoolean("success")) {
					PicUtil.handlePfPictureResult(obj, this);
				} else {
					Log.i(TAG, "图片下载失败");
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
