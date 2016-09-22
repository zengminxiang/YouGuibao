package com.zmx.youguibao.utils;

import android.widget.Toast;

import com.zmx.youguibao.MyApplication;

/**
 *作者：胖胖祥
 *时间：2016/9/6 0006 上午 11:45
 *功能模块：工具类
 */
public class Utils {

    /**
     * 显示toast
     * @param content  内容
     * @param duration  持续时间
     */
    public static void toast(String content , int duration){
        if (content == null) {
            return;
        }else {
            MyApplication.ToastMgr.builder.display(content, duration);
        }
    }
    /**
     * 显示默认持续时间为short的Toast
     * @param content  内容
     */
    public static void toast(String content){
        if (content == null) {
            return;
        }else {
            MyApplication.ToastMgr.builder.display(content, Toast.LENGTH_SHORT);
        }
    }

}
