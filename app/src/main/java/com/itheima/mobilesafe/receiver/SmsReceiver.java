package com.itheima.mobilesafe.receiver;

import com.example.mobilesafe.R;
import com.itheima.mobilesafe.service.LocationService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("拦截短信广播接收器");
		// TODO Auto-generated method stub
		Object[] objects = (Object[]) intent.getExtras().get("pdus");
		for (Object object : objects) {
			SmsMessage smsMessage=SmsMessage.createFromPdu((byte[]) object);
			String address = smsMessage.getOriginatingAddress();
			String body = smsMessage.getMessageBody();
			System.out.println(address+body);
			if ("#*alarm*#".equals(body)) {
				abortBroadcast();
				//播放报警音乐
				MediaPlayer mediaPlayer=MediaPlayer.create(context, R.raw.ylzs);
				//循环播放
				mediaPlayer.setLooping(true);
				//音量设置最大
				mediaPlayer.setVolume(1f, 1f);
				mediaPlayer.start();
				//拦截短信
							
			}else if ("#*location*#".equals(body)) {
				System.out.println("a");
				abortBroadcast();
				System.out.println("b");
				//开启定位服务，保证定位在后台稳定进行
				context.startService(new Intent(context, LocationService.class));
				SharedPreferences mpref=context.getSharedPreferences("config", Context.MODE_PRIVATE);
				String location = mpref.getString("location", "");
				//System.out.println("定位信息："+location);
				
				//发送位置信息给安全号码
				String safePhone=mpref.getString("phoneNumber", "");
				if (!TextUtils.isEmpty(safePhone)) {
					android.telephony.SmsManager smsManager = android.telephony.SmsManager
							.getDefault();
					smsManager.sendTextMessage(safePhone, null,
							location+"123", null, null);	
					System.out.println("发送定位信息");
				}
				
				
				
			}
		}

	}

}
