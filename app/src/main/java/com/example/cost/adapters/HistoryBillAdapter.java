package com.example.cost.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cost.R;
import com.example.cost.models.FeeModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryBillAdapter extends RecyclerView.Adapter<HistoryBillAdapter.ViewHolder>{

    private Context mContext;
    private List<FeeModel> mDayList;

    public HistoryBillAdapter(Context context, List<FeeModel> dayList){
        mContext = context;
        mDayList = dayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.main_history_bill, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FeeModel feeModel = mDayList.get(position);
        int food = feeModel.getFood();
        int fruitSna = feeModel.getFruit() + feeModel.getSnacks();
        int others = feeModel.getOthers();
        int room = feeModel.getRent() + feeModel.getElectricity() + feeModel.getWater() + feeModel.getProperty();
        int total = feeModel.getTotal();

        holder.mTvDate.setText(feeModel.getDay());
        holder.mTvFood.setText("-￥"+food);
        holder.mTvFruSna.setText("-￥"+fruitSna);
        holder.mTvOther.setText("-￥"+others);
        holder.mTvRentWE.setText("-￥"+room);
        holder.mTvTotal.setText("-￥"+total);
    }

    @Override
    public int getItemCount() {
        return mDayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView mTvDate;
        private final TextView mTvFood;
        private final TextView mTvFruSna;
        private final TextView mTvOther;
        private final TextView mTvRentWE;
        private final TextView mTvTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvDate = (TextView) itemView.findViewById(R.id.tv_history_card_date);
            mTvFood = (TextView) itemView.findViewById(R.id.tv_history_card_food);
            mTvFruSna = (TextView) itemView.findViewById(R.id.tv_history_card_fruit_snacks);
            mTvOther = (TextView) itemView.findViewById(R.id.tv_history_card_other);
            mTvRentWE = (TextView) itemView.findViewById(R.id.tv_history_card_rent_we);
            mTvTotal = (TextView) itemView.findViewById(R.id.tv_history_card_total_bill);
        }
    }
}
