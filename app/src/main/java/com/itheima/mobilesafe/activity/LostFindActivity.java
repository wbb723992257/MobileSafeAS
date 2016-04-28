package com.itheima.mobilesafe.activity;

import com.example.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

//�ֻ�����
public class LostFindActivity extends Activity {

	private TextView tvSafePhone;
	private ImageView ivProtect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		SharedPreferences mPref = getSharedPreferences("config", MODE_PRIVATE);
		boolean configed = mPref.getBoolean("configed", false);
		// �ж��Ƿ�����
		if (configed) {
			setContentView(R.layout.activity_lostfind);

			tvSafePhone = (TextView) findViewById(R.id.tv_safePhone);
			String safePhone = mPref.getString("phoneNumber", "");
			tvSafePhone.setText(safePhone);

			ivProtect = (ImageView) findViewById(R.id.iv_protect);
			boolean protect = mPref.getBoolean("protect", false);
			if (protect) {
				ivProtect.setImageResource(R.drawable.lock);
			} else {
				ivProtect.setImageResource(R.drawable.unlock);
			}

		} else {
			// ��ת��������ҳ
			startActivity(new Intent(LostFindActivity.this,
					Setup01Activity.class));
			finish();

		}
	}

	public void reEnter(View view) {
		startActivity(new Intent(LostFindActivity.this, Setup01Activity.class));
		finish();
	}

}
