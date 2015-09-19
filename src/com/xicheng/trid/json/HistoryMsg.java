package com.xicheng.trid.json;

import com.xicheng.trid.applib.controller.HXSDKHelper;
import com.xicheng.trid.value.RequestTypeValue;

public class HistoryMsg {
	private String type;
	private String tel;
	private String token;
	private String peer_huanxin_id;
	private int limit;
	private int offset;
	
	public HistoryMsg(){
		this.type=RequestTypeValue.SMS_HISTORY;
		this.tel=HXSDKHelper.getInstance().getHXId();
		this.token=HXSDKHelper.getInstance().getToken();
		this.peer_huanxin_id=HXSDKHelper.getInstance().getHXId();
		this.limit=10;
		this.offset=0;
	}
}
