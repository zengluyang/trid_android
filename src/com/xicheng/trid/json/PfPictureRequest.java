package com.xicheng.trid.json;

import org.json.JSONObject;

import com.xicheng.trid.value.ConnInfo;
import com.xicheng.trid.value.RequestTypeValue;

import android.content.Context;
import android.util.Log;

public class PfPictureRequest {
	private String type;
	private String tel;
	private String token;
	public PfPictureRequest() {
		type = RequestTypeValue.PF_PICTURE_REQUEST;
		tel = ConnInfo.TEL;
		token = ConnInfo.TOKEN;
		//日后记得删掉这行注释
	}
}
