package com.itheima.mobilesafe.activity;

import com.example.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

//��1��������ҳ
public class Setup01Activity extends BaseSetupActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup01);
	}

	// ��װ��ת��һҳ�Ĳ���
	public void showPreviousPage() {

	}

	// ��װ��ת��һҳ�Ĳ���
	public void showNextPage() {

		startActivity(new Intent(Setup01Activity.this, Setup02Activity.class));
		finish();
		// ����������л�����
		overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
	}

}
