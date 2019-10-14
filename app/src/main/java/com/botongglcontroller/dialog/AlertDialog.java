package com.botongglcontroller.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.botongglcontroller.R;

public class AlertDialog {
    private static AlertDialog alertDialog;
    private Context context;
    private Dialog dialog;
    private LinearLayout lLayout_bg, layout_first, layout_second;
    private TextView txt_title, txt_first, txt_first2, txt_second;
    private TextView txt_msg;
    private EditText edt_msg, edt_second;
    private Button btn_neg;
    private Button btn_pos;
    private ImageView img_line, img_title;
    private Display display;
    private boolean showTitle = false;
    private boolean showMsg = false;
    private boolean showPosBtn = false;
    private boolean showNegBtn = false;
    private boolean showEdtmsg = false;
    private boolean showImageTitle = false;
    private boolean showfirst = false;
    private boolean showSecond = false;

    public AlertDialog(Context context) {
        alertDialog = this;
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public AlertDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(R.layout.view_alertdialog, null);

        // 获取自定义Dialog布局中的控件

        edt_msg = (EditText) view.findViewById(R.id.edt_msg);
        edt_msg.setVisibility(View.GONE);
        img_title = (ImageView) view.findViewById(R.id.img);
        img_title.setVisibility(View.GONE);
        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_title.setVisibility(View.GONE);
        txt_msg = (TextView) view.findViewById(R.id.txt_msg);
        txt_msg.setVisibility(View.GONE);
        btn_neg = (Button) view.findViewById(R.id.btn_neg);
        btn_neg.setVisibility(View.GONE);
        btn_pos = (Button) view.findViewById(R.id.btn_pos);
        btn_pos.setVisibility(View.GONE);
        img_line = (ImageView) view.findViewById(R.id.img_line);
        img_line.setVisibility(View.GONE);

        layout_first = (LinearLayout) view.findViewById(R.id.ll_first);
        layout_first.setVisibility(View.GONE);
        txt_first = (TextView) view.findViewById(R.id.txt_first);
//		txt_first.setVisibility(View.GONE);
        txt_first2 = (TextView) view.findViewById(R.id.txt_first2);
//		txt_first2.setVisibility(View.GONE);
        layout_second = (LinearLayout) view.findViewById(R.id.ll_secod);
        layout_second.setVisibility(View.GONE);
        txt_second = (TextView) view.findViewById(R.id.txt_second);
//		txt_second.setVisibility(View.GONE);
        edt_second = (EditText) view.findViewById(R.id.edt_second);
//		edt_second.setVisibility(View.GONE);


        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);

        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(
                new FrameLayout.LayoutParams((int) (display.getWidth() * 0.85), LinearLayout.LayoutParams.WRAP_CONTENT));

        return this;
    }

    public AlertDialog setView(View view) {
        dialog.setContentView(view);

        return this;
    }

    public AlertDialog setFirst(String text1, String text2) {
        showfirst = true;
        if ("".equals(text1)) {
            txt_first.setText("标题");
        } else {
            txt_first.setText(text1);
        }
        if ("".equals(text2)) {
            txt_first2.setText("标题");
        } else {
            txt_first2.setText(text2);
        }
        return this;
    }

    public AlertDialog setTitle(String title) {
        showTitle = true;
        if ("".equals(title)) {
            txt_title.setText("标题");
        } else {
            txt_title.setText(title);
        }
        return this;
    }

    public AlertDialog setImagetitle(int drawable) {
        showImageTitle = true;
        if ("".equals(drawable)) {
            img_title.setImageResource(drawable);
        } else {
            img_title.setImageResource(drawable);
        }
        return this;
    }

    public AlertDialog setEdtmsg(String edtmsg) {
        showEdtmsg = true;
        if (edtmsg.equals("")) {
            edt_msg.setHint("请输入修改的值！");
        } else {
            edt_msg.setHint(edtmsg);
        }
        return this;
    }

    public String getWifiname() {
        return txt_first2.getText().toString();
    }

    public String getSecond() {
        return edt_second.getText().toString();
    }

    public AlertDialog setSecond(String text1) {
        showSecond = true;
        if ("".equals(text1)) {
            txt_second.setText("标题");
        } else {
            txt_second.setText(text1);
        }

        return this;
    }

    public AlertDialog setMsg(String msg) {
        showMsg = true;
        if ("".equals(msg)) {
            txt_msg.setText("内容");
        } else {
            txt_msg.setText(msg);
        }
        return this;
    }

    public AlertDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public AlertDialog setPositiveButton(String text, final View.OnClickListener listener) {
        showPosBtn = true;
        if ("".equals(text)) {
            btn_pos.setText("确定");
        } else {
            btn_pos.setText(text);
        }
        btn_pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    public AlertDialog setNegativeButton(String text, final View.OnClickListener listener) {
        showNegBtn = true;
        if ("".equals(text)) {
            btn_neg.setText("取消");
        } else {
            btn_neg.setText(text);
        }
        btn_neg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    private void setLayout() {
        if (!showTitle && !showMsg) {
            txt_title.setText("提示");
            txt_title.setVisibility(View.VISIBLE);
        }

        if (showTitle) {
            txt_title.setVisibility(View.VISIBLE);
        }
        if (showfirst) {
            layout_first.setVisibility(View.VISIBLE);
        }
        if (showSecond) {
            layout_second.setVisibility(View.VISIBLE);
        }
        if (showEdtmsg) {
            edt_msg.setVisibility(View.VISIBLE);
        }
        if (showImageTitle) {
            img_title.setVisibility(View.VISIBLE);
        }

        if (showMsg) {
            txt_msg.setVisibility(View.VISIBLE);
        }

        if (!showPosBtn && !showNegBtn) {
            btn_pos.setText("确定");
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
            btn_pos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        if (showPosBtn && showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alertdialog_right_selector);
            btn_neg.setVisibility(View.VISIBLE);
            btn_neg.setBackgroundResource(R.drawable.alertdialog_left_selector);
            img_line.setVisibility(View.VISIBLE);
        }

        if (showPosBtn && !showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
        }

        if (!showPosBtn && showNegBtn) {
            btn_neg.setVisibility(View.VISIBLE);
            btn_neg.setBackgroundResource(R.drawable.alertdialog_single_selector);
        }
    }

    public void show() {
        setLayout();
        dialog.show();
    }

}