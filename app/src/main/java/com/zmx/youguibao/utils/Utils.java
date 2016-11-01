package com.zmx.youguibao.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.zmx.youguibao.MyApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

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

    /**
     * 弹出输入法
     * @param context
     * @param view
     */
    public static void showSoftInput(Context context, View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        //imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 隐藏输入法
     * @param context
     * @param view
     */
    public static void hideSoftInput(Context context, View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }

    // 判断手机号是否正确
    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 15+除4的任意数
     * 18+除1和4的任意数
     * 17+除9的任意数
     * 147
     */
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

}
