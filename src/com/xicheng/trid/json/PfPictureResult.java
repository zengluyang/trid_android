package com.xicheng.trid.json;

public class PfPictureResult {
	private String type;
	private Boolean success;
	private int error_no;
	private String error_msg;
	private Pics pf;
	
	class Pics{
		private int pf_id;
		private Pic pic0;
		private Pic pic1;
	}
	class Pic{
		private String type;
		private String name_en;
		private String name_cn;
		private String data;
	}
}
