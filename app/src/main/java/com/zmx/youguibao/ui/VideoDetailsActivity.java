package com.zmx.youguibao.ui;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zmx.youguibao.BaseActivity;
import com.zmx.youguibao.R;
import com.zmx.youguibao.SharePreferenceUtil;
import com.zmx.youguibao.adapter.CommentAdapter;
import com.zmx.youguibao.adapter.VideoLikeAdapter;
import com.zmx.youguibao.ijkplayer.playervideo.VideoPlayView;
import com.zmx.youguibao.mvp.bean.VideoCommentJson;
import com.zmx.youguibao.mvp.bean.VideoLikeJson;
import com.zmx.youguibao.mvp.bean.VideoListJson;
import com.zmx.youguibao.mvp.presenter.UploadVideoPresenter;
import com.zmx.youguibao.mvp.view.VideoDetailsView;
import com.zmx.youguibao.utils.UrlConfig;
import com.zmx.youguibao.utils.view.ImageLoadOptions;
import com.zmx.youguibao.utils.view.ImageViewUtil;
import com.zmx.youguibao.utils.view.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 作者：胖胖祥
 * 时间：2016/8/27 0027 上午 11:20
 * 功能模块：视频详情界面
 */
public class VideoDetailsActivity extends BaseActivity implements VideoDetailsView {

    //播放器 斤斤计较
    private FrameLayout frameLayout, full_screen;
    private VideoPlayView videoItemView;//播放的view
    private ImageView image_bg, follow, like;//视频的背景图，关注按钮，点赞按钮
    private RelativeLayout showview;
    private ImageViewUtil userhead;//发表视频用户的头像

    private boolean WheterLogin = false;//判断是否登录
    private boolean isFollow = false;//判断是否关注了
    private String VideoID;//发表视频用户id

    //这是更新操作
    private VideoListJson videoListJson;//数据
    private TextView context, time, address, more_tv;

    //评论区
    private PtrClassicFrameLayout ptrClassicFrameLayout;
    private ListView mListView;
    private View headview,footView;
    private CommentAdapter adapter;

    //点赞
    private GridView gridview;
    private List<VideoLikeJson> likelists = new ArrayList<>();
    private VideoLikeAdapter adapterLike;
    private RelativeLayout layout_c;
    private int likePagenow;//点赞的总页数

    private UploadVideoPresenter presenter;
    private List<VideoCommentJson> listss = new ArrayList<>();
    private int pagenow;//总页数
    private int pagesize = 1;//当前页

    private EditText commentEdit;//评论框
    private Button send;//评论按钮

    private boolean wheterLike = false;//是否点赞了
    private boolean repeatClick = true;//防止多次点击点赞按钮

    private Dialog dialog;//操作按钮弹出框
    private View dialog_operation;//操作的view
    private TextView dialog_delete,dialog_cancel,dialog_report,dialog_c_follow;//弹出框的：删除、取消、举报、取消关注

    private final int ONE = 1;//视频播放
    private final int TWO = 2;//下拉刷新新
    private final int THREE = 3;//点赞列表5
    private final int FROU = 4;//是否关注了的ui更新
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case ONE:

                    FrameLayout frameLayout = (FrameLayout) videoItemView.getParent();
                    videoItemView.release();
                    if (frameLayout != null && frameLayout.getChildCount() > 0) {
                        frameLayout.removeAllViews();
                        View itemView = (View) frameLayout.getParent();

                        if (itemView != null) {
                            itemView.findViewById(R.id.showview).setVisibility(View.VISIBLE);
                        }
                    }


                    break;

                case TWO://更新adapter的数据

                   //判断有没有评论
                    if(listss.size() <= 0){

                        ptrClassicFrameLayout.setLoadMoreEnable(false);

                    }else{

                        if (pagenow == pagesize || pagenow == 0) {

                            ptrClassicFrameLayout.setLoadMoreEnable(false);

                        }

                        mListView.removeFooterView(footView);

                    }

                    adapter.notifyDataSetChanged();

                    break;
                case THREE:

                    //判断点赞的数量
                    if (likelists.size() <= 0) {

                        layout_c.setVisibility(View.GONE);

                    } else {

                        layout_c.setVisibility(View.VISIBLE);
                        if (likePagenow > 1) {
                            more_tv.setVisibility(View.VISIBLE);
                        }
                        adapterLike.notifyDataSetChanged();

                    }


