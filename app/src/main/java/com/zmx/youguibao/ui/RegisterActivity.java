package com.zmx.youguibao.ui;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zmx.youguibao.BaseActivity;
import com.zmx.youguibao.R;
import com.zmx.youguibao.utils.AndroidBug5497Workaround;
import com.zmx.youguibao.utils.view.ImageViewUtil;
import com.zmx.youguibao.utils.view.ObservableScrollView;

import java.io.File;
import java.lang.reflect.Field;

/**
 * 作者：胖胖祥
 * 时间：2016/9/7 0007 上午 10:32
 * 功能模块：注册
 */

public class RegisterActivity extends BaseActivity implements ObservableScrollView.ScrollViewListener {

    private TextView edit_sex, cancel;//选择性别，取消
    private View sex_inflate;//选择性别的view
    private Dialog sex_dialog;//弹出框
    private ImageButton secrecy, girl, boy;
    private EditText reg_phone;

    private View positionView;

    //标题栏变化
    private View layoutHead;
    private ObservableScrollView scrollView;
    private ImageView imageView;
    private int height ;

    private String phone;//手机号码


    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initViews() {

        // 沉浸式状态栏
        positionView = findViewById(R.id.position_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            int statusBarHeight = getStatusBarHeight();
            ViewGroup.LayoutParams lp = positionView.getLayoutParams();
            lp.height = statusBarHeight;
            positionView.setLayoutParams(lp);

        }
        AndroidBug5497Workaround.assistActivity(this);

        phone = getIntent().getStringExtra("phone");
        Log.e("phone","手机号"+phone);
        edit_sex = (TextView) findViewById(R.id.edit_sex);
        edit_sex.setOnClickListener(this);
        reg_phone = (EditText) findViewById(R.id.reg_phone);
        reg_phone.setText(phone);

        scrollView = (ObservableScrollView) findViewById(R.id.scrollview);

        layoutHead = findViewById(R.id.view_title);

        imageView = (ImageView) findViewById(R.id.imageView1);
        layoutHead.setBackgroundColor(Color.argb(0, 0xfd, 0x91, 0x5b));
        //获取顶部图片高度后，设置滚动监听
        ViewTreeObserver vto = imageView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                height =   imageView.getHeight();
                imageView.getWidth();
                scrollView.setScrollViewListener(RegisterActivity.this);
            }
        });

    }

    /**
     * 弹出选择性别框
     */
    public void showChoiceSex() {

        sex_dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        sex_inflate = LayoutInflater.from(this).inflate(R.layout.choice_sex_dialog, null);
        secrecy = (ImageButton) sex_inflate.findViewById(R.id.secrecy);
        secrecy.setOnClickListener(this);
        girl = (ImageButton) sex_inflate.findViewById(R.id.girl);
        girl.setOnClickListener(this);
        boy = (ImageButton) sex_inflate.findViewById(R.id.boy);
        boy.setOnClickListener(this);
        cancel = (TextView) sex_inflate.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        //将布局设置给Dialog
        sex_dialog.setContentView(sex_inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = sex_dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.y = 20;//设置Dialog距离底部的距离

//// 以下这两句是为了保证按钮可以水平满屏
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
// 设置显示位置
        sex_dialog.onWindowAttributesChanged(lp);
//       将属性设置给窗体
        sex_dialog.show();//显示对话框
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        Drawable right = getResources().getDrawable(R.mipmap.wallet_base_right_arrow);
        right.setBounds(0, 0, right.getMinimumWidth(), right.getMinimumHeight());

        switch (v.getId()) {

            case R.id.edit_sex:
                showChoiceSex();
                break;

            case R.id.boy:

                edit_sex.setText("男");
                Drawable boy_image = getResources().getDrawable(R.mipmap.profile_avatar_genderbadge_male);
                boy_image.setBounds(0, 0, boy_image.getMinimumWidth(), boy_image.getMinimumHeight());
                edit_sex.setCompoundDrawables(boy_image, null, right, null);
                sex_dialog.dismiss();
                break;

            case R.id.girl:

                edit_sex.setText("女");
                Drawable girl_image = getResources().getDrawable(R.mipmap.profile_avatar_genderbadge_female);
                girl_image.setBounds(0, 0, girl_image.getMinimumWidth(), girl_image.getMinimumHeight());
                edit_sex.setCompoundDrawables(girl_image, null, right, null);
                sex_dialog.dismiss();
                break;

            case R.id.secrecy:

                edit_sex.setText("保密");
                Drawable secrecy_image = getResources().getDrawable(R.mipmap.profile_avatar_genderbadge_secret);
                secrecy_image.setBounds(0, 0, secrecy_image.getMinimumWidth(), secrecy_image.getMinimumHeight());
                edit_sex.setCompoundDrawables(secrecy_image, null, right, null);
                sex_dialog.dismiss();
                break;

            case R.id.cancel:
                sex_dialog.dismiss();
                break;

        }

    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {

        if(y<=height && y>=0){
            float scale =(float) y /height;
            float alpha =  (255 * scale);
            //layout全部透明
//			layoutHead.setAlpha(scale);

            //只是layout背景透明(仿知乎滑动效果)
            layoutHead.setBackgroundColor(Color.argb((int) alpha, 0xfd, 0x91, 0x5b));
        }

    }

    //获取状态栏的高度
    public int getStatusBarHeight() {

        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }


}
