package com.itheima.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilesafe.R;
import com.itheima.mobilesafe.classDemo.BlackNumberInfo;
import com.itheima.mobilesafe.db.dao.BlackNumberDao;

import java.util.List;

public class CallSafeActivity extends Activity {

    private ListView listview;
    private List<BlackNumberInfo> blacknumberinfos;
    private LinearLayout layout;
    private BlackNumberDao dao;
    private CallSafeAdapter adapter;
    private int pageNumber = 0;
    private int pageSize = 10;
    private int totalNumber;
    private EditText etJump;
    private TextView tvPageNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_safe);
        layout = (LinearLayout) findViewById(R.id.ll_pb);
        layout.setVisibility(View.VISIBLE);
        etJump = (EditText) findViewById(R.id.et_page_number);
        tvPageNumber = (TextView) findViewById(R.id.tv_page_numbeer);
        initUI();
        initDate();
    }

    private void initUI() {

        listview = (ListView) findViewById(R.id.list_view);


    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            tvPageNumber.setText(pageNumber + "/" + totalNumber);
            layout.setVisibility(View.INVISIBLE);
            adapter = new CallSafeAdapter();
            listview.setAdapter(adapter);
        }
    };


    private void initDate() {


        new Thread() {
            @Override
            public void run() {

                dao = new BlackNumberDao(CallSafeActivity.this);
                totalNumber = dao.getTotalNumber() / pageSize;

                blacknumberinfos = dao.findPar(pageNumber, pageSize);
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    //点击跳转下一页
    public void nextPage(View view) {
        if (pageNumber >= totalNumber - 1) {

            Toast.makeText(CallSafeActivity.this, "已到达最后一页", Toast.LENGTH_SHORT).show();
            return;
        }
        pageNumber++;
        initDate();


    }

    //点击跳转上一页
    public void prePage(View view) {
        if (pageNumber <= 0) {

            Toast.makeText(CallSafeActivity.this, "已处于起始页", Toast.LENGTH_SHORT).show();
            return;
        }
        pageNumber--;
        initDate();


    }

    //随机跳转
    public void jump(View view) {
        String etPageText = etJump.getText().toString().trim();
        if (TextUtils.isEmpty(etPageText)) {
            Toast.makeText(CallSafeActivity.this, "不能为空", Toast.LENGTH_SHORT).show();
        } else {
            int page = Integer.parseInt(etPageText);
            if (page >= 0 && page <= totalNumber - 1) {
                pageNumber = page;
                initDate();

            } else {
                Toast.makeText(CallSafeActivity.this, "请输入正确的数值", Toast.LENGTH_SHORT).show();

            }


        }


    }


    class CallSafeAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return blacknumberinfos.size();
        }

        @Override
        public Object getItem(int i) {
            return blacknumberinfos.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            System.out.println("getView");
            final BlackNumberInfo blackNumberInfo = blacknumberinfos.get(i);
            ViewHolder holder;
            View callsafeview;
            if (convertView == null) {
                holder = new ViewHolder();
                callsafeview = View.inflate(CallSafeActivity.this, R.layout.callsafe_list_item, null);
                holder.tv_number = (TextView) callsafeview.findViewById(R.id.tv_blacknumber);
                holder.tv_mode = (TextView) callsafeview.findViewById(R.id.tv_mode);
                holder.iv_delete = (ImageView) callsafeview.findViewById(R.id.iv_delete);
                callsafeview.setTag(holder);
            } else {
                callsafeview = convertView;
                holder = (ViewHolder) callsafeview.getTag();

            }

            holder.tv_number.setText(blackNumberInfo.getNumber());
            String mode = blackNumberInfo.getMode();
            if (mode.equals("1")) {
                holder.tv_mode.setText("来电拦截+短信");
            } else if (mode.equals("2")) {
                holder.tv_mode.setText("电话拦截");
            } else if (mode.equals("3")) {
                holder.tv_mode.setText("短信拦截");
            }

            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number = blackNumberInfo.getNumber();
                    boolean delete = dao.delete(number);
                    if (delete) {
                        Toast.makeText(CallSafeActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        blacknumberinfos.remove(blackNumberInfo);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(CallSafeActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            return callsafeview;
        }
    }

    static class ViewHolder {
        TextView tv_number;
        TextView tv_mode;
        ImageView iv_delete;
    }
}
