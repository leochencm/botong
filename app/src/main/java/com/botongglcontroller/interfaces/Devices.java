package com.botongglcontroller.interfaces;

import android.util.Log;

import com.botongglcontroller.check.CRC16;
import com.botongglcontroller.socket.SocketMessage;


/**
 * Author jonathan
 * Created by Administrator on 2017/2/13.
 * Effect 与设备通信
 */

public class Devices {
    private final String TAG = getClass().getName();
    /**
     * socket
     */
    private SocketMessage socketMessage;

    private CRC16 crc16 = new CRC16();

    public Devices(SocketMessage socketMessage) {
        this.socketMessage = socketMessage;
        crc16 = new CRC16();
    }

    /**
     * 修改设备网络密码
     */
    public void settingNetworkPassword(String SSID, String password) {
        settingNetworkParameter(SSID, password, 0xa0);
    }

    /**
     * 保存路由器名称和密码
     */
    public void saveNetwork(String SSID, String password) {
        settingNetworkParameter(SSID, password, 0xa1);
    }

    //设置网络参数
    private void settingNetworkParameter(String SSID, String password, int numbre) {
        //获取长度
        int lengthSSID = SSID.length();
        int lengthPassword = password.length();
        //字符串转换
        char[] SSIDChar = new char[lengthSSID];
        char[] passwordChar = new char[lengthPassword];
        SSIDChar = SSID.toCharArray();
        passwordChar = password.toCharArray();
        //数据长度
        int datalength = lengthSSID + lengthPassword + 2 + 1 + 1 + 1 + 1 + 2;
        //提交数据数组
        byte[] submitData = new byte[datalength];
        //设置数据
        submitData[0] = (byte) 0xff;
        submitData[1] = (byte) 0xfe;//前导码
        submitData[2] = (byte) numbre;//功能码
        submitData[3] = (byte) (datalength - (2 + 1 + 1));//字节长度
        submitData[4] = (byte) lengthSSID;//网络名称长度
        //转换SSID为byte并添加
        for (int i = 0; i < lengthSSID; i++) {
            submitData[5 + i] = (byte) SSIDChar[i];
        }
        //已用长度
        int arrayLength = 5 + lengthSSID;
        submitData[arrayLength] = (byte) lengthPassword;//密码长度字节
        //转换passwordChar为byte并添加
        for (int i = 0; i < lengthPassword; i++) {
            submitData[arrayLength + 1 + i] = (byte) passwordChar[i];
        }
        //crc16校验
//        int crcValue = crc16.CRC16s(submitData, 0, (datalength - 2));
//        int front = crcValue / 256;//字符串前半段截取
//        int back = crcValue % 256;//字符串后半段截取
//        submitData[datalength - 2] = (byte) front;
//        submitData[datalength - 1] = (byte) back;
//        socketMessage.sendData(submitData);
    }

    //参数
    private void settingData(byte[] submitData, int data) {
        submitData[3] = (byte) 0x04;
        submitData[4] = (byte) 0x01;
        submitData[5] = (byte) data;
        //crc16校验
//        int crcValue = crc16.CRC16s(submitData, 0, (submitData.length - 2));
//        int front = crcValue / 256;//字符串前半段截取
//        int back = crcValue % 256;//字符串后半段截取
//        submitData[submitData.length - 2] = (byte) front;
//        submitData[submitData.length - 1] = (byte) back;
//        socketMessage.sendData(submitData);
    }

    /**
     * 切换工作环境
     *
     * @param data
     */
    public void changeNetwork(int data) {
        byte[] submitData = new byte[8];
        submitData[0] = (byte) 0xff;
        submitData[1] = (byte) 0xfe;
        submitData[2] = (byte) 0xa2;
        settingData(submitData, data);
    }

    /**
     * 锁机开关指令
     *
     * @param data
     */
    public void lockMachinetime(int data) {
        byte[] submitData = new byte[8];
        submitData[0] = (byte) 0xff;
        submitData[1] = (byte) 0xfe;
        submitData[2] = (byte) 0xa3;
        settingData(submitData, data);
    }

