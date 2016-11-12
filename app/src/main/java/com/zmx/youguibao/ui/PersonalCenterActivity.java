package com.zmx.youguibao.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zmx.youguibao.BaseActivity;
import com.zmx.youguibao.MyApplication;
import com.zmx.youguibao.R;
import com.zmx.youguibao.SharePreferenceUtil;
import com.zmx.youguibao.adapter.UserVideoAdapter;
import com.zmx.youguibao.mvp.bean.PersonalCenterPojo;
import com.zmx.youguibao.mvp.bean.VideoListJson;
import com.zmx.youguibao.mvp.presenter.UserPresenter;
import com.zmx.youguibao.mvp.view.PersonalCenterView;
import com.zmx.youguibao.utils.UrlConfig;
import com.zmx.youguibao.utils.view.ImageLoadOptions;
import com.zmx.youguibao.utils.view.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：胖胖祥
 * 时间：2016/9/19 0019 上午 10:23
 * 功能模块：个人中心
 */
public class PersonalCenterActivity extends BaseActivity implements AbsListView.OnScrollListener, PersonalCenterView {

    //其实SparseArray<E>是用来代替HashMap<Integer, E>，不了解的可以查查
    private SparseArray recordSp = new SparseArray(0);
    private int mCurrentfirstVisibleItem = 0;
    private int lastVisibileItem;
    private RelativeLayout rlTitle;//头部局
    private ImageView user_avatar,head_left;//头像
    private TextView user_name, fans, follows, user_des, head_right_message, attention_user,head_title;//姓名，粉丝，关注，简介,关注

    private ListView lvTitleFade;
    private View listView_footer;
    private UserVideoAdapter adapter;
    private List<VideoListJson> list = new ArrayList<>();
    private int pagenow;//总页数
    private int pagesize = 1;//当前页

    private boolean isFollow = false;//判断是否关注了,默认是未关注
    private int followType = 0;//对关注按钮的点击事件判断，1为进入修改个人资料界面，2为取消关注，3为添加关注

    private String uid;//要查询用户的id

    private UserPresenter presenter;

    private PersonalCenterPojo pcpojo;

    private TextView load_text, no_date;//上拉
    private ProgressBar login_load;//上拉

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_center;
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
        showLoadingView();
        uid = this.getIntent().getStringExtra("uid");
        presenter = new UserPresenter(this, this);
        //查询信息
        presenter.SelectUserMessage("UserMessage", uid);
        presenter.SelectUserVideos("UserVideos", pagesize + "", uid);
        rlTitle = (RelativeLayout) findViewById(R.id.rlTitle);
        lvTitleFade = (ListView) findViewById(R.id.lvTitleFade);
        lvTitleFade.setVisibility(View.GONE);
        //设置标题背景透明
        rlTitle.getBackground().setAlpha(0);
        //滑动监听，注意implements OnScrollListener
        lvTitleFade.setOnScrollListener(this);
        View headview = LayoutInflater.from(this).inflate(R.layout.personal_center_title, null);

        user_avatar = (ImageView) headview.findViewById(R.id.user_avatar);
        head_right_message = (TextView) findViewById(R.id.head_right_message);
        head_right_message.setOnClickListener(this);
        head_title = (TextView) findViewById(R.id.head_title);
        user_name = (TextView) headview.findViewById(R.id.user_name);
        fans = (TextView) headview.findViewById(R.id.fans);
        follows = (TextView) headview.findViewById(R.id.follows);
        user_des = (TextView) headview.findViewById(R.id.user_des);
        attention_user = (TextView) headview.findViewById(R.id.attention_user);
        attention_user.setOnClickListener(this);
        head_left = (ImageView) findViewById(R.id.head_left);
        head_left.setOnClickListener(this);

