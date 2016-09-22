package com.zmx.youguibao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zmx.youguibao.utils.Utils;

/**
 *作者：胖胖祥
 *时间：2016/8/16 0016 下午 2:24
 *功能模块：自定义fragment
 */

public abstract class BaseFragment  extends Fragment {

    /**
     * 跳转到下一个activity
     **/
    protected static final int REQUEST_ACTIVITY = 10;
    protected Activity mActivity;
    protected View mView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        initView();
        return mView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    //重写获取控件id方法
    public View findViewById(int id) {
        return mView.findViewById(id);
    }

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


    protected abstract void initView();


    /**
     * 检测网络是否连接
     *
     * @return
     */
    protected boolean isNetworkAvailable() {
        // 得到网络连接信息
        ConnectivityManager manager = (ConnectivityManager) this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        // 去进行判断网络是否连接
        if (manager.getActiveNetworkInfo() != null) {
            return manager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }


    //弹出框
    public void toast(String content){
        Utils.toast(content);
    }
    //弹出框
    public void toast(String content , int duration){
        Utils.toast(content, duration);
    }

}