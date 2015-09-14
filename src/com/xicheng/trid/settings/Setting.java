package com.xicheng.trid.settings;

import java.util.List;

import android.content.Context;
import android.widget.ImageButton;

import com.xicheng.trid.R;
import com.xicheng.trid.hx.db.UserDao;
import com.xicheng.trid.value.FinalValue;

public class Setting {
	public static final String SETTINGNAME_RECEIVE_CRUSH = "receive_crush";
	public static final String SETTINGNAME_IMMEDIATELY_CHAT = "immediately_chat";
	public static final String SETTINGNAME_RECEIVE_MESSAGE = "receive_message";
	public static final String SETTINGNAME_VOICE = "voice";
	public static final String SETTINGNAME_VIBRANCE = "vibrance";
	public static final String SETTINGNAME_TEL_PERMISSION = "tel_permission";
	public static final String SETTINGNAME_LOCATION_PERMISSION = "location_permission";
	
	
	public static List<Setting> settingsList;// 保存所有设置

	private String settingName;
	private Integer status;
	/**
	 * 初始化设置，即恢复默认设置
	 */
	public static void init(Context context){
		Setting setting;
		setting = new Setting(SETTINGNAME_RECEIVE_CRUSH, FinalValue.TRUE);
		settingsList.add(setting);
		setting = new Setting(SETTINGNAME_IMMEDIATELY_CHAT, FinalValue.TRUE);
		settingsList.add(setting);
		setting = new Setting(SETTINGNAME_RECEIVE_MESSAGE, FinalValue.TRUE);
		settingsList.add(setting);
		setting = new Setting(SETTINGNAME_VOICE, FinalValue.TRUE);
		settingsList.add(setting);
		setting = new Setting(SETTINGNAME_VIBRANCE, FinalValue.TRUE);
		settingsList.add(setting);
		setting = new Setting(SETTINGNAME_TEL_PERMISSION, FinalValue.TRUE);
		settingsList.add(setting);
		setting = new Setting(SETTINGNAME_LOCATION_PERMISSION, FinalValue.TRUE);
		settingsList.add(setting);
		new UserDao(context).saveSettings(settingsList);
	}
	
	/**
	 * 加载设置
	 * @param setting
	 * @param imageButton
	 */
	public static void load(Setting setting, ImageButton imageButton) {
		int picResource = (setting.getStatus() == FinalValue.TRUE) ? R.drawable.kaiguan_on : R.drawable.kaiguan_off;
		imageButton.setImageResource(picResource);
	}
	/**
	 * 根据设置名查询本地设置
	 * @param settingName
	 * @return
	 */
	public static Setting getSetting(String settingName){
		for(int i = 0; i < settingsList.size(); i++){
			if(settingsList.get(i).getSettingName().equals(settingName)){
				return settingsList.get(i);
			}
		}
		Setting newsetting = new Setting(settingName, FinalValue.TRUE);
		settingsList.add(newsetting);
		return newsetting;
	}
	
	public Setting(String settingName, Integer status){
		this.settingName = settingName;
		this.status = status;
	}

	public String getSettingName() {
		return settingName;
	}
	public void setSettingName(String settingName) {
		this.settingName = settingName;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
