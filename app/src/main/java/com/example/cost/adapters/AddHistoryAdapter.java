package com.example.cost.adapters;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cost.R;
import com.example.cost.constant.Constant;
import com.example.cost.models.FeeModel;
import com.example.cost.utils.SPUtils;
import com.example.cost.utils.SQLiteUtils;
import com.example.cost.utils.Tools;

import java.util.Calendar;

public class AddHistoryAdapter extends BaseAdapter {

    private static final String TAG = "AddHistoryAdapter";
    public static final int THREAD_HISTORY_CONFIRM = 4;
    public static final int HISTORY_INPUT_SUCCESS = 5;
    private final Context mContext;
    private final Handler mHandler;

    //用来判断需要更新的条目
    private boolean changeFood = false;
    private boolean changeFruit = false;
    private boolean changeOther = false;
    private boolean changeProperty = false;
    private boolean changeRent = false;
    private boolean changeSnacks = false;
    private boolean changeElect = false;
    private String spDate;

    public AddHistoryAdapter(Context context, Handler handler){
        mContext = context;
        mHandler = handler;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, Tools.getCurMethodName());
        View view;
        if (convertView == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.dialog_add_history, null);
        }else {
            view = convertView;
        }

        final EditText mEtHistoryFood = (EditText) view.findViewById(R.id.et_history_food);
        final EditText mEtHistoryFruit = (EditText) view.findViewById(R.id.et_history_fruit);
        final EditText mEtHistoryOther = (EditText) view.findViewById(R.id.et_history_other);
        final EditText mEtHistoryProperty = (EditText) view.findViewById(R.id.et_history_property);
        final EditText mEtHistoryRent = (EditText) view.findViewById(R.id.et_history_rent);
        final EditText mEtHistorySnacks = (EditText) view.findViewById(R.id.et_history_snacks);
        final EditText mEtHistoryElect = (EditText) view.findViewById(R.id.et_history_water_elect);

        Button mBtHistorySave = (Button) view.findViewById(R.id.bt_history_dialog_save);
        final TextView mTvChooseDate = (TextView) view.findViewById(R.id.tv_choose_date);

        mTvChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spDate = SPUtils.getSpDate(mContext);
                mTvChooseDate.setText(spDate);
            }
        });

        //保存添加的历史账单
        mBtHistorySave.setOnClickListener(new View.OnClickListener() {

            private int elect;
            private int snacks;
            private int rent;
            private int property;
            private int other;
            private int fruit;
            private int food;

            @Override
            public void onClick(View v) {
                String tempFood = mEtHistoryFood.getText().toString().trim();
                String tempFruit = mEtHistoryFruit.getText().toString().trim();
                String tempOther = mEtHistoryOther.getText().toString().trim();
                String tempProperty = mEtHistoryProperty.getText().toString().trim();
                String tempRent = mEtHistoryRent.getText().toString().trim();
                String tempSnacks = mEtHistorySnacks.getText().toString().trim();
                String tempElect = mEtHistoryElect.getText().toString().trim();

                if(tempFood.equals("") && tempFruit.equals("") && tempOther.equals("") && tempProperty.equals("")
                && tempRent.equals("") && tempSnacks.equals("") && tempElect.equals("")){
                    //未添加任何数据
                    Toast.makeText(mContext, "未添加任何数据", Toast.LENGTH_SHORT).show();
                }else {
                    FeeModel mFeeModel = new FeeModel();
                    if(!tempFood.equals("")){
                        changeFood = true;
                        food = Integer.parseInt(tempFood);
                        mFeeModel.setFood(food);
                    }
                    if(!tempFruit.equals("")){
                        changeFruit = true;
                        fruit = Integer.parseInt(tempFruit);
                        mFeeModel.setFruit(fruit);
                    }
                    if(!tempOther.equals("")){
                        changeOther = true;
                        other = Integer.parseInt(tempOther);
                        mFeeModel.setOthers(other);
                    }
                    if(!tempProperty.equals("")){
                        changeProperty = true;
                        property = Integer.parseInt(tempProperty);
                        mFeeModel.setProperty(property);
                    }
                    if(!tempRent.equals("")){
                        changeRent = true;
                        rent = Integer.parseInt(tempRent);
                        mFeeModel.setRent(rent);
                    }
                    if(!tempSnacks.equals("")){
                        changeSnacks = true;
                        snacks = Integer.parseInt(tempSnacks);
                        mFeeModel.setSnacks(snacks);
                    }
                    if(!tempElect.equals("")){
                        changeElect = true;
                        elect = Integer.parseInt(tempElect);
                        mFeeModel.setElectricity(elect);
                    }
                    String spDate = SPUtils.getSpDate(mContext);
                    String spMonth = SPUtils.getSpMonth(mContext);

                    mFeeModel.setDay(spDate);
                    mFeeModel.setMonth(spMonth);
                    SPUtils.clearSp(mContext);

                    if(!Tools.isAddedHistory(spDate)){
                        SQLiteUtils.insertFee(Constant.TABLE_NAME_FEE, mFeeModel);
                        Toast.makeText(mContext, "数据添加完成", Toast.LENGTH_SHORT).show();
                        Message message = new Message();
                        message.what = THREAD_HISTORY_CONFIRM;
                        message.arg1 = HISTORY_INPUT_SUCCESS;
                        mHandler.sendMessage(message);
                    }else {
                        FeeModel todayBill = SQLiteUtils.getTodayBill(Constant.TABLE_NAME_FEE, spDate);
                        //判断需要更新的条目
                        if (changeFood){
                            mFeeModel.setFood(todayBill.getFood()+food);
                            SQLiteUtils.updateFee(Constant.TABLE_NAME_FEE, mFeeModel, SPUtils.getSpDate(mContext), Constant.TYPE_FOOD);
                        }
                        if (changeFruit){
                            mFeeModel.setFruit(todayBill.getFruit()+fruit);
                            SQLiteUtils.updateFee(Constant.TABLE_NAME_FEE, mFeeModel, SPUtils.getSpDate(mContext), Constant.TYPE_FRUIT);
                        }
                        if (changeOther){
                            mFeeModel.setOthers(todayBill.getOthers()+other);
                            SQLiteUtils.updateFee(Constant.TABLE_NAME_FEE, mFeeModel, SPUtils.getSpDate(mContext), Constant.TYPE_OTHERS);
                        }
                        if (changeProperty){
                            mFeeModel.setProperty(todayBill.getProperty());
                            SQLiteUtils.updateFee(Constant.TABLE_NAME_FEE, mFeeModel, SPUtils.getSpDate(mContext), Constant.TYPE_PROPERTY);
                        }
                        if (changeRent){
                            mFeeModel.setRent(todayBill.getRent()+rent);
                            SQLiteUtils.updateFee(Constant.TABLE_NAME_FEE, mFeeModel, SPUtils.getSpDate(mContext), Constant.TYPE_RENT);
                        }
                        if (changeSnacks){
                            mFeeModel.setSnacks(todayBill.getSnacks()+snacks);
                            SQLiteUtils.updateFee(Constant.TABLE_NAME_FEE, mFeeModel, SPUtils.getSpDate(mContext), Constant.TYPE_SNACKS);
                        }
                        if (changeElect){
                            mFeeModel.setElectricity(todayBill.getElectricity()+elect);
                            SQLiteUtils.updateFee(Constant.TABLE_NAME_FEE, mFeeModel, SPUtils.getSpDate(mContext), Constant.TYPE_ELECTRICITY);
                        }
                    }
                }

                Message msg = new Message();
                msg.what = THREAD_HISTORY_CONFIRM;
                msg.arg1 = HISTORY_INPUT_SUCCESS;
                mHandler.sendMessage(msg);
            }
        });
        return view;
    }

    /**
     * 弹出选择日期对话框
     */
    public void chooseDate(){
        Calendar calendar=Calendar.getInstance();
        new DatePickerDialog( mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int mYear = year;
                int mMonth = month+1;
                int mDay = dayOfMonth;
                String mDateMonth = year+"年"+mMonth+"月";
                String mDateMonthDay = year+"年"+mMonth+"月"+dayOfMonth+"日";
                SPUtils.setSpDate(mContext, mDateMonthDay);
                SPUtils.setSpMonth(mContext, mDateMonth);

                Log.d("SPUtils", "choose day:"+mDateMonthDay);
                String spMonth = SPUtils.getSpMonth(mContext);
                String spDate = SPUtils.getSpDate(mContext);
                Log.d("SPUtils", "spMonth:"+spMonth+", spDay:"+spDate);

            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
