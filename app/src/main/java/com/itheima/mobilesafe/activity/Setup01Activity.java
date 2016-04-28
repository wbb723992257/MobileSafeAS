package com.itheima.mobilesafe.activity;

import com.example.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

//第1个设置向导页
public class Setup01Activity extends BaseSetupActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup01);
	}

	// 封装跳转上一页的步骤
	public void showPreviousPage() {

	}

	// 封装跳转下一页的步骤
	public void showNextPage() {

		startActivity(new Intent(Setup01Activity.this, Setup02Activity.class));
		finish();
		// 两个界面的切换动画
		overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
	}

}
