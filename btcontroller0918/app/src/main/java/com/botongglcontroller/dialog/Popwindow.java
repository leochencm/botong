package com.botongglcontroller.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.botongglcontroller.R;

/**
 * Created by hasee on 2017/2/10.
 */

public class Popwindow extends PopupWindow {
    private static final int TAKE_PICTURE = 0x000001;

    public Popwindow(final Activity context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.choosetype, null);

        this.setAnimationStyle(R.style.mypopwindow_anim_style);
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        ColorDrawable dw = new ColorDrawable(0x00000000);

        backgroundAlpha(context, 0.5f);
        this.setBackgroundDrawable(dw);


        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setContentView(view);
        this.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                backgroundAlpha(context, 1f);

            }
        });

        Button bt1 = (Button) view.findViewById(R.id.btn_linkguolu);
        Button bt2 = (Button) view.findViewById(R.id.btn_unbindguolu);


        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });
    }



    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow()
                .addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }
}
