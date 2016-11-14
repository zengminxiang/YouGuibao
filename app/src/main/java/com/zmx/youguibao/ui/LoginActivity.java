package com.zmx.youguibao.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zmx.youguibao.BaseActivity;
import com.zmx.youguibao.MyApplication;
import com.zmx.youguibao.R;
import com.zmx.youguibao.SharePreferenceUtil;
import com.zmx.youguibao.mvp.bean.UserJson;
import com.zmx.youguibao.mvp.presenter.UserPresenter;
import com.zmx.youguibao.mvp.view.LoginView;
import com.zmx.youguibao.qupai.AuthTest;
import com.zmx.youguibao.qupai.bean.Contant;
import com.zmx.youguibao.utils.Utils;
import com.zmx.youguibao.utils.view.StatusBarUtil;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 *作者：胖胖祥
 *时间：2016/9/6 0006 下午 3:43
 *功能模块：登录界面
 */
public class LoginActivity extends BaseActivity implements LoginView{

    private ImageView register_icon,login_icon,close;
    private LinearLayout layout_b,layout_c;
    private EditText edit_pwd,phone,reg_phone;
    private CharSequence temp;//监听前的文本
    private TextView forget_pwd,next_reg;//密码框
    private RelativeLayout layout;

    private UserPresenter userPresenter;
    // 加载
    private ProgressDialog progressDialog = null;

    public static final String action = "login";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews() {

        overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
        userPresenter = new UserPresenter(this,this);
        layout = (RelativeLayout) findViewById(R.id.layout);
        register_icon = (ImageView) findViewById(R.id.register_icon);
        login_icon = (ImageView) findViewById(R.id.login_icon);
        layout_b = (LinearLayout) findViewById(R.id.layout_b);
        layout_c = (LinearLayout) findViewById(R.id.layout_c);
        phone = (EditText) findViewById(R.id.phone);
        close = (ImageView) findViewById(R.id.close);
        close.setOnClickListener(this);
        forget_pwd = (TextView) findViewById(R.id.forget_pwd);
        forget_pwd.setOnClickListener(this);
        edit_pwd = (EditText) findViewById(R.id.edit_pwd);
        next_reg = (TextView) findViewById(R.id.next_reg);//下一步
        reg_phone = (EditText) findViewById(R.id.reg_phone);
        next_reg.setOnClickListener(this);
        StatusBarUtil.setTransparentForImageView(this,close);//状态栏一体化

        //监听是否输入密码
        edit_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e("beforeTextChanged", "输入文本之前的状态");
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("onTextChanged", "输入文字中的状态，count是一次性输入字符数");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("afterTextChanged", "输入文字后的状态");

                if(temp.length()>=6){

                    forget_pwd.setText("登录");

                }else{

                    forget_pwd.setText("忘记密码？");

                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){

            case R.id.forget_pwd:

                //判断是否是登录还是忘记密码？
                if(forget_pwd.getText().toString().equals("登录")){

                    if(TextUtils.isEmpty(phone.getText().toString())){

                        toast("手机号码不能为空");
                        return;

                    }

                    if(!Utils.isChinaPhoneLegal(phone.getText().toString())){
                        toast("手机号码不正确");
                        return;

                    }

                    progressDialog = ProgressDialog.show(mActivity, "",
                            "正在登录...", true);

                    userPresenter.Login("login",phone.getText().toString(),edit_pwd.getText().toString());

                }else{


                }

                break;

            case R.id.close:

                InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputmanger.hideSoftInputFromWindow(v.getWindowToken(), 0);
                onBackPressed();

                break;

            case R.id.next_reg:

                if(TextUtils.isEmpty(reg_phone.getText().toString())){

                    toast("请输入手机号码");
                    return;

                }

                if(!Utils.isChinaPhoneLegal(reg_phone.getText().toString())){

                    toast("手机号码不正确");
                    return;

                }

                Bundle bundle = new Bundle();
                bundle.putString("phone",reg_phone.getText().toString());
                startActivity(RegisterActivity.class,bundle);
                break;

        }

    }

    //登录切换按钮监听
    public void onClickLogin(View v){

        login_icon.setVisibility(View.VISIBLE);
        layout_b.setVisibility(View.VISIBLE);
        register_icon.setVisibility(View.GONE);
        layout_c.setVisibility(View.GONE);

    }

    //注册切换按钮监听
    public void onClickRegister(View v){

        login_icon.setVisibility(View.GONE);
        layout_b.setVisibility(View.GONE);
        register_icon.setVisibility(View.VISIBLE);
        layout_c.setVisibility(View.VISIBLE);

    }


    @Override
    public void Login(UserJson user) {
        progressDialog.dismiss();

        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, user.getU_id()));
        toast("登录成功");
        AuthTest.getInstance().initAuth(mActivity, Contant.APP_KEY,Contant.APP_SECRET, user.getU_id());//授权趣拍sdk
        SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.u_id, user.getU_id());
        MyApplication.setU_id(user.getU_id());
        SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.u_phone,user.getU_phone());
        MyApplication.setU_phone(user.getU_phone());
        SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.u_name,user.getU_name());
        MyApplication.setU_name(user.getU_name());
        SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.u_headurl,user.getU_headurl());
        MyApplication.setU_headurl(user.getU_headurl());
        SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.u_desc,user.getU_desc());
        MyApplication.setU_desc(user.getU_desc());
        SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.u_sex,user.getU_sex());
        MyApplication.setU_sex(user.getU_sex());
        MyApplication.setIsLogin(true);
        Intent intent = new Intent(action);
        sendBroadcast(intent);
        this.finish();
    }

    @Override
    public void ErrorLogin(String message) {
        progressDialog.dismiss();
        toast(message);

    }


    /**
     * 设置极光推送别名
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
                    break;
                default:
                    Log.e("e", "Unhandled msg - " + msg.what);
            }
        }
    };

}
