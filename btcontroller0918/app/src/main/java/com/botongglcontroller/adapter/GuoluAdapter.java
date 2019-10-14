package com.botongglcontroller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.botongglcontroller.MyApplication;
import com.botongglcontroller.R;
import com.botongglcontroller.utils.GlideImageLoader;
import com.botongglcontroller.beans.Boilers;
import com.botongglcontroller.dialog.Popwindow;

import java.util.List;

/**
 * Created by hasee on 2016/12/26.
 */

public class GuoluAdapter extends BaseAdapter {
    private List<Boilers> list = null;
    private LayoutInflater inflater = null;
    protected Context context;
    private Popwindow pop = null;

    public GuoluAdapter(Context context, List<Boilers> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void updateView(Context context, List<Boilers> list) {
        this.context = context;
        this.list = list;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
            view = inflater.inflate(R.layout.guolu_list, viewGroup, false);
            viewHolder.serialnumber = (TextView) view.findViewById(R.id.txt_serialnumber);
            viewHolder.workstate = (TextView) view.findViewById(R.id.txt_workstate);
            viewHolder.more = (LinearLayout) view.findViewById(R.id.ll_guolu);
            viewHolder.image = (ImageView) view.findViewById(R.id.image);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.serialnumber.setText(list.get(i).getSerialnumber());
        if(MyApplication.sp.GetNetWorkState().equals("2")||MyApplication.sp.GetNetWorkState().equals("-1")){
            viewHolder.workstate.setText("不在线");
        }else {
            if (list.get(i).getConnstate().equals("0") || list.get(i).getConnstate().equals("")) {
                viewHolder.workstate.setText("不在线");
            } else {
                viewHolder.workstate.setText("在线");
            }
        }

        //new GlideImageLoader().displayImage(context, list.get(i).getImage(), viewHolder.image);
        return view;

    }


    class ViewHolder {
        ImageView image;
        LinearLayout more;
        TextView serialnumber;
        TextView workstate;
    }
}
