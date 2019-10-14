package com.botongglcontroller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.botongglcontroller.R;
import com.botongglcontroller.configInfo.SuggestionInfo;

import java.util.List;

/**
 * Author jonathan
 * Created by Administrator on 2017/2/4.
 * Effect 意见列表数据适配器
 */

public class SuggestionAdapter extends BaseAdapter {
    private List<SuggestionInfo> suggestionsInfos;
    private Context context;

    public SuggestionAdapter(List<SuggestionInfo> suggestionsInfos, Context context) {
        this.suggestionsInfos = suggestionsInfos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return suggestionsInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return suggestionsInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = View.inflate(context, R.layout.suggestion_item, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.suggestion_item_name);
            viewHolder.message = (TextView) view.findViewById(R.id.suggestion_item_message);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.name.setText(suggestionsInfos.get(i).account + ":");
        viewHolder.message.setText(suggestionsInfos.get(i).suggestion);
        return view;
    }

    class ViewHolder {
        TextView name;
        TextView message;
    }
}
