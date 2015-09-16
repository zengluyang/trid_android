package com.xicheng.trid.json;

import com.xicheng.trid.value.ConnInfo;

public class FriendListRequest {
	private String type;
	private String tel;
	private String token;
	
	public FriendListRequest(){
		type="get_friend_list";
		tel=ConnInfo.TEL;
		token=ConnInfo.TOKEN;
	}
}
