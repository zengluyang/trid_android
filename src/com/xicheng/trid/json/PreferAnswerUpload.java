package com.xicheng.trid.json;

import java.util.ArrayList;

import com.xicheng.trid.applib.controller.HXSDKHelper;
import com.xicheng.trid.value.RequestTypeValue;

/**
 * 二选一（偏好）结果上传Json对象
 * 
 * @author DengRenbin
 * 
 */
public class PreferAnswerUpload {
	@SuppressWarnings("unused")
	private String type;// 请求类型
	@SuppressWarnings("unused")
	private String tel;
	@SuppressWarnings("unused")
	private String token;
	int count = 0;// 二选一结果数量
	ArrayList<PreferAnswer> pf_answer;

	/**
	 * 新建对象并保存上传需要的参数值
	 */
	public PreferAnswerUpload() {
		this.type = RequestTypeValue.PF_ANSWER_UPLOAD;
		this.tel = HXSDKHelper.getInstance().getHXId();
		this.token = HXSDKHelper.getInstance().getToken();
		this.pf_answer = new ArrayList<PreferAnswer>();
	}

	/**
	 * 新建对象并保存上传需要的参数值,并添加二选一结果ArrayList
	 * 
	 * @param pfAnswers
	 */
	public PreferAnswerUpload(ArrayList<PreferAnswer> pfAnswers) {
		this();
		count = pfAnswers.size();
		this.pf_answer = pfAnswers;
	}

	/**
	 * 添加一个二选一结果
	 * 
	 * @param pfAnswer
	 */
	public void addPfAnswer(PreferAnswer pfAnswer) {
		pf_answer.add(pfAnswer);
		count++;
	}

	/**
	 * 添加一个二选一结果ArrayList
	 * 
	 * @param pfAnswers
	 */
	public void addPfAnswers(ArrayList<PreferAnswer> pfAnswers) {
		for (int i = 0; i < pfAnswers.size(); i++) {
			this.pf_answer.add(pfAnswers.get(i));
		}
		count += pfAnswers.size();
	}

	// 更新上传信息
	public void refresh() {
		this.type = RequestTypeValue.PF_ANSWER_UPLOAD;
		this.tel = HXSDKHelper.getInstance().getHXId();
		this.token = HXSDKHelper.getInstance().getToken();
		this.pf_answer = new ArrayList<PreferAnswer>();
		count = 0;
	}
}
