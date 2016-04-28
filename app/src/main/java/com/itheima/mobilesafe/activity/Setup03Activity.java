package com.itheima.mobilesafe.activity;

import com.example.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
//��2��������ҳ
public class Setup03Activity extends BaseSetupActivity {
	
private EditText etPhoneNumber;
private SharedPreferences mPref;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_setup03);
	etPhoneNumber = (EditText) findViewById(R.id.et_phoneNumber);
	mPref = getSharedPreferences("config", MODE_PRIVATE);
	String number=mPref.getString("phoneNumber", "");
	etPhoneNumber.setText(number);
}

	// ��װ��ת��һҳ�Ĳ���
	public void showPreviousPage() {
		startActivity(new Intent(Setup03Activity.this, Setup02Activity.class));
		finish();
		overridePendingTransition(R.anim.trans_previousin,
				R.anim.trans_previousout);

	}

	// ��װ��ת��һҳ�Ĳ���
	public void showNextPage() {
		String number=etPhoneNumber.getText().toString().trim();
		
		if (!TextUtils.isEmpty(number)) {
			mPref.edit().putString("phoneNumber", number).commit();
			startActivity(new Intent(Setup03Activity.this, Setup04Activity.class));
			finish();
			overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
		}else {
			Toast.makeText(Setup03Activity.this, "һ��Ҫ���ð�ȫ����Ŷ", Toast.LENGTH_SHORT).show();
		}
		

	}
	//ѡ����ϵ��
	public void selectContact(View view){
		startActivityForResult(new Intent(Setup03Activity.this, ContactActivity.class), 0);
		
		
	}
	@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			// TODO Auto-generated method stub
			super.onActivityResult(requestCode, resultCode, data);
			if (resultCode==Activity.RESULT_OK) {
				String phoneNumber=data.getStringExtra("phoneNumber");
				phoneNumber.replace("-", "").replace(" ", "");
				etPhoneNumber.setText(phoneNumber);
				
			}
			
			
			
		}
}
