package com.xicheng.trid.utils;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * @author ZhengJinfu
 * @create time : 2015-8-21
 * @describe ��ͨ�����ʹ��post �������ϴ���ݵ���������
 */
public class HttpUtil {
	public static Handler currentHandler;

	public static void postRequest(final String url, final String json) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(url);

//					Handler currentHandler = HttpUtil.currentHandler;
					// httppost.addHeader("Authorization", "your token"); //
					// ��֤token

					httppost.addHeader("Content-Type", "application/json");
					Log.i("HttpUtil", "will post this data:" + json);

					httppost.setEntity(new StringEntity(json));
					HttpResponse response = httpclient.execute(httppost);
					
//					Log.i("HttpUtil", "post成功");
					// ����״̬�룬���ɹ��������
					int code = response.getStatusLine().getStatusCode();
					if (code == HttpStatus.SC_OK) {

						String result = EntityUtils.toString(response.getEntity());
						Log.i("HttpUtil","result: "+result);
						if (currentHandler != null) {
							sendHandlerMsg(result, currentHandler);
//							Log.i("HttpUtil", "sendHsndlerMsg成功");
						}

					} else {
						Log.i("HttpUtil", "post false");
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	// 向activity发服务器返回消息
	public static void sendHandlerMsg(String result, Handler h) {
		Message msg = h.obtainMessage();
		msg.obj = result;
		h.sendMessage(msg);
	}

	public static void setHandler(Handler handler) {
		currentHandler = handler;
	}

}
