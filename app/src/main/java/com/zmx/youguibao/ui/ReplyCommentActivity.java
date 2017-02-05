package com.zmx.youguibao.ui;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zmx.youguibao.BaseActivity;
import com.zmx.youguibao.MyApplication;
import com.zmx.youguibao.R;
import com.zmx.youguibao.SharePreferenceUtil;
import com.zmx.youguibao.adapter.CommentAdapter;
import com.zmx.youguibao.adapter.ReplyCommentAdapter;
import com.zmx.youguibao.emoticon.boardview.EmotionKeyboard;
import com.zmx.youguibao.emoticon.fragment.EmotionMainFragment;
import com.zmx.youguibao.emoticon.utils.SpanStringUtils;
import com.zmx.youguibao.mvp.bean.ReplyCommentJson;
import com.zmx.youguibao.mvp.bean.VideoCommentJson;
import com.zmx.youguibao.mvp.presenter.UploadVideoPresenter;
import com.zmx.youguibao.mvp.view.ReplyOneCommentView;
import com.zmx.youguibao.utils.UrlConfig;
import com.zmx.youguibao.utils.Utils;
import com.zmx.youguibao.utils.view.ImageLoadOptions;
import com.zmx.youguibao.utils.view.ImageViewUtil;
import com.zmx.youguibao.utils.view.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/9/5 0005 下午 2:44
 *功能模块：回复评论的列表
 */
public class ReplyCommentActivity extends BaseActivity implements ReplyOneCommentView,EmotionKeyboard.ChatCommentLiseners {

    private TextView name,time,comtext;
    private ImageViewUtil head;
    private String vcid;
    private String vid;

    private ReplyCommentAdapter adapter;
    private PtrClassicFrameLayout ptrClassicFrameLayout;
    private VideoCommentJson commentJson = new VideoCommentJson();
    private List<ReplyCommentJson> listss = new ArrayList<>();
    private ListView mListView;
    private View headview;

    private UploadVideoPresenter presenter;

    private EmotionKeyboard ek;
    private EmotionMainFragment emotionMainFragment;

    private String reply_name = "";//被回复的用户昵称


    @Override
    protected int getLayoutId() {
        return R.layout.activity_reply_comment;
    }

    @Override
    protected void initViews() {

        // 沉浸式状态栏
        StatusBarUtil.setColor(this, getResources().getColor(R.color.title_bage), 0);

        head_title = (TextView) findViewById(R.id.head_title);
        head_title.setText("详情");
        head_left = (ImageView) findViewById(R.id.head_left);
        head_left.setOnClickListener(this);

        ek = new EmotionKeyboard();
        ek.setChatCommentLiseners(this);

        vcid = getIntent().getStringExtra("vcid");
        vid = getIntent().getStringExtra("vid");
        presenter = new UploadVideoPresenter(this,this);
        presenter.QueryOneComment("QueryOneComment",vcid);

        headview =  View.inflate(this, R.layout.reply_comment_title, null);
        ptrClassicFrameLayout = (PtrClassicFrameLayout) this.findViewById(R.id.test_list_view_frame);
        mListView = (ListView) this.findViewById(R.id.test_list_view);
        mListView.addHeaderView(headview);
        adapter = new ReplyCommentAdapter(this,listss,handlerAdapter);
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

        initEmotionMainFragment();

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){

            case R.id.head_left:
                this.finish();
                break;

        }

    }

    private Handler handlerAdapter = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case 1:

                    Bundle bundle = msg.getData();
                    reply_name = bundle.getString("name");
                    String id = bundle.getString("uid");
                    emotionMainFragment.setEdittextHint("回复 "+reply_name+" : ");

                    //关闭输入法或者表情
                    boolean mEmotionLayout =  emotionMainFragment.isViewShown();
                    if(mEmotionLayout){
                        emotionMainFragment.isInterceptBackPress();
                    }
                    Utils.showSoftInput(mActivity);

                    break;

            }

        }
    };

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case 1:

                    name.setText(commentJson.getU_name());
                    comtext.setText(SpanStringUtils.getEmotionContent(0x0001,
                            mActivity, comtext, commentJson.getVc_content()));
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

    @Override
    public void VReplyComment(ReplyCommentJson rcj) {

        toast("评论成功");

        boolean mEmotionLayout =  emotionMainFragment.isViewShown();
        if(mEmotionLayout){
            emotionMainFragment.isInterceptBackPress();
            emotionMainFragment.hideSoftInput();
        }

        listss.add(rcj);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onChatCommentLiseners(String comment) {

        presenter.ReplyComment("ReplyComment",vcid, MyApplication.getU_id(),reply_name,comment,vid);


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
        emotionMainFragment.bindToContentView(ptrClassicFrameLayout);
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

}
