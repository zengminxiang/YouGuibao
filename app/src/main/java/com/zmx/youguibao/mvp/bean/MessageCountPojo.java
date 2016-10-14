package com.zmx.youguibao.mvp.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 *作者：胖胖祥
 *时间：2016/10/13 0013 上午 11:03
 *功能模块：未读消息数量
 */
@Entity
public class MessageCountPojo {

    @Id(autoincrement = true)
    private Long id;
    private int count;//未读数量
    private int type;//类型(1为公告，2为评论，3为点赞，4为关注，5为聊天)

    @Generated(hash = 2046929930)
    public MessageCountPojo(Long id, int count, int type) {
        this.id = id;
        this.count = count;
        this.type = type;
    }

    @Generated(hash = 1131816504)
    public MessageCountPojo() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
