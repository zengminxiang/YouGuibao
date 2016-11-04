package com.zmx.youguibao.mvp.bean;

import java.io.Serializable;

/**
 *
 * 开发者：胖胖祥
 * 时间：2016/11/2 16:29
 * 功能模块：新加关注的消息
 *
 */

public class FollowUserPojo implements Serializable{

    private String f_id;//表id
    private String gu_id;//关注人的id
    private String gu_name;//关注人的名称
    private String gu_head;//关注人的头像
    private String f_time;//关注的时间
    private String bu_id;//被关注人的id

    public String getF_id() {
        return f_id;
    }

    public void setF_id(String f_id) {
        this.f_id = f_id;
    }

    public String getGu_id() {
        return gu_id;
    }
    public void setGu_id(String gu_id) {
        this.gu_id = gu_id;
    }
    public String getGu_name() {
        return gu_name;
    }
    public void setGu_name(String gu_name) {
        this.gu_name = gu_name;
    }
    public String getGu_head() {
        return gu_head;
    }
    public void setGu_head(String gu_head) {
        this.gu_head = gu_head;
    }
    public String getF_time() {
        return f_time;
    }
    public void setF_time(String f_time) {
        this.f_time = f_time;
    }
    public String getBu_id() {
        return bu_id;
    }
    public void setBu_id(String bu_id) {
        this.bu_id = bu_id;
    }


}
