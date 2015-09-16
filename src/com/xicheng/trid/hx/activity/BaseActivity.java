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

package com.xicheng.trid.hx.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.umeng.analytics.MobclickAgent;
import com.xicheng.trid.DemoApplication;
import com.xicheng.trid.applib.controller.HXSDKHelper;
import com.xicheng.trid.applib.model.HXNotifier;
import com.xicheng.trid.hx.db.UserDao;
import com.xicheng.trid.hx.domain.User;
import com.xicheng.trid.hx.utils.UserUtils;
import com.xicheng.trid.main.Constant;
import com.xicheng.trid.receivers.NetworkChangeReceiver;
import com.xicheng.trid.utils.HttpUtil;
import com.xicheng.trid.value.ResponseTypeValue;

public abstract class BaseActivity extends FragmentActivity {
    protected NetworkChangeReceiver networkChangeReceiver;
    protected IntentFilter intentFilter;
	protected abstract void handleResult(JSONObject obj); 
    protected void handleError(){};
	Handler  handler = new Handler(){
		public void handleMessage(Message msg)
		{
			if(msg.what==ResponseTypeValue.INTENT_ERROR)
				handleError();
			else{
				try {
					JSONObject obj = new JSONObject(msg.obj.toString());
					handleResult(obj);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}	
		}
	};

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver=new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver,intentFilter);
       
    }

    @Override
    protected void onResume() {
        super.onResume();
        HttpUtil.setHandler(handler);
        // onresume时，取消notification显示
        HXSDKHelper.getInstance().getNotifier().reset();
        
        // umeng
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // umeng
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onPause()
    {
    	super.onPause();
    	HttpUtil.setHandler(null);
    }
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	unregisterReceiver(networkChangeReceiver);
    }
    /**
     * 返回
     * 
     * @param view
     */
    public void back(View view) {
        finish();
    }
}
