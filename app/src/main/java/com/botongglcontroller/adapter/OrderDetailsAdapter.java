package com.botongglcontroller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.botongglcontroller.R;
import com.botongglcontroller.beans.GetBilldetail;

import java.util.List;

/**
 * Created by maning on 16/6/22.
 */
public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.MyViewHolder> {

    private Context context;
    private List<GetBilldetail> mDatas;
    private LayoutInflater layoutInflater;
    private OnItemClickLitener mOnItemClickLitener;
    private OnItemMapClickLitener mOnItemMapClickLitener;

    public OrderDetailsAdapter(Context context, List<GetBilldetail> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        layoutInflater = LayoutInflater.from(this.context);
    }

    public void setDatas(List<GetBilldetail> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = layoutInflater.inflate(R.layout.item_orderdetails, parent, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final GetBilldetail getBill = mDatas.get(position);
        holder.boilernumber.setText(getBill.getBoiler());
        holder.price.setText(getBill.getMoney() + "/元");
        //如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
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

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public void setOnItemMapClickLitener(OnItemMapClickLitener mOnItemMapClickLitener) {
        this.mOnItemMapClickLitener = mOnItemMapClickLitener;
    }

    //item监听
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    //地图点击监听
    public interface OnItemMapClickLitener {
        void onItemClick(View view, int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView boilernumber;
        TextView price;

        public MyViewHolder(View itemView) {
            super(itemView);
            boilernumber = (TextView) itemView.findViewById(R.id.boilernumber);
            price = (TextView) itemView.findViewById(R.id.price);
        }
    }


}
