package com.example.cost.constant;

public class Constant {

    //用来识别开销种类
    public static final int TYPE_RENT = 0;
    public static final int TYPE_WATER = 1;
    public static final int TYPE_ELECTRICITY = 2;
    public static final int TYPE_PROPERTY = 3;
    public static final int TYPE_FOOD = 4;
    public static final int TYPE_FRUIT = 5;
    public static final int TYPE_SNACKS = 6;
    public static final int TYPE_OTHERS = 7;

    //用来识别总费种类
    public static final int TYPE_TOTAL_RICE = 0;
    public static final int TYPE_TOTAL_ROOM = 1;
    public static final int TYPE_TOTAL_OTHER = 2;
    public static final int TYPE_TOTAL_FRU_SNA = 3;
    public static final int TYPE_TOTAL_ALL = 4;

    //用来识别获取的时间类型
    public static final int TYPE_DATE_YEAR = 0;
    public static final int TYPE_DATE_MONTH = 1;
    public static final int TYPE_DATE_DAY = 2;
    public static final int TYPE_DATE_SECOND = 3;

    public static final String TABLE_NAME_FEE = "Fee";//每日费用模型表
    public static final String TABLE_NAME_TODAY = "Fee_total_day";//每日费用模型表
    public static final String TABLE_NAME_MONTH = "Fee_total_month";//每日费用模型表
    public static final String TABLE_NAME_YEAR = "Fee_total_year";//每日费用模型表

    public static final String CUR_DATE_TABLE_NAME = "date_temp_sp";
    public static final String CUR_DATE_KEY = "mCurDate";
    public static final String CUR_MONTH_KEY = "mCurMonth";

    //初始化为0，执行一次添加数据时候+1；
    public static int TEMP_ADD_TODAY_BILL = 0;
    public static int TEMP_ADD_HISTORY_BILL = 0;
    public static String TEMP_TODAY_DATE = "";

    public static String ACTIVITY_TEMP = "totalMonth";
    public static int ACTIVITY_REQUEST_CODE = 1;
}
