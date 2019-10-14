package com.botongglcontroller.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.botongglcontroller.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by hasee on 2016/12/26.
 */

public class NewsAdapter extends BaseAdapter {
    protected Context context;
    ArrayList<Map<String, String>> addDatas = new ArrayList<Map<String, String>>();
    private LayoutInflater inflater = null;

    public NewsAdapter(Context context, ArrayList<Map<String, String>> addDatas) {
        this.context = context;
        this.addDatas = addDatas;
        inflater = LayoutInflater.from(context);
    }

    public void updateView(Context context, ArrayList<Map<String, String>> addDatas) {
        this.addDatas = addDatas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return addDatas == null ? 0 : addDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.news, viewGroup, false);
            viewHolder.date = (TextView) view.findViewById(R.id.txt_date);
            viewHolder.msg = (TextView) view.findViewById(R.id.txt_msg);
            viewHolder.title = (TextView) view.findViewById(R.id.title);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.title.setText(addDatas.get(position).get("title"));
        viewHolder.msg.setText(addDatas.get(position).get("msg"));
        viewHolder.date.setText(addDatas.get(position).get("date"));

        if (addDatas.get(position).get("isRead").equals("0")) {
            viewHolder.title.setTextColor(Color.parseColor("#000000"));
            viewHolder.msg.setTextColor(Color.parseColor("#000000"));
            viewHolder.date.setTextColor(Color.parseColor("#000000"));
        } else {
            viewHolder.title.setTextColor(Color.parseColor("#CCCCCC"));
            viewHolder.msg.setTextColor(Color.parseColor("#CCCCCC"));
            viewHolder.date.setTextColor(Color.parseColor("#CCCCCC"));
        }
        return view;
    }

    class ViewHolder {
        LinearLayout more;
        TextView msg;
        TextView date;
        TextView title;
    }
}
