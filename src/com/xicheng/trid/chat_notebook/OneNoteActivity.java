package com.xicheng.trid.chat_notebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class OneNoteActivity extends Activity {
	protected void  onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Intent i = getIntent();
		Bundle b = i.getBundleExtra("bd");
		String name = b.getString("chatTo");
		if(name.equals(""))
		{
			//新建笔记
			
		}
		else{
			//查询历史
		}
		
	}

}
