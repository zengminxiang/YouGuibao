package com.zmx.youguibao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zmx.youguibao.BaseFragment;
import com.zmx.youguibao.R;

/**
 *作者：胖胖祥
 *时间：2016/8/16 0016 下午 2:20
 *功能模块：关注界面
 */
public class FollowFragment extends BaseFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_follow, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);

    }



    @Override
    protected void initView() {

    }
}
