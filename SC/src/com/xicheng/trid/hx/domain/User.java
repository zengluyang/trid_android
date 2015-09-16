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
package com.xicheng.trid.hx.domain;

import com.easemob.chat.EMContact;
/**
 * 值对象
 * VO类是一个包含属性和表中字段完全对应的类，并在该类中提供setter和getter方法来设置并获取该类中的属性。
 * */
public class User extends EMContact {
	private int unreadMsgCount;
	private long avatar;//即过期时间
	private int type;//1-暗恋自己的人，2-自己暗恋的人，3-陌生人 
	private String chat_title;
	private String tel;

	
	public User(){}
	
	public User(String username){
	    this.username = username;//hx_id
	}
	
	
	 public User(String tel ,String hx_id, int type, String chatTitle, long avatar ){
		 this.username  = hx_id;
		 this.tel = tel;
		 this.type = type;
		 this.chat_title = chatTitle;
		 this.avatar = avatar;
				 
	 }

	
	public int getUnreadMsgCount() {
		return unreadMsgCount;
	}

	public void setUnreadMsgCount(int unreadMsgCount) {
		this.unreadMsgCount = unreadMsgCount;
	}
	
	
/**
 * 获得头像
 */
	public long getAvatar() {
        return avatar;
    }

    public void setAvatar(long avatar) {
        this.avatar = avatar;
    }
   
    //联系人类型
    public int getType()
    {
    	return this.type;
    }
    
    public void setType(int type)
    {
    	this.type = type;
    }
    
    //聊天标题
    public String getChatTitle()
    {
    	return this.chat_title;
    }
    
    public void setChatTitle(String chat_title)
    {
    	this.chat_title = chat_title;
    }
 
    
    public void setTel(String tel)
    {
    	this.tel = tel;
    }

    public String getTel()
    {
    	return this.tel;
    }
    @Override
	public int hashCode() {
		return 17 * getUsername().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof User)) {
			return false;
		}
		return getUsername().equals(((User) o).getUsername());
	}

	@Override
	public String toString() {
		return nick == null ? username : nick;
	}
}
