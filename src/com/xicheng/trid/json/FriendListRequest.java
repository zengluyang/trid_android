package com.xicheng.trid.json;

import com.xicheng.trid.applib.controller.HXSDKHelper;

public class FriendListRequest {
	private String type;
	private String tel;
	private String token;

	public FriendListRequest() {
		type = "get_friend_list";
		tel = HXSDKHelper.getInstance().getHXId();
		token = HXSDKHelper.getInstance().getToken();
	}
}