        listView_footer = LayoutInflater.from(this).inflate(R.layout.listview_footer, null);
        login_load = (ProgressBar) listView_footer.findViewById(R.id.login_load);
        load_text = (TextView) listView_footer.findViewById(R.id.load_text);
        no_date = (TextView) listView_footer.findViewById(R.id.no_date);
        lvTitleFade.addHeaderView(headview, null, false);
        lvTitleFade.addFooterView(listView_footer);
        adapter = new UserVideoAdapter(this, list);
        lvTitleFade.setAdapter(adapter);

    }

    //滑动事件处理
    private boolean isScroll = true;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && (lastVisibileItem + 1) == lvTitleFade.getCount()) {

            Log.e("到了底部", "到了底部");

            //判断是否是最后一页
            if (isScroll) {

                listView_footer.setVisibility(View.VISIBLE);
                login_load.setVisibility(View.VISIBLE);
                load_text.setVisibility(View.VISIBLE);
                no_date.setVisibility(View.GONE);
                pagesize++;
                presenter.SelectUserVideos("UserVideos", pagesize + "", uid);

            } else {

                listView_footer.setVisibility(View.VISIBLE);
                login_load.setVisibility(View.GONE);
                load_text.setVisibility(View.GONE);
                no_date.setVisibility(View.VISIBLE);

            }

        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        //firstVisibleItem--处于顶部的Item标记
        //visibleItemCount--当前可见item数
        //totalItemCount----总item数
        lastVisibileItem = firstVisibleItem + visibleItemCount - 1;
        mCurrentfirstVisibleItem = firstVisibleItem;
        View firstView = view.getChildAt(0);
        if (null != firstView) {
            ItemRecod itemRecord = (ItemRecod) recordSp.get(firstVisibleItem);
            if (null == itemRecord) {
                itemRecord = new ItemRecod();
            }
            itemRecord.height = firstView.getHeight();//获取最顶部Item的高度
            itemRecord.top = firstView.getTop();//获取距离顶部的距离
            recordSp.append(firstVisibleItem, itemRecord);//设置值
        }

        Log.e("dmdrs", "滑动距离：" + getScrollY());
        int ScrollY = getScrollY();
        if (ScrollY >= 0 && ScrollY <= 255) {
            //设置标题栏透明度0~255
            rlTitle.getBackground().setAlpha(ScrollY);
        } else if (ScrollY > 255) {
            //滑动距离大于255就设置为不透明
            rlTitle.getBackground().setAlpha(255);
        }

    }

    private int getScrollY() {

        int height = 0;
        for (int i = 0; i < mCurrentfirstVisibleItem; i++) {
            ItemRecod itemRecod = (ItemRecod) recordSp.get(i);
            Log.d("dmdrs", "xxx1：" + itemRecod);
            if (itemRecod != null) {
                height += itemRecod.height;
            }
            Log.d("dmdrs", "xxx2：" + height);
        }
        ItemRecod itemRecod = (ItemRecod) recordSp.get(mCurrentfirstVisibleItem);
        if (null == itemRecod) {
            itemRecod = new ItemRecod();
        }
        return height - itemRecod.top;

    }

    class ItemRecod {
        int height = 0;
        int top = 0;
    }

    private final int ONE = 1;
    private final int TWO = 2;
    private final int FROU = 3;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case ONE:

                    dismissLoadingView();
                    lvTitleFade.setVisibility(View.VISIBLE);
                    user_name.setText(pcpojo.getU_name());
                    head_title.setText(pcpojo.getU_name());
                    fans.setText("粉丝：" + pcpojo.getFans());
                    follows.setText("关注：" + pcpojo.getFollows());
                    user_des.setText(pcpojo.getU_desc());
                    ImageLoader.getInstance().displayImage(UrlConfig.HEAD + pcpojo.getU_headurl(), user_avatar,
                            ImageLoadOptions.getOptions());

                    //判断当前登录的用户是否查看的是自己个人资料(关注按钮变成个人资料，和隐藏聊天图标)
                    if (MyApplication.getU_id().equals(pcpojo.getU_id())) {

                        followType = 1;
                        attention_user.setText("编辑个人资料");
                        attention_user.setCompoundDrawables(null, null, null, null);
                        head_right_message.setVisibility(View.GONE);

                    } else {

                        head_right_message.setVisibility(View.VISIBLE);
                        presenter.SelectFollows("SelectFollowUser", MyApplication.getU_id(), pcpojo.getU_id() + "");//查询是否关注

                    }

                    break;

                case TWO:

                    //判断是否是最后一页
                    if (pagenow == pagesize) {

                        isScroll = false;

                    } else {

                        isScroll = true;

                    }
                    adapter.notifyDataSetChanged();

                    break;

                case FROU:

                    if (isFollow) {

                        followType = 2;
                        attention_user.setText("取消关注");
                        attention_user.setCompoundDrawables(null, null, null, null);

                    } else {

                        followType = 3;
                        attention_user.setText("关注");

                    }

                    break;

            }

        }
    };

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {

            case R.id.message:


                Bundle bundle = new Bundle();
                bundle.putString("user_name", pcpojo.getU_name());
                bundle.putString("user_id", pcpojo.getU_id());
                bundle.putString("user_head", pcpojo.getU_headurl());

                startActivity(ChatActivity.class, bundle);

                break;

            case R.id.attention_user:

                switch (followType) {

                    //进入修改个人资料界面
                    case 1:

                        break;

                    //取消关注
                    case 2:

                        break;

                    //添加关注
                    case 3:

                        presenter.AddFollows("AddFollowUser", MyApplication.getU_id(), pcpojo.getU_id());

                        break;

                }

                break;

            //关闭
            case R.id.head_left:

                InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputmanger.hideSoftInputFromWindow(v.getWindowToken(), 0);
                onBackPressed();

                break;

        }

    }

    @Override
    public void SelectUserMessage(PersonalCenterPojo pcp) {

        this.pcpojo = pcp;
        handler.sendEmptyMessage(ONE);

    }

    @Override
    public void SelectUserVideos(int sun, List<VideoListJson> lists) {

        this.pagenow = sun;
        for (VideoListJson j : lists) {
            list.add(j);
        }

        handler.sendEmptyMessage(TWO);

    }

    /**
     * 查询是否关注了
     *
     * @param state
     */
    @Override
    public void VSelectFollow(String state) {

        if (state.equals("200")) {

            isFollow = true;

        }

        handler.sendEmptyMessage(FROU);

    }

    /**
     * 添加关注
     *
     * @param state
     */
    @Override
    public void VAddFollow(String state) {

        if (state.equals("200")) {

            toast("关注成功");
            isFollow = true;

        } else {

            toast("关注失败");
            isFollow = false;

        }

        handler.sendEmptyMessage(FROU);

    }

}
