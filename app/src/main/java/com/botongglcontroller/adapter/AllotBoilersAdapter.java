package com.botongglcontroller.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.botongglcontroller.R;
import com.botongglcontroller.layout.SwipeLayout;
import com.botongglcontroller.layout.SwipeLayoutManager;

import java.util.List;

/**
 * Created by xiaoyehai on 2016/12/1.
 */
public class AllotBoilersAdapter extends BaseAdapter implements SwipeLayout.OnSwipeStateChangeListener {

    private Context context;
    private List<String> list;
    private Handler handler;

    public AllotBoilersAdapter(Context context, List<String> list, Handler handler) {
        this.context = context;
        this.list = list;
        this.handler = handler;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_listview, null);
        }
        final ViewHolder holder = ViewHolder.getHolder(convertView);

        holder.tvName.setText(list.get(position));

        holder.swipeLayout.setTag(position);

        holder.swipeLayout.setOnSwipeStateChangeListener(this);

        //删除
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String sid=list.get(position);
                //list.remove(getItem(position));
                AllotBoilersAdapter.this.notifyDataSetChanged();
                SwipeLayoutManager.getInstance().closeCurrentLayout();
                SwipeLayoutManager.getInstance().clearCurrentLayout();
                Message message = new Message();
                message.what = 3;
                message.arg1 = position;
                Bundle bundle = new Bundle();
                bundle.putInt("sid_index", position);
                message.setData(bundle);
                handler.sendMessage(message);
                //Toast.makeText(context, sid+ "已删除", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    @Override
    public void onOpen(Object tag) {
        // Toast.makeText(context, "第" + (Integer) tag + "个打开", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClose(Object tag) {
        //Toast.makeText(context, "第" + (Integer) tag + "个关闭", Toast.LENGTH_SHORT).show();
    }

    private static class ViewHolder {
        private TextView tvName, tvDelete, tvZhiDing;
        private SwipeLayout swipeLayout;

        public ViewHolder(View convertView) {
            tvName = (TextView) convertView.findViewById(R.id.tv_name);
            tvDelete = (TextView) convertView.findViewById(R.id.tv_delete);
            swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipelayout);
        }

        public static ViewHolder getHolder(View convertView) {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }
}
