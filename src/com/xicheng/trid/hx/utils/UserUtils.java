package com.xicheng.trid.hx.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.easemob.chat.EMMessage;
import com.xicheng.trid.DemoApplication;
import com.xicheng.trid.hx.domain.User;
import com.xicheng.trid.progressbar.HorizontalProgressBarWithNumber;
import com.xicheng.trid.progressbar.RoundProgressBarWidthNumber;

public class UserUtils {
    /**
     * 根据username获取相应user
     * @param username
     * @return
     */
	
	private static final long THREEDAY_IN_MILLISECONDS = 3 * 24 * 60 * 60 * 1000;

    public static User getUserInfor(String username){
        User user = DemoApplication.getInstance().getContactList().get(username);
        if(user == null){
            user = new User(username);
            user.setAvatar(new Date().getTime());
        }
       
        return user;
    }
    
    /**
     * 设置消息中用户头像
     * @param username
     */
    
    public static void setUserAvatar(EMMessage message, RoundProgressBarWidthNumber progressBar,Long startTime){
    	//自己的头像不设置
    	if(message.direct == EMMessage.Direct.SEND)
    	{
    		return;
    	}
    
    	Long msgTime = message.getMsgTime();
        if(msgTime != null){
        	progressBar.setProgress((int)(100-(msgTime -startTime)*100/THREEDAY_IN_MILLISECONDS));
          
        }
    }
    
    /**
     * 设置对话栏的用户头像
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
	    		progressBar.setBarColor( 0xffa6e1fd , 0xff78ccf4);
	    	break;
	    	case 1:
	    		progressBar.setBarColor(0xffffffff, 0xffffffff);
	    		break;
	    	case 2:
	    		progressBar.setBarColor(0xffffffff, 0xffffffff);
	    		
	    	break;
    	}
    	Date now_date = new Date();
    	long nowTime = now_date.getTime();
    	if(deadline != null){
        	progressBar.setProgress((int)((deadline-nowTime)*100/THREEDAY_IN_MILLISECONDS));
        	
        }
    	return chat_title;
    	
    }
    
    public static void  setDefaultBarColor(int type){
    	
    	switch(type)
    	{
	    	case 0:
	    		HorizontalProgressBarWithNumber.setDefaultBarColor( 0xffa6e1fd , 0xff78ccf4);
	    	break;
	    	case 1:
	    		HorizontalProgressBarWithNumber.setDefaultBarColor(0xffffffff, 0xffffffff);
	    		break;
	    	case 2:
	    		HorizontalProgressBarWithNumber.setDefaultBarColor(0xffffffff, 0xffffffff);
	    		
	    	break;
    	}
    	
    	
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
