package com.xicheng.trid.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Environment;
/**
 * 图片下载工具
 * @author DengRenbin
 *
 */
public class PicDownloadUtil {
	
	public static void download(final String url, final File parent, final String picName){
		new Thread(new Runnable() {
			public void run() {
				try {
					//建立连接
					URL httpUrl;
					httpUrl = new URL(url);
					HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
					conn.setReadTimeout(10000);
					conn.setRequestMethod("GET");
					conn.setDoInput(true);
					
					InputStream in = conn.getInputStream();
					FileOutputStream out = null;
					File downloadFile = null;
					
					if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
						if(!parent.exists()){
							parent.mkdir();
						} 
						downloadFile = new File(parent,picName);
						out = new FileOutputStream(downloadFile);
					}
					byte[] b=new byte[2*1024];
					int len;
					if(out!=null){
						while((len = in.read(b))!=-1){
							out.write(b,0,len);
						}
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
