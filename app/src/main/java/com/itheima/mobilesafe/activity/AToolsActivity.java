package com.itheima.mobilesafe.activity;

import com.example.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AToolsActivity extends Activity {
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_atools);
	
	
}

public void queryAddress(View view){
	startActivity(new Intent(AToolsActivity.this, AddressActivity.class));
	System.out.println("跳转至归属地查询");
}

}
