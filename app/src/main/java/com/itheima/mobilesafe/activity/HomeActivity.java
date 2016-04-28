package com.itheima.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilesafe.R;
import com.itheima.mobilesafe.utils.MD5Utils;

public class HomeActivity extends Activity {
	private GridView gvHome;
	private String[] mItem = new String[] { "手机防盗", "通讯卫士", "软件管理", "进程管理",
			"流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心" };
	private int[] mPics = new int[] { R.drawable.home_safe,
			R.drawable.home_callmsgsafe, R.drawable.home_apps,
			R.drawable.home_taskmanager, R.drawable.home_netmanager,
			R.drawable.home_trojan, R.drawable.home_sysoptimize,
			R.drawable.home_tools, R.drawable.home_settings };
	private Button btnOk;
	private Button btnCancle;
	private EditText etPassword;
	private EditText etPasswordConfig;
	private SharedPreferences mpref;

	// -----------------------------------------------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		mpref = getSharedPreferences("config", MODE_PRIVATE);
		gvHome = (GridView) findViewById(R.id.gv_home);
		gvHome.setAdapter(new HomeAdapter());
		// 设置中心界面的点击侦听
		gvHome.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					showPasswordDialog();
					break;
				case 7:
					startActivity(new Intent(HomeActivity.this,
							AToolsActivity.class));
					break;
				case 8:
					startActivity(new Intent(HomeActivity.this,
							SettingActivity.class));

					break;

				default:
					break;
				}

			}

		});
	}

	// -----------------------------------------------------------------------------------
	// 弹出密码设置对话框
	protected void showPasswordDialog() {
		// TODO Auto-generated method stub
		// 判断是否设置了密码
		String savedpassword = mpref.getString("password", null);
		if (!TextUtils.isEmpty(savedpassword)) {
			showPasswordInputDialog();
		} else {
			// 没有设置密码,显示设置密码对话框
			showPasswordSetDialog();
		}

	}
	//-----------------------------------------------------------------------------------
	// 输入密码与本地存储密码进行比对
	private void showPasswordInputDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
		final AlertDialog dialog = builder.create();

		View view = View.inflate(HomeActivity.this,
				R.layout.dialog_input_password, null);
		// dialog.setView(view);
		dialog.setView(view, 0, 0, 0, 0);
		// 千万要注意下面的控件都是view中的
		etPassword = (EditText) view.findViewById(R.id.et_password);
		btnOk = (Button) view.findViewById(R.id.btn_ok);
		btnCancle = (Button) view.findViewById(R.id.btn_cancle);

		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String password = etPassword.getText().toString();
				if (!TextUtils.isEmpty(password)) {
					String savedPassword = mpref.getString("password", null);
					if (MD5Utils.encode(password).equals(savedPassword)) {
						/*Toast.makeText(HomeActivity.this, "登录成功",
								Toast.LENGTH_SHORT).show();*/
						startActivity(new Intent(HomeActivity.this, LostFindActivity.class));
						dialog.dismiss();
					} else {
						Toast.makeText(HomeActivity.this, "请确认密码",
								Toast.LENGTH_SHORT).show();

					}
				} else {
					Toast.makeText(HomeActivity.this, "输入框不能为空",
							Toast.LENGTH_SHORT).show();

				}

			}
		});

		btnCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	//-----------------------------------------------------------------------------------
	// 显示设置密码对话框
	private void showPasswordSetDialog() {
		// TODO Auto-generated method stub

		AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
		final AlertDialog dialog = builder.create();

		View view = View.inflate(HomeActivity.this,
				R.layout.dialog_set_password, null);
		// dialog.setView(view);
		dialog.setView(view, 0, 0, 0, 0);
		// 千万要注意下面的控件都是view中的
		etPassword = (EditText) view.findViewById(R.id.et_password);
		etPasswordConfig = (EditText) view
				.findViewById(R.id.et_password_config);
		btnOk = (Button) view.findViewById(R.id.btn_ok);
		btnCancle = (Button) view.findViewById(R.id.btn_cancle);

		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String password = etPassword.getText().toString();
				String passworgconfig = etPasswordConfig.getText().toString();

				if (!TextUtils.isEmpty(password)
						&& !TextUtils.isEmpty(passworgconfig)) {

					if (password.equals(passworgconfig)) {

						/*Toast.makeText(HomeActivity.this, "登录成功",
								Toast.LENGTH_SHORT).show();*/
						mpref.edit()
								.putString("password",
										MD5Utils.encode(password)).commit();
						startActivity(new Intent(HomeActivity.this, LostFindActivity.class));
						dialog.dismiss();
						
					} else {
						Toast.makeText(HomeActivity.this, "请确认密码",
								Toast.LENGTH_SHORT).show();

					}
				} else {
					Toast.makeText(HomeActivity.this, "输入框不能为空",
							Toast.LENGTH_SHORT).show();

				}

			}
		});

		btnCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		dialog.show();

	}
	//-----------------------------------------------------------------------------------
	//适配器设置
	class HomeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mItem.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mItem[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(HomeActivity.this,
					R.layout.home_list_item, null);
			ImageView ivItem = (ImageView) view.findViewById(R.id.iv_item);
			TextView tvItem = (TextView) view.findViewById(R.id.tv_item);
			tvItem.setText(mItem[position]);
			ivItem.setImageResource(mPics[position]);
			// TODO Auto-generated method stub
			return view;
		}

	}
}
