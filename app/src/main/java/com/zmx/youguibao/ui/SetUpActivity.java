package com.zmx.youguibao.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.zmx.youguibao.BaseActivity;
import com.zmx.youguibao.R;
import com.zmx.youguibao.SharePreferenceUtil;

/**
 *作者：胖胖祥
 *时间：2016/10/10 0010 上午 9:50
 *功能模块：设置界面
 */
public class SetUpActivity extends BaseActivity {

    private Button exit_login;//退出按钮
    public static final String action = "login";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_up;
    }

    @Override
    protected void initViews() {

        exit_login = (Button) findViewById(R.id.exit_login);
        exit_login.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){

            case R.id.exit_login:

                SharePreferenceUtil.getInstance(mActivity).clear();
                Intent intent = new Intent(action);
                sendBroadcast(intent);
                this.finish();

                break;

        }

    }
}
