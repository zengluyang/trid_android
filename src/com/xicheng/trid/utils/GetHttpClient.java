package com.xicheng.trid.utils;

import javax.net.ssl.SSLSocketFactory;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

/**
 * 单例模式，创建线程安全的HttpClient
 * @author zyl
 *
 */
public class GetHttpClient {
	private static volatile HttpClient mHttpClient=null;
	//设置超时的上限
	private static int MAXREQUEST_TIME=60*1000;
	private static int MAXCON_TIME=60*1000;
	
	public static synchronized  HttpClient  getInstance(){
		if(mHttpClient==null){
            BasicHttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params,
                    HTTP.DEFAULT_CONTENT_CHARSET);
            HttpProtocolParams.setUseExpectContinue(params, true);
            HttpConnectionParams.setConnectionTimeout(params, MAXREQUEST_TIME);
            //三次握手的请求时间最大设置
            HttpConnectionParams.setSoTimeout(params, MAXCON_TIME);
            //三次握手的返回时间最大设置

            SchemeRegistry schReg = new SchemeRegistry();
            //创建线程安全的httpclient，初始化参数设置
            schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
      
            ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(params, schReg);
            mHttpClient = new DefaultHttpClient(cm, params);
		}
		return mHttpClient;
	}
}
