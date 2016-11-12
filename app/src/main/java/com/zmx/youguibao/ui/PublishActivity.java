package com.zmx.youguibao.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.duanqu.qupai.bean.QupaiUploadTask;
import com.duanqu.qupai.upload.QupaiUploadListener;
import com.duanqu.qupai.upload.UploadService;
import com.zmx.youguibao.BaseActivity;
import com.zmx.youguibao.MyApplication;
import com.zmx.youguibao.R;
import com.zmx.youguibao.SharePreferenceUtil;
import com.zmx.youguibao.customview.MyRoundProgressBar;
import com.zmx.youguibao.mvp.presenter.UploadVideoPresenter;
import com.zmx.youguibao.mvp.view.UploadVideoView;
import com.zmx.youguibao.qupai.bean.Contant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.UUID;

/**
 *作者：胖胖祥
 *时间：2016/8/19 0019 上午 10:34
 *功能模块：发表说说的界面
 */
public class PublishActivity extends BaseActivity implements UploadVideoView{

    private ImageView video_img,publish,close;//视频第一帧图片、发表按钮,关闭按钮
    private MyRoundProgressBar loading;//加载提示框
    private PopupWindow pw;//弹出框
    private TextView addre;//选择地址
    private EditText content;//发表的内容
    private RelativeLayout layout;
    private int screenWidth;

    private String videourl;//视频路径
    private String videoimg;//第一帧图片路径
    private int progress = 0;//上传进度

    private UploadVideoPresenter presenter;
    private String message;//发表视频后服务器返回的参数

    private String address="";//地址


    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish;
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

        presenter = new UploadVideoPresenter(this,this);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        videourl = getIntent().getStringExtra("url");
        videoimg = getIntent().getStringExtra("img");

        Bitmap bitmap = getLoacalBitmap(videoimg);
        video_img = (ImageView) findViewById(R.id.video_img);
        video_img.setImageBitmap(bitmap);
        layout = (RelativeLayout) findViewById(R.id.layout);

        publish = (ImageView) findViewById(R.id.publish);
        publish.setOnClickListener(this);
        close = (ImageView) findViewById(R.id.close);
        close.setOnClickListener(this);
        addre = (TextView) findViewById(R.id.addre);
        addre.setOnClickListener(this);
        content = (EditText) findViewById(R.id.content);

