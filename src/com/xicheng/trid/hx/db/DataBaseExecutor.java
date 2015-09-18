package com.xicheng.trid.hx.db;

import com.xicheng.trid.applib.controller.HXSDKHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseExecutor extends SQLiteOpenHelper{
	private static final int DATABASE_VERSION = 12;
	private static final String TABLE_CONVERSATION_LIST="conversation_list";
	public DataBaseExecutor(Context context) {
		super(context, getUserDatabaseName(), null, DATABASE_VERSION);
	}
	private static String getUserDatabaseName(){
		 return  HXSDKHelper.getInstance().getHXId() + "_emmsg.db";
	}
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
	public void insertConList(String username,String groupname,String ext,int conversation_type){
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("username",username);
		values.put("groupname", groupname);
		values.put("ext", ext);
		values.put("conversation_type", conversation_type);
		db.insert(TABLE_CONVERSATION_LIST, null, values);
	}
	
	
	
}
