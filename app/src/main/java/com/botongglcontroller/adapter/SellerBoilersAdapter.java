package com.botongglcontroller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.botongglcontroller.R;
import com.botongglcontroller.activity.SetSellerActivity;
import com.botongglcontroller.beans.SellerBoilers;
import com.botongglcontroller.layout.SlideLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SellerBoilersAdapter extends BaseAdapter {
    public SlideLayout slideLayout = null;
    private Context context;
    private ArrayList<SellerBoilers> datas;
    private Set<SlideLayout> sets = new HashSet();

    public SellerBoilersAdapter(Context context, ArrayList<SellerBoilers> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_setseller, null);
            viewHolder = new ViewHolder();
            viewHolder.sidView = (TextView) convertView.findViewById(R.id.content);
            viewHolder.deleteView = (TextView) convertView.findViewById(R.id.menu);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.sidView.setText(datas.get(position).getSid());

        viewHolder.sidView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "click " + ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
            }
        });
        final SellerBoilers myContent = datas.get(position);
        viewHolder.deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SlideLayout slideLayout = (SlideLayout) v.getParent();
                slideLayout.closeMenu(); //解决删除item后下一个item变成open状态问题
                datas.remove(myContent);
                notifyDataSetChanged();
            }
        });

        SlideLayout slideLayout = (SlideLayout) convertView;
        slideLayout.setOnStateChangeListener(new MyOnStateChangeListener());


        return convertView;
    }

    static class ViewHolder {
        public TextView sidView;
        public TextView deleteView;
    }

    class MyOnStateChangeListener implements SlideLayout.OnStateChangeListener {
        /**
         * 滑动后每次手势抬起保证只有一个item是open状态，加入sets集合中
         **/
        @Override
        public void onOpen(SlideLayout layout) {
            slideLayout = layout;
            if (sets.size() > 0) {
                for (SlideLayout s : sets) {
                    s.closeMenu();
                    sets.remove(s);
                }
            }
            sets.add(layout);
        }

        @Override
        public void onMove(SlideLayout layout) {
            if (slideLayout != null && slideLayout != layout) {
                slideLayout.closeMenu();
            }
        }

        @Override
        public void onClose(SlideLayout layout) {
            if (sets.size() > 0) {
                sets.remove(layout);
            }
            if (slideLayout == layout) {
                slideLayout = null;
            }
        }
    }
}
