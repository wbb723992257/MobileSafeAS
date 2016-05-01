package com.itheima.mobilesafe.activity;

import com.example.mobilesafe.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

//��2��������ҳ
public class Setup04Activity extends BaseSetupActivity {
CheckBox cbProtect;
private SharedPreferences mpref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup04);
		cbProtect=(CheckBox) findViewById(R.id.cb_protect);
		
		mpref = getSharedPreferences("config", MODE_PRIVATE);
		boolean blprotect = mpref.getBoolean("protect", false);
		if (blprotect) {
			cbProtect.setText("���������ѿ���");
			cbProtect.setChecked(true);
		} else {
			cbProtect.setText("��������δ����");
			cbProtect.setChecked(false);
		}
		
		
		cbProtect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					cbProtect.setText("���������ѿ���");
					mpref.edit().putBoolean("protect", true).commit();
				} else {
					cbProtect.setText("��������δ����");
					mpref.edit().putBoolean("protect", false).commit();

				}
			}
		});
	}

	// ��װ��ת��һҳ�Ĳ���
	public void showPreviousPage() {
		startActivity(new Intent(Setup04Activity.this, Setup03Activity.class));
		finish();
		overridePendingTransition(R.anim.trans_previousin,
				R.anim.trans_previousout);

	}

	// ��װ��ת��һҳ�Ĳ���
	public void showNextPage() {
		startActivity(new Intent(Setup04Activity.this, LostFindActivity.class));
		SharedPreferences mpref = getSharedPreferences("config", MODE_PRIVATE);
		mpref.edit().putBoolean("configed", true).commit();
		finish();
		overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
	}

}
