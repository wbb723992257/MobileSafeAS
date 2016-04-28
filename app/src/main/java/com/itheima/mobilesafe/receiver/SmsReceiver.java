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
		System.out.println("���ض��Ź㲥������");
		// TODO Auto-generated method stub
		Object[] objects = (Object[]) intent.getExtras().get("pdus");
		for (Object object : objects) {
			SmsMessage smsMessage=SmsMessage.createFromPdu((byte[]) object);
			String address = smsMessage.getOriginatingAddress();
			String body = smsMessage.getMessageBody();
			System.out.println(address+body);
			if ("#*alarm*#".equals(body)) {
				abortBroadcast();
				//���ű�������
				MediaPlayer mediaPlayer=MediaPlayer.create(context, R.raw.ylzs);
				//ѭ������
				mediaPlayer.setLooping(true);
				//�����������
				mediaPlayer.setVolume(1f, 1f);
				mediaPlayer.start();
				//���ض���
							
			}else if ("#*location*#".equals(body)) {
				System.out.println("a");
				abortBroadcast();
				System.out.println("b");
				//������λ���񣬱�֤��λ�ں�̨�ȶ�����
				context.startService(new Intent(context, LocationService.class));
				SharedPreferences mpref=context.getSharedPreferences("config", Context.MODE_PRIVATE);
				String location = mpref.getString("location", "");
				//System.out.println("��λ��Ϣ��"+location);
				
				//����λ����Ϣ����ȫ����
				String safePhone=mpref.getString("phoneNumber", "");
				if (!TextUtils.isEmpty(safePhone)) {
					android.telephony.SmsManager smsManager = android.telephony.SmsManager
							.getDefault();
					smsManager.sendTextMessage(safePhone, null,
							location+"123", null, null);	
					System.out.println("���Ͷ�λ��Ϣ");
				}
				
				
				
			}
		}

	}

}
