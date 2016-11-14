package com.zmx.youguibao.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
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
import com.zmx.youguibao.mvp.bean.ChatMessagePojo;
import com.zmx.youguibao.mvp.bean.ChatPojo;
import com.zmx.youguibao.utils.UrlConfig;

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

public class ChatActivity extends BaseActivity implements JPushReceiver.ServerChat {

    private ChatListMessageDao ListDao = new ChatListMessageDao();
    private ChatDao chatDao = new ChatDao();

    private ListView listView;
    private ChatAdapter adapter;
    private List<ChatPojo> list = new ArrayList<>();;

    private EditText context;
    private Button send;

    private String user_name;//对方的名字
    private String user_id;//对方的id
    private String user_head;//对方的头像

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initViews() {

        // 沉浸式状态栏
        positionView = findViewById(R.id.position_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            int statusBarHeight = getStatusBarHeight();
            ViewGroup.LayoutParams lp = positionView.getLayoutParams();
            lp.height = statusBarHeight;
            positionView.setLayoutParams(lp);

        }
        head_title = (TextView) findViewById(R.id.head_title);
        head_left = (ImageView) findViewById(R.id.head_left);
        head_left.setOnClickListener(this);

        JPushReceiver.chatListeners.add(this);//设置新消息监听
        Intent intent = getIntent();
        user_name = intent.getStringExtra("user_name");
        user_id = intent.getStringExtra("user_id");
        user_head = intent.getStringExtra("user_head");
        head_title.setText(user_name);

        context = (EditText) findViewById(R.id.context);
        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.listview);
        list = chatDao.SelectAllChat(MyApplication.getU_id(),user_id);
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

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {

            case R.id.send:

                SendMessage();
                ChatMessagePojo chat = new ChatMessagePojo();
                chat.setLogin_id(MyApplication.getU_id());
                chat.setUser_name(user_name);
                chat.setUser_head(user_head);
                chat.setUser_id(user_id);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                chat.setTime(df.format(new Date()));
                chat.setContent(context.getText().toString());
                chat.setNo_reading("0");
//                添加到会话列表
                ListDao.addChatList(chat);

                ChatPojo chatPojo = new ChatPojo();
                chatPojo.setLogin_id(MyApplication.getU_id());
                SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                chatPojo.setTime(dff.format(new Date()));
                chatPojo.setType("1");
                chatPojo.setMsg(context.getText().toString());
                chatPojo.setUser_name(user_name);
                chatPojo.setUser_head(user_head);
                chatPojo.setUser_id(user_id);
                chatDao.addChatmessage(chatPojo);//添加到数据库记录表
                list.add(chatPojo);

                context.setText("");
                handler.sendEmptyMessage(1);

                break;

            //关闭
            case R.id.head_left:

                InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputmanger.hideSoftInputFromWindow(v.getWindowToken(), 0);
                onBackPressed();

                break;

        }

    }

    //发送消息
    public void SendMessage() {

        String login_id = MyApplication.getU_id();
        String login_name = MyApplication.getU_name();
        String login_head = MyApplication.getU_headurl();

        JsonObjectRequest reqA = new JsonObjectRequest(Request.Method.GET, UrlConfig.SendMessage("chat",user_id, login_id, login_name, login_head, context.getText().toString()), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("reqA","reqA");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {


                Log.e("reqA","volleyError"+volleyError.toString());

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
        if(user_id.equals(chatPojo.getUser_id())){

            list.add(chatPojo);
            handler.sendEmptyMessage(1);

        }


    }
}