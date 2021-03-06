package com.zmx.youguibao.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zmx.youguibao.BaseActivity;
import com.zmx.youguibao.JPush.JPushReceiver;
import com.zmx.youguibao.MyApplication;
import com.zmx.youguibao.R;
import com.zmx.youguibao.SharePreferenceUtil;
import com.zmx.youguibao.adapter.ChatAdapter;
import com.zmx.youguibao.dao.ChatDao;
import com.zmx.youguibao.dao.ChatListMessageDao;
import com.zmx.youguibao.emoticon.boardview.EmotionKeyboard;
import com.zmx.youguibao.emoticon.fragment.EmotionMainFragment;
import com.zmx.youguibao.mvp.bean.ChatMessagePojo;
import com.zmx.youguibao.mvp.bean.ChatPojo;
import com.zmx.youguibao.utils.UrlConfig;
import com.zmx.youguibao.utils.view.StatusBarUtil;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 开发者：胖胖祥
 * 时间：2016/11/3 19:25
 * 功能模块：聊天界面
 */

public class ChatActivity extends BaseActivity implements JPushReceiver.ServerChat, EmotionKeyboard.ChatCommentLiseners {

    private ChatListMessageDao ListDao = new ChatListMessageDao();
    private ChatDao chatDao = new ChatDao();

    private ListView listView;
    private ChatAdapter adapter;
    private List<ChatPojo> list = new ArrayList<>();

    private String user_name;//对方的名字
    private String user_id;//对方的id
    private String user_head;//对方的头像

    private EmotionMainFragment emotionMainFragment;
    private EmotionKeyboard ek;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initViews() {

        // 沉浸式状态栏
        StatusBarUtil.setColor(this, getResources().getColor(R.color.title_bage), 0);
        head_title = (TextView) findViewById(R.id.head_title);
        head_left = (ImageView) findViewById(R.id.head_left);
        head_left.setOnClickListener(this);

        JPushReceiver.chatListeners.add(this);//设置新消息监听
        ek = new EmotionKeyboard();
        ek.setChatCommentLiseners(this);
        Intent intent = getIntent();
        user_name = intent.getStringExtra("user_name");
        user_id = intent.getStringExtra("user_id");
        user_head = intent.getStringExtra("user_head");
        head_title.setText(user_name);

        listView = (ListView) findViewById(R.id.listview);
        list = chatDao.SelectAllChat(MyApplication.getU_id(), user_id);
        initEmotionMainFragment();
        info();

    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 1:
                    info();

                    break;

            }

        }
    };


    // 更新显示listview
    public void info() {

        if (adapter == null) {

            adapter = new ChatAdapter(this, list);
            listView.setAdapter(adapter);

        } else {

            adapter.notifyDataSetChanged();

        }

        listView.setSelection(list.size()-1);//控制消息保持在底部

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {

            //关闭
            case R.id.head_left:

                this.finish();
                break;

        }

    }

    //发送消息
    public void SendMessage(String comment) {

        String login_id = MyApplication.getU_id();
        String login_name = MyApplication.getU_name();
        String login_head = MyApplication.getU_headurl();

        JsonObjectRequest reqA = new JsonObjectRequest(Request.Method.GET, UrlConfig.SendMessage("chat", user_id, login_id, login_name, login_head, comment), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("reqA", "reqA");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {


                Log.e("reqA", "volleyError" + volleyError.toString());

            }
        });

        reqA.setTag("reqA");
        MyApplication.getHttpQueues().add(reqA);
        reqA.setShouldCache(true); // 控制是否缓存

    }

    //消息进来监听
    @Override
    public void onServerChat(ChatPojo chatPojo) {

        //判断当前对方聊天的id
        if (user_id.equals(chatPojo.getUser_id())) {
            list.add(chatPojo);
            handler.sendEmptyMessage(1);

        }


    }

    /**
     * 初始化表情面板
     */
    public void initEmotionMainFragment() {
        //构建传递参数
        Bundle bundle = new Bundle();
        //绑定主内容编辑框
        bundle.putBoolean(EmotionMainFragment.BIND_TO_EDITTEXT, true);
        //隐藏控件
        bundle.putBoolean(EmotionMainFragment.HIDE_BAR_EDITTEXT_AND_BTN, false);
        //替换fragment
        //创建修改实例
        emotionMainFragment = EmotionMainFragment.newInstance(EmotionMainFragment.class, bundle);
        emotionMainFragment.bindToContentView(listView);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_emotionview_main, emotionMainFragment);
        transaction.addToBackStack(null);
        //提交修改
        transaction.commit();
    }


    @Override
    public void onBackPressed() {

        /**
         * 判断是否拦截返回键操作
         */
        if (!emotionMainFragment.isInterceptBackPress()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onChatCommentLiseners(String comment) {

        SendMessage(comment);
        ChatMessagePojo chat = new ChatMessagePojo();
        chat.setLogin_id(MyApplication.getU_id());
        chat.setUser_name(user_name);
        chat.setUser_head(user_head);
        chat.setUser_id(user_id);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        chat.setTime(df.format(new Date()));
        chat.setContent(comment);
        chat.setNo_reading("0");
        ListDao.addChatList(chat);// 添加到会话列表

        ChatPojo chatPojo = new ChatPojo();
        chatPojo.setLogin_id(MyApplication.getU_id());
        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        chatPojo.setTime(dff.format(new Date()));
        chatPojo.setType("1");
        chatPojo.setMsg(comment);
        chatPojo.setUser_name(user_name);
        chatPojo.setUser_head(user_head);
        chatPojo.setUser_id(user_id);
        chatDao.addChatmessage(chatPojo);//添加到数据库记录表
        list.add(chatPojo);

        handler.sendEmptyMessage(1);

    }
}
