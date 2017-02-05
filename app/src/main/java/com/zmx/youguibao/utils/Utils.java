package com.zmx.youguibao.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.zmx.youguibao.MyApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
     * @param
     */
    public static void showSoftInput(Context context){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
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

    /**
     * 处理发表说说时间
     * @param startTime
     * @param date
     * @param format
     * @return
     */
    public static String dateDiff(String startTime, Date date, String format) {


        //按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat(format);
        String endTime = sd.format(date);


        Log.e("当前时间：","当前时间："+endTime);
        Log.e("发表时间：","发表时间："+startTime);

        long nd = 1000 * 24 * 60 * 60;//一天的毫秒数
        long nh = 1000 * 60 * 60;//一小时的毫秒数
        long nm = 1000 * 60;//一分钟的毫秒数
        long ns = 1000;//一秒钟的毫秒数long diff;try {
        //获得两个时间的毫秒时间差异
        long diff;
        try {

            diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();

            long day = diff / nd;//计算差多少天
            long hour = diff % nd / nh;//计算差多少小时
            long min = diff % nd % nh / nm;//计算差多少分钟
            long sec = diff % nd % nh % nm / ns;//计算差多少秒//输出结果
//	    	("时间相差："+day+"天"+hour+"小时"+min+"分钟"+sec+"秒。");

            if (day != 0) {
                return day + "天前";
            } else if (hour != 0) {
                return hour + "小时前";
            } else if (min != 0 && min > 10) {
                return min + "分钟前";
            } else if (min != 0 && min < 10) {
                return "刚刚";
            } else {

                return "刚刚";
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "0";

    }


}
