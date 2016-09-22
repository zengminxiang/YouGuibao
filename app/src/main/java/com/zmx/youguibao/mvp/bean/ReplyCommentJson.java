package com.zmx.youguibao.mvp.bean;

import java.io.Serializable;

/**
 *作者：胖胖祥
 *时间：2016/9/5 0005 上午 9:58
 *功能模块：回复评论的信息
 */
public class ReplyCommentJson implements Serializable{

    private String vr_id; //表id
    private String vc_id; //主评论id
    private String hu_id; //回复用户的id
    private String hu_name;//回复用户的用户名
    private String bu_name; //被回复的用户名
    private String vr_content; //回复的内容
    private String vr_time; //回复的时间

    public String getVr_id() {
        return vr_id;
    }
    public void setVr_id(String vr_id) {
        this.vr_id = vr_id;
    }
    public String getVc_id() {
        return vc_id;
    }
    public void setVc_id(String vc_id) {
        this.vc_id = vc_id;
    }
    public String getHu_id() {
        return hu_id;
    }
    public void setHu_id(String hu_id) {
        this.hu_id = hu_id;
    }
    public String getBu_name() {
        return bu_name;
    }
    public void setBu_name(String bu_name) {
        this.bu_name = bu_name;
    }
    public String getVr_content() {
        return vr_content;
    }
    public void setVr_content(String vr_content) {
        this.vr_content = vr_content;
    }
    public String getVr_time() {
        return vr_time;
    }
    public void setVr_time(String vr_time) {
        this.vr_time = vr_time;
    }

    public String getHu_name() {
        return hu_name;
    }

    public void setHu_name(String hu_name) {
        this.hu_name = hu_name;
    }
}
