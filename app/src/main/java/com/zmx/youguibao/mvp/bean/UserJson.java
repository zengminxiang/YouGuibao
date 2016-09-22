package com.zmx.youguibao.mvp.bean;

import java.io.Serializable;

/**
 *作者：胖胖祥
 *时间：2016/9/18 0018 下午 2:00
 *功能模块：用户信息
 */
public class UserJson implements Serializable{

    private String u_id;
    private String u_phone;
    private String u_pwd;
    private String u_name;
    private String u_headurl;
    private String u_desc;
    private String u_sex;
    private String u_time;
    private String u_number;
    private String u_experience;

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getU_phone() {
        return u_phone;
    }

    public void setU_phone(String u_phone) {
        this.u_phone = u_phone;
    }

    public String getU_pwd() {
        return u_pwd;
    }

    public void setU_pwd(String u_pwd) {
        this.u_pwd = u_pwd;
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

    public String getU_desc() {
        return u_desc;
    }

    public void setU_desc(String u_desc) {
        this.u_desc = u_desc;
    }

    public String getU_sex() {
        return u_sex;
    }

    public void setU_sex(String u_sex) {
        this.u_sex = u_sex;
    }

    public String getU_time() {
        return u_time;
    }

    public void setU_time(String u_time) {
        this.u_time = u_time;
    }

    public String getU_number() {
        return u_number;
    }

    public void setU_number(String u_number) {
        this.u_number = u_number;
    }

    public String getU_experience() {
        return u_experience;
    }

    public void setU_experience(String u_experience) {
        this.u_experience = u_experience;
    }
}
