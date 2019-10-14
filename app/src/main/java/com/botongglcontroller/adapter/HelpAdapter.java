package com.botongglcontroller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.botongglcontroller.R;

import com.botongglcontroller.beans.Helper;

import java.util.List;

/**
 * 求助列表
 * Created by Administrator on 2017/10/14.
 */

public class HelpAdapter extends ArrayAdapter<Helper> {

    private int resourceId;

    public HelpAdapter(Context context, int textViewResourceId,
                       List<Helper> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        } else {
            view = convertView;
        }

        Helper helper = getItem(position);
        TextView contact_name = (TextView) view.findViewById(R.id.contact_name);
        TextView is_handler = (TextView) view.findViewById(R.id.is_handler);
        TextView submit_time = (TextView) view.findViewById(R.id.submit_time);
        TextView boilerNo = (TextView) view.findViewById(R.id.boilerNo);
        TextView question_des = (TextView) view.findViewById(R.id.question_des);
        TextView account = (TextView) view.findViewById(R.id.account);

        contact_name.setText(helper.getContactname());
        is_handler.setText(helper.getHandle().equals("1") ? "已处理" : "未处理");
        submit_time.setText(helper.getCreatedate());
        boilerNo.setText(helper.getBoilerid());
        question_des.setText(helper.getRemark());
        account.setText(helper.getUserid());

        return view;
    }

}
