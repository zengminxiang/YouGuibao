package com.zmx.youguibao.ui;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.zmx.youguibao.BaseActivity;
import com.zmx.youguibao.JPush.JPushReceiver;
import com.zmx.youguibao.R;
import com.zmx.youguibao.SharePreferenceUtil;
import com.zmx.youguibao.adapter.ChatListAdapter;
import com.zmx.youguibao.adapter.MessageCommentAdapter;
import com.zmx.youguibao.dao.ChatListMessageDao;
import com.zmx.youguibao.mvp.bean.ChatMessagePojo;
import com.zmx.youguibao.mvp.bean.VideoCommentJson;
import com.zmx.youguibao.mvp.presenter.UserPresenter;

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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_char;
    }

    @Override
    protected void initViews() {

        JPushReceiver.chatListLiseners.add(this);
        lists = dao.selectAllChatlist(SharePreferenceUtil.getInstance(this).getString(SharePreferenceUtil.u_id,""));
        Log.e("da","da"+lists.size());
        mListView = (ListView) this.findViewById(R.id.test_list_view);
        adapter = new ChatListAdapter(mActivity,lists);
        adapter.setListView(mListView);
        mListView.setAdapter(adapter);

    }


    @Override
    public void onServerChatList(ChatMessagePojo chat) {

        // 更新界面
        adapter.updateItemData(chat);

    }
}
