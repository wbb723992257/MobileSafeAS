package com.itheima.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Telephony.Sms;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.text.TextUtils;

//�����ֻ������Ĺ㲥������
public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("�����㲥������");
		SharedPreferences mpref = context.getSharedPreferences("config",
				context.MODE_PRIVATE);

		// ֻ���ڷ�������������ǰ���²Ž���SIM�����кŵ��жϡ�
		boolean protect = mpref.getBoolean("protect", false);
		if (protect) {
			String sim = mpref.getString("sim", null);
			if (!TextUtils.isEmpty(sim)) {
				TelephonyManager tm = (TelephonyManager) context
						.getSystemService(context.TELEPHONY_SERVICE);
				String simSerialNumber = tm.getSimSerialNumber();
				simSerialNumber = "15697814214522";
				System.out.println(simSerialNumber);
				if (sim.equals(simSerialNumber)) {
					System.out.println("�ֻ���ȫ");
				} else {
					System.out.println("���ͱ�������");
					String safePhone = mpref.getString("phoneNumber", "");
					android.telephony.SmsManager smsManager = android.telephony.SmsManager
							.getDefault();
					smsManager.sendTextMessage(safePhone, null,
							"SIM card changed", null, null);

				}

			}
		}

	}

}
