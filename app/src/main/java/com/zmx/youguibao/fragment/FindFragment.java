package com.zmx.youguibao.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.zmx.youguibao.BaseFragment;
import com.zmx.youguibao.R;
import com.zmx.youguibao.adapter.VideoAdapter;
import com.zmx.youguibao.mvp.bean.VideoListJson;
import com.zmx.youguibao.mvp.presenter.UploadVideoPresenter;
import com.zmx.youguibao.mvp.view.VideoListView;
import com.zmx.youguibao.ui.VideoDetailsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：胖胖祥
 * 时间：2016/8/16 0016 下午 2:21
 * 功能模块：发现的界面
 */
public class FindFragment extends BaseFragment implements VideoListView {

    private RecyclerView mRecyclerView;
    //支持下拉刷新的ViewGroup
    private PtrClassicFrameLayout mPtrFrame;
    //RecyclerView自定义Adapter
    private VideoAdapter adapter;
    //添加Header和Footer的封装类
    private RecyclerAdapterWithHF mAdapter;

    private UploadVideoPresenter presenter;
    private List<VideoListJson> lists = new ArrayList<>();//数据
    private int page = 1;//当前页数
    private int pagenow;//总页数
    private int load_tag = 0;//上拉或者下拉标示


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_find, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    protected void initView() {

        presenter = new UploadVideoPresenter(this, this.getActivity());
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new VideoAdapter(this.getActivity(), lists);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("video",lists.get(position));
                startActivity(VideoDetailsActivity.class,bundle);

            }
        });

        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.rotate_header_list_view_frame);
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

                lists.clear();
                page = 1;
                load_tag = 0;
                presenter.QueryAllVideo("queryAll", page + "");
            }
        });

        //上拉加载
        mPtrFrame.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {

                load_tag = 1;
                if (page == pagenow) {

                    Toast.makeText(FindFragment.this.getActivity(), "没有更多数据", Toast.LENGTH_SHORT)
                            .show();
                    mPtrFrame.loadMoreComplete(true);
                    mPtrFrame.setLoadMoreEnable(false);

                } else {

                    page++;
                    presenter.QueryAllVideo("queryAll", page + "");

                }
            }
        });
    }

    /**
     * 更新适配器数据
     */
    public void initAdapter() {

        mAdapter.notifyDataSetChanged();
        mPtrFrame.refreshComplete();
        if (load_tag == 0) {

            mPtrFrame.setLoadMoreEnable(true);

        } else {

            mPtrFrame.loadMoreComplete(true);

        }

    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 1:
                    initAdapter();
                    break;

            }

        }
    };

    @Override
    public void AllVideoList(List<VideoListJson> lists, String pagenow) {

        for (VideoListJson v : lists) {
            this.lists.add(v);
        }
        this.pagenow = Integer.parseInt(pagenow);
        handler.sendEmptyMessage(1);

    }
}

