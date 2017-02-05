package com.zmx.youguibao.ui;


import com.zmx.youguibao.BaseActivity;
import com.zmx.youguibao.R;
import com.zmx.youguibao.utils.view.StatusBarUtil;

public class NoticeActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_notice;
    }

    @Override
    protected void initViews() {

        // 沉浸式状态栏
        StatusBarUtil.setColor(this, getResources().getColor(R.color.title_bage), 0);

    }
}
