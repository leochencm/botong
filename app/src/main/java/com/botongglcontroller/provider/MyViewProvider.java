package com.botongglcontroller.provider;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.botongglcontroller.beans.DealerList;
import com.botongglcontroller.R;
import com.botongglcontroller.interfaces.IViewProvider;

public class MyViewProvider implements IViewProvider<DealerList> {
    @Override
    public int resLayout() {
        return R.layout.scroll_picker_default_item_layout;
    }

    @Override
    public void onBindView(View view, DealerList itemData) {
        TextView tv = view.findViewById(R.id.tv_content);
        tv.setText(itemData == null ? null : itemData.getDealercompany());
        view.setTag(itemData);
    }

    @Override
    public void updateView(View itemView, boolean isSelected) {
        TextView tv = itemView.findViewById(R.id.tv_content);
        tv.setTextColor(Color.parseColor(isSelected ? "#ffffff" : "#fff000"));
    }
}
