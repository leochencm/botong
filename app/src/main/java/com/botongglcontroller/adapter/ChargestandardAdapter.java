package com.botongglcontroller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.botongglcontroller.R;
import com.botongglcontroller.beans.Standard;

import java.util.ArrayList;

/**
 * Created by maning on 16/6/22.
 */
public class ChargestandardAdapter extends RecyclerView.Adapter<ChargestandardAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Standard> mDatas;
    private LayoutInflater layoutInflater;
    private OnItemClickLitener mOnItemClickLitener;
    private OnItemMapClickLitener mOnItemMapClickLitener;

    public ChargestandardAdapter(Context context, ArrayList<Standard> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        layoutInflater = LayoutInflater.from(this.context);
    }

    public void setDatas(ArrayList<Standard> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = layoutInflater.inflate(R.layout.item_chargestandard, parent, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Standard standard = mDatas.get(position);
        holder.title.setText(standard.getName());
        holder.price.setText(standard.getMaintenance());

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

        TextView title;
        TextView price;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            price = (TextView) itemView.findViewById(R.id.price);
        }
    }


}
