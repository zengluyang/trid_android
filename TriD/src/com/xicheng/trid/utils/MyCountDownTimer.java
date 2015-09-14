package com.xicheng.trid.utils;

import com.xicheng.trid.R;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.widget.TextView;

public class MyCountDownTimer extends CountDownTimer{
	private TextView textView;
	public MyCountDownTimer(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
	}
	public MyCountDownTimer(long millisInFuture, long countDownInterval, TextView textview){
		this(millisInFuture, countDownInterval);
		this.textView = textview;
	}
	
	public void onFinish() {// 计时完毕时触发
		textView.setText("获取");
		textView.setTextColor(textView.getResources().getColor(R.color.black));
		textView.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
		textView.setClickable(true);
	}

	public void onTick(long millisUntilFinished) {// 计时过程显示
		textView.setClickable(false);
		textView.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
		textView.setTextColor(textView.getResources().getColor(R.color.gray));
		textView.setText(millisUntilFinished / 1000 + "s");
	}
}
