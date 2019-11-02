package com.botongglcontroller.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.botongglcontroller.R;
import com.botongglcontroller.Service.SocketService;
import com.botongglcontroller.beans.BoilersPara;
import com.botongglcontroller.beans.String2Date;
import com.botongglcontroller.onenetdb.Sendcommand;
import com.botongglcontroller.beans.BoilersName;
//import com.btcontroller.beans.Name;
import com.botongglcontroller.interfaces.Devices;
import com.botongglcontroller.socket.SocketMessage;
import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetApiCallback;
import com.chinamobile.iot.onenet.module.Command;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
//import com.kyleduo.switchbutton.SwitchButton;

import net.lemonsoft.lemonbubble.LemonBubble;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hasee on 2016/12/26.
 */

public class GuoluSeetingAdapter extends BaseAdapter {
    static String toasttrue = "修改成功！";
    static String toastfalse = "修改失败！";
    static ProgressDialog progressDialg = null;
    protected Context context;
    boolean isconnect = false;
    MyThread mytr;
    String mDevice;
    String apikey;
    //        BroadcastReceiver changeSwichState;
    SocketMessage socketMessage = new SocketService().getSocketMessage();
    BroadcastReceiver changeSwichState = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isconnect = true;
            hideLoadingDialog();
            if (intent.getStringExtra("state").equals("成功")) {
                LemonBubble.showRight(context, "修改开关成功", 2000);
            } else if (intent.getStringExtra("state").equals("失败")) {
                LemonBubble.showError(context, "修改开关失败", 2000);
                Intent intent1 = new Intent("com.broadcast.failure");
                context.sendBroadcast(intent1);
            }
        }
    };
    BroadcastReceiver refresh = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("刷新", "刷新");

            //getCnshu("refresh");
            // getDataStreams("refresh");

        }
    };
    private ArrayList<BoilersPara> list = null;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 要做的事情
            if (msg.what == 1) {

                hideLoadingDialog();
                LemonBubble.showError(context, "链接无线超时，请检查网络", 1000);
            } else if (msg.what == 2) {
                hideLoadingDialog();
            } else if (msg.what == 3) {
                hideLoadingDialog();
                updateView(context, list, mDevice, apikey);
                LemonBubble.showError(context, toastfalse, 1000);
            }

            super.handleMessage(msg);
        }
    };
    private List<BoilersName> list3 = new ArrayList<BoilersName>();
    private List<BoilersName> list2 = null;
    private LayoutInflater inflater = null;
    private Devices devices = new Devices(new SocketService().getSocketMessage());

    //    CheckBox btn;
    public GuoluSeetingAdapter(Context context, ArrayList<BoilersPara> list1, String mDevice, String apikey) {
        this.list = list1;
        this.apikey = apikey;
        this.mDevice = mDevice;
        this.context = context;
        inflater = LayoutInflater.from(context);
//        for (int i = 0; i < list1.size(); i++) {
//            if (list1.get(i).getVisiable().equals("1")) {
//                this.list.add(list1.get(i));
//            }
//        }
        //      IntentFilter filter = new IntentFilter("com.broadcast.refresh");
        //     context.registerReceiver(refresh, filter);

    }

    public void updateView(Context context, ArrayList<BoilersPara> list1, String mDevice, String apikey) {
        this.list = list1;
        this.mDevice = mDevice;
        this.apikey = apikey;
        this.context = context;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
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
            view = inflater.inflate(R.layout.guolusetting_list, viewGroup, false);
            viewHolder.parasname = (TextView) view.findViewById(R.id.txt_para);
//            viewHolder.image = (ImageView) view.findViewById(R.id.img_para);
            viewHolder.para = (TextView) view.findViewById(R.id.txt_num);
            viewHolder.paraunit = (TextView) view.findViewById(R.id.paraunit);
            viewHolder.num = (LinearLayout) view.findViewById(R.id.ll_num);
//           viewHolder.btn = (CheckBox) view.findViewById(R.id.btn_para);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        // 控件开关
//        final SwitchButton btn = (SwitchButton) view.findViewById(R.id.btn_para);
//        btn.setThumbSize(90,90);
//        btn.transform = CGAffineTransformMakeScale(0.75, 0.75);


        final CheckBox btn = view.findViewById(R.id.btn_para);

        // if (MyApplication.sp.Getisshoudong().equals("0")) {
        //     btn.setEnabled(false);
        // } else {
        //     btn.setEnabled(true);
        // }
        if (this.list.get(i).getKind().equals("4") && list.get(i).getVisiable().equals("1")) {
            viewHolder.num.setVisibility(View.GONE);
            btn.setVisibility(View.VISIBLE);
            if (list.get(i).getValue().equals("0")) {
                btn.setChecked(false);
                Log.i("GUANBI", "0");
            } else {
                btn.setChecked(true);
                Log.i("KAIQI", "255");
            }
        } else if (list.get(i).getKind().equals("3") && list.get(i).getVisiable().equals("1")) {
            viewHolder.num.setVisibility(View.VISIBLE);
            btn.setVisibility(View.GONE);
            viewHolder.para.setText(list.get(i).getValue());
            viewHolder.paraunit.setText(list.get(i).getUnit());

        }
        viewHolder.parasname.setText(list.get(i).getName());

        btn.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {

                                       showLoadingDialog();
                                       int index = Integer.parseInt(list.get(i).getAddr_int()) - 16384;
                                       Intent intent = new Intent("com.broadcast.issend");
                                       intent.putExtra("issend", true);
                                       context.sendBroadcast(intent);
                                       int x = btn.isChecked() ? 255 : 0;
                                       String xx = "" + x;
                                       sendCommand(Sendcommand.deviceControl(index, x), xx, list.get(i).getAddr_int(), toasttrue, toastfalse);
                                   }
                               }
        );
        return view;
    }

    private void sendCommand(byte[] by, final String select, final String str, final String toasttrue, final String toastfalse) {
        OneNetApi.sendCmdToDevice(
                mDevice,
                true,
                4000,
                Command.CommandType.TYPE_CMD_REQ,
                by,
                new OneNetApiCallback() {
                    @Override
                    public void onSuccess(String response) {
                        JsonObject resp = new JsonParser().parse(response).getAsJsonObject();
                        int errno = resp.get("errno").getAsInt();
                        if (0 == errno) {
                            mytr = new MyThread();
                            mytr.setStr(str);
                            mytr.setToasttrue(toasttrue);
                            mytr.setToastfalse(toastfalse);
                            mytr.select = select;
                            Date date = new Date(System.currentTimeMillis());
                            mytr.setDt(date.getTime());
                            new Thread(mytr).start();
                            //getDataPoint(str,str);
                        }

                    }

                    @Override
                    public void onFailed(Exception e) {
                        e.printStackTrace();
                    }
                });

    }

    private void getDataPoint(final String streamid, final long dt, final String select) {
        //final boolean[] succ=new boolean[1];
        try {
            OneNetApi.querySingleDataStream(mDevice, streamid, new OneNetApiCallback() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject resp = new JSONObject(response);
                        int errno = resp.getInt("errno");
                        if (0 == errno) {
                            JSONObject json = resp.getJSONObject("data");
                            try {
                                if (json.getString("current_value").equals(select)) {
                                    hideLoadingDialog();
                                    if (json.getString("current_value").equals(select)) {
                                        LemonBubble.showRight(context, toasttrue, 1000);
                                    } else {
                                        LemonBubble.showError(context, toastfalse, 1000);
                                    }
                                    Intent intent = new Intent("com.broadcast.issend");
                                    // intent.putExtra("addr_int",json.getString("current_value"));
                                    intent.putExtra("issend", false);
                                    context.sendBroadcast(intent);
                                    mytr.stop();


                                }
                            } catch (Exception e) {
                            }
                        }
                    } catch (JSONException e) {

                    }
                    mytr.setflag = true;
                }

                @Override
                public void onFailed(Exception e) {
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示加载Dialog
     */
    protected void showLoadingDialog() {
        if (progressDialg == null) {
            progressDialg = new ProgressDialog(context);
            progressDialg.setCancelable(false);
            progressDialg.setMessage("等待设备响应...");
        }
        try {
            progressDialg.show();
        } catch (Exception e) {
        }
    }

    /**
     * 隐藏加载Dialog
     */
    protected void hideLoadingDialog() {
        if (progressDialg != null) {
            progressDialg.dismiss();
            progressDialg = null;
        }
    }

    class ViewHolder {

        TextView parasname;
        TextView para;
        TextView paraunit;
        LinearLayout num;
        ImageView image;
//        CheckBox btn;


    }

    public class MyThread implements Runnable {
        String str, toasttrue, toastfalse;
        long dt;
        boolean _run = true;
        boolean setflag = true;
        String select = "0";

        public void setStr(String str) {
            this.str = str;
        }

        public void setDt(long dt) {
            this.dt = dt;
        }

        public void stop() {
            _run = false;
        }

        public void setToasttrue(String toasttrue) {
            this.toasttrue = toasttrue;
        }

        public void setToastfalse(String toastfalse) {
            this.toastfalse = toastfalse;
        }

        @Override
        public void run() {

            int count = 0;
            while (_run) {
                try {
                    if (_run) {
                        Thread.sleep(2000);// 线程暂停10秒，单位毫秒
                        if (setflag && _run) {
                            getDataPoint(str, dt, select);
                            setflag = false;
                        }
                    }
                    count++;

                    if (count > 9) {
                        hideLoadingDialog();
                        Message msg = new Message();
                        msg.obj = str;//message的内容
                        msg.what = 3;//指定message
                        handler.sendMessage(msg);//handler发送message
                        _run = false;
                    }
                    //progressDialogwating.incrementProgressBy(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
