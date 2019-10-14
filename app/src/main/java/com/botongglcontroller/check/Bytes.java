package com.botongglcontroller.check;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

//通信编解码工具
public class Bytes {

    private final static byte[] hex = "0123456789ABCDEF".getBytes();
    //将ASCII字符转换为汉字
    private static String hexString = "0123456789ABCDEF";

    private static int parse(char c) {
        if (c >= 'a')
            return (c - 'a' + 10) & 0x0f;
        if (c >= 'A')
            return (c - 'A' + 10) & 0x0f;
        return (c - '0') & 0x0f;
    }

    //从字节数组到十六进制字符串转换
    public static String Bytes2HexString(byte[] b) {
        byte[] buff = new byte[2 * b.length];
        for (int i = 0; i < b.length; i++) {
            buff[2 * i] = hex[(b[i] >> 4) & 0x0f];
            buff[2 * i + 1] = hex[b[i] & 0x0f];
        }
        return new String(buff);
    }

    //从十六进制字符串到字节数组转换
    public static byte[] HexString2Bytes(String hexstr) {
        byte[] b = new byte[hexstr.length() / 2];
        int j = 0;
        for (int i = 0; i < b.length; i++) {
            char c0 = hexstr.charAt(j++);
            char c1 = hexstr.charAt(j++);
            b[i] = (byte) ((parse(c0) << 4) | parse(c1));
        }
        return b;
    }

    //从十六进制字符串到带符号十进制数
    public static int HexString2Int(String hexstr) {
        int i = Integer.parseInt(hexstr, 16);
        if (i >= 32768) i -= 65536;
        return i;
    }

    /*
     * 16进制字符串转整形
     */
    public static int OxStringtoInt(String ox) throws Exception {
        ox = ox.toLowerCase();
        if (ox.startsWith("0x")) {
            ox = ox.substring(2, ox.length());
        }
        Integer ri = 0;
        Integer oxlen = ox.length();
        for (int i = 0; i < oxlen; i++) {
            char c = ox.charAt(i);
            int h;
            if (('0' <= c && c <= '9')) {
                h = c - 48;
            } else if (('a' <= c && c <= 'f')) {
                h = c - 87;

            } else if ('A' <= c && c <= 'F') {
                h = c - 55;
            } else {
                throw (new Exception("not a integer "));
            }
            byte left = (byte) ((oxlen - i - 1) * 4);
            ri |= (h << left);
        }
        return ri;

    }

    //从字符串转换为十六进制字符串
    public static String stringToHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    //从十六进制字符串转换为字符串
    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "gbk");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    /**
     * 将字符串编码成16进制数字,适用于所有字符（包括中文）
     */
    public static String encode(String str) {
        //根据默认编码获取字节数组
        byte[] bytes = str.getBytes();
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        //将字节数组中每个字节拆解成2位16进制整数
        for (int i = 0; i < bytes.length; i++) {
            sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
            sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
        }
        return sb.toString();
    }

    /**
     * 将16进制数字解码成字符串,适用于所有字符（包括中文）
     */
    public static String decode(String bytes) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2);
        //将每2位16进制整数组装成一个字节
        for (int i = 0; i < bytes.length(); i += 2)
            baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString.indexOf(bytes.charAt(i + 1))));
        return new String(baos.toByteArray());
    }

    /**
     * @param /将字节数组转换为ImageView可调用的Bitmap对象
     * @param bytes
     * @param opts
     * @return Bitmap
     */
    public static Bitmap getPicFromBytes(byte[] bytes,
                                         BitmapFactory.Options opts) {
        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
                        opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }
    // 在网上看到android图片与字符串的转换，想问一下，如果在android上一该方法将图片转成字符串，java中没有Bitmap，那在计算机上怎么转换回来？

    public static Bitmap stringtoBitmap(String string) {
        //将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static void simafgge(String str) {

        uploadFile(HexToByte(str), "");
    }

    public static void uploadFile(byte[] fs, String path) {
        path = "file://" + Environment.getExternalStorageDirectory().getPath() + "/Download/1.jpg";
        FileOutputStream imageOutput = null;
        try {
            imageOutput = new FileOutputStream(new File(path));
//                FileImageOutputStream imageOutput = new FileImageOutputStream(new File(path));

            imageOutput.write(fs, 0, fs.length);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] HexToByte(String hexString) {
        int len = hexString.length();
        byte[] b = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节
            b[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character
                    .digit(hexString.charAt(i + 1), 16));
        }
        return b;
    }


    //保存为文件
    public static File getFileFromBytes(byte[] b, String outputFile) {
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }


}
