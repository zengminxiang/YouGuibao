package com.zmx.youguibao.mvp.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 *
 * 开发者：胖胖祥
 * 时间：2016/11/3 9:59
 * 功能模块：聊天记录
 *
 */
@Entity
public class ChatMessagePojo implements Serializable{

    @Id(autoincrement = true)
    private Long id;//表id
    private String login_id;//当前登录的用户
    private String user_id;//聊天对方的id
    private String user_name;//名称
    private String user_head;//头像
    private String content;//最新的那条聊天记录
    private String time;//时间
    private String no_reading;//未读消息数

    @Generated(hash = 909401621)
    public ChatMessagePojo(Long id, String login_id, String user_id,
            String user_name, String user_head, String content, String time,
            String no_reading) {
        this.id = id;
        this.login_id = login_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_head = user_head;
        this.content = content;
        this.time = time;
        this.no_reading = no_reading;
    }

    @Generated(hash = 709992937)
    public ChatMessagePojo() {
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

    public String getUser_head() {
        return user_head;
    }

    public void setUser_head(String user_head) {
        this.user_head = user_head;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLogin_id() {
        return this.login_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getNo_reading() {
        return no_reading;
    }

    public void setNo_reading(String no_reading) {
        this.no_reading = no_reading;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }
}
