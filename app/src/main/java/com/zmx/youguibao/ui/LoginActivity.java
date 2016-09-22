package com.zmx.youguibao.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;

import com.zhy.android.percent.support.PercentRelativeLayout;
import com.zmx.youguibao.BaseActivity;
import com.zmx.youguibao.R;
import com.zmx.youguibao.SharePreferenceUtil;
import com.zmx.youguibao.mvp.bean.UserJson;
import com.zmx.youguibao.mvp.presenter.UserPresenter;
import com.zmx.youguibao.mvp.view.LoginView;
import com.zmx.youguibao.qupai.AuthTest;
import com.zmx.youguibao.qupai.bean.Contant;
import com.zmx.youguibao.utils.view.StatusBarUtil;

/**
 *作者：胖胖祥
 *时间：2016/9/6 0006 下午 3:43
 *功能模块：登录界面
 */
public class LoginActivity extends BaseActivity implements LoginView{

    private ImageView register_icon,login_icon,close;
    private LinearLayout layout_b,layout_c;
    private EditText edit_pwd,phone;
    private CharSequence temp;//监听前的文本
    private TextView forget_pwd;//密码框
    private PercentRelativeLayout layout;

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

        setTitleGone();
        overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
        userPresenter = new UserPresenter(this,this);
        layout = (PercentRelativeLayout) findViewById(R.id.layout);
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
        toast("登录成功");
        AuthTest.getInstance().initAuth(mActivity, Contant.APP_KEY,Contant.APP_SECRET, user.getU_id());//授权趣拍sdk
        SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.u_id, user.getU_id());
        SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.u_phone,user.getU_phone());
        SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.u_name,user.getU_name());
        SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.u_headurl,user.getU_headurl());
        SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.u_desc,user.getU_desc());
        Intent intent = new Intent(action);
        sendBroadcast(intent);
        this.finish();
    }

    @Override
    public void ErrorLogin(String message) {
        progressDialog.dismiss();
        toast(message);

    }

}