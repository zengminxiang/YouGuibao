package com.zmx.youguibao.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.zmx.youguibao.BaseActivity;
import com.zmx.youguibao.R;
import com.zmx.youguibao.SharePreferenceUtil;
import com.zmx.youguibao.utils.view.StatusBarUtil;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

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
        // 沉浸式状态栏
        positionView = findViewById(R.id.position_view);
        StatusBarUtil.setTransparentForImageView(this,positionView);//状态栏一体化
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
                // 调用 Handler 来异步设置别名
                mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, ""));

                break;

        }

    }


    /**
     * 设置极光推送别名（当前设置退出为null，取消接收）
     */
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.e("设置成功", logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.e("实在失败，重新设置", logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e("e", logs);
            }
            toast(logs+getApplicationContext());
        }
    };
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.e("e", "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    SetUpActivity.this.finish();
                    break;
                default:
                    Log.e("e", "Unhandled msg - " + msg.what);
            }
        }
    };


}
