package com.botongglcontroller.beans;

import com.botongglcontroller.check.CRC16;

public class Str2Para {
    protected String str;
    private CRC16 crc16= new CRC16();
    private byte[] bytes=new byte[137];
    private int[] para=new int[106];
    private int num=0;
    private int[] tounsign(byte b[])
    {
        int a[]=new int[b.length];
        for (int i=0;i<b.length;i++)
        {
            a[i]=b[i];
            a[i]=a[i]&0xff;
        }
        return a;
    }
    public boolean S2P(String str)
    {
        char[] c;
        c=str.toCharArray();
        num=str.length();
        //crc16校验
        int crcValue = crc16.CRC16s(c, 0, num-6);
        int front = crcValue / 256;//字符串前半段截取
        int back = crcValue % 256;//字符串后半段截取
        if(c[num-6] !=  front)return false;
        if(c[num-5] !=  back)return false;
        if(str.length()<137)return false;
        para[1]=(str.charAt(17)&0x80)>>7;
        para[2]=(str.charAt(17)&0x40)>>6;
        para[3]=(str.charAt(17)&0x20)>>5;
        para[4]=(str.charAt(17)&0x10)>>4;
        para[5]=(str.charAt(17)&0x08)>>3;
        para[6]=(str.charAt(17)&0x04)>>2;
        para[7]=(str.charAt(17)&0x02)>>1;
        para[8]=(str.charAt(17)&0x01);
        para[9]=(str.charAt(18)&0x80)>>7;
        para[10]=(str.charAt(18)&0x40)>>6;
        para[11]=(str.charAt(18)&0x20)>>5;
        para[12]=(str.charAt(18)&0x10)>>4;
        para[13]=(str.charAt(18)&0x08)>>3;
        para[14]=(str.charAt(18)&0x04)>>2;
        para[15]=(str.charAt(18)&0x02)>>1;
        para[16]=(str.charAt(18)&0x01);
        para[17]=(str.charAt(19)&0x80)>>7;
        para[18]=(str.charAt(19)&0x40)>>6;
        para[19]=(str.charAt(19)&0x20)>>5;
        para[20]=(str.charAt(19)&0x10)>>4;
        para[21]=(str.charAt(19)&0x08)>>3;
        para[22]=(str.charAt(19)&0x04)>>2;
        para[23]=(str.charAt(19)&0x02)>>1;
        para[24]=(str.charAt(19)&0x01);
        para[25]=(str.charAt(20)&0x80)>>7;
        para[26]=(str.charAt(20)&0x40)>>6;
        para[27]=(str.charAt(20)&0x20)>>5;
        para[28]=(str.charAt(20)&0x10)>>4;
        para[29]=(str.charAt(20)&0x08)>>3;
        para[30]=(str.charAt(20)&0x04)>>2;
        para[31]=(str.charAt(20)&0x02)>>1;
        para[32]=(str.charAt(20)&0x01);
        para[33]=(str.charAt(21)&0x80)>>7;
        para[34]=(str.charAt(21)&0x40)>>6;
        para[35]=(str.charAt(21)&0x20)>>5;
        para[36]=(str.charAt(21)&0x10)>>4;
        para[37]=(str.charAt(21)&0x08)>>3;
        para[38]=(str.charAt(21)&0x04)>>2;
        para[39]=(str.charAt(21)&0x02)>>1;
        para[40]=(str.charAt(21)&0x01);
        para[41]=(str.charAt(22)&0x80)>>7;
        para[42]=(str.charAt(22)&0x40)>>6;
        para[43]=(str.charAt(22)&0x20)>>5;
        para[44]=(str.charAt(22)&0x10)>>4;
        para[45]=(str.charAt(22)&0x08)>>3;
        para[46]=(str.charAt(22)&0x04)>>2;
        para[47]=(str.charAt(22)&0x02)>>1;
        para[48]=(str.charAt(22)&0x01);
        int j=23;
        for(int i=49;i<106;i++)
            para[i]=(str.charAt(j++)<<8|str.charAt(j++));
        return true;
    }
    public void setString(String str){this.str=str;}
    public int[] getPara(){return para;}
    public int getParam(int i)
    {return  para[i];}
}
