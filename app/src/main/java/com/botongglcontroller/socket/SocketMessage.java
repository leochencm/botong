package com.botongglcontroller.socket;

import android.util.Log;

import com.botongglcontroller.MyApplication;
import com.botongglcontroller.utils.ToastUtil;
import com.botongglcontroller.check.CRC16;
import com.botongglcontroller.interfaces.Devices;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Author jonathan
 * Created by Administrator on 2017/1/17.
 * Effect socket客户端
 */

public class SocketMessage {
    static Socket socket;
    static DataOutputStream pw;
    /**
     * 读取
     */
    static DataInputStream dis;
    static InputStream is;
    private final String TAG = getClass().getName();
    private ReceiveData myreceiveData;
    //    private BufferedReader br;
    private CRC16 crc16 = new CRC16();
    /**
     * 接口
     */
    private Devices.settingNetworkPassword mySettingNetworkPassword;//设置网络密码
    private Devices.saveNetwork mySaveNetwork;//保存网络参数
    private Devices.changeNetwork myChangeNetwork;//切换工作环境
    private Devices.lockMachine myLockMachine;//锁机开关指令
    private Devices.lockMachinetime myLockMachinetime;//锁机开关指令
    private Devices.lockMachineDays mylockMachineDays;//锁机限制天数
    private Devices.foreverOpen myforeverOpen;//永久开机指令
    private Devices.changeSwichState mychangeSwichState;//修改开关量
    private Devices.notSwichStateChange mynotSwichStateChange;//修改非开关量
    private Devices.alldata myalldata;//获取全部参数
    private Devices.alertState myalertState;//报警
    private Devices.clearalert mycancelalertState;//消报警
    private Devices.lock mylock;//锁
    private Devices.locked mylocked;//锁
    private Devices.openMachine myopenMachine;//开关机
    private Devices.timeInitialization mytimeInitialization;//初始化时间
    private Devices.temperatureCalibration mytemperatureCalibration;//初始化时间
    private Devices.foreverOpenAdmin foreverOpenAdmin;//永久开机指令管理员
    private Devices.deviceOperation mydeviceOperation;//设备运行指令（用户）
    private Devices.remoteBootlock myremoteBootlock;//远程一键开机锁机指令
    private Devices.shoudong myshoudong;//远程一键开机锁机指令


