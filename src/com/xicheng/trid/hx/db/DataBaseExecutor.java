package com.xicheng.trid.hx.db;

import com.xicheng.trid.applib.controller.HXSDKHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseExecutor extends SQLiteOpenHelper{
	private static final int DATABASE_VERSION = 12;
	private static final String TABLE_CONVERSATION_LIST="conversation_list";
	private static final String TABLE_CHAT="chat";
	private static int id=1;
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
	/**
	 * 插入数据到conlist表
	 * @param username
	 * @param groupname
	 * @param ext
	 * @param conversation_type
	 */
	public void insertConList(String username,String groupname,String ext,int conversation_type){
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("username",username);
		values.put("groupname", groupname);
		values.put("ext", ext);
		values.put("conversation_type", conversation_type);
		db.insert(TABLE_CONVERSATION_LIST, null, values);
	}
	/**
	 * 插入数据到chat表
	 * @param msgid
	 * @param msgtime
	 * @param participant
	 * @param msgbody
	 */
	public void insertMsgList(String msgid,long msgtime,String participant,String msgbody){
		
		SQLiteDatabase db=getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("_id", id++);
		values.put("msgid",msgid);
		values.put("msgtime", msgtime);
		values.put("msgdir",0 );
		values.put("isacked", 0);
		values.put("isdelivered", 1);
		values.put("status", 0);
		values.put("participant", participant);
		values.put("islistened", 1);
		values.put("msgbody", msgbody);
		values.put("msgtype", 0);
		db.insert(TABLE_CHAT, null, values);
		
	}
	
	/**
	 * 查询id=指定值的数据
	 */
	public Cursor getSelector(int id){
		Cursor cur;
		SQLiteDatabase db=this.getReadableDatabase();
		cur=db.query(this.TABLE_CHAT, new String[]{"_id","msgid","msgtime","msgdir","isacked","isdelivered",
				"status","participant","islistened","msgbody","msgtype","groupname"}, 
				"_id=?", new String[]{id+""}, null, null, null);
		return cur;
		
	}
	
	/**
	 * 得到特定的列值
	 */
	public String getColumnValue(int id,String columnName){
		Cursor cur=getSelector(id);
		String msgbody = null;
		while(cur.moveToNext()){
			 msgbody=cur.getString(cur.getColumnIndex(columnName));
		}
		cur.close();
		return msgbody;
	
	}
	
	
}
