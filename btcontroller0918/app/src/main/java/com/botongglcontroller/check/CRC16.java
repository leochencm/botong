package com.botongglcontroller.check;

/******************************************************************************
 * Compilation:  javac CRC16.java
 * Execution:    java CRC16 s
 * <p>
 * Reads in a string s as a command-line argument, and prints out
 * its 16-bit Cyclic Redundancy Check (CRC16). Uses a lookup table.
 * <p>
 * Reference:  http://www.gelato.unsw.edu.au/lxr/source/lib/crc16.c
 * <p>
 * % java CRC16 123456789
 * CRC16 = bb3d
 * <p>
 * Uses irreducible polynomial:  1 + x^2 + x^15 + x^16
 ******************************************************************************/

public class CRC16 {
    private final String TAG = getClass().getName();

    public static short addcrc16(byte[] buffer,int n)
    {
        int flag, a;
        int crc16 = 0X0ffff;
        //short xx=0xff;
        for(int i=0;i<n;i++)
        {
            short xx=(short) (buffer[i]&0x00ff);
            crc16 = (crc16 ^ xx);
            for(int j=0;j<8;j++)
            {
                a = crc16;
                flag = a & 0x0001;
                crc16 = (short) (crc16 >> 1);
                if (flag == 1) crc16 =(crc16 ^ (short)0xa001)&0x0000ffff;
            }
        }
        return (short)(crc16 & 0xffff);
    }

    public static int CRC16s(char[] dat, int offset, int count) {
        int crc = 0x0000;
        return (short) crc & 0xffff;
    }

}