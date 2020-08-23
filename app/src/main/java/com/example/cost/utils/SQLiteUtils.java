package com.example.cost.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cost.constant.Constant;
import com.example.cost.helpers.FeeDatabaseHelper;
import com.example.cost.models.FeeModel;

import java.util.ArrayList;
import java.util.List;

public class SQLiteUtils {
    private static final String TAG = "SQLiteUtils";
    private static SQLiteDatabase db;
    private static Context mContext;
    /**
     * 创建数据库
     *
     * @param context 上下文
     * @param DBname  创建的数据库名
     * @param version 数据库版本号
     */
    public static void createFeeDatabase(Context context, String DBname, int version) {
        LogUtils.showLog(TAG, Tools.getCurMethodName());
        mContext = context;
        FeeDatabaseHelper helper = new FeeDatabaseHelper(context, DBname, null, version);
        db = helper.getWritableDatabase();
    }

    /**
     * 向数据库表中插入数据
     *
     * @param tableName 要插入数据的数据库表名
     * @param feeModel  插入的数据模型
     */
    public static void insertFee(String tableName, FeeModel feeModel) {
        LogUtils.showLog(TAG, Tools.getCurMethodName());
        ContentValues values = new ContentValues();
        values.put("month", feeModel.getMonth());
        values.put("day", feeModel.getDay());
        values.put("rent", feeModel.getRent());
        values.put("water", feeModel.getWater());
        values.put("electricity", feeModel.getElectricity());
        values.put("property", feeModel.getProperty());
        values.put("food", feeModel.getFood());
        values.put("fruit", feeModel.getFruit());
        values.put("snacks", feeModel.getSnacks());
        values.put("others", feeModel.getOthers());
        db.insert(tableName, null, values);
        values.clear();
    }

    /**
     * 更新每一天的费用数据
     *
     * @param tableName 需要更新数据的表名
     * @param feeModel  携带数据的model
     * @param date      用来作为查询条件的日期
     * @param type      需要更改的条目
     */
    public static void updateFee(String tableName, FeeModel feeModel, String date, int type) {
        LogUtils.showLog(TAG, Tools.getCurMethodName());
        ContentValues values = new ContentValues();
        switch (type) {
            case Constant.TYPE_RENT:
                values.put("rent", feeModel.getRent());
                db.update(tableName, values, "day = ?", new String[]{date});
                values.clear();
                break;
            case Constant.TYPE_WATER:
                values.put("water", feeModel.getWater());
                db.update(tableName, values, "day = ?", new String[]{date});
                values.clear();
                break;
            case Constant.TYPE_ELECTRICITY:
                values.put("electricity", feeModel.getElectricity());
                db.update(tableName, values, "day = ?", new String[]{date});
                values.clear();
                break;
            case Constant.TYPE_PROPERTY:
                values.put("property", feeModel.getProperty());
                db.update(tableName, values, "day = ?", new String[]{date});
                values.clear();
                break;
            case Constant.TYPE_FOOD:
                values.put("food", feeModel.getFood());
                db.update(tableName, values, "day = ?", new String[]{date});
                values.clear();
                break;
            case Constant.TYPE_FRUIT:
                values.put("fruit", feeModel.getFruit());
                db.update(tableName, values, "day = ?", new String[]{date});
                values.clear();
                break;
            case Constant.TYPE_SNACKS:
                values.put("snacks", feeModel.getSnacks());
                db.update(tableName, values, "day = ?", new String[]{date});
                values.clear();
                break;
            case Constant.TYPE_OTHERS:
                values.put("others", feeModel.getOthers());
                db.update(tableName, values, "day = ?", new String[]{date});
                values.clear();
                break;
        }
    }

    /**
     * @param tableName 查询的表名
     * @param day 查询条件---日期
     * @return 返回封装好的bean对象
     */
    public static FeeModel getTodayBill(String tableName,String day){
        LogUtils.showLog(TAG, Tools.getCurMethodName());
        FeeModel feeModel = new FeeModel();
        Cursor cursor = db.query(tableName, null, "day = ?", new String[]{day}, null, null, "day");
        if(cursor.moveToFirst()){
            do{
                int rent = cursor.getInt(cursor.getColumnIndex("rent"));
                feeModel.setRent(rent);
                int water = cursor.getInt(cursor.getColumnIndex("water"));
                feeModel.setWater(water);
                int electricity = cursor.getInt(cursor.getColumnIndex("electricity"));
                feeModel.setElectricity(electricity);
                int property = cursor.getInt(cursor.getColumnIndex("property"));
                feeModel.setProperty(property);
                int food = cursor.getInt(cursor.getColumnIndex("food"));
                feeModel.setFood(food);
                int fruit = cursor.getInt(cursor.getColumnIndex("fruit"));
                feeModel.setFruit(fruit);
                int snacks = cursor.getInt(cursor.getColumnIndex("snacks"));
                feeModel.setSnacks(snacks);
                int others = cursor.getInt(cursor.getColumnIndex("others"));
                feeModel.setOthers(others);

                int total = rent + water + electricity + property + food + fruit + snacks + others;
                feeModel.setTotal(total);

            }while (cursor.moveToNext());
        }

        return feeModel;
    }

