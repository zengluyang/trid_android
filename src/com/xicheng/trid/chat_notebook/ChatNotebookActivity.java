package com.xicheng.trid.chat_notebook;


import com.xicheng.trid.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ChatNotebookActivity extends Activity {
	
	String chatTo;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_notebook);
		TextView title = (TextView) findViewById(R.id.title);
		Intent i = getIntent();
		Bundle bd = i.getBundleExtra("bd");
		chatTo = bd.getString("chatTo");
		title.setText(chatTo);
		ImageButton rightBut = (ImageButton) findViewById(R.id.right_button);
		rightBut.setImageResource(R.drawable.add);
		rightBut.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Bundle bd = new Bundle();
				bd.putString("name", "");
				Intent i = new Intent(ChatNotebookActivity.this, OneNoteActivity.class);
			    startActivity(i);
				
			}
			
		});
		ListView bookList = (ListView) findViewById(R.id.chat_notelist);
		bookList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Bundle bd = new Bundle();
				bd.putString("chatTo", chatTo);
				Intent i = new Intent(ChatNotebookActivity.this, OneNoteActivity.class);
			    i.putExtra("bd", bd);
				startActivity(i);
	 		}
			
		});
		
		initNotebook();
	}
	
	private void initNotebook(){
		//查询该用户是否有笔记
		
		//有则加入list
	}
}