    public void lockMachine(int data) {
        byte[] submitData = new byte[8];
        submitData[0] = (byte) 0xff;
        submitData[1] = (byte) 0xfe;
        submitData[2] = (byte) 0xa9;
        settingData(submitData, data);
    }

    /**
     * 开机关机指令
     *
     * @param data
     */
    public void openMachine(int data) {
        byte[] submitData = new byte[8];
        submitData[0] = (byte) 0xff;
        submitData[1] = (byte) 0xfe;
        submitData[2] = (byte) 0xa7;
        settingData(submitData, data);
    }

    /**
     * 消报警指令
     *
     * @param data
     */
    public void cancelAlert(int data) {
        byte[] submitData = new byte[7];
        submitData[0] = (byte) 0xff;
        submitData[1] = (byte) 0xfe;
        submitData[2] = (byte) 0xb3;
        submitData[3] = (byte) 0x03;
        submitData[4] = (byte) data;
        //crc16校验
//        int crcValue = crc16.CRC16s(submitData, 0, (submitData.length - 2));
//        int front = crcValue / 256;//字符串前半段截取
//        int back = crcValue % 256;//字符串后半段截取
//        submitData[submitData.length - 2] = (byte) front;
//        submitData[submitData.length - 1] = (byte) back;
    }

    /**
     * 手动模式
     *
     * @param data
     */
    public void shoudong(int data) {
        byte[] submitData = new byte[8];
        submitData[0] = (byte) 0xff;
        submitData[1] = (byte) 0xfe;
        submitData[2] = (byte) 0xaa;
        settingData(submitData, data);
    }

    /**
     * 设置锁机宽限天数
     *
     * @param days
     */
    public void lockMachineDays(int days) {
        byte[] submitData = new byte[8];
        submitData[0] = (byte) 0xff;
        submitData[1] = (byte) 0xfe;
        submitData[2] = (byte) 0xa4;
        settingData(submitData, days);
    }

    /**
     * 永久开机指令
     * 密码长度必须4位
     *
     * @param password
     */
    public void foreverOpen(String password) {
        //获取长度
        int lengthpassword = password.length();
        //字符串转换
        char[] passwordChar = new char[lengthpassword];
        passwordChar = password.toCharArray();
        //需提交的数据
        int num = 7 + lengthpassword;
        Log.e("foreverOpen", "password" + password + "lengthpassword" + lengthpassword + "num" + num + "passwordChar.length" + passwordChar.length);
        byte[] submitData = new byte[num];
        submitData[0] = (byte) 0xff;
        submitData[1] = (byte) 0xfe;//前导码
        submitData[2] = (byte) 0xa5;//功能码
        submitData[3] = (byte) (passwordChar.length + 3);//字节长度
        submitData[4] = (byte) passwordChar.length;//数据长度
        //数据
        for (int i = 0; i < passwordChar.length; i++) {
            submitData[4 + i + 1] = (byte) passwordChar[i];
        }
//            submitData[5] = (byte) passwordChar[0];
//            submitData[6] = (byte) passwordChar[1];
//            submitData[7] = (byte) passwordChar[2];
//            submitData[8] = (byte) passwordChar[3];
        //crc16校验
//            int crcValue = crc16.CRC16s(submitData, 0, num-2);
//            int front = crcValue / 256;//字符串前半段截取
//            int back = crcValue % 256;//字符串后半段截取
//            submitData[num-2] = (byte) front;
//            submitData[num-1] = (byte) back;
//            socketMessage.sendData(submitData);

    }

