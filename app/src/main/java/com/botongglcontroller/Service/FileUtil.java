
package com.botongglcontroller.Service;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * 文件处理类
 *
 * @author zhangxing
 */
public class FileUtil {

    public static final String KonkaApplication = "konkaUpdateApplication";
    public static File updateDir = null;
    public static File updateFile = null;
    public static boolean isCreateFileSucess;


    public static void createFile(String app_name) {

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            isCreateFileSucess = true;

            updateDir = new File(Environment.getExternalStorageDirectory() + "/" + KonkaApplication + "/");
            updateFile = new File(updateDir + "/" + app_name + ".apk");

            if (!updateDir.exists()) {
                updateDir.mkdirs();
            }
            if (!updateFile.exists()) {
                try {
                    updateFile.createNewFile();
                } catch (IOException e) {
                    isCreateFileSucess = false;
                    e.printStackTrace();
                }
            }

        } else {
            isCreateFileSucess = false;
        }
    }
}