                    break;

                case FROU:

                    follow.setVisibility(View.GONE);

                    break;

            }

        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_details;
    }

    @Override
    protected void initViews() {

        if (!SharePreferenceUtil.getInstance(this).getString(SharePreferenceUtil.u_id, "").equals("")) {
            WheterLogin = true;
        }
        setTitleGone();
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);//弹出的样式
        videoItemView = new VideoPlayView(this);
        videoListJson = (VideoListJson) getIntent().getSerializableExtra("video");
        VideoID = videoListJson.getUid() + "";
        presenter = new UploadVideoPresenter(this, this);
        presenter.QueryComment("QueryComment", pagesize + "", videoListJson.getV_id() + "");//查询评论

        headview = View.inflate(this, R.layout.video_xml_head, null);
        footView = View.inflate(this,R.layout.list_foot_no_data,null);
        ptrClassicFrameLayout = (PtrClassicFrameLayout) this.findViewById(R.id.test_list_view_frame);
        mListView = (ListView) this.findViewById(R.id.test_list_view);
        mListView.addHeaderView(headview);
        mListView.addFooterView(footView);
        adapter = new CommentAdapter(this, listss);
        mListView.setAdapter(adapter);
        commentEdit = (EditText) findViewById(R.id.comment);
        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(this);
        context = (TextView) headview.findViewById(R.id.context);
        context.setText(videoListJson.getV_content());
        time = (TextView) headview.findViewById(R.id.time);
        time.setText(videoListJson.getV_time());
        address = (TextView) headview.findViewById(R.id.address);
        address.setText(videoListJson.getV_addre());
        userhead = (ImageViewUtil) findViewById(R.id.user_head);
        ImageLoader.getInstance().displayImage(UrlConfig.HEAD + videoListJson.getU_headurl(), userhead,
                ImageLoadOptions.getOptions());
        userhead.setOnClickListener(this);

        gridview = (GridView) headview.findViewById(R.id.gridview);
        more_tv = (TextView) headview.findViewById(R.id.more_tv);
        layout_c = (RelativeLayout) headview.findViewById(R.id.layout_c);
        adapterLike = new VideoLikeAdapter(this, likelists);
        gridview.setAdapter(adapterLike);

        presenter.SelectLike("SelectLike", videoListJson.getV_id() + "", "1");//查询点赞列表

        follow = (ImageView) findViewById(R.id.follow);
        follow.setOnClickListener(this);
        like = (ImageView) findViewById(R.id.like);
        like.setOnClickListener(this);

        //查询是否点赞了，在登录的状态下
        if (WheterLogin) {

            //判断是否是自己发表的视频
            if (SharePreferenceUtil.getInstance(this).getString(SharePreferenceUtil.u_id, "").equals(VideoID)) {
                follow.setVisibility(View.GONE);
            }

            presenter.QueryWheterLike("WhetherLike", videoListJson.getV_id() + "", SharePreferenceUtil.getInstance(this).getString(SharePreferenceUtil.u_id, ""));//查询是否点赞
            presenter.SelectFollows("SelectFollowUser", SharePreferenceUtil.getInstance(this).getString(SharePreferenceUtil.u_id, ""), videoListJson.getUid() + "");//查询是否关注

        }

        //视频模块
        frameLayout = (FrameLayout) headview.findViewById(R.id.layout_video);//不全屏的
        full_screen = (FrameLayout) findViewById(R.id.full_screen);//全屏的
        frameLayout.removeAllViews();
        frameLayout.addView(videoItemView);
        videoItemView.start(videoListJson.getV_videourl());

        image_bg = (ImageView) headview.findViewById(R.id.image_bg);
        ImageLoader.getInstance().displayImage(videoListJson.getV_videoimgurl(), image_bg,
                ImageLoadOptions.getOptions());

        showview = (RelativeLayout) headview.findViewById(R.id.showview);


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

                        if (pagenow == pagesize || pagenow == 0) {

                            ptrClassicFrameLayout.setLoadMoreEnable(false);

                        }else {

                        pagesize++;
                        presenter.QueryComment("QueryComment", pagesize + "", videoListJson.getV_id() + "");//查询评论
                        ptrClassicFrameLayout.loadMoreComplete(true);

                        }
                    }
                }, 1000);

            }
        });

        //播放完监听
        videoItemView.setCompletionListener(new VideoPlayView.CompletionListener() {
            @Override
            public void completion(IMediaPlayer mp) {

                handler.sendEmptyMessage(ONE);

            }
        });

        //注册监听广播
        IntentFilter filter = new IntentFilter(LoginActivity.action);
        registerReceiver(broadcastReceiver, filter);

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (videoItemView != null) {
            videoItemView.stop();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (videoItemView != null) {
            videoItemView.pause();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (videoItemView != null) {
            videoItemView.pause();
        }
    }

    //返回按钮
    public void onClickBack(View v) {

        InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(v.getWindowToken(), 0);
        onBackPressed();

    }

    //操作
    public void onClickOperation(View v) {

        if (!SharePreferenceUtil.getInstance(this).getString(SharePreferenceUtil.u_id, "").equals("")) {

            showOperation();

        } else {

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {

            //评论
            case R.id.send:

                if (!SharePreferenceUtil.getInstance(this).getString(SharePreferenceUtil.u_id, "").equals("")) {

                    if (TextUtils.isEmpty(commentEdit.getText().toString())) {
                        Toast.makeText(this, "请输入内容", Toast.LENGTH_LONG).show();
                        return;
                    }
                    presenter.AddComment("AddComment", videoListJson.getV_id() + "",videoListJson.getUid()+"", SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.u_id, ""), commentEdit.getText().toString());
                    commentEdit.setText("");
                    hideSoftInput(commentEdit.getContext(), commentEdit);

                } else {

                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);

                }

                break;

            //视频播放完监听
            case R.id.showview:

                showview.setVisibility(View.GONE);
                frameLayout.removeAllViews();
                frameLayout.addView(videoItemView);
                videoItemView.start(videoListJson.getV_videourl());

                break;

            //关注
            case R.id.follow:

                if (!SharePreferenceUtil.getInstance(this).getString(SharePreferenceUtil.u_id, "").equals("")) {

                    presenter.AddFollows("AddFollowUser", SharePreferenceUtil.getInstance(this).getString(SharePreferenceUtil.u_id, ""), VideoID);

                } else {

                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);

                }
                break;

            //点赞
            case R.id.like:

                if(!repeatClick){
                    return;
                }

                if (!SharePreferenceUtil.getInstance(this).getString(SharePreferenceUtil.u_id, "").equals("")) {

                    if (wheterLike) {

                        repeatClick = false;
                        presenter.CancelLike("CancelLike", videoListJson.getV_id() + "", SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.u_id, "")); //取消点赞

                    } else {

                        repeatClick = false;
                        presenter.AddClickALike("like",videoListJson.getUid()+"", videoListJson.getV_id() + "", SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.u_id, ""));//点赞

                    }

                } else {

                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);

                }


                break;

            //点击头像
            case R.id.user_head:

                Intent intent = new Intent(this, PersonalCenterActivity.class);
                intent.putExtra("uid", VideoID);
                startActivity(intent);

                break;

            //操作的取消
            case R.id.dialog_cancel:

                dialog.dismiss();

                break;

        }

    }

    //全屏设置
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (videoItemView != null) {

            videoItemView.onChanged(newConfig);

            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

                full_screen.setVisibility(View.GONE);
                full_screen.removeAllViews();
                frameLayout.removeAllViews();
                frameLayout.addView(videoItemView);
                headview.setVisibility(View.VISIBLE);

                videoItemView.setContorllerVisiable();

            } else {

                ViewGroup viewGroup = (ViewGroup) videoItemView.getParent();
                if (viewGroup == null)
                    return;
                viewGroup.removeAllViews();
                full_screen.addView(videoItemView);
                headview.setVisibility(View.GONE);
                full_screen.setVisibility(View.VISIBLE);

            }

        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                return true;

            }
        }
        return super.onKeyUp(keyCode, event);

    }

    /**
     * 查询评论
     *
     * @param lists   评论
     * @param pagenow 页码
     */
    @Override
    public void QueryComment(List<VideoCommentJson> lists, String pagenow) {

        this.pagenow = Integer.parseInt(pagenow);

        for (VideoCommentJson v : lists) {
            listss.add(v);
        }

        handler.sendEmptyMessage(TWO);

    }

    /**
     * 添加评论
     *
     * @param comment 评论成功
     */
    @Override
    public void AddComment(VideoCommentJson comment) {

        toast("评论成功，经验+1");
        if (pagenow == pagesize || pagenow == 0) {

            listss.add(comment);
            handler.sendEmptyMessage(TWO);

        }

    }

    /**
     * 点赞视频
     *
     * @param state
     */
    @Override
    public void ClickALike(String state) {

        if (state.equals("200")) {

            toast("点赞成功");
            repeatClick = true;
            wheterLike = true;
            like.setBackgroundResource(R.mipmap.nav_btn_like_selected);
            presenter.SelectLike("SelectLike", videoListJson.getV_id() + "", "1");

        }

    }

    /**
     * 查询是否点赞了
     *
     * @param wheterLike
     */
    @Override
    public void WheterLike(boolean wheterLike) {

        this.wheterLike = wheterLike;

        Log.e("是否点赞了", " " + wheterLike);

        if (wheterLike) {

            like.setBackgroundResource(R.mipmap.nav_btn_like_selected);

        } else {

            like.setBackgroundResource(R.mipmap.nav_btn_like_pressed);

        }


    }

    /**
     * 取消点赞
     *
     * @param state
     */
    @Override
    public void VCancelLike(String state) {

        if (state.equals("200")) {

            toast("取消点赞");
            wheterLike = false;
            repeatClick = true;
            like.setBackgroundResource(R.mipmap.nav_btn_like_pressed);
            presenter.SelectLike("SelectLike", videoListJson.getV_id() + "", "1");
        }

    }

    //点赞列表
    @Override
    public void VSelectLike(List<VideoLikeJson> likeJson, String pagenow) {

        Log.e("pagenow","pagenow"+pagenow);
        this.likePagenow = Integer.parseInt(pagenow);
        likelists.clear();
        for (VideoLikeJson j : likeJson) {
            likelists.add(j);
        }
        handler.sendEmptyMessage(THREE);

    }

    //查询是否关注了
    @Override
    public void VSelectFollow(String state) {

        Log.e("state", "s" + state);

        if (state.equals("200")) {
            handler.sendEmptyMessage(FROU);
            isFollow = true;
        }

    }

    //添加关注
    @Override
    public void VAddFollow(String state) {

        if (state.equals("200")) {
            toast("关注成功");
            handler.sendEmptyMessage(FROU);
        }

    }

    /**
     * 弹出操作的按钮
     */
    public void showOperation() {

        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        dialog_operation = LayoutInflater.from(this).inflate(R.layout.dialog_operation, null);

        dialog_delete = (TextView) dialog_operation.findViewById(R.id.dialog_delete);
        dialog_delete.setOnClickListener(this);
        dialog_cancel = (TextView) dialog_operation.findViewById(R.id.dialog_cancel);
        dialog_cancel.setOnClickListener(this);
        dialog_report = (TextView) dialog_operation.findViewById(R.id.dialog_report);
        dialog_report.setOnClickListener(this);
        dialog_c_follow = (TextView) dialog_operation.findViewById(R.id.dialog_c_follow);
        dialog_c_follow.setOnClickListener(this);

        //是否关注了当前用户，显示取消关注按钮
        if(isFollow){
            dialog_c_follow.setVisibility(View.VISIBLE);//显示取消关注
        }

        //判断是否是自己发表的视频
        if (SharePreferenceUtil.getInstance(this).getString(SharePreferenceUtil.u_id, "").equals(VideoID)) {
            dialog_delete.setVisibility(View.VISIBLE);//显示删除
        }else{
            dialog_report.setVisibility(View.VISIBLE);//显示举报
        }

        //将布局设置给Dialog
        dialog.setContentView(dialog_operation);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.y = 20;//设置Dialog距离底部的距离

//// 以下这两句是为了保证按钮可以水平满屏
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
// 设置显示位置
        dialog.onWindowAttributesChanged(lp);
//       将属性设置给窗体
        dialog.show();//显示对话框
    }

    public static void hideSoftInput(Context context, View view) {

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘

    }


    //注册广播监听是否登录
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            presenter.QueryWheterLike("WhetherLike", videoListJson.getV_id() + "", SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.u_id, ""));

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);//销毁广播

        videoItemView.stop();
        videoItemView.release();
        videoItemView.onDestroy();
        videoItemView=null;

    }


}
