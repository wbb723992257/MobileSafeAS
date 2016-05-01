package com.itheima.mobilesafe.service;

import com.example.mobilesafe.R;
import com.itheima.mobilesafe.db.dao.AddressDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * �������ѷ���
 */
public class AddressService extends Service {

	private TelephonyManager tm;
	private MyListener listener;
	private OutCallReceiver receiver;
	private WindowManager mWM;
	private View view;
	private SharedPreferences mPref;
	private WindowManager.LayoutParams params;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("AddressService_�������ѷ���");

		mPref = getSharedPreferences("config", MODE_PRIVATE);

		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);// ��������״̬

		// ע��ȥ��㲥������
		receiver = new OutCallReceiver();

		IntentFilter filter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(receiver, filter);
	}

	class MyListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:// �绰����
				System.out.println("�绰����");
				String address = AddressDao.getAddress(incomingNumber);// ��ѯ������
				/*
				 * Toast.makeText(AddressService.this, address,
				 * Toast.LENGTH_LONG) .show();
				 */
				showToast(address);
				break;

			case TelephonyManager.CALL_STATE_IDLE:
				if (mWM != null && view != null) {
					mWM.removeView(view);
					view = null;
				}
				break;
			default:
				break;
			}

			super.onCallStateChanged(state, incomingNumber);
		}

	}

	/**
	 * ����ȥ��Ĺ㲥������ ��ҪȨ��: android.permission.PROCESS_OUTGOING_CALLS
	 * 
	 * 
	 */
	class OutCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String number = getResultData();// ��ȡȥ��ĵ绰����

			String address = AddressDao.getAddress(number);
			// Toast.makeText(context, address, Toast.LENGTH_LONG).show();
			showToast(address);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);// ֹͣ�������

		unregisterReceiver(receiver);// ע���㲥
	}

	/**
	 * ������ʾ
	 */
	private void showToast(String text) {
		mWM = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		final int windowWidth = mWM.getDefaultDisplay().getWidth();
		final int windowheight = mWM.getDefaultDisplay().getHeight();
		

		params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_PHONE;
		params.setTitle("Toast");
		params.gravity = Gravity.LEFT + Gravity.TOP;

		int lastX = mPref.getInt("lastX", 0);
		int lastY = mPref.getInt("lastY", 0);

		params.x = lastX;
		params.y = lastY;

		// view = new TextView(this);
		view = View.inflate(this, R.layout.toast_addressview, null);

		int[] bgs = new int[] { R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green,
				R.drawable.call_locate_orange };
		int style = mPref.getInt("address_style", 0);

		view.setBackgroundResource(bgs[style]);// ���ݴ洢����ʽ���±���

		TextView tvText = (TextView) view.findViewById(R.id.tv_number);
		tvText.setText(text);
		mWM.addView(view, params);// ��view�������Ļ��(Window)

		view.setOnTouchListener(new OnTouchListener() {
			
			private int startX;
			private int startY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX =(int) event.getRawX();
					startY =(int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int  endX=(int) event.getRawX();
					int  endY=(int) event.getRawY();
					
					int dx=endX-startX;
					int dy=endY-startY;
					params.x+=dx;
					params.y+=dy;
					
					
					if (params.x < 0) {
						params.x = 0;
					}

					if (params.y < 0) {
						params.y = 0;
					}

					// ��ֹ����ƫ����Ļ
					if (params.x > windowWidth - view.getWidth()) {
						params.x = windowWidth - view.getWidth();
					}

					if (params.y > windowheight - view.getHeight()) {
						params.y = windowheight - view.getHeight();
					}

					
					mWM.updateViewLayout(view, params);
					startX =(int) event.getRawX();
					startY =(int) event.getRawY();

					break;
				case MotionEvent.ACTION_UP:
					int lastX=params.x;
					int lastY=params.y;
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