    /**
     * 永久开机指令管理员
     * 密码长度必须4位
     *
     * @param password
     */
    public void foreverOpenAdmin(String password) {
//        if (password.length() == 4) {
//            //获取长度
//            int lengthpassword = password.length();
//            //字符串转换
//            char[] passwordChar = new char[lengthpassword];
//            passwordChar = password.toCharArray();
//            //需提交的数据
//            byte[] submitData = new byte[11];
//            submitData[0] = (byte) 0xff;
//            submitData[1] = (byte) 0xfe;//前导码
//            submitData[2] = (byte) 0xa6;//功能码
//            submitData[3] = (byte) 0x07;//字节长度
//            submitData[4] = (byte) 0x04;//数据长度
//            //数据
//            submitData[5] = (byte) passwordChar[0];
//            submitData[6] = (byte) passwordChar[1];
//            submitData[7] = (byte) passwordChar[2];
//            submitData[8] = (byte) passwordChar[3];
//            //crc16校验
//            int crcValue = crc16.CRC16s(submitData, 0, 9);
//            int front = crcValue / 256;//字符串前半段截取
//            int back = crcValue % 256;//字符串后半段截取
//            submitData[9] = (byte) front;
//            submitData[10] = (byte) back;
//            socketMessage.sendData(submitData);
//        } else {
//            Log.e(TAG, "密码长度必须为四位");
//        }
    }

    /**
     * 修改开关
     * 开关地址、开关状态
     *
     * @param index
     * @param state
     */
    public void changeSwichState(int index, int state) {
//        int point = 0;
//        if(index<=16&&index>0){
//            point=0x01;
//        }else if(index<=32&&index>16){
//            point=0x02;
//        }else if(index<=48&&index>32){
//            point=0x03;
//        }
        //需提交的数据
//        byte[] submitData = new byte[9];
//        submitData[0] = (byte) 0xff;
//        submitData[1] = (byte) 0xfe;//前导码
//        submitData[2] = (byte) 0x01;//功能码
//        submitData[3] = (byte) 0x05;//字节长度
//        submitData[4] = (byte) 0x02;//数据长度
//        submitData[5] = (byte) index;
//        submitData[6] = (byte) state;
//        //crc16校验
//        int crcValue = crc16.CRC16s(submitData, 0, 7);
//        int front = crcValue / 256;//字符串前半段截取
//        int back = crcValue % 256;//字符串后半段截取
//        submitData[7] = (byte) front;
//        submitData[8] = (byte) back;
//        socketMessage.sendData(submitData);
    }

    /**
     * 非开关量修改
     * 功能码、数据字节
     * functionCode大于9
     *
     * @param functionCode
     * @param data
     */
    public void notSwichStateChange(int functionCode, int data) {
        //需提交的数据
//        int code=functionCode-48+3;
//        byte[] submitData = new byte[9];
//        submitData[0] = (byte) 0xff;
//        submitData[1] = (byte) 0xfe;//前导码
//        submitData[2] = (byte) code;//功能码
//        submitData[3] = (byte) 0x05;//字节长度
//        submitData[4] = (byte) 0x02;//数据长度
//        int dataFront = data / 256;//字符串前半段截取BF
//        int dataBdsfrxack = data % 256;//字符串后半段截取
//        submitData[5] = (byte) dataFront;
//        submitData[6] = (byte) dataBdsfrxack;
//        //crc16校验
//        int crcValue = crc16.CRC16s(submitData, 0, 7);
//        int front = crcValue / 256;//字符串前半段截取
//        int back = crcValue % 256;//字符串后半段截取
//        submitData[7] = (byte) front;
//        submitData[8] = (byte) back;
//
//        socketMessage.sendData(submitData);
    }

    /**
     * 时间初始化设置（管理员权限）
     */

    public void timeInitialization(String s) {


    }

    /**
     * 3.温度标定（管理员权限）
     */

    public void temperatureCalibration(int data, int data2) {


    }

    /**
     * 9.设备运行指令（用户）
     */

    public void deviceOperation(int data) {


    }

    /**
     * 9.设备开机指令（用户）
     */

    public void remoteBootlock(int data) {


    }

    /**
     * 设置网络密码
     */
    public interface settingNetworkPassword {
        /**
         * 设备唯一id，当前请求状态。
         *
         * @param UID
         * @param state
         */
        void settingNetworkPassword(String UID, boolean state);
    }

    /**
     * 保存网络参数
     */
    public interface saveNetwork {
        /**
         * 设备唯一id，当前请求状态。
         *
         * @param UID
         * @param state
         */
        void saveNetWork(String UID, int state);
    }

    /**
     * 切换工作环境
     */
    public interface changeNetwork {
        /**
         * 设备唯一id，当前请求状态。
         *
         * @param UID
         * @param state
         */
        void changeNetwork(String UID, boolean state);
    }

