package com.botongglcontroller.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.botongglcontroller.MyApplication;
import com.botongglcontroller.utils.NetUtil;
import com.botongglcontroller.beans.BoilersName;
import com.botongglcontroller.interfaces.Devices;
import com.botongglcontroller.socket.SocketMessage;

import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasee on 2017/2/18.
 */

public class SocketService extends Service {

    public static final String TAG = "SocketService";
    static SocketMessage socketMessage;
    public Devices devices;
    public Socket socket;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == 0x01) {
                Log.i(TAG, "已连接");
//                socket_state.setText("已连接");
            } else if (msg.arg1 == 0x02) {
//                get_message_tv.setText(msg.obj.toString());
                Log.i(TAG, msg.obj.toString());
            }
        }
    };
    /**
     * 接口
     */
    private SocketMessage.ReceiveData receiveData = new SocketMessage.ReceiveData() {

        @Override
        public void data(int[] data) {
            Log.i(TAG, "DATA");
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < data.length; i++) {
                stringBuilder.append("-" + data[i]);
            }
            Message msg = new Message();
            msg.obj = stringBuilder;
            msg.arg1 = 0x02;
            handler.sendMessage(msg);
        }

        @Override
        public void statesocket() {
            //子县城
            Message message = new Message();
            message.arg1 = 0x01;
            handler.sendMessage(message);
        }
    };
    //获取全部参数
    private Devices.alldata mymyalldata = new Devices.alldata() {
        @Override
        public void data(String UID, String[] data) {
            Log.i(TAG, "全部回掉成功+UID：" + UID);
            List<BoilersName> list = new ArrayList<BoilersName>();
            for (int i = 0; i < data.length; i++) {
                BoilersName name = new BoilersName();
                name.setValue(String.valueOf(data[i]));
                name.setParakey(String.valueOf("parameter" + (i + 1)));
                list.add(name);
            }
//            Boilers boilers = new Boilers();
//            boilers.serialnumber = "31FFD9054259333510761843";
//            String[] str = mg.querytenews(boilers);
//            Log.i("showenable", str[1]);

            Intent intent = new Intent("com.broadcast.mymyalldata");
            intent.putExtra("serialnumber", "31FFD9054259333510761843");
            intent.putExtra("list", (Serializable) list);
            intent.putExtra("UID", UID);
            sendBroadcast(intent);
        }
    };
    /**
     * 设置接口
     */
    //锁
    private Devices.lock lock = new Devices.lock() {
        @Override
        public void lock(String UID, int[] data) {
            Log.i(TAG, "锁机+UID：" + UID + data[0]);
            Intent intent = new Intent("com.broadcast.lock");
            intent.putExtra("lockstate", String.valueOf(data[0]));
            intent.putExtra("lockdate", String.valueOf(data[1]));
            intent.putExtra("isopen", String.valueOf(data[2]));
            intent.putExtra("isoperation", String.valueOf(data[3]));
            intent.putExtra("isshoudong", String.valueOf(data[4]));
            intent.putExtra("state", String.valueOf(data[5]));

            sendBroadcast(intent);
        }
    };
    /**
     * 手动
     */

    private Devices.shoudong shoudong = new Devices.shoudong() {

        @Override
        public void shoudong(String UID, boolean state) {
            String date = "";
            if (state == true) {
                Log.i(TAG, "设置手动成功");
                date = "成功";
            } else {
                date = "失败";
                Log.i(TAG, "设置手动失败");
            }
            Intent intent = new Intent("com.broadcast.shoudong");
            intent.putExtra("state", date);
            sendBroadcast(intent);

        }
    };
    //锁机限制天数
    private Devices.lockMachineDays lockMachineDays = new Devices.lockMachineDays() {
        @Override
        public void lockMachineDays(String UID, boolean state) {
            String date = "";
            if (state == true) {
                Log.i(TAG, "设置锁机宽限天数成功");
                date = "成功";
            } else {
                date = "失败";
                Log.i(TAG, "设置锁机宽限天数失败");
            }
            Intent intent = new Intent("com.broadcast.lockMachineDays");
            intent.putExtra("state", date);
            sendBroadcast(intent);
        }
    };
    //设置网络密码
    private Devices.settingNetworkPassword settingNetworkPassword = new Devices.settingNetworkPassword() {

        @Override
        public void settingNetworkPassword(String UID, boolean state) {
            String date = "";
            if (state == true) {
                Log.i(TAG, "设置网络密码成功");
                date = "成功";
            } else {
                date = "失败";
                Log.i(TAG, "设置网络密码失败");
            }
            Intent intent = new Intent("com.broadcast.settingNetworkPassword");
            intent.putExtra("state", date);
            sendBroadcast(intent);
        }
    };
    //保存网络参数
    private Devices.saveNetwork saveNetwork = new Devices.saveNetwork() {
        @Override
        public void saveNetWork(String UID, int state) {
            Log.i(TAG, "保存网络参数成功" + state);
            Intent intent = new Intent("com.broadcast.saveNetwork");
            intent.putExtra("state", state);
            sendBroadcast(intent);
        }
    };
    //切换工作环境
    private Devices.changeNetwork changeNetwork = new Devices.changeNetwork() {

        @Override
        public void changeNetwork(String UID, boolean state) {
            String date = "";
            if (state == true) {
                Log.i(TAG, "切换工作环境成功");
                date = "成功";
            } else {
                date = "失败";
                Log.i(TAG, "切换工作环境失败");
            }
            Intent intent = new Intent("com.broadcast.changeNetwork");
            intent.putExtra("state", date);
            sendBroadcast(intent);
        }
    };
    //锁机开关指令
    private Devices.lockMachine lockMachine = new Devices.lockMachine() {


        @Override
        public void lockMachine(String UID, boolean state) {
            String date = "";
            if (state == true) {
                Log.i(TAG, "设置锁机开关成功");
                date = "成功";
            } else {
                date = "失败";
                Log.i(TAG, "设置锁机开关失败");
            }
            Intent intent = new Intent("com.broadcast.lockMachine");
            intent.putExtra("state", date);
            sendBroadcast(intent);
        }
    };
    //锁机开关指令时间
    private Devices.lockMachinetime lockMachinetime = new Devices.lockMachinetime() {


        @Override
        public void lockMachinetime(String UID, boolean state) {
            String date = "";
            if (state == true) {
                Log.i(TAG, "设置锁机开关成功");
                date = "成功";
            } else {
                date = "失败";
                Log.i(TAG, "设置锁机开关失败");
            }
            Intent intent = new Intent("com.broadcast.lockMachinetime");
            intent.putExtra("state", date);
            sendBroadcast(intent);
        }
    };
    //开关机指令
    private Devices.openMachine openMachine = new Devices.openMachine() {


        @Override
        public void openMachine(String UID, boolean state) {
            String date = "";
            if (state == true) {
                Log.i(TAG, "设置开关机成功");
                date = "成功";
            } else {
                date = "失败";
                Log.i(TAG, "设置开关机失败");
            }
            Intent intent = new Intent("com.broadcast.openMachine");
            intent.putExtra("state", date);
            sendBroadcast(intent);
        }


    };
    //永久开机指令
    private Devices.foreverOpen foreverOpen = new Devices.foreverOpen() {


        @Override
        public void foreverOpen(String UID, boolean state) {
            String date = "";
            if (state == true) {
                Log.i(TAG, "设置永久开机指令成功");
                date = "成功";
            } else {
                date = "失败";
                Log.i(TAG, "设置永久开机指令失败");
            }
            Intent intent = new Intent("com.broadcast.foreverOpen");
            intent.putExtra("state", date);
            sendBroadcast(intent);
        }
    };
    //永久开机指令
    private Devices.foreverOpenAdmin foreverOpenAdmin = new Devices.foreverOpenAdmin() {


        @Override
        public void foreverOpenAdmin(String UID, boolean state) {
            String date = "";
            if (state == true) {
                Log.i(TAG, "设置永久开机指令Admin成功");
                date = "成功";
            } else {
                date = "失败";
                Log.i(TAG, "设置永久开机指令Admin失败");
            }
            Intent intent = new Intent("com.broadcast.foreverOpenAdmin");
            intent.putExtra("state", date);
            sendBroadcast(intent);
        }
    };
    //修改开关量
    private Devices.changeSwichState changeSwichState = new Devices.changeSwichState() {


        @Override
        public void changeSwichState(String UID, boolean state) {
            String date = "";
            if (state == true) {
                Log.i(TAG, "修改开关成功");
                date = "成功";
            } else {
                date = "失败";
                Log.i(TAG, "修改开关失败");
            }
            Intent intent = new Intent("com.broadcast.changeSwichState");
            intent.putExtra("state", date);
            sendBroadcast(intent);
        }
    };
    //修改非开关量
    private Devices.notSwichStateChange notSwichStateChange = new Devices.notSwichStateChange() {


        @Override
        public void notSwichStateChange(String UID, boolean state) {
            String date = "";
            if (state == true) {
                Log.i(TAG, "设修改非开关成功");
                date = "成功";
            } else {
                date = "失败";
                Log.i(TAG, "修改非开关失败");
            }
            Intent intent = new Intent("com.broadcast.notSwichStateChange");
            intent.putExtra("state", date);
            sendBroadcast(intent);
        }
    };
    //消报警
    private Devices.clearalert cancelalert = new Devices.clearalert() {

        @Override
        public void clearalert(String UID, int[] data) {
            Log.i(TAG, "消报警");
            Intent intent = new Intent("com.broadcast.cancelalertState");
            intent.putExtra("state", data[0]);
            sendBroadcast(intent);
            devices.cancelAlert(1);
        }
    };
    //报警
    private Devices.alertState alertState = new Devices.alertState() {
        @Override
        public void alertState(String UID, int[] data) {
            int position;
            Intent intent = new Intent("com.broadcast.alertState");
            intent.putExtra("state", data[0]);
            position = data[1];
            if (position == 1) {
                intent.putExtra("position", data[2]);
            } else if (position == 2) {
                intent.putExtra("position", data[2] + 16);
            } else if (position == 3) {
                intent.putExtra("position", data[2] + 32);
            } else if (position > 3) {
                intent.putExtra("position", position + 48 - 3);
//                intent.putExtra("date",String.valueOf((data[1]<< 8) + data[2]));
            }
            Log.i(TAG, "设置报警" + position);

            sendBroadcast(intent);
        }
    };
    //初始化时间
    private Devices.timeInitialization timeInitialization = new Devices.timeInitialization() {


        @Override
        public void timeInitialization(String UID, boolean state) {
            String date = "";
            if (state == true) {
                Log.i(TAG, "设置初始化时间成功");
                date = "成功";
            } else {
                date = "失败";
                Log.i(TAG, "设置初始化时间失败");
            }
            Intent intent = new Intent("com.broadcast.timeInitialization");
            intent.putExtra("state", date);
            sendBroadcast(intent);
        }
    };
    //标定温度
    private Devices.temperatureCalibration temperatureCalibration = new Devices.temperatureCalibration() {


        @Override
        public void temperatureCalibration(String UID, int[] data) {
            String date = "";
            if (data[0] == 0x01) {
                Log.i(TAG, "标定温度成功");
                date = "成功";
            } else if (data[0] == 0x00) {
                Log.i(TAG, "标定温度失败");
                date = "失败";
            }
            Intent intent = new Intent("com.broadcast.temperatureCalibration");
            intent.putExtra("state", date);
            sendBroadcast(intent);
        }


    };
    //设备已被锁通知
    private Devices.locked locked = new Devices.locked() {


        @Override
        public void locked(String UID, int[] data) {
            Log.i(TAG, "设备已被锁通知" + data[0]);
            String date = null;
            if (data[0] == 0x01) {
                date = "设备已锁，需要解锁后才能操作";
                Intent intent = new Intent("com.broadcast.locked");
                intent.putExtra("state", date);
                sendBroadcast(intent);
            }

        }


    };
    //9.设备运行指令（用户）
    private Devices.deviceOperation deviceOperation = new Devices.deviceOperation() {


        @Override
        public void deviceOperation(String UID, boolean state) {
            String date = "";
            if (state == true) {
                Log.i(TAG, "设置设备运行指令成功");
                date = "成功";
            } else {
                date = "失败";
                Log.i(TAG, "设置设备运行指令失败");
            }
            Intent intent = new Intent("com.broadcast.deviceOperation");
            intent.putExtra("state", date);
            sendBroadcast(intent);
        }
    };
    //.远程一键开机锁机指令（管理员后台操作）
    private Devices.remoteBootlock remoteBootlock = new Devices.remoteBootlock() {
        @Override
        public void remoteBootlock(String UID, boolean state) {
            String date = "";
            if (state == true) {
                Log.i(TAG, "设置远程一键开机锁机指令成功");
                date = "成功";
            } else {
                date = "失败";
                Log.i(TAG, "设置远程一键开机锁机指令失败");
            }
            Intent intent = new Intent("com.broadcast.remoteBootlock");
            intent.putExtra("state", date);
            sendBroadcast(intent);
        }

    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MsgBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("SocketService", "onCreate() executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("SocketService", "onStartCommand() executed");
        Log.i("getLocalIP", NetUtil.getLocalIP());
        //获取当前路由器IP地址
        String IP = NetUtil.getLocalIP().substring(0, NetUtil.getLocalIP().length() - 1) + "1";
        Log.i("链接的WIFIID", "" + IP);
        //连接scket
        socketMessage = new SocketMessage("192.168.4.1", 8899, receiveData);
        devices = new Devices(socketMessage);
        socketMessage.Setlock(lock);
        socketMessage.Setalldata(mymyalldata);
        socketMessage.SetlockMachineDays(lockMachineDays);
        socketMessage.SetsettingNetworkPassword(settingNetworkPassword);
        socketMessage.SetsaveNetwork(saveNetwork);
        socketMessage.SetchangeNetwork(changeNetwork);
        socketMessage.SetlockMachine(lockMachine);
        socketMessage.SetlockMachinetime(lockMachinetime);
        socketMessage.SetforeverOpen(foreverOpen);
        socketMessage.SetforeverOpenAdmin(foreverOpenAdmin);
        socketMessage.SetchangeSwichState(changeSwichState);
        socketMessage.SetnotSwichStateChange(notSwichStateChange);
        socketMessage.SetalertState(alertState);
        socketMessage.SetCancelalertState(cancelalert);
        socketMessage.SetTimeInitialization(timeInitialization);
        socketMessage.SetTemperatureCalibration(temperatureCalibration);
        socketMessage.Setlocked(locked);
        socketMessage.SetopenMachine(openMachine);
        socketMessage.SetdeviceOperation(deviceOperation);
        socketMessage.SetremoteBootlock(remoteBootlock);
        socketMessage.Setshoudong(shoudong);
        getSocketState();
        return super.onStartCommand(intent, flags, startId);
    }

    public SocketMessage getSocketMessage() {

        if (socketMessage != null) {
            Log.i("SocketMessage", "非空");
        } else {
            Log.i("SocketMessage", "空");
        }
        return socketMessage;
    }

    public Devices getDevice() {
        devices = new Devices(socketMessage);
        if (devices != null) {
            Log.i("devices22", "非空");
        } else {
            Log.i("devices22", "空");
        }
        return devices;
    }

    public boolean getSocketState() {
        boolean isconnect;
        if (socketMessage != null) {
            Log.i("SocketMessage", "非空");
            isconnect = true;
            MyApplication.sp.setsocketState(true);
        } else {
            Log.i("SocketMessage", "空");
            isconnect = false;
            MyApplication.sp.setsocketState(false);
        }
        return isconnect;
    }

    public Socket getSocket() {
        if (!(socketMessage.getSocket() == null) && socketMessage.getSocket().isConnected()) {
            Log.i("Socket", "链接");
            socket = socketMessage.getSocket();
        } else {
            Log.i("Socket", "未连接");
        }
        return socket;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("SocketService", "onDestroy() executed");
    }

    public class MsgBinder extends Binder {
        /**
         * 获取当前Service的实例
         *
         * @return
         */
        public SocketService getService() {

            return SocketService.this;
        }
    }


}
