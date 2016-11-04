package com.zmx.youguibao.JPush;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import com.zmx.youguibao.MyApplication;
import com.zmx.youguibao.SharePreferenceUtil;
import com.zmx.youguibao.dao.ChatDao;
import com.zmx.youguibao.dao.ChatListMessageDao;
import com.zmx.youguibao.dao.MessageDao;
import com.zmx.youguibao.mvp.bean.ChatMessagePojo;
import com.zmx.youguibao.mvp.bean.ChatPojo;
import com.zmx.youguibao.mvp.bean.MessageCountPojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import greenDao.MessageCountPojoDao;

/**
 * 作者：胖胖祥
 * 时间：2016/10/12 0012 上午 9:43
 * 功能模块：极光推送自定义服务
 */
public class JPushReceiver extends BroadcastReceiver {

    private static final String TAG = "MyReceiver";

    private NotificationManager nm;
    private Context context;

    private MessageDao MessageDao = new MessageDao();
    private ChatListMessageDao listDao = new ChatListMessageDao();
    private ChatDao chatDao = new ChatDao();

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Bundle bundle = intent.getExtras();

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            Log.e(TAG, "JPush用户注册成功");

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.e(TAG, "接受到推送下来的自定义消息");

            //获取后端返回的消息类型
            String jsonType = bundle.getString(JPushInterface.EXTRA_EXTRA);//获取短信的类型
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);//获取短信的内容
            if (!jsonType.equals("") || jsonType != null) {

                try {

                    JSONObject jsonObject = new JSONObject(jsonType);
                    String type = jsonObject.getString("type");

                    if (type.equals("5")) {

                        JSONObject jsonMessage = new JSONObject(message);

                        //如果当前用户没有在会话列表中就添加
                        ChatMessagePojo chat = new ChatMessagePojo();
                        chat.setLogin_id(SharePreferenceUtil.getInstance(context).getString(SharePreferenceUtil.u_id, ""));
                        chat.setUser_name(jsonMessage.getString("login_name"));
                        chat.setUser_head(jsonMessage.getString("login_head"));
                        chat.setUser_id(jsonMessage.getString("login_id"));
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        chat.setTime(df.format(new Date()));
                        chat.setContent(jsonMessage.getString("content"));
                        chat.setNo_reading("0");
                        //添加到会话列表
                        listDao.addChatList(chat);

                        //把消息加入到聊天记录中
                        ChatPojo chatPojo = new ChatPojo();
                        chatPojo.setLogin_id(SharePreferenceUtil.getInstance(context).getString(SharePreferenceUtil.u_id, ""));
                        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        chatPojo.setTime(dff.format(new Date()));
                        chatPojo.setType("0");
                        chatPojo.setMsg(jsonMessage.getString("content"));
                        chatPojo.setUser_name(jsonMessage.getString("login_name"));
                        chatPojo.setUser_head(jsonMessage.getString("login_head"));
                        chatPojo.setUser_id(jsonMessage.getString("login_id"));
                        chatDao.addChatmessage(chatPojo);//添加到数据库记录表
                        // 回调函数
                        for (int i = 0; i < chatListeners.size(); i++)
                            chatListeners.get(i).onServerChat(chatPojo);

                    } else {

                        //查询本地数据库，拿到消息的类型，进行更改未读消息
                        MessageCountPojo count = MessageDao.SelectMessage(Integer.parseInt(type));
                        int counts = count.getCount();
                        counts++;
                        count.setCount(counts);
                        MessageDao.UpdateMessage(count);//重新更新未读消息写入到数据库

                        // 回调函数
                        for (int i = 0; i < msgListeners.size(); i++)
                            msgListeners.get(i).onServerMessage(counts);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.e(TAG, "接受到推送下来的通知");
            receivingNotification(context, bundle);

        } else {

            Log.e(TAG, "Unhandled intent - " + intent.getAction());

        }

    }


    private String receivingNotification(Context context, Bundle bundle) {
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        Log.e(TAG, " title : " + title);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        Log.e(TAG, "message : " + message);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.e(TAG, "extras : " + extras);

        return extras;
    }


    // 消息回调接口
    public static ArrayList<ServerMessage> msgListeners = new ArrayList<ServerMessage>();

    public interface ServerMessage {

        void onServerMessage(int count);//未读消息

    }

    //聊天消息回调接口
    public static ArrayList<ServerChat> chatListeners = new ArrayList<>();

    public interface ServerChat{
        void onServerChat(ChatPojo chatPojo);//更新新消息
    }


}
