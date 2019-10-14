package com.botongglcontroller.onenetdb;

import com.botongglcontroller.check.CRC16;

public class Sendcommand {
    //private CRC16 crc16= new CRC16();
    //设备锁机
    public static byte[] deviceLock(int data) {
        CRC16 crc16= new CRC16();
        byte[] submitData = new byte[12];
        submitData[0] = (byte) 0xff;
        submitData[1] = (byte) 0xfe;//前导码
        submitData[2] = (byte) 0xa0;//地址
        submitData[3] = (byte) 0x00;//地址
        submitData[4] = (byte) 0x01;//数据个数
        //数据
        submitData[5] = (byte) data;
        //crc16校验
        short crcValue = (short) crc16.addcrc16(submitData, 6);

        byte front = (byte)(crcValue >>8);//字符串前半段截取
        byte back = (byte)(crcValue % 256);//字符串后半段截取
        submitData[6] = (byte) front;
        submitData[7] = (byte) back;
        submitData[8]=(byte)0xff;
        submitData[9]=(byte)0xea;
        submitData[10]=(byte)0xff;
        submitData[11]=(byte)0xff;
        return submitData;
    }
    //设备锁机倒计时开关
    public static byte[] deviceLockstart(int data) {
        CRC16 crc16= new CRC16();
        byte[] submitData = new byte[12];
        submitData[0] = (byte) 0xff;
        submitData[1] = (byte) 0xfe;//前导码
        submitData[2] = (byte) 0xa0;//地址
        submitData[3] = (byte) 0x01;//地址
        submitData[4] = (byte) 0x01;//数据个数
        //数据
        submitData[5] = (byte) data;
        //crc16校验
        short crcValue = (short) crc16.addcrc16(submitData, 6);

        byte front = (byte)(crcValue >>8);//字符串前半段截取
        byte back = (byte)(crcValue % 256);//字符串后半段截取
        submitData[6] = (byte) front;
        submitData[7] = (byte) back;
        submitData[8]=(byte)0xff;
        submitData[9]=(byte)0xea;
        submitData[10]=(byte)0xff;
        submitData[11]=(byte)0xff;
        return submitData;
    }
    //设备开机
    public static byte[] devicePower(int data) {
        CRC16 crc16= new CRC16();
        byte[] submitData = new byte[12];
        submitData[0] = (byte) 0xff;
        submitData[1] = (byte) 0xfe;//前导码
        submitData[2] = (byte) 0xa0;//地址
        submitData[3] = (byte) 0x02;//地址
        submitData[4] = (byte) 0x01;//数据个数
        //数据
        submitData[5] = (byte) data;
        //crc16校验
        short crcValue = (short) crc16.addcrc16(submitData, 6);

        byte front = (byte)(crcValue >>8);//字符串前半段截取
        byte back = (byte)(crcValue % 256);//字符串后半段截取
        submitData[6] = (byte) front;
        submitData[7] = (byte) back;
        submitData[8]=(byte)0xff;
        submitData[9]=(byte)0xea;
        submitData[10]=(byte)0xff;
        submitData[11]=(byte)0xff;
        return submitData;
    }
    //设备运行
    public static byte[] deviceRun(int data) {
        CRC16 crc16= new CRC16();
        byte[] submitData = new byte[12];
        submitData[0] = (byte) 0xff;
        submitData[1] = (byte) 0xfe;//前导码
        submitData[2] = (byte) 0xa0;//地址
        submitData[3] = (byte) 0x03;//地址
        submitData[4] = (byte) 0x01;//数据个数
        //数据
        submitData[5] = (byte) data;
        //crc16校验
        short crcValue = (short) crc16.addcrc16(submitData, 6);

        byte front = (byte)(crcValue >>8);//字符串前半段截取
        byte back = (byte)(crcValue % 256);//字符串后半段截取
        submitData[6] = (byte) front;
        submitData[7] = (byte) back;
        submitData[8]=(byte)0xff;
        submitData[9]=(byte)0xea;
        submitData[10]=(byte)0xff;
        submitData[11]=(byte)0xff;
        return submitData;
    }
    //设备手动自动
    public static byte[] deviceHand(int data) {
        CRC16 crc16= new CRC16();
        byte[] submitData = new byte[12];
        submitData[0] = (byte) 0xff;
        submitData[1] = (byte) 0xfe;//前导码
        submitData[2] = (byte) 0xa0;//地址
        submitData[3] = (byte) 0x04;//地址
        submitData[4] = (byte) 0x01;//数据个数
        //数据
        submitData[5] = (byte) data;
        //crc16校验
        short crcValue = (short) crc16.addcrc16(submitData, 6);

        byte front = (byte)(crcValue >>8);//字符串前半段截取
        byte back = (byte)(crcValue % 256);//字符串后半段截取
        submitData[6] = (byte) front;
        submitData[7] = (byte) back;
        submitData[8]=(byte)0xff;
        submitData[9]=(byte)0xea;
        submitData[10]=(byte)0xff;
        submitData[11]=(byte)0xff;
        return submitData;
    }
    public static byte[] devicePara(int index,int data) {
        CRC16 crc16= new CRC16();
        //int m=index-;
        short n=(short) data;
        byte[] submitData = new byte[13];
        submitData[0] = (byte) 0xff;
        submitData[1] = (byte) 0xfe;//前导码
        submitData[2] = (byte) 0x90;//地址
        submitData[3] = (byte) index;//地址
        submitData[4] = (byte) 0x01;//数据个数
        //数据
        submitData[5] = (byte) (n>>8);
        submitData[6] = (byte) (n%256);
        //crc16校验
        short crcValue = (short) crc16.addcrc16(submitData, 7);

        byte front = (byte)(crcValue >>8);//字符串前半段截取
        byte back = (byte)(crcValue % 256);//字符串后半段截取
        submitData[7] = (byte) front;
        submitData[8] = (byte) back;
        submitData[9]=(byte)0xff;
        submitData[10]=(byte)0xea;
        submitData[11]=(byte)0xff;
        submitData[12]=(byte)0xff;
        return submitData;
    }
    public static byte[] deviceControl(int index,int data) {
        CRC16 crc16= new CRC16();
        byte[] submitData = new byte[12];
        submitData[0] = (byte) 0xff;
        submitData[1] = (byte) 0xfe;//前导码
        submitData[2] = (byte) 0xa0;//地址
        submitData[3] = (byte) index;//地址
        submitData[4] = (byte) 0x01;//数据个数
        //数据
        submitData[5] = (byte) data;
        //crc16校验
        short crcValue = (short) crc16.addcrc16(submitData, 6);

        byte front = (byte)(crcValue >>8);//字符串前半段截取
        byte back = (byte)(crcValue % 256);//字符串后半段截取
        submitData[6] = (byte) front;
        submitData[7] = (byte) back;
        submitData[8]=(byte)0xff;
        submitData[9]=(byte)0xea;
        submitData[10]=(byte)0xff;
        submitData[11]=(byte)0xff;
        return submitData;
    }

}
