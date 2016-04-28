package com.itheima.mobilesafe.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.R;
import com.itheima.mobilesafe.classDemo.ContectMessage;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ContactActivity extends Activity {
	private ListView lv;
	private Uri rawcontentUri;
	private Uri dataUri;
	List<ContectMessage> list = new ArrayList<ContectMessage>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);

		readContacts();
		lv = (ListView) findViewById(R.id.lv_list);
		ContectAdapter adapter = new ContectAdapter();
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String phoneNumber=list.get(position).getNumber();
				Intent intent=new Intent();
				intent.putExtra("phoneNumber", phoneNumber);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});

	}

	private void readContacts() {
		// 1.从raw_contacts中读取联系人的id("contact_id")
		// 2.根据contact_id从data表中查询相应的电话号码和联系人
		// 3.根据mimitype来区分哪个是联系人哪个是电话号码
		rawcontentUri = Uri
				.parse("content://com.android.contacts/raw_contacts");
		dataUri = Uri.parse("content://com.android.contacts/data");
		Cursor rawcontentCursor = getContentResolver().query(rawcontentUri,
				null, null, null, null);
		if (rawcontentCursor != null) {
			while (rawcontentCursor.moveToNext()) {
				// 1.从raw_contacts中读取联系人的id("contact_id")
				String contactId = rawcontentCursor.getString(rawcontentCursor
						.getColumnIndex("contact_id"));
				// System.out.println(contactId);
				// 2.根据contact_id从data表中查询相应的电话号码和联系人,实际上查询的是视图View_data
				Cursor dataCursor = getContentResolver().query(dataUri,
						new String[] { "data1", "mimetype" }, "contact_id=?",
						new String[] { contactId }, null);
				if (dataCursor != null) {
					ContectMessage cm = null;
					String name = null;
					String number = null;
					while (dataCursor.moveToNext()) {
						String data1 = dataCursor.getString(dataCursor
								.getColumnIndex("data1"));
						String mimetype = dataCursor.getString(dataCursor
								.getColumnIndex("mimetype"));
						// System.out.println(contactId+"  +"+data1+"+"+mimetype);

						if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
							number = data1;
							System.out.println(data1);
						} else if ("vnd.android.cursor.item/name"
								.equals(mimetype)) {
							name = data1;
							System.out.println(data1);
						}

					}
					cm = new ContectMessage(name, number);
					list.add(cm);
					dataCursor.close();
				}

			}
			rawcontentCursor.close();
		}

	}

	class ContectAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = View.inflate(ContactActivity.this,
					R.layout.contact_list_item, null);
			TextView tvName = (TextView) view.findViewById(R.id.tv_name);
			TextView tvNumber = (TextView) view.findViewById(R.id.tv_number);
			ContectMessage cm = list.get(position);
			tvName.setText(cm.getName());
			tvNumber.setText(cm.getNumber());
			return view;

		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

	}
}
