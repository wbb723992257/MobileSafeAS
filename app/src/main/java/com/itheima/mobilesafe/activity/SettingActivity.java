package com.itheima.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.itheima.mobilesafe.service.AddressService;
import com.itheima.mobilesafe.service.CallSafeService;
import com.itheima.mobilesafe.utils.ServiceStatusUtils;
import com.itheima.mobilesafe.view.SettingClickView;
import com.itheima.mobilesafe.view.SettingItemView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingActivity extends Activity {

	private SettingItemView sivUpdate;
	private SharedPreferences mpref;
	private SettingItemView sivAddress;
	private SettingItemView sivCallSafe;
	private SettingClickView scvAddressStyle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		mpref = getSharedPreferences("config", MODE_PRIVATE);
		initUpdateView();
		initAddressView();
		initAddressStyle();
		initAddressLocation();
		initBlackView();

	}
	//--------------------------------------------------------------------------
	// 自动更新设置
	private void initUpdateView() {
		sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
		// sivUpdate.setTitle("自动更新设置");

		boolean autoUpdate = mpref.getBoolean("auto_update", true);
		if (autoUpdate) {
			// sivUpdate.setDecs("自动更新已开启");
			sivUpdate.setChecked(true);
		} else {
			// sivUpdate.setDecs("自动更新已关闭");
			sivUpdate.setChecked(false);
		}

		sivUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (sivUpdate.isChecked()) {
					sivUpdate.setChecked(false);
					// sivUpdate.setDecs("自动更新已关闭");
					mpref.edit().putBoolean("auto_update", false).commit();
				} else {
					sivUpdate.setChecked(true);
					// sivUpdate.setDecs("自动更新已开启");
					mpref.edit().putBoolean("auto_update", true).commit();
				}
			}
		});

	}
	//--------------------------------------------------------------------------
	private void initAddressView() {
	sivAddress = (SettingItemView) findViewById(R.id.siv_address);

		// 根据归属地服务是否运行来更新checkbox
	boolean serviceRunning = ServiceStatusUtils.isServiceRunning(SettingActivity.this, "com.example.mobilesafe.service.AddressService");
		if (serviceRunning) {
			sivAddress.setChecked(true);
		} else {
			sivAddress.setChecked(false);
		}

		sivAddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sivAddress.isChecked()) {
					sivAddress.setChecked(false);
					stopService(new Intent(SettingActivity.this,
							AddressService.class));// 停止归属地服务
				} else {
					sivAddress.setChecked(true);
					startService(new Intent(SettingActivity.this, AddressService.class));// 开启归属地服务
					System.out.println("SettingActivity_开启来电显示服务");
				}
			}
		});
		
		
	}
	/**
	 * 修改提示框显示风格
	 */
	final String[] items = new String[] { "卫士蓝", "金属灰", "苹果绿","活力橙" };
	private void initAddressStyle() {
		
	scvAddressStyle = (SettingClickView) findViewById(R.id.scv_address_style);

		scvAddressStyle.setTitle("归属地提示框风格");

		int style = mpref.getInt("address_style", 0);// 读取保存的style
		scvAddressStyle.setDesc(items[style]);

		scvAddressStyle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showSingleChooseDailog();
			}
		});
	}
	
	/**
	 * 弹出选择风格的单选框
	 */
	protected void showSingleChooseDailog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("归属地提示框风格");

		int style =mpref.getInt("address_style", 0);// 读取保存的style
		builder.setSingleChoiceItems(items, style, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				mpref.edit().putInt("address_style", which).commit();// 保存选择的风格
				dialog.dismiss();// 让dialog消失

				scvAddressStyle.setDesc(items[which]);// 更新组合控件的描述信息
			}
		});

		builder.setNegativeButton("取消", null);
		builder.show();
	}
	
	/**
	 * 修改归属地显示位置
	 */
	private void initAddressLocation() {
		SettingClickView scvAddressLocation =(SettingClickView) findViewById(R.id.scv_address_location);
		scvAddressLocation.setTitle("归属地提示框显示位置");
		scvAddressLocation.setDesc("设置归属地提示框的显示位置");

		scvAddressLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(SettingActivity.this,
						DragViewActivity.class));
			}
		});
	}

	//--------------------------------------------------------------------------
	private void initBlackView() {
		sivCallSafe = (SettingItemView) findViewById(R.id.siv_callsafe);

		// 根据黑名单服务是否运行来更新checkbox
		boolean serviceRunning = ServiceStatusUtils.isServiceRunning(SettingActivity.this, "com.example.mobilesafe.service.CallSafeService");
		if (serviceRunning) {
			sivCallSafe.setChecked(true);
		} else {
			sivCallSafe.setChecked(false);
		}

		sivCallSafe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sivCallSafe.isChecked()) {
					sivCallSafe.setChecked(false);
					stopService(new Intent(SettingActivity.this,
							CallSafeService.class));// 停止黑名单服务
					System.out.println(" 停止黑名单服务");
				} else {
					sivCallSafe.setChecked(true);
					startService(new Intent(SettingActivity.this, CallSafeService.class));// 开启黑名单服务
					System.out.println("开启黑名单服务");
				}
			}
		});


	}
}
