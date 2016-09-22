package com.zmx.youguibao.ui;

import android.graphics.Color;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.zmx.youguibao.BaseActivity;
import com.zmx.youguibao.R;
import com.zmx.youguibao.utils.view.StatusBarUtil;

import java.util.ArrayList;

/**
 *作者：胖胖祥
 *时间：2016/9/19 0019 上午 10:23
 *功能模块：个人中心
 */
public class PersonalCenterActivity extends BaseActivity implements AbsListView.OnScrollListener {

    //其实SparseArray<E>是用来代替HashMap<Integer, E>，不了解的可以查查
    private SparseArray recordSp = new SparseArray(0);
    private int mCurrentfirstVisibleItem = 0;
    private RelativeLayout rlTitle,relayout;

    private ListView lvTitleFade;

    ArrayList<String> data1 = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_center;
    }

    @Override
    protected void initViews() {

        setTitleGone();
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

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data1);

        for (int i = 0; i < 15; i++) {
            data1.add((i+1)+"");
        }
        lvTitleFade.setAdapter(adapter);

    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    //滑动事件处理
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

            //06-07 21:00:21.601: D/dmdrs(23096): xxx1：com.dmdrs.titlefade.MainActivity$ItemRecod@529122fc
            //06-07 21:00:21.601: D/dmdrs(23096): xxx2：300
            //06-07 21:00:21.601: D/dmdrs(23096): xxx1：null
            //快速滑动会为空，判断一下，发现的bug

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

}