    /**
     * @param tableName 要查询的表名
     * @param month 要查询的月份
     * @return 返回这个月的数据费用模型
     */
    public static List<FeeModel> getMonthBill(String tableName, String month){
        LogUtils.showLog(TAG, Tools.getCurMethodName());
        List<FeeModel> result = new ArrayList<>();
        Cursor cursor = db.query(tableName, null, "month = ?", new String[]{month}, null, null, "month");
        if(cursor.moveToFirst()){
            do{
                FeeModel feeModel = new FeeModel();
                String day = cursor.getString(cursor.getColumnIndex("day"));
                feeModel.setDay(day);
                String tempMonth = cursor.getString(cursor.getColumnIndex("month"));
                feeModel.setMonth(tempMonth);
                int rent = cursor.getInt(cursor.getColumnIndex("rent"));
                feeModel.setRent(rent);
                int water = cursor.getInt(cursor.getColumnIndex("water"));
                feeModel.setWater(water);
                int electricity = cursor.getInt(cursor.getColumnIndex("electricity"));
                feeModel.setElectricity(electricity);
                int property = cursor.getInt(cursor.getColumnIndex("property"));
                feeModel.setProperty(property);
                int food = cursor.getInt(cursor.getColumnIndex("food"));
                feeModel.setFood(food);
                int fruit = cursor.getInt(cursor.getColumnIndex("fruit"));
                feeModel.setFruit(fruit);
                int snacks = cursor.getInt(cursor.getColumnIndex("snacks"));
                feeModel.setSnacks(snacks);
                int others = cursor.getInt(cursor.getColumnIndex("others"));
                feeModel.setOthers(others);
                int total = rent + water + electricity + property + food + fruit + snacks + others;
                feeModel.setTotal(total);

                ContentValues values = new ContentValues();
                values.put("total", total);
                db.update(Constant.TABLE_NAME_FEE, values, "day = ?", new String[]{day});

                Log.d("SQLiteUtils", "month:"+feeModel.getMonth()+", day:"+day+", rent:"+rent+", water:"+water+", electricity:"+electricity+", food:"+food
                +", fruit:"+fruit+", snacks:"+snacks+", others:"+others);

                result.add(feeModel);
            }while (cursor.moveToNext());
        }

        return result;
    }

    /**
     * 查询各类别费用总支出
     * @param month 查询的月份
     * @param type 查询各类支出综合
     * @return
     */
    public static int getMonthKindTotal(String month, int type) {
        LogUtils.showLog(TAG, Tools.getCurMethodName());
        int totalRoom = 0;
        int totalRice = 0;
        int totalFruSna = 0;
        int totalOthers = 0;
        Cursor cursor = db.query(Constant.TABLE_NAME_FEE, null, "month = ?", new String[]{month}, null, null, "day");
        if (cursor.moveToFirst()) {
            do {
                int rent = cursor.getInt(cursor.getColumnIndex("rent"));
                int water = cursor.getInt(cursor.getColumnIndex("water"));
                int electricity = cursor.getInt(cursor.getColumnIndex("electricity"));
                int property = cursor.getInt(cursor.getColumnIndex("property"));
                int food = cursor.getInt(cursor.getColumnIndex("food"));
                int fruit = cursor.getInt(cursor.getColumnIndex("fruit"));
                int snacks = cursor.getInt(cursor.getColumnIndex("snacks"));
                int others = cursor.getInt(cursor.getColumnIndex("others"));

                //房租、水电、物业总费
                totalRoom += rent + water + electricity + property;
                //主食费用
                totalRice += food;
                //水果和零食
                totalFruSna += fruit + snacks;
                //其他费用
                totalOthers += others;

            } while (cursor.moveToNext());
        }
        cursor.close();

        switch (type) {
            case Constant.TYPE_TOTAL_ALL:
                return totalFruSna + totalOthers + totalRice + totalRoom;
            case Constant.TYPE_TOTAL_ROOM:
                return totalRoom;
            case Constant.TYPE_TOTAL_RICE:
                return totalRice;
            case Constant.TYPE_TOTAL_FRU_SNA:
                return totalFruSna;
            case Constant.TYPE_TOTAL_OTHER:
                return totalOthers;
            default:
                return 0;
        }
    }

    /**
     * 查询当月总支出
     * @param month 查询的月份
     * @return 返回查询当前月份的总支出
     */
    public static int getCurMonthTotal(String month){
        int total = 0;
        Cursor cursor = db.query(Constant.TABLE_NAME_FEE, new String[]{"total"}, "month = ?", new String[]{month}, null, null, null);
        if (cursor.moveToFirst()){
            do{
                int totalDay = cursor.getInt(cursor.getColumnIndex("total"));
                total += totalDay;
            }while (cursor.moveToNext());
        }

        return total;
    }
}
