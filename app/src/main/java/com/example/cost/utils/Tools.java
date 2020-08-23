package com.example.cost.utils;

import com.example.cost.constant.Constant;
import com.example.cost.models.FeeModel;

import java.util.Calendar;

public class Tools {
    /**
     * @return 返回调用者的方法名
     */
    public static String getCurMethodName(){
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        return stackTraceElement.getMethodName();
    }

    /**
     * @param type 需要获取的时间的种类
     * @return 返回系统当前时间
     */
    public static String getCurTime(int type){

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        switch (type){
            case Constant.TYPE_DATE_YEAR:
                return year+"年";
                case Constant.TYPE_DATE_MONTH:
                    return year+"年"+month+"月";
            case Constant.TYPE_DATE_DAY:
                return year+"年"+month+"月"+day+"日";
            case Constant.TYPE_DATE_SECOND:
                return year+"."+month+"."+day+"  "+hour+":"+minute+":"+second;
            default:
                return "";
        }
    }

    /**
     * @param day 日期 判断是否已经添加过该日期的费用数据
     * @return true已经添加过 false未添加过
     */
    public static boolean isAddedHistory(String day){
        FeeModel todayBill = SQLiteUtils.getTodayBill(Constant.TABLE_NAME_FEE, day);
        int total = todayBill.getTotal();
        if(total == 0) return false;
        return true;
    }
}
