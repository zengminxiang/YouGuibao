package com.zmx.youguibao.utils.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 *作者：胖胖祥
 *时间：2016/9/8 0008 上午 10:50
 *功能模块：带滚动监听的ScrollView
 */
public class ObservableScrollView extends ScrollView {

    public interface ScrollViewListener {

        void onScrollChanged(ObservableScrollView scrollView, int x, int y,
                             int oldx, int oldy);

    }

    private ScrollViewListener scrollViewListener = null;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);

        if (scrollViewListener != null) {

            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);

        }
    }


}