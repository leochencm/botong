package com.botongglcontroller.view;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

@SuppressLint("NewApi")
public class CountView extends TextView {
    // 动画时长 ms
    int duration = 1500;
    String number;

    public CountView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void showNumberWithAnimation(String number) {
        // 修改number属性，会调用setNumber方法
        int count = Integer.valueOf(number);
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(this, "number", 0,
                count);
        objectAnimator.setDuration(duration);
        // 加速器，从慢到快到再到慢
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
        // setText(String.format("%1$07.2f", number));
        setText(number);
    }
}
