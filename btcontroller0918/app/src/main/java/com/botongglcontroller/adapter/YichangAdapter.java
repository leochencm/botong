package com.botongglcontroller.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.botongglcontroller.R;
import com.botongglcontroller.beans.BoilersPara;

import java.util.ArrayList;

/**
 * Author jonathan
 * Created by Administrator on 2017/2/4.
 * Effect 异常列表数据适配器
 */

public class YichangAdapter extends BaseAdapter {
    private Context context = null;
    private LayoutInflater inflater;
    private ArrayList<BoilersPara> list = null;

    public YichangAdapter(Context context, ArrayList<BoilersPara> list) {
        this.context = context;
        inflater = null;
        inflater = LayoutInflater.from(context);
        this.list=list;

        for (int i = 0; i <list.size() ; i++) {
                Log.i("name", list.get(i).getName());
        }
    }

    public void updateView(Context context,ArrayList<BoilersPara> list) {
        this.list=list;
        this.context = context;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        convertView = inflater.inflate(R.layout.guolucanshu_yichang, null);
        TextView text= (TextView) convertView.findViewById(R.id.txt_paraname);
        text.setText(list.get(position).getName());
        return convertView;
    }
}

