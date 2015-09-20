package com.xicheng.trid.json;

import com.xicheng.trid.applib.controller.HXSDKHelper;
import com.xicheng.trid.value.RequestTypeValue;

public class DeleteFriends {
	private String type;
	private String tel;
	private String token;
	private String peer_tel;
	
	public DeleteFriends(String peer_tel){
		type=RequestTypeValue.DELETE_FRIEND_REQUEST;
		this.tel = HXSDKHelper.getInstance().getHXId();
		this.token = HXSDKHelper.getInstance().getToken();
		this.peer_tel = peer_tel;
	}
}
