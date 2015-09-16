package com.xicheng.trid.json;

import com.xicheng.trid.value.ConnInfo;
import com.xicheng.trid.value.RequestTypeValue;
/**
 * 请求个人信息数据
 * @author DengRenbin
 *
 */
public class Profile {
	private String type;
	private String token;
	private String tel;
	public Profile(){
		type = RequestTypeValue.SMS_VALIDATION_REQUEST;
		this.token = ConnInfo.TOKEN;
		this.tel = ConnInfo.TEL;
	}
}
