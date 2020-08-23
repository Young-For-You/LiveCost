package com.example.cost.adapters;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cost.R;
import com.example.cost.constant.Constant;
import com.example.cost.models.FeeModel;
import com.example.cost.utils.LogUtils;
import com.example.cost.utils.SQLiteUtils;
import com.example.cost.utils.Tools;

public class BillDiaLogAdapter extends BaseAdapter {

    private static final String TAG = "BillDiaLogAdapter";
    private final Context mContext;
    public static final int THREAD_CONFIRM = 2;
    public static final int CREATE_HISTORY_DIALOG = 3;
    public static final int INPUT_SUCCESS = 0;
    public static final int INPUT_FAILED = 1;
    private Handler mHandler;

    //用来判断需要更新的条目
    private boolean changeFood = false;
    private boolean changeFruit = false;
    private boolean changeOther = false;
    private boolean changeProperty = false;
    private boolean changeRent = false;
    private boolean changeSnacks = false;
    private boolean changeElect = false;



    public BillDiaLogAdapter(Context context, Handler handler) {
        LogUtils.showLog(TAG, Tools.getCurMethodName());
        mContext = context;
        mHandler = handler;
    }

    @Override
    public int getCount() {
        LogUtils.showLog(TAG, Tools.getCurMethodName());
        return 1;
    }

    @Override
    public Object getItem(int position) {
        LogUtils.showLog(TAG, Tools.getCurMethodName());
        return null;
    }

