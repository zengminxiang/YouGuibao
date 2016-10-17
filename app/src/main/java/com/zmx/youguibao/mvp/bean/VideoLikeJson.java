package com.zmx.youguibao.mvp.bean;

import java.io.Serializable;

/**
 *作者：胖胖祥
 *时间：2016/9/13 0013 下午 4:33
 *功能模块：点赞列表头像
 */
public class VideoLikeJson implements Serializable{

    private String vl_id; //表id
    private String v_id;//视频id
    private String u_id;//用户id
    private String vl_time;//点赞时间
    private String u_headurl;//用户的头像
    private String u_name;//用户昵称

    private String v_videoimgurl;//视频的图片路径
    private String v_content;//视频的内容

    public String getVl_id() {
        return vl_id;
    }
    public void setVl_id(String vl_id) {
        this.vl_id = vl_id;
    }
    public String getV_id() {
        return v_id;
    }
    public void setV_id(String v_id) {
        this.v_id = v_id;
    }
    public String getU_id() {
        return u_id;
    }
    public void setU_id(String u_id) {
        this.u_id = u_id;
    }
    public String getVl_time() {
        return vl_time;
    }
    public void setVl_time(String vl_time) {
        this.vl_time = vl_time;
    }
    public String getU_headurl() {
        return u_headurl;
    }
    public void setU_headurl(String u_headurl) {
        this.u_headurl = u_headurl;
    }

    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public String getV_videoimgurl() {
        return v_videoimgurl;
    }

    public void setV_videoimgurl(String v_videoimgurl) {
        this.v_videoimgurl = v_videoimgurl;
    }

    public String getV_content() {
        return v_content;
    }

    public void setV_content(String v_content) {
        this.v_content = v_content;
    }
}
