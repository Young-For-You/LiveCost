package com.example.cost.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.cost.R;
import com.example.cost.constant.Constant;

public class ComingActivity extends AppCompatActivity {
    private EditText mEtInput;
    private Button mBtDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coming);

        initView();
        clickEvent();
    }

    private void initView() {
        mEtInput = (EditText) findViewById(R.id.et_coming_input);
        mBtDone = (Button) findViewById(R.id.bt_coming_done);
    }

    private void clickEvent() {
        mBtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputFee = mEtInput.getText().toString().trim();
                if (!inputFee.equals("")){
                    Intent intent = new Intent();
                    intent.putExtra(Constant.ACTIVITY_TEMP, Integer.valueOf(inputFee));
                    setResult(0,intent);

                    // TODO: 2020/8/7 将收入数据保存至数据库
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
