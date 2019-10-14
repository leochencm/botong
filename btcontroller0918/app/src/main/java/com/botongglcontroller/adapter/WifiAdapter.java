package com.botongglcontroller.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

//import com.example.hasee.boilerapplication.Utils.GlideImageLoader;
//import com.example.hasee.boilerapplication.beans.Boilers;
//import com.example.hasee.boilerapplication.dialog.Popwindow;

import java.util.List;

/**
 * Created by hasee on 2016/12/26.
 */

public class WifiAdapter extends BaseAdapter {

    LayoutInflater inflater;
    List<ScanResult> list;
    public WifiAdapter(Context context, List<ScanResult> list) {
        // TODO Auto-generated constructor stub
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
//        View view = null;
//        view = inflater.inflate(R.layout.item_wifi_list, null);
//        ScanResult scanResult = list.get(position);
//
//        TextView textView = (TextView) view.findViewById(R.id.wifiname);
//        textView.setText(scanResult.SSID);
//        TextView signalStrenth = (TextView) view.findViewById(R.id.signal_strenth);
//        signalStrenth.setText(String.valueOf(Math.abs(scanResult.level)));
//        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

  //      return view;
        return convertView;
    }

}


