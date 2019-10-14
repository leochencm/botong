package com.botongglcontroller.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.botongglcontroller.R;
import com.botongglcontroller.activity.OrderDetailsActivity;
import com.botongglcontroller.beans.GetBill;

import java.util.List;

/**
 * Created by maning on 16/6/22.
 */
public class BillManageAdapter extends RecyclerView.Adapter<BillManageAdapter.MyViewHolder> {

    private Context context;
    private List<GetBill> mDatas;
    private LayoutInflater layoutInflater;

    public BillManageAdapter(Context context, List<GetBill> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        layoutInflater = LayoutInflater.from(this.context);
    }

    public void setDatas(List<GetBill> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = layoutInflater.inflate(R.layout.item_billmanage, parent, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final GetBill getBill = mDatas.get(position);
        holder.title.setText(getBill.getBill());
        holder.time.setText(getBill.getEnddate());
        if(getBill.getPayed().equals("0")){
            holder.state.setText("待支付");
        }else{
            holder.state.setText("已支付");
        }
        holder.price.setText(getBill.getMoney()+"元");
        //如果设置了回调，则设置点击事件
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    mOnItemClickLitener.onItemClick(holder.itemView, position);
                    if(getBill.getPayed().equals("0")){
                        /*Bundle bundle=new Bundle();
                        bundle.putString("billid",getBill.getId());
                        bundle.putString("subject",getBill.getBill());
                        bundle.putString("orderNo",getBill.getId());
                        bundle.putString("totalAmount",getBill.getMoney());
                        Intents.getIntents().Intent(context, OrderDetailsActivity.class,bundle);*/
                        Intent intent = new Intent(context, OrderDetailsActivity.class);
                        intent.putExtra("billid",getBill.getId());
                        intent.putExtra("subject",getBill.getBill());
                        intent.putExtra("orderNo",getBill.getId());
                        intent.putExtra("totalAmount",getBill.getMoney());
                        context.startActivity(intent);
                    }
                }
            });
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mOnItemMapClickLitener.onItemClick(holder.itemView, position);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView time;
        TextView state;
        TextView price;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            time = (TextView) itemView.findViewById(R.id.time);
            state = (TextView) itemView.findViewById(R.id.state);
            price = (TextView) itemView.findViewById(R.id.price);
        }
    }

    //item监听
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    //地图点击监听
    public interface OnItemMapClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemMapClickLitener mOnItemMapClickLitener;

    public void setOnItemMapClickLitener(OnItemMapClickLitener mOnItemMapClickLitener) {
        this.mOnItemMapClickLitener = mOnItemMapClickLitener;
    }


}
