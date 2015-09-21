package com.xicheng.trid.json;

import com.xicheng.trid.applib.controller.HXSDKHelper;
import com.xicheng.trid.value.RequestTypeValue;

public class PfPictureRequest {
	private String type;
	private String tel;
	private String token;
	public PfPictureRequest() {
		type = RequestTypeValue.PF_PICTURE_REQUEST;
		tel = HXSDKHelper.getInstance().getHXId();
		token = HXSDKHelper.getInstance().getToken();
	}
}
