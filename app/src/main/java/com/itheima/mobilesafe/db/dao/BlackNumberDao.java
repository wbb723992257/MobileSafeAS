package com.itheima.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.itheima.mobilesafe.classDemo.BlackNumberInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${wbb} on 2016/4/29.
 */
public class BlackNumberDao {

    public  BlackNumberOpenHelper openHelper;

    public BlackNumberDao(Context context) {
        openHelper = new BlackNumberOpenHelper(context);
    }

    /**
     * @param number
     * @param mode
     */
    public boolean add(String number, String mode) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("number", number);
        contentValues.put("mode", mode);
        long rowid = db.insert("blacknumber", null, contentValues);
        if (rowid == -1) {
            return false;

        } else {
            return true;
        }

    }

    /**
     * 通过电话号码删除
     *
     * @param number 电话号码
     */
    public boolean delete(String number) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        int rowNumber = db.delete("blacknumber", "number=?", new String[]{number});
        if (rowNumber == 0) {
            return false;
        } else {
            return true;
        }
    }
    /**
     * 通过电话号码修改拦截的模式
     *
     * @param number
     */
    public boolean changeNumberMode(String number, String mode) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mode", mode);
        int rownumber =db.update("blacknumber", values, "number=?", new String[]{number});
        if (rownumber == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 返回一个黑名单号码拦截模式
     *
     * @return
     */
    public String findNumber(String number) {
        String mode = "";
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.query("blacknumber", new String[]{"mode"}, "number=?", new String[]{number}, null, null, null);
        if (cursor.moveToNext()) {
            mode = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return mode;
    }

    /**
     * 查询所有的黑名单
     *
     * @return
     */
    public List<BlackNumberInfo> findAll() {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        List<BlackNumberInfo> blackNumberInfos = new ArrayList<BlackNumberInfo>();
        Cursor cursor = db.query("blacknumber", new String[]{"number", "mode"}, null, null, null, null, null);
        BlackNumberInfo blackNumberInfo;
        while (cursor.moveToNext()) {
            blackNumberInfo =new BlackNumberInfo();
            blackNumberInfo.setNumber(cursor.getString(0));
            blackNumberInfo.setMode(cursor.getString(1));
            blackNumberInfos.add(blackNumberInfo);
        }

        cursor.close();
        db.close();
        return blackNumberInfos;
    }
    /**
     * 分页加载数据
     *
     * @param pageNumber 表示当前是哪一页
     * @param pageSize   表示每一页有多少条数据
     * @return limit 表示限制当前有多少数据
     * offset 表示跳过 从第几条开始
     */
    public List<BlackNumberInfo> findPar(int pageNumber, int pageSize) {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select number,mode from blacknumber limit ? offset ?", new String[]{String.valueOf(pageSize),
                String.valueOf(pageSize * pageNumber)});
        List<BlackNumberInfo> blackNumberInfos = new ArrayList<BlackNumberInfo>();
        while (cursor.moveToNext()) {
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            blackNumberInfo.setMode(cursor.getString(1));
            blackNumberInfo.setNumber(cursor.getString(0));
            blackNumberInfos.add(blackNumberInfo);
        }
        cursor.close();
        db.close();
        return blackNumberInfos;
    }
    /**
     * 获取总的记录数
     * @return
     */
    public int getTotalNumber(){
        SQLiteDatabase db=openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from blacknumber", null);
        cursor.moveToNext();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return  count;


    }

}
