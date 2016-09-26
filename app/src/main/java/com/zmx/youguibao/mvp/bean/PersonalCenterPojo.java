package com.zmx.youguibao.mvp.bean;

import java.io.Serializable;

/**
 *作者：胖胖祥
 *时间：2016/9/26 0026 上午 11:05
 *功能模块：个人中心
 */
public class PersonalCenterPojo implements Serializable{

    private String u_id;
    private String u_name;
    private String u_headurl;
    private String u_desc;
    private String u_sex;
    private String u_experience;

    private String follows;  //关注的用户
    private String Fans;    //粉丝数量

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
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

    public String getU_experience() {
        return u_experience;
    }

    public void setU_experience(String u_experience) {
        this.u_experience = u_experience;
    }

    public String getFollows() {
        return follows;
    }

    public void setFollows(String follows) {
        this.follows = follows;
    }

    public String getFans() {
        return Fans;
    }

    public void setFans(String fans) {
        Fans = fans;
    }

}
