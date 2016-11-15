package com.zmx.youguibao.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.zmx.youguibao.BaseActivity;
import com.zmx.youguibao.MyApplication;
import com.zmx.youguibao.R;
import com.zmx.youguibao.SharePreferenceUtil;
import com.zmx.youguibao.mvp.presenter.UserPresenter;
import com.zmx.youguibao.mvp.view.ModifyUserView;
import com.zmx.youguibao.utils.UrlConfig;
import com.zmx.youguibao.utils.view.ImageLoadOptions;
import com.zmx.youguibao.utils.view.ImageViewUtil;
import com.zmx.youguibao.utils.view.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class ModifyUserActivity extends BaseActivity implements ModifyUserView {

    public static final String action = "updateHead";
    private ImageViewUtil user_head;//显示头像
    private TextView user_text_head;//修改头像
    private TextView photograph;//照相机
    private TextView choice_photo;//相册
    private TextView edit_sex;//性别显示的text
    private TextView cancel;//取消
    private TextView name;//取消
    private EditText edit_desc;//简介

    private View sex_inflate, photo_inflate;//选择性别的view
    private ImageButton secrecy, girl, boy;
    private Dialog sex_dialog, photo_dialog;//弹出框
    private Drawable right;//性别选择的右边图标
    private String sex,sex_h;//性别

    // 上传头像
    private Intent dataIntent = null;

    private String isHead = "0";//是否修改了头像,默认0是不修改
    private String isSex ;//是否修改了性别,默认是不修改

    UserPresenter up;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_user;
    }

    @Override
    protected void initViews() {

        // 沉浸式状态栏
        positionView = findViewById(R.id.position_view);
        StatusBarUtil.setTransparentForImageView(this,positionView);//状态栏一体化

        up = new UserPresenter(this, this);
        right = getResources().getDrawable(R.mipmap.wallet_base_right_arrow);
        right.setBounds(0, 0, right.getMinimumWidth(), right.getMinimumHeight());
        head_title = (TextView) findViewById(R.id.head_title);
        head_title.setText("修改资料");
        head_left = (ImageView) findViewById(R.id.head_left);
        head_left.setOnClickListener(this);
        name = (TextView) findViewById(R.id.name);
        edit_sex = (TextView) findViewById(R.id.edit_sex);
        edit_sex.setOnClickListener(this);
        user_text_head = (TextView) findViewById(R.id.user_text_head);
        user_text_head.setOnClickListener(this);
        user_head = (ImageViewUtil) findViewById(R.id.user_head);
        edit_desc = (EditText) findViewById(R.id.edit_desc);

        name.setText(MyApplication.getU_name());
        edit_desc.setText(MyApplication.getU_desc());
        sex = MyApplication.getU_sex();
        isSex = sex;
        if (sex.equals("1")) {

            edit_sex.setText("男");
            Drawable boy_image = getResources().getDrawable(R.mipmap.profile_avatar_genderbadge_male);
            boy_image.setBounds(0, 0, boy_image.getMinimumWidth(), boy_image.getMinimumHeight());
            edit_sex.setCompoundDrawables(boy_image, null, right, null);

        } else if (sex.equals("2")) {

            edit_sex.setText("女");
            Drawable girl_image = getResources().getDrawable(R.mipmap.profile_avatar_genderbadge_female);
            girl_image.setBounds(0, 0, girl_image.getMinimumWidth(), girl_image.getMinimumHeight());
            edit_sex.setCompoundDrawables(girl_image, null, right, null);

        } else {

            edit_sex.setText("保密");
            Drawable secrecy_image = getResources().getDrawable(R.mipmap.profile_avatar_genderbadge_secret);
            secrecy_image.setBounds(0, 0, secrecy_image.getMinimumWidth(), secrecy_image.getMinimumHeight());
            edit_sex.setCompoundDrawables(secrecy_image, null, right, null);

        }
        ImageLoader.getInstance().displayImage(UrlConfig.HEAD + MyApplication.getU_headurl(), user_head,
                ImageLoadOptions.getOptions());

    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 1:

                    Intent intents = new Intent(action);
                    sendBroadcast(intents);

                    isHead = "1";
                    ImageLoader.getInstance().displayImage(UrlConfig.HEAD + MyApplication.getU_headurl(), user_head,
                            ImageLoadOptions.getOptions());
                    toast("头像更新成功");

                    break;

                //修改性别
                case 2:

                    toast("修改成功！");
                    if (sex.equals("1")) {

                        edit_sex.setText("男");
                        Drawable boy_image = getResources().getDrawable(R.mipmap.profile_avatar_genderbadge_male);
                        boy_image.setBounds(0, 0, boy_image.getMinimumWidth(), boy_image.getMinimumHeight());
                        edit_sex.setCompoundDrawables(boy_image, null, right, null);

                    } else if (sex.equals("2")) {

                        edit_sex.setText("女");
                        Drawable girl_image = getResources().getDrawable(R.mipmap.profile_avatar_genderbadge_female);
                        girl_image.setBounds(0, 0, girl_image.getMinimumWidth(), girl_image.getMinimumHeight());
                        edit_sex.setCompoundDrawables(girl_image, null, right, null);

                    } else {

                        edit_sex.setText("保密");
                        Drawable secrecy_image = getResources().getDrawable(R.mipmap.profile_avatar_genderbadge_secret);
                        secrecy_image.setBounds(0, 0, secrecy_image.getMinimumWidth(), secrecy_image.getMinimumHeight());
                        edit_sex.setCompoundDrawables(secrecy_image, null, right, null);

                    }

                    break;

            }

        }
    };

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {

            //修改头像
            case R.id.user_text_head:
                showPhoto();//弹出选择框
                break;

            //关闭
            case R.id.head_left:

                Intent intentss = new Intent();
                intentss.putExtra("isHead", isHead);
                intentss.putExtra("isSex",isSex);
                setResult(1, intentss);
                this.finish();

                break;

            //拍照
            case R.id.photograph:

                Intent intent_graph = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                // 下面这句指定调用相机拍照后的照片存储的路径
                intent_graph.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                        .fromFile(new File(Environment
                                .getExternalStorageDirectory(),
                                "xiaoma.jpg")));
                startActivityForResult(intent_graph, 2);
                photo_dialog.dismiss();
                break;

            //选择图片
            case R.id.choice_photo:

                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 1);
                photo_dialog.dismiss();
                break;

            case R.id.edit_sex:
                showChoiceSex();
                break;

            case R.id.boy:

                if (sex.equals("1")) {
                    sex_dialog.dismiss();
                    return;
                }
                sex_h = "1";
                up.UpdateUserMessage("AlterUserMessage", "sex", MyApplication.getU_id(), "1");
                sex_dialog.dismiss();
                break;

            case R.id.girl:

                if (sex.equals("2")) {
                    sex_dialog.dismiss();
                    return;
                }
                sex_h = "2";
                up.UpdateUserMessage("AlterUserMessage", "sex", MyApplication.getU_id(), "2");
                sex_dialog.dismiss();

                break;

            case R.id.secrecy:

                if (sex.equals("0")) {
                    sex_dialog.dismiss();
                    return;
                }
                sex_h = "0";
                up.UpdateUserMessage("AlterUserMessage", "sex", MyApplication.getU_id(), "0");
                sex_dialog.dismiss();

                break;

            case R.id.cancel:
                sex_dialog.dismiss();
                break;

        }

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


    /**
     * 弹出选择头像框
     */
    public void showPhoto() {

        photo_dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        photo_inflate = LayoutInflater.from(this).inflate(R.layout.photo_dialog, null);
        photograph = (TextView) photo_inflate.findViewById(R.id.photograph);
        photograph.setOnClickListener(this);
        choice_photo = (TextView) photo_inflate.findViewById(R.id.choice_photo);
        choice_photo.setOnClickListener(this);
        //将布局设置给Dialog
        photo_dialog.setContentView(photo_inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = photo_dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.y = 20;//设置Dialog距离底部的距离

//// 以下这两句是为了保证按钮可以水平满屏
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
// 设置显示位置
        photo_dialog.onWindowAttributesChanged(lp);
//       将属性设置给窗体
        photo_dialog.show();//显示对话框
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle _Bundle;
        switch (requestCode) {

            // 如果是直接从相册获取
            case 1:

                if (data == null || "".equals(data)) {
                    return;
                } else {

                    startPhotoZoom(data.getData());
                }

                break;
            // 如果是调用相机拍照时
            case 2:
                File temp = new File(Environment.getExternalStorageDirectory()
                        + "/xiaoma.jpg");
                startPhotoZoom(Uri.fromFile(temp));
                break;
            // 取得裁剪后的图片
            case 3:

                /**
                 * 非空判断大家一定要验证，如果不验证的话， 在剪裁之后如果发现不满意，要重新裁剪，丢弃
                 * 当前功能时，会报NullException，小马只 在这个地方加下，大家可以根据不同情况在合适的 地方做判断处理类似情况
                 *
                 */
                if (data != null) {
                    dataIntent = data;
                    UpdateHead();
                }
                break;

            default:
                break;
        }

    }


    /**
     * 上传头像
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        /*
         * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能, 是直接调本地库的，小马不懂C C++
		 * 这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么 制做的了...吼吼
		 */
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    //上传修改头像
    public void UpdateHead() {

        try {

            if (dataIntent != null) {
                Bundle extras = dataIntent.getExtras();
                if (extras != null) {

                    Bitmap photodata = extras.getParcelable("data");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    photodata.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    baos.close();
                    byte[] buffer = baos.toByteArray();

                    //将图片的字节流数据加密成base64字符输出
                    String photo = Base64.encodeToString(buffer, 0, buffer.length, Base64.DEFAULT);

                    RequestParams params = new RequestParams();
                    params.put("photo", photo);
                    params.put("tag", "AlterHead");//传输的字符数据
                    params.put("uid", MyApplication.getU_id());
                    params.put("userphone", MyApplication.getU_phone());

                    AsyncHttpClient client = new AsyncHttpClient();
                    client.post(UrlConfig.USER, params, new AsyncHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, String content) {

                            try {

                                JSONObject jsonObject = new JSONObject(content);
                                String state = jsonObject.getString("state");
                                if (state.equals("200")) {

                                    String headurl = jsonObject.getString("head");
                                    SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.u_headurl, headurl);
                                    MyApplication.setU_headurl(headurl);
                                    DiskCacheUtils.removeFromCache(UrlConfig.HEAD + MyApplication.getU_headurl(), ImageLoader.getInstance().getDiskCache());
                                    MemoryCacheUtils.removeFromCache(UrlConfig.HEAD + MyApplication.getU_headurl(), ImageLoader.getInstance().getMemoryCache());
                                    handler.sendEmptyMessage(1);

                                } else {
                                    toast("头像更新失败");
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Throwable e, String data) {

                        }
                    });
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void VUpdateUserMessage(String state, String type) {

        if (state.equals("200")) {

            if (type.equals("sex")) {

                sex = sex_h;
                SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.u_sex,sex);
                MyApplication.setU_sex(sex);
                handler.sendEmptyMessage(2);

            }


        } else {
            toast("修改失败");
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {

            Intent intentss = new Intent();
            intentss.putExtra("isHead", isHead);
            intentss.putExtra("isSex",isSex);
            setResult(1, intentss);
            this.finish();


        }

        return false;

    }

}