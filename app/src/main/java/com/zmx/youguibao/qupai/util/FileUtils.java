package com.zmx.youguibao.qupai.util;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 *作者：胖胖祥
 *时间：2016/8/18 0018 上午 10:25
 *功能模块：配置保存文件的类
 */
public class FileUtils {

    /**
     * 保存路径的文件夹名称
     */
    public static  String DIR_NAME = "qupai_video_test";

    /**
     * 给指定的文件名按照时间命名
     */
    private static  SimpleDateFormat OUTGOING_DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");

    /**
     * 得到指定的Video保存路径
     * @return
     */
    public static File getDoneVideoPath() {
        File dir = new File(Environment.getExternalStorageDirectory()
                + File.separator + DIR_NAME);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir;
    }

    /**
     * 得到输出的Video保存路径
     * @return
     */
    public static String newOutgoingFilePath() {
        String str = OUTGOING_DATE_FORMAT.format(new Date());
        String fileName = getDoneVideoPath() + File.separator
                + "video_" + str + ".mp4";
        return fileName;
    }

}