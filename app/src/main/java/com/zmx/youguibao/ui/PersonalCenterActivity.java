package com.zmx.youguibao.ui;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zmx.youguibao.BaseActivity;
import com.zmx.youguibao.R;
import com.zmx.youguibao.SharePreferenceUtil;
import com.zmx.youguibao.adapter.UserVideoAdapter;
import com.zmx.youguibao.mvp.bean.PersonalCenterPojo;
import com.zmx.youguibao.mvp.bean.VideoLikeJson;
import com.zmx.youguibao.mvp.bean.VideoListJson;
import com.zmx.youguibao.mvp.presenter.UserPresenter;
import com.zmx.youguibao.mvp.view.PersonalCenterView;
import com.zmx.youguibao.utils.UrlConfig;
import com.zmx.youguibao.utils.view.ImageLoadOptions;
import com.zmx.youguibao.utils.view.StatusBarUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/9/19 0019 上午 10:23
 *功能模块：个人中心
 */
public class PersonalCenterActivity extends BaseActivity implements AbsListView.OnScrollListener,PersonalCenterView {

    //其实SparseArray<E>是用来代替HashMap<Integer, E>，不了解的可以查查
    private SparseArray recordSp = new SparseArray(0);
    private int mCurrentfirstVisibleItem = 0;
    private RelativeLayout rlTitle,relayout;

    private ImageView user_avatar;//头像
    private TextView user_name,fans,follows,user_des;//姓名，粉丝，关注，简介

    private ListView lvTitleFade;
    private PtrClassicFrameLayout ptrClassicFrameLayout;
    private UserVideoAdapter adapter;
    private List<VideoListJson> list = new ArrayList<>();

    private UserPresenter presenter;

    private PersonalCenterPojo pcpojo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_center;
    }

    @Override
    protected void initViews() {

        setTitleGone();
        presenter = new UserPresenter(this,this);

        //查询信息
        presenter.SelectUserMessage("UserMessage", SharePreferenceUtil.getInstance(this).getString(SharePreferenceUtil.u_id,""));
        presenter.SelectUserVideos("UserVideos","1",SharePreferenceUtil.getInstance(this).getString(SharePreferenceUtil.u_id,""));
        rlTitle = (RelativeLayout) findViewById(R.id.rlTitle);
        lvTitleFade = (ListView) findViewById(R.id.lvTitleFade);
        relayout = (RelativeLayout) findViewById(R.id.relayout);
        StatusBarUtil.setTransparentForImageView(this,relayout);//状态栏一体化
        //设置标题背景透明
        rlTitle.getBackground().setAlpha(0);
        //滑动监听，注意implements OnScrollListener
        lvTitleFade.setOnScrollListener(this);
        View headview = LayoutInflater.from(this).inflate(R.layout.personal_center_title,null);
        lvTitleFade.addHeaderView(headview);

        adapter = new UserVideoAdapter(this,list);
        lvTitleFade.setAdapter(adapter);

        user_avatar = (ImageView) headview.findViewById(R.id.user_avatar);
        user_name = (TextView) headview.findViewById(R.id.user_name);
        fans = (TextView) headview.findViewById(R.id.fans);
        follows = (TextView) headview.findViewById(R.id.follows);
        user_des = (TextView) headview.findViewById(R.id.user_des);

        ptrClassicFrameLayout = (PtrClassicFrameLayout) this.findViewById(R.id.test_list_view_frame);
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

                        ptrClassicFrameLayout.loadMoreComplete(true);
                    }
                }, 1000);

            }
        });


    }

    //滑动事件处理
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        //firstVisibleItem--处于顶部的Item标记
        //visibleItemCount--当前可见item数
        //totalItemCount----总item数
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
        Log.d("dmdrs", "滑动距离：" + getScrollY());
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
            if(itemRecod != null){
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
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case ONE:

                    user_name.setText(pcpojo.getU_name());
                    fans.setText("粉丝："+pcpojo.getFans());
                    follows.setText("关注："+pcpojo.getFollows());
                    user_des.setText(pcpojo.getU_desc());

                    ImageLoader.getInstance().displayImage(UrlConfig.HEAD+pcpojo.getU_headurl(), user_avatar,
                            ImageLoadOptions.getOptions());

                    break;

                case TWO:
                    UpdateAdapter();
                    break;

            }

        }
    };

    @Override
    public void SelectUserMessage(PersonalCenterPojo pcp) {

        this.pcpojo = pcp;
        handler.sendEmptyMessage(ONE);

    }

    @Override
    public void SelectUserVideos(int sun, List<VideoListJson> lists) {

        for (VideoListJson j:lists){
            list.add(j);
        }

    }

    private void UpdateAdapter(){

            adapter.notifyDataSetChanged();


    }


}
