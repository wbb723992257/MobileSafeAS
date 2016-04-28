package com.itheima.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.itheima.mobilesafe.view.SettingItemView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

//第2个设置向导页
public class Setup02Activity extends BaseSetupActivity {



	private SettingItemView sivSim;
	private SharedPreferences mpref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup02);
		sivSim = (SettingItemView) findViewById(R.id.siv_sim);
		
		mpref = getSharedPreferences("config", MODE_PRIVATE);
		String sim= mpref.getString("sim", null);
		if (!TextUtils.isEmpty(sim)) {
			sivSim.setChecked(true);
		} else {
			sivSim.setChecked(false);
			

		}
		
		sivSim.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				if (sivSim.isChecked()) {
					sivSim.setChecked(false);
				} else {
					sivSim.setChecked(true);
					TelephonyManager  tm=(TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					String simSerialNumber = tm.getSimSerialNumber();
					//System.out.println("simSerialNumber:   "+simSerialNumber);
					mpref.edit().putString("sim", simSerialNumber).commit();

				}
				
				
				
			}
		});

	}

	// 封装跳转上一页的步骤
	public void showPreviousPage() {

		startActivity(new Intent(this, Setup01Activity.class));
		finish();
		
		//平缓过度的动画
		overridePendingTransition(R.anim.trans_previousin,
				R.anim.trans_previousout);

	}

	// 封装跳转下一页的步骤
	public void showNextPage() {
		String sim= mpref.getString("sim", null);
		if (!TextUtils.isEmpty(sim)) {

			startActivity(new Intent(this, Setup03Activity.class));
			finish();
			overridePendingTransition(R.anim.trans_in, R.anim.trans_out);

		}else {
			Toast.makeText(Setup02Activity.this, "请绑定SIM卡", Toast.LENGTH_SHORT).show();
		}

	}	
	

}
