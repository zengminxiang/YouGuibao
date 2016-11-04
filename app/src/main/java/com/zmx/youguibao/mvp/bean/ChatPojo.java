package com.zmx.youguibao.mvp.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 *
 * 开发者：胖胖祥
 * 时间：2016/11/3 21:07
 * 功能模块：消息记录
 *
 */
@Entity
public class ChatPojo {

    @Id(autoincrement = true)
    private Long id;//表id
    private String user_id;//对话人的id
    private String user_name;//对话人的名称
    private String user_head;//对话人的头像
    private String login_id;//当前登录用户的id
    private String msg;//内容
    private String time;//时间
    private String type;//类型


    @Generated(hash = 1727725777)
    public ChatPojo(Long id, String user_id, String user_name, String user_head,
            String login_id, String msg, String time, String type) {
        this.id = id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_head = user_head;
        this.login_id = login_id;
        this.msg = msg;
        this.time = time;
        this.type = type;
    }

    @Generated(hash = 1418529328)
    public ChatPojo() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }

    public String getUser_head() {
        return user_head;
    }

    public void setUser_head(String user_head) {
        this.user_head = user_head;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
