package com.xicheng.trid.json;

import com.xicheng.trid.value.RequestTypeValue;

public class SendInvitationRequest {
	private String type;
	private String tel;
	private String token;
	private String peer_tel;
	private String word;
	public SendInvitationRequest(String tel, String token, String peer_tel, String word){
		this.type = RequestTypeValue.SEND_INVITATION_REQUEST;
		this.tel = tel;
		this.token = token;
		this.peer_tel = peer_tel;
		this.word = word;
	}
}