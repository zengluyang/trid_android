package com.xicheng.trid.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMConversation.EMConversationType;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.LocationMessageBody;
import com.easemob.chat.MessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VoiceMessageBody;
import com.xicheng.trid.applib.controller.HXSDKHelper;

/**
 * 单例模式,json解析器
 * @author zyl
 *
 */
public class JsonParser {
	private static JsonParser parser=null;
	private EMConversation conversation;
	private List<EMMessage> list;
	private EMMessage msg;
	private static String chatName;
	private final static String TAG="JsonParser";
	//私有构造器，防止非法创建
	private JsonParser(){}
	//创建一个单例
	public static synchronized JsonParser getInstance(){
		if(parser==null){
			parser=new JsonParser();
		}
		return parser;
	}
	/**
	 * 将零散的消息信息包装成EMMessage
	 * @param obj
	 */
	private void setMessage(JSONObject obj){
		list=new ArrayList();
		try {
			JSONArray data=obj.getJSONArray("chat_records");
			for (int i=0;i<data.length();i++){
				JSONObject info=data.getJSONObject(i);
				//消息实体
				JSONObject body=info.getJSONObject("payload").getJSONArray("bodies")
						.getJSONObject(0);
				//判断消息类型，是发送的消息还是接收的消息
				String messageType=info.getString("from").equals(HXSDKHelper.getInstance().getHXId())?"send":"receive";
				String type=info.getJSONObject("payload").getJSONArray("bodies")
						.getJSONObject(0).getString("type");
				//消息类型为文本类
				if(type.equals("txt")){
					if(messageType.equals("send"))
						msg=EMMessage.createSendMessage(EMMessage.Type.TXT);
					else
						msg=EMMessage.createReceiveMessage(EMMessage.Type.TXT);
					msg.addBody(new TextMessageBody(body.getString("msg")));
					msg.setType(EMMessage.Type.TXT);
				}
				//消息类型为图片类
				else if(type.equals("img")){
					if(messageType.equals("send"))
						msg=EMMessage.createSendMessage(EMMessage.Type.IMAGE);
					else
						msg=EMMessage.createReceiveMessage(EMMessage.Type.IMAGE);
					msg.addBody(new ImageMessageBody(new File(body.getString("url"))));
					msg.setType(EMMessage.Type.IMAGE);
					
				}
				//消息类型为语音类
				else if(type.equals("audio")){
					if(messageType.equals("send"))
						msg=EMMessage.createSendMessage(EMMessage.Type.VOICE);
					else
						msg=EMMessage.createReceiveMessage(EMMessage.Type.VOICE);
					msg.addBody(new VoiceMessageBody(new File(body.getString("url")),body.getInt("length")));
					msg.setType(EMMessage.Type.VOICE);
					
				}
				//消息类型为地理位置类
				else if(type.equals("loc")){
					if(messageType.equals("send"))
						msg=EMMessage.createSendMessage(EMMessage.Type.LOCATION);
					else
						msg=EMMessage.createReceiveMessage(EMMessage.Type.LOCATION);
					msg.addBody(new LocationMessageBody(body.getString("addr"),body.getDouble("lat"),body.getDouble("lng")));
					msg.setType(EMMessage.Type.VOICE);
				}
				else{
					Log.i(TAG,"error");
					break;
				}
				msg.setTo(info.getString("to"));
				msg.setFrom(info.getString("from"));
				msg.setMsgId(info.getString("msg_id"));
				msg.setChatType(EMMessage.ChatType.Chat);
				if(messageType.equals("send")){
					chatName=info.getString("to");
				}
				list.add(msg);
			}
				
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		
	}
	/**
	 * 填充message并封装成conversation
	 * @param obj
	 * @return
	 */
	
	public EMConversation getConversation(JSONObject obj){
		setMessage(obj);
		conversation=EMChatManager.getInstance().getConversationByType(chatName, EMConversationType.Chat);
		for(EMMessage msg:list)
			conversation.addMessage(msg);
		return conversation;
		
	}
	/**
	 * 根据服务器返回的历史记录信息添加对话
	 */
	public List<EMConversation> getConversationList(JSONObject obj){
		List<EMConversation> list=new ArrayList();
		try {
			JSONArray data=obj.getJSONArray("chat_records");
			for(int i=0;i<data.length();i++){
				list.add(getConversation(data.getJSONObject(i)));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 测试方法，用于检测是否接收到服务器的存储消息
	 */
	public void logTest(){
		for (int i = 0; i < list.size(); i++) {
			EMMessage msg=list.get(i);
			Log.i(TAG,msg.getMsgId());
			MessageBody msBody=msg.getBody();
			switch(msg.getType()){
				case TXT:
					Log.i(TAG,((TextMessageBody)msBody).getMessage());
					break;
				case IMAGE:
					Log.i(TAG,((ImageMessageBody)msBody).getThumbnailUrl());
					break;
				case VOICE:
					Log.i(TAG, ((VoiceMessageBody)msBody).getLength()+"");
					break;
				default:
					break;
			}
		}
	}
	
}
