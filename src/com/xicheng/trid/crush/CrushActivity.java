package com.xicheng.trid.crush;

import java.util.Hashtable;

import org.json.JSONException;
import org.json.JSONObject;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.google.gson.Gson;
import com.xicheng.trid.R;
import com.xicheng.trid.hx.activity.BaseActivity;
import com.xicheng.trid.hx.db.UserDao;
import com.xicheng.trid.hx.domain.User;
import com.xicheng.trid.json.AddFriendRequest;
import com.xicheng.trid.utils.HttpUtil;
import com.xicheng.trid.value.RequestUrlValue;
import com.xicheng.trid.value.ResultTypeValue;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 
 * @author DengRenbin
 *
 */
public class CrushActivity extends BaseActivity {

	private EditText edt_phoneNum;// 电话号码输入框
	private EditText edt_someWords;// 一句话输入框
	private Button btn_send;// 确认按钮
	private Button btn_return;// 取消按钮
	

	private AddFriendRequest post;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crush);
		// 按钮初始化
		edt_phoneNum = (EditText) findViewById(R.id.edt_crush_phoneNum);
		edt_someWords = (EditText) findViewById(R.id.edt_crush_somewords);
		btn_send = (Button) findViewById(R.id.btn_crush_send);
		btn_return = (Button) findViewById(R.id.btn_crush_return);

		// 确认发送
		btn_send.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String phoneNum = edt_phoneNum.getText().toString();
				String words = edt_someWords.getText().toString();
				if (phoneNum.length() == 0) {
					Toast.makeText(getApplicationContext(), "请输入对方手机号码", Toast.LENGTH_SHORT).show();
				} else {
					post = new AddFriendRequest(phoneNum, words);
					HttpUtil.postRequest(RequestUrlValue.ADD_FRIEND_REQUEST, new Gson().toJson(post));
				}
			}
		});
		// 取消按钮
		btn_return.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void handleResult(JSONObject obj) {
		try {
			if (obj.getString("type").equals(ResultTypeValue.ADD_FRIEND_RESULT)) {
				if (obj.getBoolean("success")) {
					JSONObject friend = obj.getJSONObject("friend");
					//存入该联系人信息到数据库库
					String from = friend.getString("huanxin_id");
					User user = new User(from);
					user.setAvatar(friend.getLong("expire"));
    			    user.setType(friend.getInt("type"));
    			    user.setTel(friend.getString("peer_tel"));
    			    if (friend.getString("chat_title")!= null)
    			    user.setChatTitle(friend.getString("chat_title"));
    			    UserDao dao = new UserDao(CrushActivity.this);
                	dao.saveContact(user);
					//发送第一条信息
					EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
					//设置消息body
					TextMessageBody txtBody = new TextMessageBody(obj.getString("word"));
					message.addBody(txtBody);
					message.setReceipt(from);

					//发送消息
					try {
						EMChatManager.getInstance().sendMessage(message);
					} catch (EaseMobException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Toast.makeText(getApplicationContext(), "您的申请已经成功发送给对方", Toast.LENGTH_SHORT).show();
					finish();
				} else
					switch (obj.getInt("error_no")) {
					case 3:
						Toast.makeText(getApplicationContext(), "请输入有效的电话号码", Toast.LENGTH_SHORT).show();
						break;
					case 5:
						Toast.makeText(getApplicationContext(), "不可以自己暗恋自己哦", Toast.LENGTH_SHORT).show();
						break;
					case 7:
						Toast.makeText(getApplicationContext(), "对方已经是己方好友", Toast.LENGTH_SHORT).show();
						break;
					case 6:
						Toast.makeText(getApplicationContext(), "对方尚未注册，我们将通过短信邀请对方", Toast.LENGTH_SHORT).show();
						finish();
						HttpUtil.postRequest(RequestUrlValue.SEND_INVITATION_REQUEST, new Gson().toJson(post));
						break;
					case 8:
						Toast.makeText(getApplicationContext(), "对方拒绝了您的请求/(ㄒoㄒ)/~~", Toast.LENGTH_SHORT).show();
						break;
					default:
						Toast.makeText(getApplicationContext(), "发生了错误", Toast.LENGTH_SHORT).show();
						break;
					}
			} else if (obj.getString("type").equals(ResultTypeValue.SEND_INVITATION_RESULT)) {
				if (obj.getBoolean("success")) {
					Toast.makeText(getApplicationContext(), "短信发送成功", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "短信发送失败", Toast.LENGTH_SHORT).show();
				}
			} else {
				Log.i("err", "CrushActivity-->handleResult(JSONObject obj)");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
