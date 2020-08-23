package com.example.cost.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class FeeDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "FeeDatabaseHelper";

    private final Context mContext;

    private static final String CREATE_FEE = "create table Fee(" +
            "month text," +
            "day text," +
            "rent integer," +
            "water integer," +
            "electricity integer," +
            "property integer," +
            "food integer," +
            "fruit integer," +
            "snacks integer," +
            "others integer," +
            "total integer)";

    private static final String CREATE_TOTAL = "create table FeeTotal(" +
            "dDay text," +
            "dMonth text," +
            "dYear text," +
            "tToday int," +
            "tMonth int," +
            "tYear int," +
            "coming int," +
            "yearComing int)";

    public FeeDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FEE);
        db.execSQL(CREATE_TOTAL);
        Toast.makeText(mContext, "数据库创建成功", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "数据库创建成功!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Fee");
        db.execSQL("drop table if exists FeeTotal");
        Toast.makeText(mContext, "数据库升级成功", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "数据库升级成功!");
    }
}
