package com.xicheng.trid.hx.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.easemob.chat.EMMessage;
import com.xicheng.trid.DemoApplication;
import com.xicheng.trid.R;
import com.xicheng.trid.hx.domain.User;
import com.xicheng.trid.progressbar.HorizontalProgressBarWithNumber;
import com.xicheng.trid.progressbar.RoundProgressBarWidthNumber;

public class UserUtils {
    /**
     * 根据username获取相应user
     * @param username
     * @return
     */
	
	private static final long THREEDAY_IN_MILLISECONDS = 3*24*60 * 60 * 1000;

    public static User getUserInfor(String username){
        User user = DemoApplication.getInstance().getContactList().get(username);
        if(user == null){
            user = new User(username);
            user.setAvatar(new Date().getTime());
        }
       
        return user;
    }
    
    /**
     * 设置聊天消息中用户头像颜色
     * */
    public static void  setDefaultBarColor(int type){
    	
    	switch(type)
    	{
	    	case 0:
	    		HorizontalProgressBarWithNumber.setDefaultBarColor(DemoApplication.getInstance().getResources().getColor(R.color.mylove_reach),
	    				DemoApplication.getInstance().getResources().getColor(R.color.mylove_unreach));
	    		
	    	break;
	    	case 1:
	    		HorizontalProgressBarWithNumber.setDefaultBarColor( DemoApplication.getInstance().getResources().getColor(R.color.loveme_reach),
	    				DemoApplication.getInstance().getResources().getColor(R.color.loveme_unreach));
	    		
	    		break;
	    	case 2:
	    		HorizontalProgressBarWithNumber.setDefaultBarColor(DemoApplication.getInstance().getResources().getColor(R.color.mylove_reach),
	    				DemoApplication.getInstance().getResources().getColor(R.color.mylove_unreach));
	    	break;
    	}
    	
    }
    
    /**
     * 设置聊天消息中用户头像进度
     * @param username
     */
    public static void setUserAvatar(EMMessage message, RoundProgressBarWidthNumber progressBar,Long deadline){
    	//自己的头像不设置
    	if(message.direct == EMMessage.Direct.SEND)
    	{
    		return;
    	}
    
    	Long msgTime = message.getMsgTime();
    	Date date = new Date(msgTime);
    	SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	System.out.println(s.format(date));
    	
    	Date date1 = new Date(deadline);
    	SimpleDateFormat s1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	System.out.println(s1.format(date));
    	
        if(msgTime != null){
        	int temp =  (int)((deadline-msgTime)*100/THREEDAY_IN_MILLISECONDS);
        	progressBar.setProgress((int)((deadline-msgTime)*100/THREEDAY_IN_MILLISECONDS));
          
        }
    }
    
    /**
     * 设置历史栏的用户头像
     * 根据username获取该联系人的到期时间（avatar）
     * */
    public static String  getUserAvatar_ChatTitle(String username, RoundProgressBarWidthNumber progressBar){
    	User user = getUserInfor(username);
    	Long deadline  = user.getAvatar();
    	String  chat_title = user.getChatTitle();
    	//设置头像颜色
    	int type = user.getType();
    	switch(type)
    	{
	    	case 0:
	    		progressBar.setBarColor(DemoApplication.getInstance().getResources().getColor(R.color.mylove_reach),
	    				DemoApplication.getInstance().getResources().getColor(R.color.mylove_unreach));
	    		
	    	break;
	    	case 1:
	    		progressBar.setBarColor(DemoApplication.getInstance().getResources().getColor(R.color.loveme_reach),
	    				DemoApplication.getInstance().getResources().getColor(R.color.loveme_unreach));
	    		break;
	    	case 2:
	    		progressBar.setBarColor(DemoApplication.getInstance().getResources().getColor(R.color.stranger_reach),
	    				DemoApplication.getInstance().getResources().getColor(R.color.stranger_unreach));
	    		
	    	break;
    	}
    	Date now_date = new Date();
    	long nowTime = now_date.getTime();
    	if(deadline != null){
    		Log.i("userutil", (deadline-nowTime)+"");
    		
        	progressBar.setProgress((int)((deadline-nowTime)*100/THREEDAY_IN_MILLISECONDS));
        	
        }
    	return chat_title;
    	
    }
    

  
    
    public static List<User> createUserList(JSONArray jArray)
    {
    	List<User> userList = new ArrayList<User>();
    	for(int i = 0; i<jArray.length();i++ )
    	{
    		try {
				JSONObject obj = jArray.getJSONObject(i);
				User user = new User(
						             obj.getString("huanxin_id"),
						             obj.getString("peer_tel"),
						             obj.getInt("type"),
						             obj.getString("chat_title"),
						             (long)obj.getInt("expire"));
				userList.add(user);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	
    	return userList;
    }
    
}
