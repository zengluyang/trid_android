package com.xicheng.trid.hx.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.google.gson.Gson;
import com.xicheng.trid.Constant;
import com.xicheng.trid.DemoApplication;
import com.xicheng.trid.R;
import com.xicheng.trid.applib.controller.HXSDKHelper;
import com.xicheng.trid.hx.adapter.ChatAllHistoryAdapter;
import com.xicheng.trid.hx.db.DataBaseExecutor;
import com.xicheng.trid.hx.db.UserDao;
import com.xicheng.trid.hx.domain.User;
import com.xicheng.trid.hx.utils.UserUtils;
import com.xicheng.trid.json.CommonInfo;
import com.xicheng.trid.json.DeleteFriends;
import com.xicheng.trid.json.FriendListRequest;
import com.xicheng.trid.main.MainActivity;
import com.xicheng.trid.utils.ActivityController;
import com.xicheng.trid.utils.HttpUtil;
import com.xicheng.trid.utils.JsonParser;
import com.xicheng.trid.value.RequestUrlValue;
import com.xicheng.trid.value.ResponseTypeValue;

/**
 * 显示所有会话记录，比较简单的实现，更好的可能是把陌生人存入本地，这样取到的聊天记录是可控的
 * 
 */
public class ChatAllHistoryFragment extends Fragment implements OnClickListener {

	private InputMethodManager inputMethodManager;
	private ListView listView;
	private ChatAllHistoryAdapter adapter;
	private Handler handler;
	private boolean hidden;
	private List<EMConversation> conversationList = new ArrayList<EMConversation>();
	private boolean isfromSever=false;
	private DataBaseExecutor executor;
	private static final String TAG="ChatAllHistoryFragment";
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.i("fragmnet","onCreateView");
	
		return inflater.inflate(R.layout.fragment_conversation_history, container, false);
		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.i("fragmnet","onActivityCreate");;
		super.onActivityCreated(savedInstanceState);
		if(savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
		inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);	
		//requestList();
		conversationList.clear();
		conversationList.addAll(loadConversationsWithRecentChat());
		//双方关系三天之后从列表中删除
//		delUserContact(conversationList);
		listView = (ListView) getView().findViewById(R.id.list);
		executor=new DataBaseExecutor(getActivity());
		Log.i(TAG+"MSGBODY",executor.getColumnValue(1, "msgbody")+" ");
		Log.i(TAG+"listsize",conversationList.size()+"  ");
		if(conversationList.size()==0){
			requestConver();
		}
		else{
			adapter = new ChatAllHistoryAdapter(getActivity(), 1, conversationList);
			listView.setAdapter(adapter);		
		}
		final String st2 = getResources().getString(R.string.Cant_chat_with_yourself);
		
