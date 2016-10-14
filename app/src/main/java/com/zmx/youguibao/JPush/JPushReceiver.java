package com.zmx.youguibao.JPush;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import com.zmx.youguibao.DBManager;
import com.zmx.youguibao.MyApplication;
import com.zmx.youguibao.mvp.bean.MessageCountPojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import greenDao.DaoMaster;
import greenDao.DaoSession;
import greenDao.MessageCountPojoDao;

/**
 * 作者：胖胖祥
 * 时间：2016/10/12 0012 上午 9:43
 * 功能模块：极光推送自定义服务
 */
public class JPushReceiver extends BroadcastReceiver {

    private static final String TAG = "MyReceiver";

    private NotificationManager nm;

    private MessageCountPojoDao dao = MyApplication.getInstance().getDaoSession().getMessageCountPojoDao();

    @Override
    public void onReceive(Context context, Intent intent) {

        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Bundle bundle = intent.getExtras();

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            Log.e(TAG, "JPush用户注册成功");

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.e(TAG, "接受到推送下来的自定义消息");

            //获取后端返回的消息类型
            String json = receivingNotification(context, bundle);

            Log.e("json", "json  " + json);

            if (!json.equals("") || json != null) {

                try {

                    JSONObject jsonObject = new JSONObject(json);
                    String type = jsonObject.getString("type");

                    MessageCountPojo count = SelectMessageCount(Integer.parseInt(type));

                    int counts = count.getCount();
                    counts++;

                    Log.e("更新未读消息", "： " + counts);
                    count.setCount(counts);
                    dao.update(count);//重新更新未读消息写入到数据库

                    // 回调函数
                    for (int i = 0; i < msgListeners.size(); i++)
                        msgListeners.get(i).onServerMessage(counts);


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
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        Log.e(TAG, "message : " + message);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.e(TAG, "extras : " + extras);

        return extras;
    }

    /**
     * 查询信息
     *
     * @param type 消息的类型
     */
    public MessageCountPojo SelectMessageCount(int type) {

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


    // 系统消息回调接口
    public static ArrayList<ServerMessage> msgListeners = new ArrayList<ServerMessage>();

    public interface ServerMessage {

        void onServerMessage(int count);//未读消息

    }


}
