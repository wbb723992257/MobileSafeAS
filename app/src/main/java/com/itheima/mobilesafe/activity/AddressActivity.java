package com.itheima.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.itheima.mobilesafe.db.dao.AddressDao;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AddressActivity extends Activity {
	private EditText etNumber;
	private TextView tvResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		etNumber = (EditText) findViewById(R.id.et_number);
		tvResult = (TextView) findViewById(R.id.tv_result);

		//监听edittext文本框的变化
		etNumber.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				String address = AddressDao.getAddress(s.toString());
				//System.out.println("已传递");
				tvResult.setText(address);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	public void queryAdress(View view) {
		String number = etNumber.getText().toString().trim();
		if (!TextUtils.isEmpty(number)) {
			//System.out.println("丢个号码给你查查");
			String address = AddressDao.getAddress(number);
			//System.out.println("已传递");
			tvResult.setText(address);
		}

	}

}