		//单击进入单聊页面
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				EMConversation conversation = (EMConversation) adapter.getItem(position);
				String username = conversation.getUserName();
				if (username.equals(DemoApplication.getInstance().getUserName()))
					Toast.makeText(getActivity(), st2, 0).show();
				else {
				    // 进入聊天页面（单聊）
					Log.i(TAG+115, "readyto startChat");
				    Intent intent = new Intent(getActivity(), ChatActivity.class);
				    intent.putExtra("userId", username);
				    startActivity(intent);
				}
			}
		});
		// 注册上下文菜单
		registerForContextMenu(listView);

		listView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 隐藏软键盘
				hideSoftKeyboard();
				return false;
			}

		});
		
		
		
		Handler newhandler=new Handler(Looper.getMainLooper()){
			public void handleMessage(Message msg)
			{
				if(msg.what==100){
					//调用系统函数重启(放弃)
					ActivityController.finishAll();
					Intent i = getActivity().getBaseContext().getPackageManager() 
							.getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName()); 
							i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
							startActivity(i);
				}	
				else if(msg.what==200){
					//刷新视图
					refresh();
				}
			}
		};
		importMessageIfNecessary(newhandler);
		
	}
	//由于imoportmessage导致conversationlist的消息为空，这里采取开子线程的方法，放慢importmessage的步骤，让
	//adapter先一步加载
	private void importMessageIfNecessary(final Handler handler) {
		new Thread(new Runnable() {
			@Override
			public void run() {	
				while (true){
					try {
						Thread.sleep(50);
						if(isfromSever){
							for(int i=0;i<conversationList.size();i++){
								EMChatManager.getInstance().importMessages(conversationList.get(i).getAllMessages());
							}
							EMChatManager.getInstance().loadAllConversations();
							isfromSever=false;
							Log.i("TAG LINE165",conversationList.size()+" ");
							Message msg=handler.obtainMessage();
							msg.what=200;
							//Thread.sleep(5000);
						    handler.sendMessage(msg);
							
							break;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
			}	
		}).start();
	}

	/**
	 * 处理与联系人的关系，三天后删除
	 * @param conversationList
	 */
	private void delUserContact(List<EMConversation> conversationList) {
		List<EMConversation> templist=new ArrayList();
		for(int i=0;i<conversationList.size();i++){
			User user=UserUtils.getUserInfor(conversationList.get(i).getUserName());
			//超过三天，关系移除
			if((user.getAvatar()-new Date().getTime())<=0){
				templist.add(conversationList.get(i));
				requestDeleteList(user.getUsername());
				EMChatManager.getInstance().deleteConversation(user.getUsername());
			}
		}
		conversationList.removeAll(templist);
		refresh();
		
	}
	/**
	 * 告诉服务器用户将到期的联系人删除
	 */
	private void requestDeleteList(final String username){
		Handler handler=new Handler(Looper.getMainLooper()){
			public void handleMessage(Message msg){
				if(msg.what==ResponseTypeValue.INTENT_ERROR)
					handleError();
				else{
					try {
						JSONObject obj = new JSONObject(msg.obj.toString());
						handleResult(obj,username);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}	
			}
		};
		HttpUtil.setHandler(handler);
		HttpUtil.postRequest(RequestUrlValue.DELETE_FRIEND_REQUEST, new Gson().toJson(new DeleteFriends(username)));
	}
	/**
	 * 从网络获取与所有联系人的历史记录
	 */
	private void requestConver(){
		handler=new Handler(Looper.getMainLooper()){
			public void handleMessage(Message msg)
			{
				if(msg.what==ResponseTypeValue.INTENT_ERROR)
					handleError();
				else{
					try {
						JSONObject obj = new JSONObject(msg.obj.toString());
						handleConverResult(obj);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}	
			}
		};
		HttpUtil.setHandler(handler);
		HttpUtil.postRequest(RequestUrlValue.GET_CHAT_RECORD, new Gson().toJson(new CommonInfo()));
	}
	/**
	 * 获取历史记录以得到conversationList
	 * @param obj
	 */
	private void handleConverResult(JSONObject obj)throws Exception{
		conversationList=JsonParser.getInstance().getConversationList(obj);	
		adapter = new ChatAllHistoryAdapter(getActivity(), 1, conversationList);
		listView.setAdapter(adapter);	
		this.isfromSever=true;
		Log.i(TAG+"SIZE",conversationList.size()+"");
		
		
	}
	

	private void handleError(){
		Toast.makeText(getActivity(), "获取失败，请检查网络", Toast.LENGTH_LONG).show();
	}
	/**
	 * 在本地数据库中删除联系人
	 * @param obj
	 */
	protected void handleResult(JSONObject obj,String username) {
		try {
			if(obj.getBoolean("success")){
				//更新内存中的好友列表
	    		UserDao dao = new UserDao(getActivity());
            	dao.deleteContact(username);
            	DemoApplication.getInstance().updateContactList();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
	}

	void hideSoftKeyboard() {
		if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getActivity().getCurrentFocus() != null)
				inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		// if(((AdapterContextMenuInfo)menuInfo).position > 0){ m,
		getActivity().getMenuInflater().inflate(R.menu.delete_message, menu); 
		// }
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		boolean handled = false;
		boolean deleteMessage = false;
		if (item.getItemId() == R.id.delete_message) {
			deleteMessage = true;
			handled = true;
		} else if (item.getItemId() == R.id.delete_contact) {
			deleteMessage = true;
			handled = true;
			EMConversation tobeDeleteCons = (EMConversation) adapter.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
			// 删除此会话
			EMChatManager.getInstance().deleteConversation(tobeDeleteCons.getUserName(), tobeDeleteCons.isGroup(), deleteMessage);
			//删除好友
			requestDeleteList(tobeDeleteCons.getUserName());
		}
		return handled ? true : super.onContextItemSelected(item);
	}

	/**
	 * 刷新页面
	 */
	public void refresh() {
		conversationList.clear();
		conversationList.addAll(loadConversationsWithRecentChat());
		Log.i(TAG+"refresh", conversationList.size()+"");
		if(adapter != null)
		    adapter.notifyDataSetChanged();
	}

	/**
	 * 获取所有会话
	 * 
	 * @param context
	 * @return
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        +	 */
	private List<EMConversation> loadConversationsWithRecentChat() {
		
		EMChatManager.getInstance().loadAllConversations();
		// 获取内存中的所有会话（先用load message），包括陌生人?
		Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
		Log.i("conversations",conversations.toString());
		// 过滤掉messages size为0的conversation
		/**
		 * 如果在排序过程中有新消息收到，lastMsgTime会发生变化
		 * 影响排序过程，Collection.sort会产生异常
		 * 保证Conversation在Sort过程中最后一条消息的时间不变 
		 * 避免并发问题
		 */
		List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
		synchronized (conversations) {
			for (EMConversation conversation : conversations.values()) {
				String username = conversation.getUserName(); 
				User user = UserUtils.getUserInfor(username);
		    	Long deadline  = user.getAvatar();
		    	if(deadline !=null && deadline < System.currentTimeMillis())
		    	{
		    		//三天到期，发送解除好友请求，删除对话
		    		EMChatManager.getInstance().deleteConversation(username);

		    		requestDeleteList(username);		    	
		    	}
		    	else if (conversation.getAllMessages().size() != 0 ) {
						sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
				}
		    	
			}
			Log.i("sortlist", sortList.size()+" ");
		}
		try {
			// Internal is TimSort algorithm, has bug
			sortConversationByLastChatTime(sortList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<EMConversation> list = new ArrayList<EMConversation>();
		for (Pair<Long, EMConversation> sortItem : sortList) {
			list.add(sortItem.second);
		}
		Log.i("list",list.toString());
		return list;
	}

	/**
	 * 根据最后一条消息的时间排序
	 * 
	 * @param usernames
	 */
	private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
		Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
			@Override
			public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

				if (con1.first == con2.first) {
					return 0;
				} else if (con2.first > con1.first) {
					return 1;
				} else {
					return -1;
				}
			}

		});
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.hidden = hidden;
		if (!hidden) {
			refresh();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!hidden && ! ((MainActivity)getActivity()).isConflict) {
			Log.i(TAG,"onResume");
			refresh();
		}
	}

	@Override
    public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
        if(((MainActivity)getActivity()).isConflict){
        	outState.putBoolean("isConflict", true);
        }else if(((MainActivity)getActivity()).getCurrentAccountRemoved()){
        	outState.putBoolean(Constant.ACCOUNT_REMOVED, true);
        }
    }

    @Override
    public void onClick(View v) {        
    }
    
   
}
