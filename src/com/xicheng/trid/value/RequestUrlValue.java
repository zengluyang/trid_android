package com.xicheng.trid.value;

public class RequestUrlValue {
	// IP地址
	public static final String URL_PARENT = "http://101.200.89.240/";
	// 添加好友
	public static final String ADD_FRIEND_REQUEST;
	// 上传基本信息
	public static final String BASIC_INFO_UPLOAD;
	// 上传偏好结果
	public static final String PF_ANSWER_UPLOAD;
	// 请求偏好图片信息
	public static final String PF_PICTURE_REQUEST;
	// 请求我的信息
	public static final String PROFILE;
	// 发送短信邀请
	public static final String SEND_INVITATION_REQUEST;
	// 请求验证码
	public static final String SMS_VALIDATION_REQUEST;
	// 验证验证码
	public static final String SMS_VALIDATION_CODE;
	//获取聊天记录
	public static final String SMS_MSG_HISTORY;
	//获取好友列表
	public static final String GET_FRIEND_REQUEST;

	/**
	 * 执行初始化
	 */
	static {
		ADD_FRIEND_REQUEST = URL_PARENT + "index.php?r=contact/add-friend";
		BASIC_INFO_UPLOAD = URL_PARENT + "index.php?r=info/basic-info-upload";
		PF_ANSWER_UPLOAD = URL_PARENT + "index.php?r=info/pf-answer-upload";
		PF_PICTURE_REQUEST = URL_PARENT + "index.php?r=info/pf-picture-request";
		PROFILE = URL_PARENT + "index.php?r=user/profile";
		SEND_INVITATION_REQUEST = URL_PARENT + "index.php?r=contact/send-invitation";
		SMS_VALIDATION_REQUEST = URL_PARENT + "index.php?r=user/sms-validation-request";
		SMS_VALIDATION_CODE = URL_PARENT + "index.php?r=user/sms-validation-code";
		SMS_MSG_HISTORY=URL_PARENT+"index.php?r=chat-record/get-chat-record-per-conversation";
		GET_FRIEND_REQUEST=URL_PARENT+"index.php?r=contact/get-friend-list";
	}
}
