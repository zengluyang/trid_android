package com.xicheng.trid.chat_alarm;

public class ChatAlarm {
	
	String time_for_me = null;
	String time_for_he = null;
	String content_for_me = null;
	String content_for_he = null;
	public int isTurnOnForMe = 1;
	public int isTurnOnForHe = 1;

	public void setTimeForMe(String time)
	{
		this.time_for_me = time;
	}
	
	public void setTimeForHe(String time)
	{
		this.time_for_he = time;
	}
	
	public void setContentForMe(String content)
	{
		this.content_for_me = content;
	}
	
	public void setContentForHe(String content)
	{
		this.content_for_he = content;
	}
	
	public String getTimeForMe()
	{
		return time_for_me;
	}
	
	public String getTimeForHe()
	{
		return time_for_he ;
	}
	
	public String getContentForMe()
	{
		return content_for_me;
	}
	
	public String getContentForHe()
	{
		return content_for_he;
	}
	
	public void switchAlarmForHe(int set)
	{
	    this.isTurnOnForHe = set;
	}
	
	public void switchAlarmForMe(int set)
	{
	    this.isTurnOnForMe = set;
	}
}
