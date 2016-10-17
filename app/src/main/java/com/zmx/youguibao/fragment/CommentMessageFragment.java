package com.zmx.youguibao.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.zmx.youguibao.BaseFragment;
import com.zmx.youguibao.MyApplication;
import com.zmx.youguibao.R;
import com.zmx.youguibao.SharePreferenceUtil;
import com.zmx.youguibao.adapter.MessageCommentAdapter;
import com.zmx.youguibao.mvp.bean.MessageCountPojo;
import com.zmx.youguibao.mvp.bean.VideoCommentJson;
import com.zmx.youguibao.mvp.presenter.UserPresenter;
import com.zmx.youguibao.mvp.view.MessageView;

import java.util.ArrayList;
import java.util.List;

import greenDao.MessageCountPojoDao;

/**
 *作者：胖胖祥
 *时间：2016/10/10 0010 下午 3:35
 *功能模块：评论的消息列表
 */
public class CommentMessageFragment extends BaseFragment implements MessageView{

    private UserPresenter up;

    //评论区
    private PtrClassicFrameLayout mPtrFrame;
    private ListView mListView;
    private MessageCommentAdapter adapter;
    private List<VideoCommentJson> listss = new ArrayList<>();

    private int page = 1;//当前页数
    private int pagenow;//总页数
    private int load_tag = 0;//上拉或者下拉标示、

    private MessageCountPojoDao dao = MyApplication.getInstance().getDaoSession().getMessageCountPojoDao();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.message_comment_fragment, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    protected void initView() {

        UpdateMessageCount();//重置未读消息
        up = new UserPresenter(this.getActivity(),this);
        mListView = (ListView) this.findViewById(R.id.test_list_view);
        adapter = new MessageCommentAdapter(mActivity,listss);
        mListView.setAdapter(adapter);
        mPtrFrame = (PtrClassicFrameLayout) this.findViewById(R.id.test_list_view_frame);

        //下拉刷新支持时间
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        //下拉刷新一些设置 详情参考文档
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        // default is false
        mPtrFrame.setPullToRefresh(false);
        // default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);
        //进入Activity就进行自动下拉刷新
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {

                mPtrFrame.autoRefresh();

            }
        }, 100);

        //下拉刷新
        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

                listss.clear();
                page = 1;
                load_tag = 0;
                up.SelectMessageComment("QueryCommentMessage",page+"", SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.u_id,""));


            }
        });

        //上拉加载
        mPtrFrame.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                load_tag = 1;
                if (page == pagenow) {

                    Toast.makeText(mActivity, "没有更多数据", Toast.LENGTH_SHORT)
                            .show();
                    mPtrFrame.loadMoreComplete(true);
                    mPtrFrame.setLoadMoreEnable(false);

                } else {

                    page++;
                    up.SelectMessageComment("QueryCommentMessage",page+"", SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.u_id,""));


                }
            }
        });

         }

    @Override
    public void SelectMessageComment(List<VideoCommentJson> lists, String pagenow) {

        this.pagenow = Integer.parseInt(pagenow);
        for (int i=0;i<lists.size();i++){

            listss.add(lists.get(i));
        }
        handler.sendEmptyMessage(1);

    }

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case 1:
                    initAdapter();
                    break;

            }

        }
    };

    /**
     * 更新适配器数据
     */
    public void initAdapter() {

        adapter.notifyDataSetChanged();
        mPtrFrame.refreshComplete();
        if (load_tag == 0) {

            mPtrFrame.setLoadMoreEnable(true);

        } else {

            mPtrFrame.loadMoreComplete(true);

        }

    }

    //修改存储未读信息的个数为0条
    public void UpdateMessageCount(){

        MessageCountPojo count = SelectMessageCount(2);
        count.setCount(0);
        dao.update(count);//重新更新未读消息写入到数据库

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


}
