package com.xicheng.trid.hx.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.xicheng.trid.hx.adapter.ChatAllHistoryAdapter;
import com.xicheng.trid.hx.db.DataBaseExecutor;
import com.xicheng.trid.hx.db.UserDao;
import com.xicheng.trid.hx.domain.User;
import com.xicheng.trid.json.CommonInfo;
import com.xicheng.trid.json.FriendListRequest;
import com.xicheng.trid.main.MainActivity;
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
	private UserDao dao;
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
		conversationList.addAll(loadConversationsWithRecentChat());
		listView = (ListView) getView().findViewById(R.id.list);
		executor=new DataBaseExecutor(getActivity());
		Log.i(TAG+"MSGBODY",executor.getColumnValue(1, "msgbody")+" ");
		if(conversationList.size()==0){
			requestConver();
		}
		else{
			adapter = new ChatAllHistoryAdapter(getActivity(), 1, conversationList);
			// 设置adapter
			listView.setAdapter(adapter);
		}		
		
		final String st2 = getResources().getString(R.string.Cant_chat_with_yourself);
		
		//单击进入单聊页面
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				EMConversation conversation = adapter.getItem(position);
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
					} catch (JSONException e) {
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
	private void handleConverResult(JSONObject obj){
		conversationList=JsonParser.getInstance().getConversationList(obj);
		for(int i=0;i<conversationList.size();i++){
			EMChatManager.getInstance().importMessages(conversationList.get(i).getAllMessages());
		}
		Log.i(TAG+"SIZE",conversationList.size()+"");
		
		adapter = new ChatAllHistoryAdapter(getActivity(), 1, conversationList);
		listView.setAdapter(adapter);
	}
	

	private void handleError(){
		Toast.makeText(getActivity(), "获取失败，请检查网络", Toast.LENGTH_LONG).show();
	}
	/**
	 * 从网络获取好友列表并存储
	 * @param obj
	 */
	protected void handleResult(JSONObject obj) {
		// TODO Auto-generated method stub
		
		
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
		} else if (item.getItemId() == R.id.delete_conversation) {
			deleteMessage = false;
			handled = true;
		}
		EMConversation tobeDeleteCons = adapter.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
		// 删除此会话
		EMChatManager.getInstance().deleteConversation(tobeDeleteCons.getUserName(), tobeDeleteCons.isGroup(), deleteMessage);
		
		
		return handled ? true : super.onContextItemSelected(item);
	}

	/**
	 * 刷新页面
	 */
	public void refresh() {
		conversationList.clear();
		conversationList.addAll(loadConversationsWithRecentChat());
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

		// 获取内存中的所有会话（先用load message），包括陌生人?
		Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
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
				if (conversation.getAllMessages().size() != 0 ) {
						sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
				}
			}
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
