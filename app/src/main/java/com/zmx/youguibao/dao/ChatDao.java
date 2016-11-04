package com.zmx.youguibao.dao;

import android.util.Log;

import com.zmx.youguibao.MyApplication;
import com.zmx.youguibao.mvp.bean.ChatPojo;

import java.util.List;

import greenDao.ChatPojoDao;

/**
 *
 * 开发者：胖胖祥
 * 时间：2016/11/4 13:36
 * 功能模块：聊天
 *
 */
public class ChatDao {

    private ChatPojoDao dao = MyApplication.getInstance().getDaoSession().getChatPojoDao();


    /**
     * 分页查询聊天记录
     * @param
     * @return
     */
    public List<ChatPojo> SelectAllChat(String login_id,String user_id){

        return dao.queryBuilder().where(ChatPojoDao.Properties.Login_id.eq(login_id),ChatPojoDao.Properties.User_id.eq(user_id)).build().list();

    }

    /**
     * 插入聊天记录
     * @param chat
     */
    public void addChatmessage(ChatPojo chat){

        Log.e("进来插入数据","进来插入数据");

        dao.insert(chat);

    }


}
