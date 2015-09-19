package com.xicheng.trid.json;

import com.xicheng.trid.applib.controller.HXSDKHelper;
import com.xicheng.trid.value.RequestTypeValue;

public class CommonInfo {
	private String type;
	private String tel;
	private String token;
	public CommonInfo(){
		type = RequestTypeValue.FRIEND_HISTORY;
		tel = HXSDKHelper.getInstance().getHXId();
		token = HXSDKHelper.getInstance().getToken();
	}
}
