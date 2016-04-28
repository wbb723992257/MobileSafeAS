package com.itheima.mobilesafe.activity;

import com.example.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
//设置引导页活动的基类，不需要在清单文件中注册，因为不需要显示出来
public abstract class BaseSetupActivity extends Activity {
	
	private GestureDetector mDetector;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	
	//手势识别器
	mDetector = new GestureDetector(this,
			new GestureDetector.SimpleOnGestureListener() {

				@Override
				public boolean onFling(MotionEvent e1, MotionEvent e2,
						float velocityX, float velocityY) {
					// TODO Auto-generated method stub
					// 监听手机滑动事件
					if (e2.getRawX() - e1.getRawX() > 100) {
						showPreviousPage();
					}
					if (e1.getRawX() - e2.getRawX() > 100) {
						showNextPage();

					}

					return super.onFling(e1, e2, velocityX, velocityY);
				}

			});

	
}
//封装跳转上一页的步骤,抽象方法必须实现，有抽象方法我们的类也必须是抽象的
public abstract void showPreviousPage();
//封装跳转下一页的步骤
public abstract void showNextPage();



//点击监听
	public void next(View view) {
		showNextPage();
	}

	public void previous(View view) {
		showPreviousPage();
	}


@Override
public boolean onTouchEvent(MotionEvent event) {
	// TODO Auto-generated method stub

	mDetector.onTouchEvent(event);
	return super.onTouchEvent(event);
}
}
