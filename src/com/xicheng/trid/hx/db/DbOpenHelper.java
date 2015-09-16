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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xicheng.trid.applib.controller.HXSDKHelper;

//管理数据库的工具类，创建和更新
public class DbOpenHelper extends SQLiteOpenHelper{

	private static final int DATABASE_VERSION = 4;
	private static DbOpenHelper instance;

	//users table
	private static final String USERNAME_TABLE_CREATE = "CREATE TABLE "
			+ UserDao.TABLE_NAME + " ("
			+ UserDao.COLUMN_NAME_USERNAME + " TEXT PRIMARY KEY, "
			+ UserDao.COLUMN_NAME_CHAT_TITLE + " INT, "
			+ UserDao.COLUMN_NAME_AVATAR + " BIGINT, "
			+ UserDao.COLUMN_NAME_TYPE + " INT, "
			+ UserDao.COLUMN_NAME_TEL + " INT  );";
	
	
	private static final String ROBOT_TABLE_CREATE = "CREATE TABLE "
			+ UserDao.ROBOT_TABLE_NAME + " ("
			+ UserDao.ROBOT_COLUMN_NAME_ID + " TEXT PRIMARY KEY, "
			+ UserDao.ROBOT_COLUMN_NAME_NICK + " TEXT, "
			+ UserDao.ROBOT_COLUMN_NAME_AVATAR + " TEXT);";
	
	//prefer table		
	private static final String CREATE_PREF_TABLE = "CREATE TABLE "
            + UserDao.PREF_TABLE_NAME + " ("
            + UserDao.COLUMN_NAME_DISABLED_GROUPS + " TEXT, "
            + UserDao.COLUMN_NAME_DISABLED_IDS + " TEXT);";
	
	//notes table:	time ,for who, content 
		private static final String CREATE_NOTES_TABLE = "CREATE TABLE "
	            + UserDao.NOTES_TABLE_NAME + " ("
	            + UserDao.COLUMN_NAME_TIME + " TEXT, "
	            + UserDao.COLUMN_NAME_CONTENT + " TEXT);";
    //notes table:	time ,for who, content 
		private static final String CREATE_NOTELIST_TABLE = "CREATE TABLE "
	            + UserDao.NOTELIST_TABLE_NAME + " ("
	            + UserDao.COLUMN_NAME_TIME + " TEXT PRIMARY KEY, "
	            + UserDao.COLUMN_NAME_WHO + " TEXT);";

	// ALARM table		
	private static final String CREATE_ALARM_TABLE = "CREATE TABLE "
			+ UserDao.ALARM_TABLE_NAME + " ("
			+ UserDao.COLUMN_NAME_WRITER + " TEXT, "
			+ UserDao.COLUMN_NAME_FOR_WHO + " TEXT,"
			+ UserDao.COLUMN_NAME_CONTENT + " TEXT, "
			+ UserDao.COLUMN_NAME_TIME + " TEXT PRIMARY KEY," 
		    + UserDao.COLUMN_NAME_SWITCH + " INT);";
	
	// settings table
	private static final String CREATE_SETTINGS_TABLE = "CREATE TABLE "
			+ UserDao.SETTINGS_TABLE_NAME + " ("
			+ UserDao.SETTINGS_COLUMN_NAME_ID + " TEXT, "
			+ UserDao.SETTINGS_COLUMN_NAME_SETTINGNAME + " TEXT, "
			+ UserDao.SETTINGS_COLUMN_NAME_STATUS + " INT, "
			+ "constraint pk_settings primary key ("
			+ UserDao.SETTINGS_COLUMN_NAME_ID + ","
			+ UserDao.SETTINGS_COLUMN_NAME_SETTINGNAME + "));";
	
	private DbOpenHelper(Context context) {
		super(context, getUserDatabaseName(), null, DATABASE_VERSION);
	}
	
	public static DbOpenHelper getInstance(Context context) {
		if (instance == null) {
			instance = new DbOpenHelper(context.getApplicationContext());
		}
		return instance;
	}
	
	private static String getUserDatabaseName() {
        return  HXSDKHelper.getInstance().getHXId() + "_demo.db";
    }
	
	//第一次创建DB时调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(USERNAME_TABLE_CREATE);
		db.execSQL(CREATE_PREF_TABLE);
		db.execSQL(ROBOT_TABLE_CREATE);
		//db.execSQL(CREATE_NOTES_TABLE);
		db.execSQL(CREATE_ALARM_TABLE);
		db.execSQL(CREATE_SETTINGS_TABLE);//创建本地设置表
	}
	
	
    //版本更新时调用
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(oldVersion < 2){
		    db.execSQL("ALTER TABLE "+ UserDao.TABLE_NAME +" ADD COLUMN "+ 
		            UserDao.COLUMN_NAME_AVATAR + " TEXT ;");
		}
		
		if(oldVersion < 3){
		    db.execSQL(CREATE_PREF_TABLE);
        }
		if(oldVersion < 4){
			db.execSQL(ROBOT_TABLE_CREATE);
		}
	}
	
	public void closeDB() {
	    if (instance != null) {
	        try {
	            SQLiteDatabase db = instance.getWritableDatabase();
	            db.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        instance = null;
	    }
	}
	
}