    @Override
    public long getItemId(int position) {
        LogUtils.showLog(TAG, Tools.getCurMethodName());
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LogUtils.showLog(TAG, Tools.getCurMethodName());
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.dialog_bill, null);
        } else {
            view = convertView;
        }

        final EditText mEtFood = (EditText) view.findViewById(R.id.et_food);
        final EditText mEtFruit = (EditText) view.findViewById(R.id.et_fruit);
        final EditText mEtOther = (EditText) view.findViewById(R.id.et_other);
        final EditText mEtProperty = (EditText) view.findViewById(R.id.et_property);
        final EditText mEtRent = (EditText) view.findViewById(R.id.et_rent);
        final EditText mEtSnacks = (EditText) view.findViewById(R.id.et_snacks);
        final EditText mEtElect = (EditText) view.findViewById(R.id.et_water_elect);

        Button mBtSave = (Button) view.findViewById(R.id.tv_dialog_save);
        Button mBtAddHistory = (Button) view.findViewById(R.id.bt_add_history_bill);

        //打开添加历史账单数据页面并关闭dialog
        mBtAddHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //通知Activity打开另一个弹窗来添加历史账单
                Message message = new Message();
                message.what = BillDiaLogAdapter.THREAD_CONFIRM;
                message.arg1 = CREATE_HISTORY_DIALOG;
                mHandler.sendMessage(message);
            }
        });

        mBtSave.setOnClickListener(new View.OnClickListener() {

            private String month;
            private String day;
            private int elect;
            private int snacks;
            private int rent;
            private int property;
            private int other;
            private int fruit;
            private int food;

            @Override
            public void onClick(View v) {

                FeeModel mFeeModel = new FeeModel();
                String tempFood = mEtFood.getText().toString().trim();
                String tempFruit = mEtFruit.getText().toString().trim();
                String tempOther = mEtOther.getText().toString().trim();
                String tempProperty = mEtProperty.getText().toString().trim();
                String tempRent = mEtRent.getText().toString().trim();
                String tempSnacks = mEtSnacks.getText().toString().trim();
                String tempElect = mEtElect.getText().toString().trim();

                if (tempFood.equals("") && tempFruit.equals("") && tempOther.equals("") && tempProperty.equals("")
                        && tempRent.equals("") && tempSnacks.equals("") && tempElect.equals("")) {
                    //未添加任何数据
                    Toast.makeText(mContext, "未添加任何费用", Toast.LENGTH_SHORT).show();
                } else {
                    if (!tempFood.equals("")) {
                        changeFood = true;
                        food = Integer.parseInt(tempFood);
                        mFeeModel.setFood(food);
                    }
                    if (!tempFruit.equals("")) {
                        changeFruit = true;
                        fruit = Integer.parseInt(tempFruit);
                        mFeeModel.setFruit(fruit);
                    }
                    if (!tempOther.equals("")) {
                        changeOther = true;
                        other = Integer.parseInt(tempOther);
                        mFeeModel.setOthers(other);
                    }
                    if (!tempProperty.equals("")) {
                        changeProperty = true;
                        property = Integer.parseInt(tempProperty);
                        mFeeModel.setProperty(property);
                    }
                    if (!tempRent.equals("")) {
                        changeRent = true;
                        rent = Integer.parseInt(tempRent);
                        mFeeModel.setRent(rent);
                    }
                    if (!tempSnacks.equals("")) {
                        changeSnacks = true;
                        snacks = Integer.parseInt(tempSnacks);
                        mFeeModel.setSnacks(snacks);
                    }
                    if (!tempElect.equals("")) {
                        changeElect = true;
                        elect = Integer.parseInt(tempElect);
                        mFeeModel.setElectricity(elect);
                    }

                    day = Tools.getCurTime(Constant.TYPE_DATE_DAY);
                    mFeeModel.setDay(day);

                    month = Tools.getCurTime(Constant.TYPE_DATE_MONTH);
                    mFeeModel.setMonth(month);

                    if(!Tools.isAddedHistory(Tools.getCurTime(Constant.TYPE_DATE_DAY))){
                        SQLiteUtils.insertFee(Constant.TABLE_NAME_FEE, mFeeModel);
                    }else {
                        FeeModel todayModel = SQLiteUtils.getTodayBill(Constant.TABLE_NAME_FEE, day);

                        if(changeFood){
                            mFeeModel.setFood(todayModel.getFood()+food);
                            SQLiteUtils.updateFee(Constant.TABLE_NAME_FEE, mFeeModel, Tools.getCurTime(Constant.TYPE_DATE_DAY), Constant.TYPE_FOOD);
                        }
                        if(changeFruit){
                            mFeeModel.setFruit(todayModel.getFruit()+fruit);
                            SQLiteUtils.updateFee(Constant.TABLE_NAME_FEE, mFeeModel, Tools.getCurTime(Constant.TYPE_DATE_DAY), Constant.TYPE_FRUIT);
                        }
                        if(changeSnacks){
                            mFeeModel.setSnacks(todayModel.getSnacks()+snacks);
                            SQLiteUtils.updateFee(Constant.TABLE_NAME_FEE, mFeeModel, Tools.getCurTime(Constant.TYPE_DATE_DAY), Constant.TYPE_SNACKS);
                        }
                        if(changeOther){
                            mFeeModel.setOthers(todayModel.getOthers()+other);
                            SQLiteUtils.updateFee(Constant.TABLE_NAME_FEE, mFeeModel, Tools.getCurTime(Constant.TYPE_DATE_DAY), Constant.TYPE_OTHERS);
                        }
                        if(changeElect){
                            mFeeModel.setElectricity(todayModel.getElectricity()+elect);
                            SQLiteUtils.updateFee(Constant.TABLE_NAME_FEE, mFeeModel, Tools.getCurTime(Constant.TYPE_DATE_DAY), Constant.TYPE_ELECTRICITY);
                        }
                        if(changeRent){
                            mFeeModel.setRent(todayModel.getRent()+rent);
                            SQLiteUtils.updateFee(Constant.TABLE_NAME_FEE, mFeeModel, Tools.getCurTime(Constant.TYPE_DATE_DAY), Constant.TYPE_RENT);
                        }
                        if(changeProperty){
                            mFeeModel.setProperty(todayModel.getProperty()+property);
                            SQLiteUtils.updateFee(Constant.TABLE_NAME_FEE, mFeeModel, Tools.getCurTime(Constant.TYPE_DATE_DAY), Constant.TYPE_PROPERTY);
                        }
                    }

                    Message message = new Message();
                    message.what = BillDiaLogAdapter.THREAD_CONFIRM;
                    message.arg1 = INPUT_SUCCESS;
                    mHandler.sendMessage(message);
                }
            }
        });
        return view;
    }
}
