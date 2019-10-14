package com.botongglcontroller.interfaces;

import android.view.View;

public interface IViewProvider<T> {
    int resLayout();

    void onBindView(View view, T itemData);

    void updateView(View itemView, boolean isSelected);
}
