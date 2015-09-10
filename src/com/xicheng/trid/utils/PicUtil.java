package com.xicheng.trid.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.xicheng.trid.value.ConnInfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

public class PicUtil {

	public static String TAG = "PicUtil";
	/**
	 * 由base64转化为bitmap
	 * @param base64Data
	 * @return
	 */
	public static Bitmap base64ToBitmap(String base64Data){
		byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
	    return BitmapFactory.decodeByteArray(bytes, 0, bytes.length); 
	}
	/**
	 * 保存为png格式
	 * @param bitmap
	 * @param parent
	 * @param picName
	 */
	public static void savaAsPng(Bitmap bitmap, File parent, String picName){
		if(!parent.exists()){
			parent.mkdir();
		}
		File pic = new File(parent, picName);
		  if (pic.exists()) {
		   pic.delete();
		  }
		FileOutputStream out;
		try {
			out = new FileOutputStream(pic);
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
			Log.i(TAG, "已经保存");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 处理二选一图片下载请求返回结果
	 * @param obj
	 */
	public static void handlePfPictureResult(JSONObject obj, Context context){
			JSONObject picObj;
			SharedPreferences spref = context.getSharedPreferences("prefer_data_" + ConnInfo.TEL, context.MODE_PRIVATE);
			Editor editor = spref.edit();
			int last = spref.getInt("last", 0);// 记录循环队列末尾下一个图片文件夹编号
			try {
				picObj = obj.getJSONObject("pf");
				JSONObject Jpic0 = picObj.getJSONObject("pic0");
				JSONObject Jpic1 = picObj.getJSONObject("pic1");
				
				int prefer_id = picObj.getInt("pf_id");
				File parent = new File(Environment.getExternalStorageDirectory() 
						+ "/Xicheng/TriD/" + ConnInfo.TEL + "/prefer/" + "/" + prefer_id + "/");
				if (!parent.exists()) {
					parent.mkdir();
				}
				handleJpic(Jpic0, parent, "0.png");
				handleJpic(Jpic1, parent, "1.png");
				editor.putInt("" + last, prefer_id);
				// 完成下载，last图片编号+1
				last = (last + 1) % 11;
				editor.putInt("last", last);
				editor.commit();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	/**
	 * 解析偏好返回的图片对象
	 * @param jpic
	 * @param parent
	 * @param name
	 */
	private static void handleJpic(JSONObject jpic, File parent, String name) {
		try {
			Bitmap pic = PicUtil.base64ToBitmap(jpic.getString("data"));
			savaAsPng(pic, parent, name);
			File data = new File(parent+"", "data"+name.charAt(0)+".txt");
			FileOutputStream out = new FileOutputStream(data);
			if (!data.exists()) {
				data.createNewFile();
			}
			//保存名字信息
			String name_cn = "name_cn"+"\n"+jpic.getString("name_cn")+"\n";
//			String name_cn = "name_cn"+" "+name+"\n"+jpic.getString("name_cn")+"\n";
			String name_en = "name_en"+"\n"+jpic.getString("name_en")+"\n";
//			String name_en = "name_en"+" "+name+"\n"+jpic.getString("name_en")+"\n";
			byte[] dataInBytes = name_cn.getBytes();
			out.write(dataInBytes);
			out.flush();
			dataInBytes = name_en.getBytes();
			out.write(dataInBytes);
			out.flush();
			out.close();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
