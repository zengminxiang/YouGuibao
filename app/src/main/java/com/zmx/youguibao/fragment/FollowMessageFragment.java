package com.zmx.youguibao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zmx.youguibao.BaseFragment;
import com.zmx.youguibao.R;

/**
 * Created by Administrator on 2016/10/14 0014.
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
