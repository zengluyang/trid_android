package com.xicheng.trid.hx.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.easemob.util.HanziToPinyin;
import com.xicheng.trid.Constant;
import com.xicheng.trid.chat_alarm.ChatAlarm;
import com.xicheng.trid.hx.domain.InviteMessage;
import com.xicheng.trid.hx.domain.RobotUser;
import com.xicheng.trid.hx.domain.User;
import com.xicheng.trid.hx.domain.InviteMessage.InviteMesageStatus;

public class DemoDBManager {
    static private DemoDBManager dbMgr = new DemoDBManager();
    private DbOpenHelper dbHelper;
    
    void onInit(Context context){
        dbHelper = DbOpenHelper.getInstance(context);
    }
    
    public static synchronized DemoDBManager getInstance(){
        return dbMgr;
    }
    
    /**
     * 保存好友list
     * 
     * @param contactList
     */
    synchronized public void saveContactList(List<User> contactList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(UserDao.TABLE_NAME, null, null);
            for (User user : contactList) {
                ContentValues values = new ContentValues();
                values.put(UserDao.COLUMN_NAME_USERNAME, user.getUsername());
                values.put(UserDao.COLUMN_NAME_AVATAR, user.getAvatar());
            	values.put(UserDao.COLUMN_NAME_TYPE, user.getType());
            	values.put(UserDao.COLUMN_NAME_TEL, user.getTel());
                if(user.getChatTitle() != null)
                    values.put(UserDao.COLUMN_NAME_CHAT_TITLE, user.getChatTitle());
                   
                db.replace(UserDao.TABLE_NAME, null, values);
            }
        }
    }

    /**
     * 获取好友list
     * 
     * @return
     */
    synchronized public Map<String, User> getContactList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Map<String, User> users = new HashMap<String, User>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + UserDao.TABLE_NAME /* + " desc" */, null);
            while (cursor.moveToNext()) {
                String username = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_USERNAME));
             //   String nick = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_NICK));
                long avatar = cursor.getLong(cursor.getColumnIndex(UserDao.COLUMN_NAME_AVATAR));
                String tel = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_TEL));
                int type =  cursor.getInt(cursor.getColumnIndex(UserDao.COLUMN_NAME_TYPE));
                String chat_title = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_CHAT_TITLE));
                
                User user = new User();
                user.setUsername(username);
            //    user.setNick(nick);
                user.setAvatar(avatar);
                user.setType(type);
                if(chat_title!= null)
                {
                	user.setChatTitle(chat_title);
                }
               
                users.put(tel, user);
            }
            cursor.close();
        }
        return users;
    }
    
    /**
     * 删除一个联系人
     * @param username
     */
    synchronized public void deleteContact(String username){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(db.isOpen()){
            db.delete(UserDao.TABLE_NAME, UserDao.COLUMN_NAME_USERNAME + " = ?", new String[]{username});
        }
    }
    
    /**
     * 保存一个联系人
     * @param user
     */
    synchronized public void saveContact(User user){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserDao.COLUMN_NAME_USERNAME, user.getUsername());
        values.put(UserDao.COLUMN_NAME_AVATAR, user.getAvatar());
    	values.put(UserDao.COLUMN_NAME_TYPE, user.getType());
    	values.put(UserDao.COLUMN_NAME_TEL, user.getTel());
        if(user.getChatTitle() != null)
            values.put(UserDao.COLUMN_NAME_CHAT_TITLE, user.getChatTitle());
        values.put(UserDao.COLUMN_NAME_AVATAR, user.getAvatar());
        if(db.isOpen()){
            db.replace(UserDao.TABLE_NAME, null, values);
        }
    }
    
    public void setDisabledGroups(List<String> groups){
        setList(UserDao.COLUMN_NAME_DISABLED_GROUPS, groups);
    }
    
    public List<String>  getDisabledGroups(){       
        return getList(UserDao.COLUMN_NAME_DISABLED_GROUPS);
    }
    
    public void setDisabledIds(List<String> ids){
        setList(UserDao.COLUMN_NAME_DISABLED_IDS, ids);
    }
    
    public List<String> getDisabledIds(){
        return getList(UserDao.COLUMN_NAME_DISABLED_IDS);
    }
    
    synchronized private void setList(String column, List<String> strList){
        StringBuilder strBuilder = new StringBuilder();
        
        for(String hxid:strList){
            strBuilder.append(hxid).append("$");
        }
        
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(column, strBuilder.toString());

            db.update(UserDao.PREF_TABLE_NAME, values, null,null);
        }
    }
    
    synchronized private List<String> getList(String column){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + column + " from " + UserDao.PREF_TABLE_NAME,null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }

        String strVal = cursor.getString(0);
        if (strVal == null || strVal.equals("")) {
            return null;
        }
        
        cursor.close();
        
        String[] array = strVal.split("$");
        
        if(array != null && array.length > 0){
            List<String> list = new ArrayList<String>();
            for(String str:array){
                list.add(str);
            }
            
            return list;
        }
        
        return null;
    }
    
 
    
  
    

    
   
    
    synchronized public void closeDB(){
        if(dbHelper != null){
            dbHelper.closeDB();
        }
    }
    
    
    /**
     * Save Robot list
     */
	synchronized public void saveRobotList(List<RobotUser> robotList) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.delete(UserDao.ROBOT_TABLE_NAME, null, null);
			for (RobotUser item : robotList) {
				ContentValues values = new ContentValues();
				values.put(UserDao.ROBOT_COLUMN_NAME_ID, item.getUsername());
				if (item.getNick() != null)
					values.put(UserDao.ROBOT_COLUMN_NAME_NICK, item.getNick());
				if (item.getAvatar() != null)
					values.put(UserDao.ROBOT_COLUMN_NAME_AVATAR, item.getAvatar());
				db.replace(UserDao.ROBOT_TABLE_NAME, null, values);
			}
		}
	}
    
    /**
     * load robot list
     */
	synchronized public Map<String, RobotUser> getRobotList() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Map<String, RobotUser> users = null;
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from " + UserDao.ROBOT_TABLE_NAME, null);
			if(cursor.getCount()>0){
				users = new HashMap<String, RobotUser>();
			};
			while (cursor.moveToNext()) {
				String username = cursor.getString(cursor.getColumnIndex(UserDao.ROBOT_COLUMN_NAME_ID));
				String nick = cursor.getString(cursor.getColumnIndex(UserDao.ROBOT_COLUMN_NAME_NICK));
				String avatar = cursor.getString(cursor.getColumnIndex(UserDao.ROBOT_COLUMN_NAME_AVATAR));
				RobotUser user = new RobotUser();
				user.setUsername(username);
				user.setNick(nick);
				user.setAvatar(avatar);
				String headerName = null;
				if (!TextUtils.isEmpty(user.getNick())) {
					headerName = user.getNick();
				} else {
					headerName = user.getUsername();
				}
				if(Character.isDigit(headerName.charAt(0))){
					user.setHeader("#");
				}else{
					user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target
							.substring(0, 1).toUpperCase());
					char header = user.getHeader().toLowerCase().charAt(0);
					if (header < 'a' || header > 'z') {
						user.setHeader("#");
					}
				}
				
				users.put(username, user);
			}
			cursor.close();
		}
		return users;
	}
      public ChatAlarm queryAlarm(String username, String myName)
      {
    	  SQLiteDatabase db = dbHelper.getReadableDatabase();
    	  ChatAlarm myAlarm = new ChatAlarm();
    	  if (db.isOpen()) {
  			Cursor cursor = db.rawQuery("select * from " + UserDao.ALARM_TABLE_NAME, null);
  			if(cursor!= null){
  				
  				while (cursor.moveToNext()) {
  				   String writer  = cursor.getString(0); //获取第一列的值,第一列的索引从0开始
  				   if(writer.equals(username))
  				   {
  					 String for_who = cursor.getString(1);//获取第二列的值
  					 if(for_who.equals(myName))
  					 {
  						 myAlarm.setContentForMe(cursor.getString(2));
  						 myAlarm.setTimeForMe(cursor.getString(3));
  						 myAlarm.switchAlarm(cursor.getInt(4));
  					 }
  					   
  				   }
  				   else if(writer.equals(myName))
  				   {
  					 String for_who = cursor.getString(1);//获取第二列的值
  					 if(for_who.equals(username))
  					 {
  						 myAlarm.setContentForHe(cursor.getString(2));
  						 myAlarm.setTimeForHe(cursor.getString(3));
  					 }
  				   }
  				}
  				cursor.close();
  				return myAlarm;
  			}
  			
  		}
    	  return null;
      }
      
      public void saveAlarm(String chatTo, String myName,String content, String time)
      {
    	  SQLiteDatabase db = null;
    	  //插入一个记录
    	  try {
    		  db = dbHelper.getReadableDatabase();
    		  if(db.isOpen()){
    			  ContentValues values = new ContentValues();
        	      values.put(UserDao.COLUMN_NAME_WRITER , myName);
        	      values.put(UserDao.COLUMN_NAME_FOR_WHO, chatTo);
        	      values.put(UserDao.COLUMN_NAME_CONTENT, content);
        	      values.put(UserDao.COLUMN_NAME_TIME, time);
        	      db.insert(UserDao.ALARM_TABLE_NAME, null, values);
    		  }
    	      
    	    } catch (Exception e) {
    	      // TODO: handle exception
    	    } finally {
    	      db.close();
    	    }
    	  
    	  
      }
    
}