    /**
     * 锁机开关指令
     */
    public interface lockMachine {
        /**
         * 设备唯一id，当前请求状态。
         *
         * @param UID
         * @param state
         */
        void lockMachine(String UID, boolean state);
    }

    /**
     * 锁机开关指令时间限制的
     */
    public interface lockMachinetime {
        /**
         * 设备唯一id，当前请求状态。
         *
         * @param UID
         * @param state
         */
        void lockMachinetime(String UID, boolean state);
    }

    /**
     * 开机关机指令
     */
    public interface openMachine {
        /**
         * 设备唯一id，当前请求状态。
         *
         * @param UID
         * @param state
         */
        void openMachine(String UID, boolean state);
    }

    /**
     * 锁机限制天数
     */
    public interface lockMachineDays {
        /**
         * 设备唯一id，当前请求状态。
         *
         * @param UID
         * @param state
         */
        void lockMachineDays(String UID, boolean state);
    }

    /**
     * 永久开机指令
     */
    public interface foreverOpen {
        /**
         * 设备唯一id，当前请求状态。
         *
         * @param UID
         * @param state
         */
        void foreverOpen(String UID, boolean state);
    }

    /**
     * 永久开机指令
     */
    public interface foreverOpenAdmin {
        /**
         * 设备唯一id，当前请求状态。
         *
         * @param UID
         * @param state
         */
        void foreverOpenAdmin(String UID, boolean state);
    }

    /**
     * 修改开关量
     */
    public interface changeSwichState {
        /**
         * 设备唯一id，当前请求状态。
         *
         * @param UID
         * @param state
         */
        void changeSwichState(String UID, boolean state);
    }

    /**
     * 修改非开关量
     */
    public interface notSwichStateChange {
        /**
         * 设备唯一id，当前请求状态。
         *
         * @param UID
         * @param state
         */
        void notSwichStateChange(String UID, boolean state);
    }

    /**
     * 全部参数
     */
    public interface alldata {
        /**
         * 设备唯一id、全部参数（60）
         *
         * @param UID
         * @param data
         */
        void data(String UID, String[] data);
    }

    /**
     * 报警
     */
    public interface alertState {
        /**
         * 设备唯一id、两个数
         *
         * @param UID
         * @param data
         */
        void alertState(String UID, int[] data);
    }

    /**
     * 报警
     */
    public interface clearalert {
        /**
         * 设备唯一id、两个数
         *
         * @param UID
         * @param data
         */
        void clearalert(String UID, int[] data);
    }

    /**
     * 锁
     */
    public interface lock {
        /**
         * 设备唯一id、两个数
         *
         * @param UID
         * @param data
         */
        void lock(String UID, int[] data);
    }

    /**
     * 4.设备已被锁通知
     */
    public interface locked {
        /**
         * 设备唯一id、两个数
         *
         * @param UID
         * @param data
         */
        void locked(String UID, int[] data);
    }

    /**
     * 时间初始化设置（管理员权限）
     */
    public interface timeInitialization {
        /**
         * 设备唯一id、两个数
         *
         * @param UID
         * @param
         */
        void timeInitialization(String UID, boolean state);
    }

    /**
     * 3.温度标定（管理员权限）
     */
    public interface temperatureCalibration {
        /**
         * 设备唯一id、两个数
         *
         * @param UID
         * @param data
         */
        void temperatureCalibration(String UID, int[] data);
    }

    /**
     * 9.设备运行指令（用户）
     */
    public interface deviceOperation {
        /**
         * 设备唯一id、两个数
         *
         * @param UID
         * @param state
         */
        void deviceOperation(String UID, boolean state);
    }

    /**
     * 9.设备运行指令（用户）
     */
    public interface remoteBootlock {
        /**
         * 设备唯一id、两个数
         *
         * @param UID
         * @param state
         */
        void remoteBootlock(String UID, boolean state);
    }

    /**
     * 9.设备手动指令（用户）
     */
    public interface shoudong {
        /**
         * 设备唯一id、两个数
         *
         * @param UID
         * @param state
         */
        void shoudong(String UID, boolean state);
    }

}
