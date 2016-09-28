package com.zmx.youguibao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zmx.youguibao.utils.Utils;

/**
 * 作者：胖胖祥
 * 时间：2016/8/23 0023 下午 5:36
 * 功能模块：自定义activity
 */
public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener {

    protected Activity mActivity;

    protected RelativeLayout layouts;//头部布局文件
    protected TextView title;
    protected View back;

    private LinearLayout load_view;//加载提示布局

    /**
     * 跳转到下一个activity
     **/
    protected static final int REQUEST_ACTIVITY = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_base);
        mActivity = this;

        //找到资源文件的XML
        if (getLayoutId() != 0) {

            View vContent = LayoutInflater.from(mActivity).inflate(getLayoutId(), null);
            ((FrameLayout) findViewById(R.id.frame_content)).addView(vContent);

        }
        //加载头部文件
        layouts = (RelativeLayout) findViewById(R.id.main_title_rl);
        title = (TextView) findViewById(R.id.title);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        initViews();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.back:

                InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputmanger.hideSoftInputFromWindow(v.getWindowToken(), 0);
                onBackPressed();

                break;

        }

    }

    /**
     * 隐藏头部
     */
    public void setTitleGone() {
        layouts.setVisibility(View.GONE);
    }

    /**
     * 设置头部信息
     *
     * @param s
     */
    public void setTitle(String s) {
        title.setText(s);
    }

    /**
     * 设置加载提示框
     */
    protected void showLoadingView(){

        if(load_view == null){

            load_view = (LinearLayout)LayoutInflater.from(mActivity).inflate(R.layout.load_view, null);
            ((FrameLayout)findViewById(R.id.frame_content)).addView(load_view);//主布局LinearLayout

        }
        load_view.setVisibility(View.VISIBLE);

    }
    /**
     * 数据加载完成
     */
    protected void dismissLoadingView(){

        if(load_view != null){

            Log.e("55555555","sssssssss");
            load_view.setVisibility(View.GONE);

        }

    }

    /**
     * 初始化Activity的常用变量 举例:
     * <b>mLayoutResID=页面XML资源ID 必须存在的</b>
     */
    protected abstract int getLayoutId();

    /**
     * 初始化视图
     **/
    protected abstract void initViews();

    /**
     * 通过Class跳转界面
     *
     * @param cls
     */
    protected void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 通过Class跳转界面
     *
     * @param cls
     * @param bundle
     */
    protected void startActivity(Class<?> cls, Bundle bundle) {
        startActivity(cls, bundle, REQUEST_ACTIVITY);
    }

    /**
     * 通过Class跳转界面
     *
     * @param cls
     * @param bundle
     * @param requestCode
     */
    protected void startActivity(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(mActivity, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    //弹出框
    public void toast(String content) {
        Utils.toast(content);
    }

    //弹出框
    public void toast(String content, int duration) {
        Utils.toast(content, duration);
    }

}
