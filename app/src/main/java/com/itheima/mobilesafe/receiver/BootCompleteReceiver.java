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

//监听手机开机的广播接收器
public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("重启广播接收器");
		SharedPreferences mpref = context.getSharedPreferences("config",
				context.MODE_PRIVATE);

		// 只有在防盗保护开启的前提下才进行SIM卡序列号的判断。
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
					System.out.println("手机安全");
				} else {
					System.out.println("发送报警短信");
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
