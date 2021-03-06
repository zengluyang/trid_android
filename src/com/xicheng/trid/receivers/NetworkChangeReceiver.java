package com.xicheng.trid.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.xicheng.trid.applib.controller.HXSDKHelper;

/**
 * 
 * 广播监听器，监听网络状态
 * @author zyl
 *
 */
public class NetworkChangeReceiver extends BroadcastReceiver{
	@Override
    public void onReceive(Context context,Intent intent){
        ConnectivityManager connectivityManager=(ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isAvailable()){
            //Toast.makeText(context, "network is available", Toast.LENGTH_SHORT).show();
            HXSDKHelper.getInstance().setNetworkState(true);
        }
        else{
            Toast.makeText(context,"没有网络信号",Toast.LENGTH_SHORT).show();
            HXSDKHelper.getInstance().setNetworkState(true);
        }
    }

}
