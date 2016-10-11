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
 *时间：2016/10/10 0010 下午 3:37
 *功能模块：赞的信息列表
 */
public class ZanMessageFragment extends BaseFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.message_comment_fragment, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    protected void initView() {

    }
}
