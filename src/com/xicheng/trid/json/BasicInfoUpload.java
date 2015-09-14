package com.xicheng.trid.json;

import com.xicheng.trid.value.ConnInfo;
import com.xicheng.trid.value.RequestTypeValue;

/**
 * 基础信息上传
 * @author DengRenbin
 *
 */
public class BasicInfoUpload {
	private String type;// 请求类型
	private String tel;
	private String token;
	private int sex;//性别
	private Birthdate birthdate;//生日
	/**
	 * 新建对象并保存上传需要的参数值
	 */
	public BasicInfoUpload() {
		this.type = RequestTypeValue.BASIC_INFO_UPLOAD;
		this.tel = ConnInfo.TEL;
		this.token = ConnInfo.TOKEN;
	}
	/**
	 * 新建对象并保存上传需要的参数值，同时保存性别和生日
	 * @param sex
	 * @param birthdate
	 */
	public BasicInfoUpload(int sex, Birthdate birthdate){
		this();
		this.sex =sex;
		this.birthdate= birthdate;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public void setBirthdate(Birthdate birthdate) {
		this.birthdate = birthdate;
	}
}
