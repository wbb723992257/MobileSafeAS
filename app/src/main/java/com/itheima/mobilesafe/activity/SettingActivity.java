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
	// �Զ���������
	private void initUpdateView() {
		sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
		// sivUpdate.setTitle("�Զ���������");

		boolean autoUpdate = mpref.getBoolean("auto_update", true);
		if (autoUpdate) {
			// sivUpdate.setDecs("�Զ������ѿ���");
			sivUpdate.setChecked(true);
		} else {
			// sivUpdate.setDecs("�Զ������ѹر�");
			sivUpdate.setChecked(false);
		}

		sivUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (sivUpdate.isChecked()) {
					sivUpdate.setChecked(false);
					// sivUpdate.setDecs("�Զ������ѹر�");
					mpref.edit().putBoolean("auto_update", false).commit();
				} else {
					sivUpdate.setChecked(true);
					// sivUpdate.setDecs("�Զ������ѿ���");
					mpref.edit().putBoolean("auto_update", true).commit();
				}
			}
		});

	}
	//--------------------------------------------------------------------------
	private void initAddressView() {
	sivAddress = (SettingItemView) findViewById(R.id.siv_address);

		// ���ݹ����ط����Ƿ�����������checkbox
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
							AddressService.class));// ֹͣ�����ط���
				} else {
					sivAddress.setChecked(true);
					startService(new Intent(SettingActivity.this, AddressService.class));// ���������ط���
					System.out.println("SettingActivity_����������ʾ����");
				}
			}
		});
		
		
	}
	/**
	 * �޸���ʾ����ʾ���
	 */
	final String[] items = new String[] { "��ʿ��", "������", "ƻ����","������" };
	private void initAddressStyle() {
		
	scvAddressStyle = (SettingClickView) findViewById(R.id.scv_address_style);

		scvAddressStyle.setTitle("��������ʾ����");

		int style = mpref.getInt("address_style", 0);// ��ȡ�����style
		scvAddressStyle.setDesc(items[style]);

		scvAddressStyle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showSingleChooseDailog();
			}
		});
	}
	
	/**
	 * ����ѡ����ĵ�ѡ��
	 */
	protected void showSingleChooseDailog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("��������ʾ����");

		int style =mpref.getInt("address_style", 0);// ��ȡ�����style
		builder.setSingleChoiceItems(items, style, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				mpref.edit().putInt("address_style", which).commit();// ����ѡ��ķ��
				dialog.dismiss();// ��dialog��ʧ

				scvAddressStyle.setDesc(items[which]);// ������Ͽؼ���������Ϣ
			}
		});

		builder.setNegativeButton("ȡ��", null);
		builder.show();
	}
	
	/**
	 * �޸Ĺ�������ʾλ��
	 */
	private void initAddressLocation() {
		SettingClickView scvAddressLocation =(SettingClickView) findViewById(R.id.scv_address_location);
		scvAddressLocation.setTitle("��������ʾ����ʾλ��");
		scvAddressLocation.setDesc("���ù�������ʾ�����ʾλ��");

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

		// ���ݺ����������Ƿ�����������checkbox
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
							CallSafeService.class));// ֹͣ����������
					System.out.println(" ֹͣ����������");
				} else {
					sivCallSafe.setChecked(true);
					startService(new Intent(SettingActivity.this, CallSafeService.class));// ��������������
					System.out.println("��������������");
				}
			}
		});


	}
}
