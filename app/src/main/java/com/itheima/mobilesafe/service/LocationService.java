package com.itheima.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class LocationService extends Service {

	private SharedPreferences mPref;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		
		//��λ������
		LocationManager lm=(LocationManager) getSystemService(LOCATION_SERVICE);
		
		//Criteria�������λ���ṩ�ߵı�׼
		Criteria criteria=new Criteria();
		//��λ���ȣ�һ��
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		//�Ƿ������ѣ���������������λ
		criteria.setCostAllowed(true);
		//����׼�����������һ��true��ʾprovider����ʱʹ��
		String bestProvider = lm.getBestProvider(criteria, true);
		
		
		
		lm.requestLocationUpdates(bestProvider, 10, 5, new MyListener());
			
	}
	
	class MyListener implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
		String longitude ="����"+location.getLongitude();
		String  latitude ="γ��"+location.getLatitude();
		String  accuracy ="����"+location.getAccuracy();
		String  altitude ="����"+location.getAltitude();
		String locationNumber=longitude+"/n"+latitude+"/n"+accuracy+"/n"+altitude+"/n";
		mPref.edit().putString("location", locationNumber).commit();	
		
		//ÿ�ν���λ���ݴ洢�󣬽��˷������٣�
		stopSelf();
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
