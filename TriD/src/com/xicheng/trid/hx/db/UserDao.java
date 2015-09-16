/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xicheng.trid.hx.db;

import java.util.List;
import java.util.Map;

import android.content.Context;


import com.xicheng.trid.chat_alarm.ChatAlarm;
import com.xicheng.trid.hx.domain.RobotUser;
import com.xicheng.trid.hx.domain.User;
/**
 *DAO实现类   ，实现了contact相关的数据库操作
 *如果要操作数据，则新建一个该对象，然后调用相应方法
 */
public class UserDao {
	public static final String TABLE_NAME = "uers";
	public static final String COLUMN_NAME_USERNAME = "username";
	public static final String COLUMN_NAME_AVATAR = "avatar";
	public static final String COLUMN_NAME_TYPE = "type";
	public static final String COLUMN_NAME_CHAT_TITLE  = "chat_title";
	public static final String COLUMN_NAME_TEL = "tel";
	
	public static final String PREF_TABLE_NAME = "pref";
	public static final String COLUMN_NAME_DISABLED_GROUPS = "disabled_groups";
	public static final String COLUMN_NAME_DISABLED_IDS = "disabled_ids";
	
	
	public static final String ALARM_TABLE_NAME = "alarm_table";
	public static final String COLUMN_NAME_CONTENT = "alarm_content";
	public static final String COLUMN_NAME_TIME = "alarm_time";
	public static final String COLUMN_NAME_WRITER = "alarm_writer";
	public static final String COLUMN_NAME_SWITCH = "alarm_on_off";
	public static final String COLUMN_NAME_FOR_WHO = "alarm_for_who";
	
	
	public static final String NOTES_TABLE_NAME = "notes_table";
	public static final String NOTELIST_TABLE_NAME = "notelist_table";
	public static final String COLUMN_NAME_WHO = "for_who";

	public static final String ROBOT_TABLE_NAME = "robots";
	public static final String ROBOT_COLUMN_NAME_ID = "username";
	public static final String ROBOT_COLUMN_NAME_NICK = "nick";
	public static final String ROBOT_COLUMN_NAME_AVATAR = "avatar";
	
	
	public UserDao(Context context) {
	    DemoDBManager.getInstance().onInit(context);
	}

	/**
	 * 保存好友list
	 * 
	 * @param contactList
	 */
	public void saveContactList(List<User> contactList) {
	    DemoDBManager.getInstance().saveContactList(contactList);
	}

	/**
	 * 获取好友list
	 * 
	 * @return
	 */
	public Map<String, User> getContactList() {
		
	    return DemoDBManager.getInstance().getContactList();
	}
	
	/**
	 * 删除一个联系人
	 * @param username
	 */
	public void deleteContact(String username){
	    DemoDBManager.getInstance().deleteContact(username);
	}
	
	/**
	 * 保存一个联系人
	 * @param user
	 */
	public void saveContact(User user){
	    DemoDBManager.getInstance().saveContact(user);
	}
	
	public void setDisabledGroups(List<String> groups){
	    DemoDBManager.getInstance().setDisabledGroups(groups);
    }
    
    public List<String>  getDisabledGroups(){       
        return DemoDBManager.getInstance().getDisabledGroups();
    }
    
    public void setDisabledIds(List<String> ids){
        DemoDBManager.getInstance().setDisabledIds(ids);
    }
    
    public List<String> getDisabledIds(){
        return DemoDBManager.getInstance().getDisabledIds();
    }
    
    public Map<String, RobotUser> getRobotUser(){
    	return DemoDBManager.getInstance().getRobotList();
    }
    
    public void saveRobotUser(List<RobotUser> robotList){
    	DemoDBManager.getInstance().saveRobotList(robotList);
    }
    
    public ChatAlarm queryAlarm(String username, String myName)
    {
    	return DemoDBManager.getInstance().queryAlarm(username,myName);
    }
    public void SaveAlarm(String username, String myName, String content, String time)
    {
    	DemoDBManager.getInstance().saveAlarm(username,myName, content, time);
    	
    }
}
