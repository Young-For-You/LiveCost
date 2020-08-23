package com.example.cost;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cost.activites.ComingActivity;
import com.example.cost.adapters.AddHistoryAdapter;
import com.example.cost.adapters.BillDiaLogAdapter;
import com.example.cost.adapters.HistoryBillAdapter;
import com.example.cost.constant.Constant;
import com.example.cost.models.FeeModel;
import com.example.cost.utils.LogUtils;
import com.example.cost.utils.SQLiteUtils;
import com.example.cost.utils.Tools;
import com.githang.statusbar.StatusBarCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView mTvMonthTotalIncome;
    private TextView mTvTodayExpend, mTvTotalExpend, mTvDate, mTvFoodMoney, mTvFruitMoney, mTvSnacksMoney, mTvRentMoney, mTvPropertyMoney,
            mTvElectMoney, mTvFood, mTvFruit, mTvSnacks, mTvRent, mTvElect, mTvProperty;
    private RecyclerView mRlvHistoryBill;
    private LinearLayout mLlBill;
    private AlertDialog dialog;
    private String DBName = "Bill.db";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (msg.what == BillDiaLogAdapter.THREAD_CONFIRM) {
                switch (msg.arg1) {
                    case BillDiaLogAdapter.INPUT_SUCCESS:
                        initData();
                        dialog.cancel();
                        break;
                    case BillDiaLogAdapter.CREATE_HISTORY_DIALOG:
                        createHistoryDialog();
                        break;
                }
            }

            if (msg.what == AddHistoryAdapter.THREAD_HISTORY_CONFIRM) {
                if (msg.arg1 == AddHistoryAdapter.HISTORY_INPUT_SUCCESS) {
                    historyDialog.cancel();
                    dialog.cancel();
                    initData();
                }
            }
        }
    };
    private FeeModel mFeeModel;
    private TextView mTvOtherMoney;
    private TextView mTvCurTime;
    private AlertDialog historyDialog;
    private int todayTotal;//今日总支出
    private List<FeeModel> monthBillList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置状态栏属性
        StatusBarCompat.setStatusBarColor(this, Color.BLACK,false);

        initView();
        initDatabase();
        initData();
        initEvent();
    }

    private void initView() {
        mTvMonthTotalIncome = (TextView) findViewById(R.id.tv_month_total_income);
        mTvTodayExpend = (TextView) findViewById(R.id.tv_today_expenditure);
        mTvTotalExpend = (TextView) findViewById(R.id.tv_month_total_expenditure);
        mTvDate = (TextView) findViewById(R.id.tv_today_date);
        mRlvHistoryBill = (RecyclerView) findViewById(R.id.rlv_history_bill);

        mTvFood = (TextView) findViewById(R.id.tv_food);
        mTvFoodMoney = (TextView) findViewById(R.id.tv_food_money);
        mTvFruit = (TextView) findViewById(R.id.tv_fruit);
        mTvFruitMoney = (TextView) findViewById(R.id.tv_fruit_money);
        mTvSnacks = (TextView) findViewById(R.id.tv_snacks);
        mTvSnacksMoney = (TextView) findViewById(R.id.tv_snacks_money);
        mTvRent = (TextView) findViewById(R.id.tv_rent);
        mTvRentMoney = (TextView) findViewById(R.id.tv_rent_money);
        mTvElect = (TextView) findViewById(R.id.tv_water_elect);
        mTvElectMoney = (TextView) findViewById(R.id.tv_water_elect_money);
        mTvProperty = (TextView) findViewById(R.id.tv_property);
        mTvPropertyMoney = (TextView) findViewById(R.id.tv_property_money);
        mTvOtherMoney = (TextView) findViewById(R.id.tv_others_money);
        mTvCurTime = (TextView) findViewById(R.id.tv_today_date);

        mLlBill = (LinearLayout) findViewById(R.id.ll_today_bill);
    }

    private void initDatabase() {
        //创建数据库
        SQLiteUtils.createFeeDatabase(this, DBName, 2);
    }

    private void initData() {
        mFeeModel = SQLiteUtils.getTodayBill("Fee", Tools.getCurTime(Constant.TYPE_DATE_DAY));
        mTvCurTime.setText(Tools.getCurTime(Constant.TYPE_DATE_MONTH));

        //今日总支出
        todayTotal = mFeeModel.getFood()+mFeeModel.getFruit()+mFeeModel.getSnacks()+mFeeModel.getRent() + mFeeModel.getWater() + mFeeModel.getElectricity()+mFeeModel.getProperty()+mFeeModel.getOthers();
        //展示今日各类支出
        initTodayBill();
        //历史总支出
        initHistoryBill();
    }

    private void initEvent() {
        //点击输入本月总收入
        mTvMonthTotalIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ComingActivity.class);
                startActivityForResult(intent, Constant.ACTIVITY_REQUEST_CODE);
            }
        });

        //点击今日账单区域 弹窗输入今日以及往日消费
        mLlBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.ACTIVITY_REQUEST_CODE){
            if (data != null){
                int totalMonth = data.getIntExtra(Constant.ACTIVITY_TEMP, 0);
                if(totalMonth != 0){
                    mTvMonthTotalIncome.setText("+￥"+totalMonth);
                }
            }
        }
    }

    /**
     * 弹出编辑和添加今日消费的对话框
     */
    private void showEditDialog() {
        LogUtils.showLog(TAG, Tools.getCurMethodName());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.input_bill);
        builder.setAdapter(new BillDiaLogAdapter(MainActivity.this, handler), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog = builder.create();
        dialog.show();

        //解决dialog中的EditText不能弹出软键盘问题
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    private void createHistoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("输入历史费用");
        AddHistoryAdapter addHistoryAdapter = new AddHistoryAdapter(MainActivity.this, handler);
        builder.setAdapter(addHistoryAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        historyDialog = builder.create();
        historyDialog.show();

        //解决dialog中的EditText不能弹出软键盘问题
        historyDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        addHistoryAdapter.chooseDate();
    }

    private void initTodayBill() {
        mTvFoodMoney.setText("￥" + mFeeModel.getFood());
        mTvFruitMoney.setText("￥" + mFeeModel.getFruit());
        mTvSnacksMoney.setText("￥" + mFeeModel.getSnacks());
        mTvRentMoney.setText("￥" + mFeeModel.getRent());
        mTvElectMoney.setText("￥" + (mFeeModel.getWater() + mFeeModel.getElectricity()));
        mTvPropertyMoney.setText("￥" + mFeeModel.getProperty());
        mTvOtherMoney.setText("￥" + mFeeModel.getOthers());
        mTvTodayExpend.setText("-￥"+todayTotal);
    }

    private void initHistoryBill() {
        monthBillList = SQLiteUtils.getMonthBill(Constant.TABLE_NAME_FEE, Tools.getCurTime(Constant.TYPE_DATE_MONTH));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        HistoryBillAdapter historyBillAdapter = new HistoryBillAdapter(this, monthBillList);
        mRlvHistoryBill.setLayoutManager(linearLayoutManager);
        mRlvHistoryBill.setAdapter(historyBillAdapter);
        initCurMonthTotal();//加载完首页每日数据之后更新每个月消费总额
    }

    private void initCurMonthTotal() {
        int curMonthTotal = SQLiteUtils.getCurMonthTotal(Tools.getCurTime(Constant.TYPE_DATE_MONTH));
        mTvTotalExpend.setText("-￥"+curMonthTotal);
    }
}
