package com.zmx.youguibao.dao;

import android.util.Log;

import com.zmx.youguibao.MyApplication;
import com.zmx.youguibao.mvp.bean.MessageCountPojo;

import java.util.List;

import greenDao.MessageCountPojoDao;

/**
 *
 * 开发者：胖胖祥
 * 时间：2016/11/3 13:04
 * 功能模块：未读消息提示处理
 *
 */
public class MessageDao {

    private MessageCountPojoDao dao = MyApplication.getInstance().getDaoSession().getMessageCountPojoDao();

    //修改存储未读信息的个数为0条
    public void UpdateMessageCount(int type){

        MessageCountPojo count = SelectMessage(type);
        count.setCount(0);
        dao.update(count);//重新更新未读消息写入到数据库

    }

    //修改消息未读的数量
    public void UpdateMessage(MessageCountPojo count){
        dao.update(count);//重新更新未读消息写入到数据库
    }

    /**
     * 查询信息,用于修改未读个数为0
     * @param type 消息的类型
     */
    public MessageCountPojo SelectMessage(int type) {

        MessageCountPojo count = null;

        List<MessageCountPojo> lmcps = dao.queryBuilder()
                .where(MessageCountPojoDao.Properties.Type.eq(type))
                .orderAsc(MessageCountPojoDao.Properties.Type)
                .list();

        for (MessageCountPojo m : lmcps) {

            count = m;

        }

        return count;

    }

    /**
     * 查询本地缓存了多少条未读消息
     * @param type  消息的类型
     */
    public int SelectMessageCount(int type){

        int counts = 0;
        MessageCountPojo count = null;
        List<MessageCountPojo> lmcps = dao.queryBuilder()
                .where(MessageCountPojoDao.Properties.Type.eq(type))
                .orderAsc(MessageCountPojoDao.Properties.Type)
                .list();

        for(MessageCountPojo m:lmcps){

            count = m;

        }

        if(count == null){

            insertCount(type,0);//查询是否有未读消息类型了，没有的话就添加进去

        }else {

            counts = count.getCount();

        }

        return counts;

    }

    /**
     * 插入数据
     * @param type
     * @param count
     *
     */
    public void insertCount(int type,int count){

        MessageCountPojo mcp = new MessageCountPojo();
        mcp.setCount(count);
        mcp.setType(type);
        dao.insert(mcp);

    }



}
