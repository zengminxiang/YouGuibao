package com.zmx.youguibao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zmx.youguibao.BaseFragment;
import com.zmx.youguibao.R;

/**
 *
 * 开发者：胖胖祥
 * 时间：2016/11/1 16:08
 * 功能模块：关注的未读消息
 *
 */
public class FollowMessageFragment  extends BaseFragment {

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
