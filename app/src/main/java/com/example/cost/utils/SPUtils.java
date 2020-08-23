package com.example.cost.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.cost.constant.Constant;

public class SPUtils {

    /**
     * 将用户选择的日期保存到SP中
     * @param context
     * @param date
     */
    public static void setSpDate(Context context,String date){
        SharedPreferences sp = createSP(context);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(Constant.CUR_DATE_KEY, date);
        edit.commit();
        edit.clear();
    }

    /**
     * 获取保存完成的日期
     * @param context
     * @return
     */
    public static String getSpDate(Context context){
        SharedPreferences sp = createSP(context);
        return sp.getString(Constant.CUR_DATE_KEY, "");
    }

    /**
     * 保存用户的选择的月份信息
     * @param context
     * @param month
     */
    public static void setSpMonth(Context context,String month){
        SharedPreferences sp = createSP(context);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(Constant.CUR_MONTH_KEY, month);
        edit.commit();
        edit.clear();
    }

    /**
     * 获取用户布保存的月份信息
     * @param context
     * @return
     */
    public static String getSpMonth(Context context){
        SharedPreferences sp = createSP(context);
        return sp.getString(Constant.CUR_MONTH_KEY, "");
    }

    /**
     * @param context 上下文
     */
    public static void clearSp(Context context){
        SharedPreferences sp = createSP(context);
        sp.edit().clear();
    }

    private static SharedPreferences createSP(Context context){
        SharedPreferences sp = context.getSharedPreferences(Constant.CUR_DATE_TABLE_NAME, Context.MODE_PRIVATE);
        return sp;
    }
}
