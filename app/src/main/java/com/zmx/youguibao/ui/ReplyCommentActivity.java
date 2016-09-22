package com.zmx.youguibao.ui;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zmx.youguibao.BaseActivity;
import com.zmx.youguibao.R;
import com.zmx.youguibao.adapter.CommentAdapter;
import com.zmx.youguibao.adapter.ReplyCommentAdapter;
import com.zmx.youguibao.mvp.bean.ReplyCommentJson;
import com.zmx.youguibao.mvp.bean.VideoCommentJson;
import com.zmx.youguibao.mvp.presenter.UploadVideoPresenter;
import com.zmx.youguibao.mvp.view.ReplyOneCommentView;
import com.zmx.youguibao.utils.UrlConfig;
import com.zmx.youguibao.utils.view.ImageLoadOptions;
import com.zmx.youguibao.utils.view.ImageViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/9/5 0005 下午 2:44
 *功能模块：回复评论的列表
 */
public class ReplyCommentActivity extends BaseActivity implements ReplyOneCommentView{

    private TextView name,time,comtext;
    private ImageViewUtil head;
    private ImageView comment_button;
    private String vcid;

    private ReplyCommentAdapter adapter;
    private PtrClassicFrameLayout ptrClassicFrameLayout;
    private VideoCommentJson commentJson = new VideoCommentJson();
    private List<ReplyCommentJson> listss = new ArrayList<>();
    private ListView mListView;
    private View headview;

    private UploadVideoPresenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reply_comment;
    }

    @Override
    protected void initViews() {

        vcid = getIntent().getStringExtra("vcid");

        presenter = new UploadVideoPresenter(this,this);
        presenter.QueryOneComment("QueryOneComment",vcid);

        headview =  View.inflate(this, R.layout.reply_comment_title, null);
        ptrClassicFrameLayout = (PtrClassicFrameLayout) this.findViewById(R.id.test_list_view_frame);
        mListView = (ListView) this.findViewById(R.id.test_list_view);
        mListView.addHeaderView(headview);
        adapter = new ReplyCommentAdapter(this,listss);
        mListView.setAdapter(adapter);

        ptrClassicFrameLayout.setLoadMoreEnable(true);
        //上拉
        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

                ptrClassicFrameLayout.refreshComplete();

                if (!ptrClassicFrameLayout.isLoadMoreEnable()) {
                    ptrClassicFrameLayout.setLoadMoreEnable(true);
                }
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return false;
            }
        });

        //下拉
        ptrClassicFrameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        adapter.notifyDataSetChanged();
                        ptrClassicFrameLayout.loadMoreComplete(true);

                    }
                }, 1000);

            }
        });


        name = (TextView) headview.findViewById(R.id.video_comment_item_name);
        comtext = (TextView) headview.findViewById(R.id.comtext);
        time = (TextView) headview.findViewById(R.id.time);
        head = (ImageViewUtil) headview.findViewById(R.id.head);
        comment_button = (ImageView) headview.findViewById(R.id.comment_button);

    }

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case 1:

                    name.setText(commentJson.getU_name());
                    comtext.setText(commentJson.getVc_content());
                    time.setText(commentJson.getVc_time());
                    ImageLoader.getInstance().displayImage(UrlConfig.HEAD+commentJson.getU_head(), head,
                            ImageLoadOptions.getOptions());

                    for (ReplyCommentJson replys:commentJson.getReplylist()) {

                        listss.add(replys);

                    }

                    adapter.notifyDataSetChanged();

                    break;

            }

        }
    };

    @Override
    public void QureyOneComment(VideoCommentJson comment) {

        this.commentJson = comment;
        handler.sendEmptyMessage(1);


    }
}
