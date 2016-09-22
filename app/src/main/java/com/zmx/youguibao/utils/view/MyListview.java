package com.zmx.youguibao.utils.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.zmx.youguibao.R;

/**
 * 上拉刷新ListView
 *
 * @author xiejinxiong
 */
public class MyListview extends ListView implements OnScrollListener {

    private View footer;//底部布局

    private int totalItemCount;//总的数量
    private int lastVisibleItem;//最后一个可见的item
    boolean isLoanding;//正在加载

    private LoadMore loadmore;


    /*三个构造函数**/
    public MyListview(Context context) {
        super(context);
        initView(context);
    }

    public MyListview(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MyListview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);

    }


    /*初始化的时候把底部布局添加进去*/
    public void initView(Context context) {

        //自定义一个view，需要获取这个view的布局，需要用到LayoutInflater
        LayoutInflater inflater = LayoutInflater.from(context);

        //获取底部布局
        footer = inflater.inflate(R.layout.listview_loadbar, null);

        //设置底部默认为不可见(因为获取的id不同，所有导致底部布局一直存在)
        footer.findViewById(R.id.footer).setVisibility(View.GONE);

        //添加到listview底部
        this.addFooterView(footer);

        //设计滚动监听
        this.setOnScrollListener(this);

    }


    //arg1为第一个可见的list参数，arg2可见list参数的数量，arg3总的数量
    @Override
    public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {

        //最后一个item等于第一个加上可见的数量
        this.lastVisibleItem = arg1 + arg2 - 1;
        //总的数量
        this.totalItemCount = arg3 - 1;

    }

    //滚动停止
    @Override
    public void onScrollStateChanged(AbsListView arg0, int arg1) {


        //判断总的数量是否等于lastVisibleItem和滚动是否停止了
        if (totalItemCount == lastVisibleItem && arg1 == SCROLL_STATE_IDLE) {

            //判断
            if (!isLoanding) {

                isLoanding = true;

                footer.findViewById(R.id.footer).setVisibility(View.VISIBLE);

                //加载更多
                loadmore.Load();

            }

        }
    }

    //提示加载完成
    public void setLoadOK() {

        isLoanding = false;

        footer.findViewById(R.id.footer).setVisibility(View.GONE);

    }


    //调用加载更多接口
    public void setLoadMore(LoadMore loadmore) {

        this.loadmore = loadmore;

    }


    //回掉接口，用来加载更多数据
    public interface LoadMore {

        public void Load();

    }


    //下拉回弹
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
                                   int scrollY, int scrollRangeX, int scrollRangeY,
                                   int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {


        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
                scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

}