package com.botongglcontroller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.botongglcontroller.R;
import com.botongglcontroller.beans.BoilersPara;
//import com.btcontroller.beans.Name;

import java.util.ArrayList;

/**
 * Created by hasee on 2016/12/26.
 */

public class GuoluCanshuAdapter extends BaseAdapter {
    private ArrayList<BoilersPara> list_now = null;
    private ArrayList<BoilersPara> list3 = new ArrayList<BoilersPara>();
    private LayoutInflater inflater = null;
    protected Context context;

    public GuoluCanshuAdapter(Context context, ArrayList<BoilersPara> listnow) {
        this.list_now = listnow;
        this.context = context;
        inflater = LayoutInflater.from(context);


        for (int i = 0; i < list_now.size(); i++) {
            int index=Integer.parseInt(list_now.get(i).getKind());
            if(index==1&&list_now.get(i).getVisiable().equals("1")){
                list3.add(list_now.get(i));
            }
        }
        for (int i = 0; i < list_now.size(); i++) {
            int index=Integer.parseInt(list_now.get(i).getKind());
            if(index==4&&list_now.get(i).getVisiable().equals("1")){
                list3.add(list_now.get(i));
            }
        }
        for (int i = 0; i < list_now.size(); i++) {
            int index=Integer.parseInt(list_now.get(i).getKind());
            if (index==2&&list_now.get(i).getVisiable().equals("1")) {
                list3.add(list_now.get(i));
            }
           // else if(index==3&&list_now.get(i).getVisiable().equals("1")){
           //     list3.add(list_now.get(i));
           // }
        }
    }
    public void updateView(Context context, ArrayList<BoilersPara> listnow) {
        this.list_now = listnow;
        this.context = context;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list3.size();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.guolucanshu_list, viewGroup, false);
            viewHolder.parasname = (TextView) view.findViewById(R.id.txt_paraname);
            viewHolder.state = (TextView) view.findViewById(R.id.state);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.parasname.setText(list3.get(i).getName());

       // for (int j = 0; j < list3.size(); j++) {
            if(Integer.parseInt(list3.get(i).getKind())==4)
            {
                if(list3.get(i).getValue().equals("0"))viewHolder.state.setText("关闭");
                else viewHolder.state.setText("打开");
            }
           else  if(Integer.parseInt(list3.get(i).getKind())==1)
            {
                if(list3.get(i).getValue().equals("0"))viewHolder.state.setText("断开");
                else viewHolder.state.setText("闭合");
            }
            else
                viewHolder. state.setText(list3.get(i).getValue());
       // }

        return view;
    }


    class ViewHolder {
        TextView parasname;
        TextView state;
    }
}
