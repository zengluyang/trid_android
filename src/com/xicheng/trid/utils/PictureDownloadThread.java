package com.xicheng.trid.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.widget.ImageView;

public class PictureDownloadThread extends Thread{

	private String url;
	private Handler handler;
	private ImageView imageView;
	private String picName;
	
	public PictureDownloadThread(String url, ImageView imageView, Handler handler, String picName) {
		this.url = url;
		this.imageView = imageView;
		this.handler = handler;
		this.picName = picName;
	}
	
	public void run() {
		runimage();
	}
	
	private void runimage() {
		try {
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
				File parent = new File(Environment.getExternalStorageDirectory()+"/Xicheng/");
				if(!parent.exists()){
					parent.mkdir();
				} 
				parent = new File(parent+"/TriD/");
				if(!parent.exists()){
					parent.mkdir();
				} 
				parent = new File(parent+"/pic/");
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
			final Bitmap bitmap = BitmapFactory.decodeFile(downloadFile.getAbsolutePath());
			handler.post(new Runnable() {
				public void run() {
					imageView.setImageBitmap(bitmap);
				}
			});
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
