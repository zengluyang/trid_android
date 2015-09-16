package com.xicheng.trid.json;

import com.xicheng.trid.value.ConnInfo;
import com.xicheng.trid.value.RequestTypeValue;

public class AddFriendRequest {
	private String type;
	private String tel;
	private String token;
	private String peer_tel;
	private String word;
	public AddFriendRequest(String peer_tel, String word){
		this.type = RequestTypeValue.ADD_FRIEND_REQUEST;
		this.tel = ConnInfo.TEL;
		this.token = ConnInfo.TOKEN;
		this.peer_tel = peer_tel;
		this.word = word;
	}
}
