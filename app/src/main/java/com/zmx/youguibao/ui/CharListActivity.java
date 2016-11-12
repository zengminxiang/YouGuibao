package com.zmx.youguibao.ui;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.zmx.youguibao.BaseActivity;
import com.zmx.youguibao.JPush.JPushReceiver;
import com.zmx.youguibao.MyApplication;
import com.zmx.youguibao.R;
import com.zmx.youguibao.SharePreferenceUtil;
import com.zmx.youguibao.adapter.ChatListAdapter;
import com.zmx.youguibao.adapter.MessageCommentAdapter;
import com.zmx.youguibao.dao.ChatListMessageDao;
import com.zmx.youguibao.dao.MessageDao;
import com.zmx.youguibao.mvp.bean.ChatMessagePojo;
import com.zmx.youguibao.mvp.bean.VideoCommentJson;
import com.zmx.youguibao.mvp.presenter.UserPresenter;
import com.zmx.youguibao.utils.view.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 开发者：胖胖祥
 * 时间：2016/11/3 9:58
 * 功能模块：聊天记录
 *
 */
public class CharListActivity extends BaseActivity implements JPushReceiver.ServerChatList {


    private ListView mListView;
    private ChatListAdapter adapter;
    private List<ChatMessagePojo> lists = new ArrayList<>();

    private ChatListMessageDao dao = new ChatListMessageDao();
    private MessageDao messageDao = new MessageDao();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_char;
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
        head_title.setText("会话列表");
        head_left = (ImageView) findViewById(R.id.head_left);
        head_left.setOnClickListener(this);

        messageDao.UpdateMessageCount(5);//重置未读消息
        JPushReceiver.chatListLiseners.add(this);
        lists = dao.selectAllChatlist(MyApplication.getU_id());
        mListView = (ListView) this.findViewById(R.id.test_list_view);
        adapter = new ChatListAdapter(mActivity,lists);
        adapter.setListView(mListView);
        mListView.setAdapter(adapter);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.e("sssss","sssss");

                return false;
            }
        });

    }


    @Override
    public void onServerChatList(ChatMessagePojo chat) {

        // 更新界面
        adapter.updateItemData(chat);

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){

            //关闭
            case R.id.head_left:

                InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputmanger.hideSoftInputFromWindow(v.getWindowToken(), 0);
                onBackPressed();

                break;

        }

    }

}
