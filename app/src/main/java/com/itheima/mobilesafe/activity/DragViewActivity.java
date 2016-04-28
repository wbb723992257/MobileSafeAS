package com.itheima.mobilesafe.activity;

import com.example.mobilesafe.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 修改归属地显示位置
 *  
 */
public class DragViewActivity extends Activity {

	private TextView tvTop;
	private TextView tvBottom;

	private ImageView ivDrag;

	private int startX;
	private int startY;
	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drag_view);

		mPref = getSharedPreferences("config", MODE_PRIVATE);

		tvTop = (TextView) findViewById(R.id.tv_top);
		tvBottom = (TextView) findViewById(R.id.tv_bottom);
		ivDrag = (ImageView) findViewById(R.id.iv_drag);	
		int lastx = mPref.getInt("lastX", 0);
		int lasty = mPref.getInt("lastY", 0);
		
		final int width = getWindowManager().getDefaultDisplay().getWidth();
		final int height = getWindowManager().getDefaultDisplay().getHeight();
		if (lasty>height/2) {
			tvTop.setVisibility(View.VISIBLE);
			tvBottom.setVisibility(View.INVISIBLE);
		}else {
			tvTop.setVisibility(View.INVISIBLE);
			tvBottom.setVisibility(View.VISIBLE);
		}
		RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams) ivDrag.getLayoutParams();
		layoutParams.leftMargin=lastx;
		layoutParams.topMargin=lasty;
		ivDrag.setLayoutParams(layoutParams);
		

		
		//双击居中
		final long[] mHits = new long[2];  //点击三次产生事件
		ivDrag.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				System.arraycopy(mHits, 1, mHits, 0, mHits.length-1);
				            mHits[mHits.length-1] = SystemClock.uptimeMillis();
				            if (mHits[0] >= (SystemClock.uptimeMillis()-700)) {
				            	int l=width/2-ivDrag.getWidth()/2;
								int t=height/2-ivDrag.getHeight()/2;
								int r=width/2+ivDrag.getWidth()/2;
								int b=height/2+ivDrag.getHeight()/2;
				                ivDrag.layout(l, t, r, b);
				                
				                
				                Editor edit = mPref.edit();
								edit.putInt("lastX", l);
								edit.putInt("lastY", t);
								edit.commit();
				            }
	
			}
		});

		
		
		//设置触摸监听
		ivDrag.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int  endX=(int) event.getRawX();
					int  endY=(int) event.getRawY();
					
					int dx=endX-startX;
					int dy=endY-startY;
					int l=ivDrag.getLeft()+dx;
					int t=ivDrag.getTop()+dy;
					int r=ivDrag.getRight()+dx;
					int b=ivDrag.getBottom()+dy;
					
					if (l<0||t<0||r>width||b>height-20) {
						break;
					}
					if (t>height/2) {
						tvTop.setVisibility(View.VISIBLE);
						tvBottom.setVisibility(View.INVISIBLE);
					}else {
						tvTop.setVisibility(View.INVISIBLE);
						tvBottom.setVisibility(View.VISIBLE);
					}
					
					
					ivDrag.layout(l, t, r, b);
					startX=(int) event.getRawX();;
					startY= (int) event.getRawY();

					break;
				case MotionEvent.ACTION_UP:
					int lastX=ivDrag.getLeft();
					int lastY=ivDrag.getTop();
					Editor edit = mPref.edit();
					edit.putInt("lastX", lastX);
					edit.putInt("lastY", lastY);
					edit.commit();

					break;
					

				default:
					break;
				}
				
				return false;
			}
		});


	}
}