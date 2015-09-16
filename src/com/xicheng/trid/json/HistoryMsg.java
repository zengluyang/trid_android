package com.xicheng.trid.json;

import com.xicheng.trid.value.ConnInfo;
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
		this.tel=ConnInfo.TEL;
		this.token=ConnInfo.TOKEN;
		this.peer_huanxin_id=ConnInfo.HUANXIN_ID;
		this.limit=10;
		this.offset=0;
	}
}
