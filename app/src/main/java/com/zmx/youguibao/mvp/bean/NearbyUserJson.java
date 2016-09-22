package com.zmx.youguibao.mvp.bean;

import java.io.Serializable;

/**
 *作者：胖胖祥
 *时间：2016/9/8 0008 下午 5:21
 *功能模块：附近用户
 */
public class NearbyUserJson implements Serializable{

    private String u_id;//用户id
    private String u_headurl;//用户头像
    private String c_longitude;//精度
    private String c_latitude;//维度

    public String getU_id() {
        return u_id;
    }
    public void setU_id(String u_id) {
        this.u_id = u_id;
    }
    public String getU_headurl() {
        return u_headurl;
    }
    public void setU_headurl(String u_headurl) {
        this.u_headurl = u_headurl;
    }
    public String getC_longitude() {
        return c_longitude;
    }
    public void setC_longitude(String c_longitude) {
        this.c_longitude = c_longitude;
    }
    public String getC_latitude() {
        return c_latitude;
    }
    public void setC_latitude(String c_latitude) {
        this.c_latitude = c_latitude;
    }

}