        initpw();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){
            //关闭
            case R.id.close:

                InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputmanger.hideSoftInputFromWindow(v.getWindowToken(), 0);
                onBackPressed();

                break;

            //发表
            case R.id.publish:

                if(TextUtils.isEmpty(content.getText().toString())){
                    Toast.makeText(this,"输入内容",Toast.LENGTH_LONG).show();
                    return;
                }
                pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
                makeWindowDark();
                startUpload();

            break;

            case R.id.addre:

                startActivity(AddressChoiceActivity.class,null,1);

                break;

        }

    }

    private final int UPLOAD = 1;
    private final int FINISH = 2;

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case UPLOAD:
                    loading.setProgress(progress);
                    break;

                case FINISH:

                    if(message.equals("200")){

                        Toast.makeText(mActivity,"发送成功",Toast.LENGTH_LONG).show();
                        PublishActivity.this.finish();

                    }else{

                        Toast.makeText(mActivity,"发送失败",Toast.LENGTH_LONG).show();

                    }

                    break;

            }

        }
    };

    /**
     * 初始化弹出框
     */
    private void initpw() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.popup_window_upoad, null);
        loading = (MyRoundProgressBar) view.findViewById(R.id.loading);
        pw = new PopupWindow(view, screenWidth * 4 / 5, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pw.setFocusable(true);

        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                makeWindowLight();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });
    }

    /**
     * 让屏幕变暗
     */
    public void makeWindowDark() {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.5f;
        window.setAttributes(lp);
    }

    /**
     * 让屏幕变亮
     */
    public void makeWindowLight() {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 1f;
        window.setAttributes(lp);
    }

    /**
     * 创建一个上传任务
     * @param context
     * @param uuid        随机生成的UUID
     * @param _VideoFile  完整视频文件
     * @param _Thumbnail  缩略图
     * @param accessToken 通过调用鉴权得到token
     * @param space        开发者生成的Quid，必须要和token保持一致
     * @param share       是否公开 0公开分享 1私有(default) 公开类视频不需要AccessToken授权
     * @param tags        标签 多个标签用 "," 分隔符
     * @param description 视频描述
     * @return
     */
    private QupaiUploadTask createUploadTask(Context context, String uuid, File _VideoFile, File _Thumbnail, String accessToken,
                                             String space, int share, String tags, String description) {
        UploadService uploadService = UploadService.getInstance();
        return uploadService.createTask(context, uuid, _VideoFile, _Thumbnail,
                accessToken, space, share, tags, description);
    }

    /**
     * 开始上传
     */

    private String videoUrl;
    private String imageUrl;
    private void startUpload() {

        UploadService uploadService = UploadService.getInstance();
        uploadService.setQupaiUploadListener(new QupaiUploadListener() {
            @Override
            public void onUploadProgress(String uuid, long uploadedBytes, long totalBytes) {
                progress = (int) (uploadedBytes * 100 / totalBytes);
                handler.sendEmptyMessage(UPLOAD);

            }

            @Override
            public void onUploadError(String uuid, int errorCode, String message) {
                Log.e("错误错误错误错误", "uuid:" + uuid + "onUploadError" + errorCode + message);
            }

            @Override
            public void onUploadComplte(String uuid, int responseCode, String responseMessage) {
                //http://{DOMAIN}/v/{UUID}.mp4?token={ACCESS-TOKEN}

                //这里返回的uuid是你创建上传任务时生成的uuid.开发者可以使用其他作为标识
                //videoUrl返回的是上传成功的视频地址,imageUrl是上传成功的图片地址
                videoUrl = Contant.domain + "/v/" + responseMessage + ".mp4";
                imageUrl = Contant.domain + "/v/" + responseMessage + ".jpg";
                Log.e("videoUrl",""+videoUrl);
                Log.e("imageUrl",""+imageUrl);
                presenter.Upload("publish", MyApplication.getU_id(),address,content.getText().toString(),imageUrl,videoUrl);
            }
        });

        String uuid = UUID.randomUUID().toString();
        Log.e("uuid","uuid"+uuid);
        Log.e("QupaiAuth",  "accessToken" + SharePreferenceUtil.getInstance(this).getString(SharePreferenceUtil.accessToken,"") +"space"+ Contant.space);
        startUpload(createUploadTask(this, uuid, new File(videourl), new File(videoimg),
                SharePreferenceUtil.getInstance(this).getString(SharePreferenceUtil.accessToken,""), MyApplication.getU_id(), Contant.shareType, Contant.tags, Contant.description));
    }

    /**
     * 开始上传
     * @param data 上传任务的task
     */
    private void startUpload(QupaiUploadTask data) {
        try {
            UploadService uploadService = UploadService.getInstance();
            uploadService.startUpload(data);
        } catch (IllegalArgumentException exc) {
            Log.d("upload", "Missing some arguments. " + exc.getMessage());
        }
    }

    /**
     * 加载本地图片
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(pw != null){
            pw.dismiss();
        }

    }

    /**
     * 上传视频的回调
     * @param message
     */
    @Override
    public void UploadFinishMessage(String message) {
        this.message = message;

        handler.sendEmptyMessage(FINISH);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bundle;

       switch (requestCode){

           case 1:

               if(data != null){

                   bundle = data.getExtras();
                   String adds = bundle.getString("name");
                   String city = bundle.getString("city");

                   address = bundle.getString("name")+bundle.getString("city")+"";

                   addre.setText(city+" "+adds);

               }

               break;

       }
    }
}
