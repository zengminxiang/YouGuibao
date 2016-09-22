package com.zmx.youguibao.mvp.bean;

import java.io.Serializable;

/**
 *作者：胖胖祥
 *时间：2016/8/26 0026 下午 12:22
 *功能模块：视频列表
 */
public class VideoListJson implements Serializable{


    private int v_id;// 视频id
    private String v_videourl;// 视频路径
    private String v_videoimgurl;// 视频第一帧图片
    private String v_time;// 发表的时间
    private int v_browse_number;// 浏览量
    private String v_content;// 发表的内容
    private String v_addre;// 发表的地址
    private int uid;// 发表用户的id
    private String u_name;// 用户的昵称
    private String u_headurl;// 用户的头像
    private String count_comment;//评论总数
    private String count_like;//点赞总数

    public String getV_videourl() {
        return v_videourl;
    }

    public void setV_videourl(String v_videourl) {
        this.v_videourl = v_videourl;
    }

    public String getV_videoimgurl() {
        return v_videoimgurl;
    }

    public void setV_videoimgurl(String v_videoimgurl) {
        this.v_videoimgurl = v_videoimgurl;
    }

    public String getV_time() {
        return v_time;
    }

    public void setV_time(String v_time) {
        this.v_time = v_time;
    }

    public int getV_browse_number() {
        return v_browse_number;
    }

    public void setV_browse_number(int v_browse_number) {
        this.v_browse_number = v_browse_number;
    }

    public String getV_content() {
        return v_content;
    }

    public void setV_content(String v_content) {
        this.v_content = v_content;
    }

    public String getV_addre() {
        return v_addre;
    }

    public void setV_addre(String v_addre) {
        this.v_addre = v_addre;
    }

    public int getV_id() {
        return v_id;
    }

    public void setV_id(int v_id) {
        this.v_id = v_id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public String getU_headurl() {
        return u_headurl;
    }

    public void setU_headurl(String u_headurl) {
        this.u_headurl = u_headurl;
    }

    public String getCount_comment() {
        return count_comment;
    }

    public void setCount_comment(String count_comment) {
        this.count_comment = count_comment;
    }

    public String getCount_like() {
        return count_like;
    }

    public void setCount_like(String count_like) {
        this.count_like = count_like;
    }
}
