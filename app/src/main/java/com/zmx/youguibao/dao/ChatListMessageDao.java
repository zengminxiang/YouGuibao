package com.zmx.youguibao.dao;

import android.util.Log;

import com.zmx.youguibao.MyApplication;
import com.zmx.youguibao.mvp.bean.ChatMessagePojo;

import java.util.List;

import greenDao.ChatMessagePojoDao;

/**
 * 开发者：胖胖祥
 * 时间：2016/11/3 13:19
 * 功能模块：聊天列表操作的dao
 */
public class ChatListMessageDao {

    private ChatMessagePojoDao dao = MyApplication.getInstance().getDaoSession().getChatMessagePojoDao();

    /**
     * 插入一个聊天记录
     *
     * @param chat
     */
    public void addChatList(ChatMessagePojo chat) {

        //先查询当前的记录是不是已经在回话中了
        ChatMessagePojo c = selectChatlist(chat.getUser_id());

        //如果等于null就可以插入最新聊天记录，有的的话就更新聊天的内容
        if(c == null){

            dao.insert(chat);

        }else {

            //更新最新的未读消息
            c.setContent(chat.getContent());
            c.setTime(chat.getTime());
            UpdateChatContent(c);

        }

    }

    /**
     * 根据用户id查询数据
     */
    public ChatMessagePojo selectChatlist(String uid) {

        return dao.queryBuilder().where(ChatMessagePojoDao.Properties.User_id.eq(uid)).build().unique();
    }

    /**
     * 查询所有聊天列表
     * @return
     */
    public List<ChatMessagePojo> selectAllChatlist(String login_id){

        return dao.queryBuilder().where(ChatMessagePojoDao.Properties.Login_id.ge(login_id)).build().list();

    }

    /**
     * 修改最新未读消息的提示
     * @param c
     */
    public void UpdateChatContent(ChatMessagePojo c){

        dao.update(c);


    }

}
