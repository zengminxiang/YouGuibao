package com.zmx.youguibao;

import android.app.Application;
import android.content.Context;
import android.hardware.Camera;
import android.support.multidex.MultiDex;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;
import com.duanqu.qupai.engine.session.MovieExportOptions;
import com.duanqu.qupai.engine.session.ProjectOptions;
import com.duanqu.qupai.engine.session.ThumbnailExportOptions;
import com.duanqu.qupai.engine.session.UISettings;
import com.duanqu.qupai.engine.session.VideoSessionCreateInfo;
import com.duanqu.qupai.sdk.android.QupaiManager;
import com.duanqu.qupai.sdk.android.QupaiService;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.zmx.youguibao.qupai.bean.Contant;

/**
 * 作者：胖胖祥
 * 时间：2016/8/18 0018 上午 10:00
 * 功能模块：
 */
public class MyApplication extends Application {

    public static MyApplication mInstance;

    public static RequestQueue queue;//volley全局变量

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        ToastMgr.builder.init(getApplicationContext());//弹出框
        mInstance = this;
        //初始化百度地图
        SDKInitializer.initialize(getApplicationContext());

        //初始化volley
        queue = Volley.newRequestQueue(getApplicationContext());

        //初始化趣拍sdk
        QupaiService qupaiService = QupaiManager
                .getQupaiService(this);
        //UI设置参数
        UISettings _UISettings = new UISettings() {

            @Override
            public boolean hasEditor() {
                return true;//是否需要编辑功能
            }

            @Override
            public boolean hasImporter() {
                return true;//是否需要导入功能
            }

            @Override
            public boolean hasGuide() {
                return false;//是否启动引导功能，建议用户第一次使用时设置为true
            }

            @Override
            public boolean hasSkinBeautifer() {
                return false;//是否显示美颜图标
            }

        };

//压缩参数
        MovieExportOptions movie_options = new MovieExportOptions.Builder()
                .setVideoBitrate(Contant.DEFAULT_BITRATE)
                .configureMuxer("movflags", "+faststart")
                .build();

     //输出视频的参数
        ProjectOptions projectOptions = new ProjectOptions.Builder()
                //输出视频宽高目前只能设置1：1的宽高，建议设置480*480.
                .setVideoSize(480, 480)
                //帧率
                .setVideoFrameRate(30)
                //时长区间
                .setDurationRange(Contant.DEFAULT_MIN_DURATION_LIMIT,Contant.DEFAULT_DURATION_LIMIT)
                .get();

        //缩略图参数,可设置取得缩略图的数量，默认10张
        ThumbnailExportOptions thumbnailExportOptions = new ThumbnailExportOptions.Builder().setCount(10).get();

        VideoSessionCreateInfo info = new VideoSessionCreateInfo.Builder()
                //水印地址，如"assets://Qupai/watermark/qupai-logo.png"
                .setWaterMarkPath(Contant.WATER_MARK_PATH)
                //水印的位置
                .setWaterMarkPosition(1)
                //摄像头方向,可配置前置或后置摄像头
                .setCameraFacing(Camera.CameraInfo.CAMERA_FACING_BACK)
                //美颜百分比,设置之后内部会记住，多次设置无效
                .setBeautyProgress(80)
                //默认是否开启
                .setBeautySkinOn(false)
                .setMovieExportOptions(movie_options)
                .setThumbnailExportOptions(thumbnailExportOptions)
                .build();

//初始化，建议在application里面做初始化，这里做是为了方便开发者认识参数的意义
        qupaiService.initRecord(info, projectOptions, _UISettings);


        //异步加载图片配置
        configImageLoader();
        DisplayImageOptions defaultOptions = new DisplayImageOptions
                .Builder()
                .showImageForEmptyUri(R.mipmap.filter_preview_lomo)
                .showImageOnFail(R.mipmap.filter_preview_lomo)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .discCacheSize(50 * 1024 * 1024)//
                .discCacheFileCount(100)//缓存一百张图片
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);

    }

    public static MyApplication getInstance() {
        return mInstance;
    }

    public static RequestQueue getHttpQueues(){

        return queue;

    }

    /**
     * 配置ImageLoder
     */
    private void configImageLoader() {

        // 初始化ImageLoader
        @SuppressWarnings("deprecation")
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.mipmap.filter_preview_lomo) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.filter_preview_lomo) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.filter_preview_lomo) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }


    /**
     * 弹出框
     */
    public enum ToastMgr{
        builder;
        private View view;
        private TextView tv;
        private Toast toast;

        /**
         * 初始化Toast
         * @param context
         */
        public void init(Context context){
            view = LayoutInflater.from(context).inflate(R.layout.toast_view, null);
            tv = (TextView) view.findViewById(R.id.toast_textview);
            toast = new Toast(context);
            toast.setView(view);
        }

        /**
         * 显示Toast
         * @param content
         * @param duration Toast持续时间
         */
        public void display(CharSequence content , int duration){
            if (content.length()!=0) {
                tv.setText(content);
                toast.setDuration(duration);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }

}



