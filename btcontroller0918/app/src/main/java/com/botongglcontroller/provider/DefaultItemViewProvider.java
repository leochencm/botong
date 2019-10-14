package com.botongglcontroller.provider;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.botongglcontroller.R;
import com.botongglcontroller.interfaces.IViewProvider;

public class DefaultItemViewProvider implements IViewProvider<String> {
    @Override
    public int resLayout() {
        return R.layout.scroll_picker_default_item_layout;
    }

    @Override
    public void onBindView( View view,  String text) {
        TextView tv = view.findViewById(R.id.tv_content);
        tv.setText(text);
        view.setTag(text);
        tv.setTextSize(18);
    }

    @Override
    public void updateView( View itemView, boolean isSelected) {
        TextView tv = itemView.findViewById(R.id.tv_content);
        tv.setTextSize(isSelected ? 18 : 14);
        tv.setTextColor(Color.parseColor(isSelected ? "#ffffff" : "#ff0000"));
    }
}
