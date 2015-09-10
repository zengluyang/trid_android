package com.xicheng.trid.json;

import com.xicheng.trid.value.RequestTypeValue;

public class SmsValidationCode {
	private String type;
	private String tel;
	private Long time;
	private String code;

	public SmsValidationCode(String tel, String code) {
		this.type = RequestTypeValue.SMS_VALIDATION_CODE;
		this.tel = tel;
		this.code = code;
		this.time = System.currentTimeMillis();
	}
}
