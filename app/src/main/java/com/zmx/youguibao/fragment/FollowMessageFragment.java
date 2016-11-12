package com.zmx.youguibao.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
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
import com.zmx.youguibao.adapter.MessageFollowAdapter;
import com.zmx.youguibao.dao.MessageDao;
import com.zmx.youguibao.mvp.bean.FollowUserPojo;
import com.zmx.youguibao.mvp.bean.MessageCountPojo;
import com.zmx.youguibao.mvp.bean.VideoCommentJson;
import com.zmx.youguibao.mvp.bean.VideoListJson;
import com.zmx.youguibao.mvp.presenter.UserPresenter;
import com.zmx.youguibao.mvp.view.FollowFragmentView;
import com.zmx.youguibao.mvp.view.MessageFollowsView;

import java.util.ArrayList;
import java.util.List;

import greenDao.MessageCountPojoDao;

/**
 *
 * 开发者：胖胖祥
 * 时间：2016/11/1 16:08
 * 功能模块：关注的未读消息
 *
 */
public class FollowMessageFragment  extends BaseFragment implements MessageFollowsView{

    private UserPresenter up;

    //评论区
    private PtrClassicFrameLayout mPtrFrame;
    private ListView mListView;
    private MessageFollowAdapter adapter;
    private List<FollowUserPojo> listss = new ArrayList<>();

    private int page = 1;//当前页数
    private int pagenow;//总页数
    private int load_tag = 0;//上拉或者下拉标示、

    private MessageDao dao = new MessageDao();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.follow_comment_fragment, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    protected void initView() {

        dao.UpdateMessageCount(4);//重置未读消息
        up = new UserPresenter(this.getActivity(),this);
        mListView = (ListView) this.findViewById(R.id.test_list_view);
        adapter = new MessageFollowAdapter(mActivity,listss);
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
                up.SelectFollowsMessage("QueryFollowMessage",page+"", MyApplication.getU_id());


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
                    up.SelectFollowsMessage("QueryFollowMessage",page+"", MyApplication.getU_id());


                }
            }
        });

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


    @Override
    public void SelectMessageComment(List<FollowUserPojo> lists, String pagenow) {

        this.pagenow = Integer.parseInt(pagenow);
        for (int i=0;i<lists.size();i++){

            listss.add(lists.get(i));
        }
        handler.sendEmptyMessage(1);

    }


}
