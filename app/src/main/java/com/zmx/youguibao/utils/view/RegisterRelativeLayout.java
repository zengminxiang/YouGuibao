package com.zmx.youguibao.utils.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 *
 * 开发者：胖胖祥
 * 时间：2016/11/1 10:45
 * 功能模块：注册中使用的布局，解决状态栏一体化无法
 *
 */
public class RegisterRelativeLayout extends RelativeLayout {

    private int[] mInsets = new int[4];

    public RegisterRelativeLayout(Context context) {
        super(context);
    }

    public RegisterRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RegisterRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public final int[] getInsets() {
        return mInsets;
    }

    @Override
    protected final boolean fitSystemWindows(Rect insets) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            mInsets[0] = insets.left;
            mInsets[1] = insets.top;
            mInsets[2] = insets.right;
            return super.fitSystemWindows(insets);
        } else {
            return super.fitSystemWindows(insets);
        }
    }
}