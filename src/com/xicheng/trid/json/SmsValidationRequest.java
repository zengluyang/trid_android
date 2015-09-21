package com.xicheng.trid.json;

import com.xicheng.trid.value.RequestTypeValue;

public class SmsValidationRequest {
	
	private String type;
	private String tel;
	private Long time;
	public SmsValidationRequest(String tel) {
		this.type = RequestTypeValue.SMS_VALIDATION_REQUEST;
		this.tel = tel;
		this.time = System.currentTimeMillis();
	}
}
