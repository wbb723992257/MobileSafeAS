package com.itheima.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
//��������ҳ��Ļ��࣬����Ҫ���嵥�ļ���ע�ᣬ��Ϊ����Ҫ��ʾ����
public abstract class BaseSetupActivity extends Activity {
	
	private GestureDetector mDetector;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	
	//����ʶ����
	mDetector = new GestureDetector(this,
			new GestureDetector.SimpleOnGestureListener() {

				@Override
				public boolean onFling(MotionEvent e1, MotionEvent e2,
						float velocityX, float velocityY) {
					// TODO Auto-generated method stub
					// �����ֻ������¼�
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
//��װ��ת��һҳ�Ĳ���,���󷽷�����ʵ�֣��г��󷽷����ǵ���Ҳ�����ǳ����
public abstract void showPreviousPage();
//��װ��ת��һҳ�Ĳ���
public abstract void showNextPage();



//�������
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
