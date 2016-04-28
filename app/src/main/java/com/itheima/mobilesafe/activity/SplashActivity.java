package com.itheima.mobilesafe.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilesafe.R;
import com.itheima.mobilesafe.utils.StreamUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class SplashActivity extends Activity {
	protected static final int CODE_UPDATE_DIALOG = 0;
	protected static final int CODE_URL_ERROR = 1;
	protected static final int CODE_NET_ERROR = 2;
	protected static final int CODE_JSON_ERROR = 3;
	protected static final int CODE_ENTER_HOME = 4;
	private TextView tvVersion;
	private TextView tvProgress;
	private String mVersionName;
	private int mVersionCode;
	private String mDescription;
	private String mDownloadUrl;
	private SharedPreferences mpref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		tvVersion = (TextView) findViewById(R.id.tv_version);
		tvVersion.setText("�汾�ţ�" + getVersionName());
		// ���ý��潥�䶯��
		RelativeLayout rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
		AlphaAnimation anim = new AlphaAnimation(0.5f, 1f);
		anim.setDuration(2000);
		rlRoot.startAnimation(anim);
		// ��SharedPreferences��ȡ���ݲ鿴�û��Ƿ������Զ�����
		mpref = getSharedPreferences("config", MODE_PRIVATE);
		// �������ݿ�
		copyDB("address.db");

		boolean autoUpdate = mpref.getBoolean("auto_update", true);
		System.out.println("auto_update" + autoUpdate);
		if (autoUpdate) {
			checkVersion();
		} else {
			mHandler.sendEmptyMessageDelayed(CODE_ENTER_HOME, 2000);// ��ʾ���뷢����Ϣ

		}

		tvProgress = (TextView) findViewById(R.id.tv_progress);
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case CODE_UPDATE_DIALOG:
				showUpdateDialog();

				break;
			case CODE_URL_ERROR:
				Toast.makeText(SplashActivity.this, "Url��ַ����",
						Toast.LENGTH_LONG).show();
				enterHome();
				break;
			case CODE_NET_ERROR:
				Toast.makeText(SplashActivity.this, "�������", Toast.LENGTH_LONG)
						.show();
				enterHome();
				break;
			case CODE_JSON_ERROR:
				Toast.makeText(SplashActivity.this, "���ݽ���", Toast.LENGTH_LONG)
						.show();
				enterHome();
				break;
			case CODE_ENTER_HOME:
				enterHome();
				break;

			default:
				break;
			}

		}

	};

	// ------------------------------------------------------------------------------------
	// ��ȡϵͳ�汾��
	private String getVersionName() {
		String versionName = null;
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			versionName = packageInfo.versionName;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return versionName;
	}

	// ------------------------------------------------------------------------------------
	// ��ȡϵͳ�汾�ţ����������ݿ�汾�ŶԱȣ��ж��Ƿ�����
	private int getVersionCode() {
		int versionCode = 0;
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			versionCode = packageInfo.versionCode;
			return versionCode;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return -1;
	}

	// ------------------------------------------------------------------------------------
	// �ӷ���������JSON���ݼ��汾��
	private void checkVersion() {
		final long startTime = System.currentTimeMillis();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message message = Message.obtain();
				HttpURLConnection conn = null;
				try {
					URL url = new URL("http://10.0.2.2/update01.json");
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(5000);
					if (conn.getResponseCode() == 200) {
						InputStream inputStream = conn.getInputStream();
						String result = StreamUtils.readFromStream(inputStream);// �������������ȡ�ı�
						// System.out.println(result + "");
						// ����JSON�ı�
						JSONObject jsonObject = new JSONObject(result);

						mVersionName = jsonObject.getString("versionName");
						mVersionCode = jsonObject.getInt("versionCode");
						mDescription = jsonObject.getString("description");
						mDownloadUrl = jsonObject.getString("downloadUrl");
						// System.out.println("" + mVersionName);
						// �ж����ݿ����ϵͳ�汾�Ƿ���ڱ�ϵͳ�汾
						if (mVersionCode > getVersionCode()) {
							message.what = CODE_UPDATE_DIALOG;

						} else {
							message.what = CODE_ENTER_HOME;
						}

					}

				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					message.what = CODE_URL_ERROR;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					message.what = CODE_NET_ERROR;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					message.what = CODE_JSON_ERROR;
				} finally {
					// ǿ�����������ӣ�չʾsplash����ҳ��
					long stopTime = System.currentTimeMillis();
					long timeUsed = stopTime - startTime;
					if (timeUsed < 2000) {
						try {
							Thread.sleep(2000 - timeUsed);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					mHandler.sendMessage(message);
					if (conn != null) {
						conn.disconnect();
					}
				}

			}
		}).start();
	}

	// ------------------------------------------------------------------------------------
	// ��ʾ�Ƿ���°汾����ʾ��dialog
	protected void showUpdateDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SplashActivity.this);
		builder.setTitle("���°汾" + mVersionName);
		builder.setMessage(mDescription);
		builder.setPositiveButton("��������", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				System.out.println("��������");
				download();
			}
		});
		builder.setNegativeButton("�ݲ�����", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				enterHome();
			}
		});
		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				enterHome();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	// ------------------------------------------------------------------------------------
	// ����apk�ļ�
	protected void download() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// TODO Auto-generated method stub
			tvProgress.setVisibility(View.VISIBLE);
			String target = Environment.getExternalStorageDirectory()
					+ "/update.apk";
			HttpUtils utils = new HttpUtils();
			utils.download(mDownloadUrl, target, new RequestCallBack<File>() {
				// ���ؽ���
				@Override
				public void onLoading(long total, long current,
						boolean isUploading) {
					// TODO Auto-generated method stub
					super.onLoading(total, current, isUploading);
					System.out.println("���ؽ���:" + current + "/" + total);

					tvProgress.setText("���ؽ��ȣ�" + current * 100 / total + "%");

				}

				// ���سɹ�
				@Override
				public void onSuccess(ResponseInfo<File> arg0) {
					// TODO Auto-generated method stub
					System.out.println("���سɹ�");
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.setDataAndType(Uri.fromFile(arg0.result),
							"application/vnd.android.package-archive");
					startActivityForResult(intent, 0);

				}

				// ����ʧ��
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					// TODO Auto-generated method stub
					Toast.makeText(SplashActivity.this, "����ʧ��",
							Toast.LENGTH_LONG).show();
				}
			});
		} else {
			Toast.makeText(SplashActivity.this, "δ�ҵ�sdcard", Toast.LENGTH_LONG)
					.show();
			enterHome();
		}

	}

	// ------------------------------------------------------------------------------------
	// �û�ȡ����װʱ��ص��˷���
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		enterHome();
		Toast.makeText(SplashActivity.this, "ȡ����װ", Toast.LENGTH_SHORT).show();
	}

	// ------------------------------------------------------------------------------------
	// ����������
	private void enterHome() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		finish();
	}

	// �������ݿ�
	public void copyDB(String dbName) {
		InputStream in = null;
		FileOutputStream out = null;
		File dbFile = new File(getFilesDir(), "address.db");
		if (!dbFile.exists()) {

			try {
				in = getAssets().open(dbName);
				out = new FileOutputStream(dbFile);
				int length = 0;
				byte[] bf = new byte[1024];
				while ((length = in.read(bf)) != -1) {
					out.write(bf, 0, length);

				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (in != null | out != null) {
					try {
						in.close();
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}

	}

}