    /**
     * 构造函数
     * 地址 、 端口 、 实现接口
     *
     * @param adress
     * @param pot
     * @param receiveData
     */
    public SocketMessage(final String adress, final int pot, ReceiveData receiveData) {
        //创建客户端Socket，指定服务器地址和端口
        if (!(socket == null)) {
            try {
//                socket.shutdownInput();
//                socket.shutdownOutput();
//                pw.close();
//                dis.close();
//                is.close();
                socket.close();
//                socket=null;
//                System.gc();
                Log.i("socket", "正在关闭socketSystem");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("socket", "socket开");
        } else {
            Log.i("socket", "socket关");
        }
        if (socket == null) {
            Log.i("是否还开着", "socket==null");
        } else {
            Log.i("是否还开着", "socket开");

        }
        myreceiveData = receiveData;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(adress, pot);
                    socket.setKeepAlive(true);
                    Log.d(TAG, "connectMessage->" + adress + "-" + pot + "-socket->" + socket + " - socket.isConnected();->" + socket.isConnected());
                    if (socket.isConnected()) {
                        Log.i(TAG, "连接->OK");
                        myreceiveData.statesocket();
                        pw = new DataOutputStream(socket.getOutputStream());//将输出流包装成打印流
                        socket.setSoTimeout(10000);//超时
                        //循环检测读取数据
                        while (socket.isConnected()) {
                            getData();
                        }
                    } else {
                        Log.i(TAG, "连接->失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (socket != null) socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 十进制转换为十六进制字符串
     *
     * @param
     * @return String 对应的十六进制字符串
     */
    public static String algorismToHEXString(int algorism, int maxLength) {
        String result = "";
        result = Integer.toHexString(algorism);

        if (result.length() % 2 == 1) {
            result = "0" + result;
        }
        return patchHexString(result.toUpperCase(), maxLength);
    }

    /*
     * HEX字符串前补0，主要用于长度位数不足。
     *
     * @param str String 需要补充长度的十六进制字符串
     *
     * @param maxLength int 补充后十六进制字符串的长度
     *
     * @return 补充结果
     */
    static public String patchHexString(String str, int maxLength) {
        String temp = "";
        for (int i = 0; i < maxLength - str.length(); i++) {
            temp = "0" + temp;
        }
        str = (temp + str).substring(0, maxLength);
        return str;
    }

    public Socket getSocket() {
        if (socket == null) {
            Log.i("socket", "kong");
        } else {
            Log.i("socket", "feikong");
        }
        return socket;
    }

    /**
     * 发送数据
     *
     * @param by
     */
    public void sendData(final byte[] by) {
        //获取输出流，向服务器端发送信息
        new Thread() {
            @Override
            public void run() {
                try {
                    if (socket != null && !socket.equals("") && socket.isConnected()) {
//              pw = new OutputStreamWriter(socket.getOutputStream());//将输出流包装成打印流
                        pw.write(by);

                        pw.flush();
                        Log.i(TAG, "发送");
                        //关闭资源
                    } else {
                        ToastUtil.showToast(MyApplication.getContext(), "发送失败,请检查是否链接");
                        Log.e(TAG, "socket空了");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
            }
        }.start();


    }

    public void getData1() {
        try {


        } catch (Exception e) {
            if (e.getMessage() != null) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    /**
     * 获取数据
     * 调用接口
     */
    public void getData() {
        try {
            //检测socket是否已连接
            if (socket.isConnected()) {
                //获取输入流，并读取服务器端的响应信息
//            InputStream is = socket.getInputStream();
//            BufferedReader br = new BufferedReader(new InputStreamReader(is));
                is = socket.getInputStream();//输入流
//            br = new BufferedReader(new InputStreamReader(is));
                dis = new DataInputStream(is);//读取输入流
                byte[] bytes = new byte[512];
                int leth = dis.read(bytes);//读取值，并返回长度
                if (leth > 0) {//读取的长度大于0时
                    Log.i(TAG, "-" + leth);
                    int[] dataInt = new int[leth];
                    for (int indext = 0; indext < leth; indext++) {
                        dataInt[indext] = (((int) bytes[indext]) & 0xff);
//                        System.out.println("我是客户端，服务器说：" + dataInt[indext]);
                    }
                    Log.i(TAG, "data=ok");
                    myreceiveData.data(dataInt);
                    //数据处理
                    handleData(dataInt);
                }
//              Log.i(TAG,"leth="+is.available());
//              int info;
//              while ((info = br.read())>= 0) {
//                    System.out.println("我是客户端，服务器说：" + (info & 0xff));
//              }
                //关闭资源
//            br.close();
//            is.close();
            } else {
                Log.e(TAG, "socet state:" + socket.isConnected());
            }
        } catch (Exception e) {
            if (e.getMessage() != null) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    /**
     * 数据
     *
     * @param data
     */
    public void handleData(int[] data) {
        //首先判断data长度大于六
        if (data.length > 6) {
            int[] check;
            //校验数据包-并获取返回
            check = inspect(data);
            //根据数据包判断校验是否正确
            if (check.length > 2) {
                Log.i(TAG, "check.length:" + check.length);
                //获得设备UID
                String UID = "";
                for (int i = 0; i < 12; i++) {
//                    UID = UID + check[2 + i] / 16 + check[2 + i] % 16;
                    UID = UID + algorismToHEXString(check[2 + i], 2);
                }
                Log.i(TAG, "功能码：" + check[14] + "UID:" + UID);

                Log.i(TAG, "前导码：" + check[1] + "UID:" + UID);
                //判断功能码
                switch (check[14]) {
                    case 0xa0://修改设备网络密码
                        int value_a0 = check[check.length - 1];
                        if (value_a0 == 0) {
                            Log.i(TAG, "执行了-修改设备网络密码-true");
                            if (mySettingNetworkPassword != null) {
                                mySettingNetworkPassword.settingNetworkPassword(UID, true);
                            }
                        } else if (value_a0 == 1) {
                            Log.i(TAG, "执行了-修改设备网络密码-false");
                            if (mySettingNetworkPassword != null) {
                                mySettingNetworkPassword.settingNetworkPassword(UID, false);
                            }
                        }
                        break;
                    case 0xa1://保存路由器名称和密码
                        Log.i(TAG, "执行了-保存路由器名称和密码-true");
                        int value_a1 = check[check.length - 1];
                        if (mySaveNetwork != null) {
                            mySaveNetwork.saveNetWork(UID, value_a1);
                        }

                        break;
                    case 0xa2://切换工作环境
                        int value_a2 = check[check.length - 1];
                        if (value_a2 == 0) {
                            Log.i(TAG, "执行了-切换工作环境-true");
                            if (myChangeNetwork != null) {
                                myChangeNetwork.changeNetwork(UID, true);
                            }
                        } else if (value_a2 == 1) {
                            Log.i(TAG, "执行了-切换工作环境-true");
                            if (myChangeNetwork != null) {
                                myChangeNetwork.changeNetwork(UID, false);
                            }
                        }
                        break;
                    case 0xa3://锁机开关指令
                        int value_a3 = check[check.length - 1];
                        if (value_a3 == 0) {
                            Log.i(TAG, "执行了-锁机开关指令-true");
                            if (myLockMachinetime != null) {
                                myLockMachinetime.lockMachinetime(UID, true);
                            }
                        } else if (value_a3 == 1) {
                            Log.i(TAG, "执行了-锁机开关指令-false");
                            if (myLockMachinetime != null) {
                                myLockMachinetime.lockMachinetime(UID, false);
                            }
                        }
                        break;
                    case 0xaa://手动开关指令
                        int value_aa = check[check.length - 1];

                        if (value_aa == 0) {
                            Log.i(TAG, "执行了-手动开关指令-true" + "value_aa" + value_aa);
                            if (myshoudong != null) {
                                myshoudong.shoudong(UID, true);
                            }
                        } else if (value_aa == 1) {
                            Log.i(TAG, "执行了-手动开关指令-false");
                            if (myshoudong != null) {
                                myshoudong.shoudong(UID, false);
                            }
                        }
                        break;
                    case 0xa9://锁机开关指令
                        int value_a9 = check[check.length - 1];
                        if (value_a9 == 0) {
                            Log.i(TAG, "执行了-锁机开关指令-true");
                            if (myLockMachine != null) {
                                myLockMachine.lockMachine(UID, true);
                            }
                        } else if (value_a9 == 1) {
                            Log.i(TAG, "执行了-锁机开关指令-false");
                            if (myLockMachine != null) {
                                myLockMachine.lockMachine(UID, false);
                            }
                        }
                        break;

                    case 0xa8://设备运行指令（用户）
                        int value_a8 = check[check.length - 1];
                        if (value_a8 == 0) {
                            Log.i(TAG, "设备运行指令（用户）-true");
                            if (mydeviceOperation != null) {
                                mydeviceOperation.deviceOperation(UID, true);
                            }
                        } else if (value_a8 == 1) {
                            Log.i(TAG, "执行了设备运行指令（用户）-false");
                            if (mydeviceOperation != null) {
                                mydeviceOperation.deviceOperation(UID, false);
                            }
                        }
                        break;
                    case 0xa7://1远程一键开机锁机指令
                        int value_a7 = check[check.length - 1];
                        if (value_a7 == 0) {
                            Log.i(TAG, "执行了远程一键开机锁机指令（管理员后台操作））-true");
                            if (myremoteBootlock != null) {
                                myremoteBootlock.remoteBootlock(UID, true);
                            }
                        } else if (value_a7 == 1) {
                            Log.i(TAG, "执行了远程一键开机锁机指令（管理员后台操作））-false");
                            if (myremoteBootlock != null) {
                                myremoteBootlock.remoteBootlock(UID, false);
                            }
                        }
                        break;
                    case 0xa4://设置锁机宽限天数
                        int value_a4 = check[check.length - 1];
                        if (value_a4 == 0) {
                            Log.i(TAG, "执行了-设置锁机宽限天数-true");
                            if (mylockMachineDays != null) {
                                mylockMachineDays.lockMachineDays(UID, true);
                            }

                        } else if (value_a4 == 1) {
                            Log.i(TAG, "执行了-设置锁机宽限天数-false");
                            if (mylockMachineDays != null) {
                                mylockMachineDays.lockMachineDays(UID, false);
                            }
                        }
                        break;
                    case 0xa5://永久开机命令
                        int value_a5 = check[check.length - 1];
                        if (value_a5 == 0) {
                            Log.i(TAG, "执行了-永久开机命令-true");
                            if (myforeverOpen != null) {
                                myforeverOpen.foreverOpen(UID, true);
                            }
                        } else if (value_a5 == 1) {
                            Log.i(TAG, "执行了-永久开机命令-false");
                            if (myforeverOpen != null) {
                                myforeverOpen.foreverOpen(UID, false);
                            }
                        }
                        break;
                    case 0xa6://永久开机命令管理员
                        int value_a6 = check[check.length - 1];
                        if (value_a6 == 0) {
                            Log.i(TAG, "执行了-永久开机命令-true");
                            if (foreverOpenAdmin != null) {
                                foreverOpenAdmin.foreverOpenAdmin(UID, true);
                            }
                        } else if (value_a6 == 1) {
                            Log.i(TAG, "执行了-永久开机命令-false");
                            if (foreverOpenAdmin != null) {
                                foreverOpenAdmin.foreverOpenAdmin(UID, false);
                            }
                        }
                        break;
                    case 0xb1://全部参数状态包
                        Log.i(TAG, "执行了-全部参数状态包");
//                        int[] parameter = new int[105];
                        List<String> list = new ArrayList<String>();
                        for (int i = 0, j = 0; i < 48; i++) {
                            if (i < 6) {
                                // int转String(二进制)
                                String binaryStr = Integer.toBinaryString(check[17 + i]);
                                while (binaryStr.length() < 8) {
                                    binaryStr = "0" + binaryStr;
                                }
                                char[] binaryChar = new char[binaryStr.length()];
                                binaryChar = binaryStr.toCharArray();
                                for (int k = 0; k < binaryStr.length(); k++) {
                                    list.add(String.valueOf(Character.getNumericValue(binaryChar[k])));
                                }
                            }
                        }
                        for (int j = 0; j < 114; j = j + 2) {
//                            int H = Integer.parseInt(String.valueOf(check[23 + j]), 16);
//                            int L = Integer.parseInt(String.valueOf(check[23 + j + 1]), 16);
                            int H = check[23 + j];
                            int L = check[23 + j + 1];
//                            System.out.println((H << 8) + L);
                            list.add(String.valueOf((H << 8) + L)
                            );
                        }
                        int size = list.size();
                        String[] arr = (String[]) list.toArray(new String[size]);
                        if (myalldata != null) {
                            Log.i("长度", String.valueOf(list.size()));
                            myalldata.data(UID, arr);
                        }
                        break;
                    case 0xb2://报警
                        Log.i(TAG, "执行了-报警");
                        int value_b2[] = new int[4];
                        value_b2[0] = check[1];
                        value_b2[1] = check[check.length - 3];
                        value_b2[2] = check[check.length - 2];
                        value_b2[3] = check[check.length - 1];
                        if (myalertState != null) {
                            myalertState.alertState(UID, value_b2);
                        }
                        break;
                    case 0xb3://消报警
                        Log.i(TAG, "执行了-消除报警");
                        int value_b3[] = new int[1];
                        value_b3[0] = check[check.length - 1];
                        if (mycancelalertState != null) {
                            mycancelalertState.clearalert(UID, value_b3);
                        }
                        break;

                    case 0x01://修改开关量
                        int value_01 = check[check.length - 1];
                        if (value_01 == 0) {
                            Log.i(TAG, "执行了-修改开关量-true");
                            if (mychangeSwichState != null) {
                                mychangeSwichState.changeSwichState(UID, true);
                            }

                        } else if (value_01 == 1) {
                            Log.i(TAG, "执行了-修改开关量-false");
                            if (mychangeSwichState != null) {
                                mychangeSwichState.changeSwichState(UID, false);
                            }

                        }
                        break;
                    case 0xc1://锁设置
                        Log.i(TAG, "执行了-锁设置");
                        int[] value_c1 = new int[6];
                        value_c1[0] = check[check.length - 6];
                        value_c1[1] = check[check.length - 5];
                        value_c1[2] = check[check.length - 4];
                        value_c1[3] = check[check.length - 3];
                        value_c1[4] = check[check.length - 2];
                        value_c1[5] = check[check.length - 1];
                        if (mylock != null) {
                            mylock.lock(UID, value_c1);
                        }
                        break;
                    case 0xC4://设备已被锁通知
                        Log.i(TAG, "执行了-设备已被锁通知");
                        int value_c4[] = new int[1];
                        value_c4[0] = check[check.length - 1];
                        if (mylocked != null) {
                            mylocked.locked(UID, value_c4);
                        }
                        break;
                    case 0xC2://时间初始化设置（管理员权限）
                        Log.i(TAG, "执行了-时间初始化设置（管理员权限）");
                        int value_c2 = check[check.length - 1];
                        if (value_c2 == 0) {
                            Log.i(TAG, "执行了-时间初始化设置true");
                            if (mytimeInitialization != null) {
                                mytimeInitialization.timeInitialization(UID, true);
                            }

                        } else if (value_c2 == 1) {
                            Log.i(TAG, "执行了-时间初始化设置（-false");
                            if (mytimeInitialization != null) {
                                mytimeInitialization.timeInitialization(UID, false);
                            }

                        }

                        break;
                    case 0xC3://3.温度标定（管理员权限）
                        Log.i(TAG, "执行了-温度标定（管理员权限）");
                        int[] value_c3 = new int[1];
                        value_c3[0] = check[check.length - 1];
                        if (mytemperatureCalibration != null) {
                            mytemperatureCalibration.temperatureCalibration(UID, value_c3);
                        }
                        break;
                    default://修改非开关
                        int value = check[check.length - 1];
                        if (value == 0) {
                            Log.i(TAG, "执行了-修改非开关-true");
                            if (mynotSwichStateChange != null) {
                                mynotSwichStateChange.notSwichStateChange(UID, true);
                            }
                        } else if (value == 1) {
                            Log.i(TAG, "执行了-修改非开关-false");
                            if (mynotSwichStateChange != null) {
                                mynotSwichStateChange.notSwichStateChange(UID, false);
                            }
                        }
                        break;
                }
            } else {
                Log.e(TAG, "数据校验错误");
            }
        } else {
            Log.e(TAG, "数据包不全！");
        }
    }

    public int[] inspect(int[] submitData) {
        //获得数据长度
        int datalength = submitData.length;
        Log.i(TAG, "datalength:" + datalength);
        byte[] Cutdata = new byte[datalength - 2];
        int[] rightdata = new int[datalength - 2];
        int[] errordata = new int[2];
        for (int i = 0; i < Cutdata.length; i++) {
            Cutdata[i] = (byte) submitData[i];
        }
        //crc16校验
        int crcValue = 0;//= crc16.(Cutdata, 0, Cutdata.length);
        int front = crcValue / 256;//字符串前半段截取
        int back = crcValue % 256;//字符串后半段截取
        if (front == submitData[datalength - 2] && back == submitData[datalength - 1]) {
            for (int i = 0; i < Cutdata.length; i++) {
                rightdata[i] = submitData[i];
            }
            //正确返回有用数据包
            return rightdata;
        } else {
            Log.i(TAG, "校验失败");
        }
        //错误返回错误数据包
        return errordata;
    }

    /**
     * 关闭资源
     */
    public void clossSocketResources() {
        try {
            pw.close();
//            br.close();
            dis.close();
            is.close();
            socket.close();
            Log.i(TAG, "colse");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    public void clossS() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clossSocket() {
        if (socket != null) {
            try {
                socket.shutdownInput();
                socket.shutdownOutput();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                pw.close();
//            br.close();
                dis.close();
                is.close();
                socket.close();
            } catch (IOException e) {

            }
        }

    }

    /**
     * 设置接口
     */
    //设置网络密码
    public void SetsettingNetworkPassword(Devices.settingNetworkPassword settingNetworkPassword) {
        this.mySettingNetworkPassword = settingNetworkPassword;
    }

    //保存网络参数
    public void SetsaveNetwork(Devices.saveNetwork saveNetwork) {
        this.mySaveNetwork = saveNetwork;
    }

    //切换工作环境
    public void SetchangeNetwork(Devices.changeNetwork changeNetwork) {
        this.myChangeNetwork = changeNetwork;
    }

    //锁机开关指令
    public void SetlockMachinetime(Devices.lockMachinetime lockMachinetime) {
        this.myLockMachinetime = lockMachinetime;
    }

    //锁机开关指令
    public void SetlockMachine(Devices.lockMachine lockMachine) {
        this.myLockMachine = lockMachine;
    }

    //锁机限制天数
    public void SetlockMachineDays(Devices.lockMachineDays lockMachineDays) {
        this.mylockMachineDays = lockMachineDays;
    }

    //永久开机指令
    public void SetforeverOpen(Devices.foreverOpen foreverOpen) {
        this.myforeverOpen = foreverOpen;
    }

    //永久开机指令
    public void SetforeverOpenAdmin(Devices.foreverOpenAdmin foreverOpenadmin) {
        this.foreverOpenAdmin = foreverOpenadmin;
    }

    //修改开关量
    public void SetchangeSwichState(Devices.changeSwichState changeSwichState) {
        this.mychangeSwichState = changeSwichState;
    }

    //修改非开关量
    public void SetnotSwichStateChange(Devices.notSwichStateChange notSwichStateChange) {
        this.mynotSwichStateChange = notSwichStateChange;
    }

    //获取全部参数
    public void Setalldata(Devices.alldata alldata) {
        this.myalldata = alldata;
    }

    //报警
    public void SetalertState(Devices.alertState alertState) {
        this.myalertState = alertState;
    }

    //消报警
    public void SetCancelalertState(Devices.clearalert clearalert) {
        this.mycancelalertState = clearalert;
    }

    //锁
    public void Setlock(Devices.lock lock) {
        this.mylock = lock;
    }

    //4.设备已被锁通知
    public void Setlocked(Devices.locked Setlocked) {
        this.mylocked = Setlocked;
    }

    //时间初始化
    public void SetTimeInitialization(Devices.timeInitialization timeInitialization) {
        this.mytimeInitialization = timeInitialization;
    }

    //时间初始化
    public void SetTemperatureCalibration(Devices.temperatureCalibration temperatureCalibration) {
        this.mytemperatureCalibration = temperatureCalibration;
    }

    //时间初始化
    public void SetopenMachine(Devices.openMachine openMachine) {
        this.myopenMachine = openMachine;
    }

    //时间初始化
    public void SetdeviceOperation(Devices.deviceOperation deviceOperation) {
        this.mydeviceOperation = deviceOperation;
    }

    //10.远程一键开机锁机指令（管理员后台操作）
    public void SetremoteBootlock(Devices.remoteBootlock remoteBootlock) {
        this.myremoteBootlock = remoteBootlock;
    }

    public void Setshoudong(Devices.shoudong shoudong) {
        this.myshoudong = shoudong;
    }

    public interface ReceiveData {
        void data(int[] data);

        void statesocket();
    }
}