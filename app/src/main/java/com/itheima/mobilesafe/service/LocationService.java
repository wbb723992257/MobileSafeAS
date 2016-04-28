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
		
		//定位管理器
		LocationManager lm=(LocationManager) getSystemService(LOCATION_SERVICE);
		
		//Criteria设置最佳位置提供者的标准
		Criteria criteria=new Criteria();
		//定位精度：一般
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		//是否允许付费：允许，比如联网定位
		criteria.setCostAllowed(true);
		//将标准传入参数，后一个true表示provider可用时使用
		String bestProvider = lm.getBestProvider(criteria, true);
		
		
		
		lm.requestLocationUpdates(bestProvider, 10, 5, new MyListener());
			
	}
	
	class MyListener implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
		String longitude ="经度"+location.getLongitude();
		String  latitude ="纬度"+location.getLatitude();
		String  accuracy ="精度"+location.getAccuracy();
		String  altitude ="海拔"+location.getAltitude();
		String locationNumber=longitude+"/n"+latitude+"/n"+accuracy+"/n"+altitude+"/n";
		mPref.edit().putString("location", locationNumber).commit();	
		
		//每次讲定位数据存储后，将此服务销毁；
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
