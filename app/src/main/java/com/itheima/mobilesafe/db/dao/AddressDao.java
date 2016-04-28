package com.itheima.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressDao {

	private static final String PATH = "data/data/com.example.mobilesafe/files/address.db";
	private static SQLiteDatabase db;

	public static String getAddress(String number) {
		// TODO Auto-generated method stub
		String address = "δ֪����";
		db = SQLiteDatabase.openDatabase(PATH, null,
				SQLiteDatabase.OPEN_READONLY);
		System.out.println("�յ�����");
		// ������ʽ��������1{3~8}����9λ���ֵ�ʱ��ſ�ʼ����
		if (number.matches("^1[3-8]\\d{9}$")) {
			Cursor cursor1 = db.query("data1", new String[] { "id", "outkey" },
					"id=?", new String[] { number.substring(0, 7) }, null,
					null, null);
			System.out.println("ָ��1");
			if (cursor1.moveToNext()) {
				String outkeyNumber = cursor1.getString(cursor1
						.getColumnIndex("outkey"));
				Cursor cursor2 = db.query("data2", new String[] { "location",
						"id" }, "id=?", new String[] { outkeyNumber }, null,
						null, null);
				if (cursor2.moveToNext()) {
					address = cursor2.getString(cursor2
							.getColumnIndex("location"));
					System.out.println(address);
					System.out.println("ָ��2");
				}

			}
		}else if (number.matches("^\\d{3}$")) {
			//��������
			address="��������";
		}

		return address;
	}

}
