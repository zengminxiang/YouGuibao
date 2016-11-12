package com.zmx.youguibao;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
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
import com.zhy.autolayout.config.AutoLayoutConifg;
import com.zmx.youguibao.qupai.bean.Contant;

import cn.jpush.android.api.JPushInterface;
import greenDao.DaoMaster;
import greenDao.DaoSession;

/**
 * 作者：胖胖祥
 * 时间：2016/8/18 0018 上午 10:00
 * 功能模块：
 */
public class MyApplication extends Application {

    public static String u_id = "u_id";//用户id
    public static String u_phone = "u_phone";//手机号码
    public static String u_pwd = "u_pwd";//密码
    public static String u_name = "u_name";//用户名
    public static String u_headurl = "u_headurl";//用户路径
    public static String u_desc = "u_desc";//用户简介
    public static String u_sex = "u_sex";//用户性别
    public static String u_time = "u_time";//注册时间
    public static String u_number = "u_number";//用户编号
    public static String u_experience = "u_experience";//用户经验
    private static boolean isLogin;//是否登录
    private static String accessToken;//趣拍的token

    public static MyApplication mInstance;

    public static RequestQueue queue;//volley全局变量

    //green数据缓存
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        ToastMgr.builder.init(getApplicationContext());//弹出框
        mInstance = this;
        //初始化百度地图
        SDKInitializer.initialize(getApplicationContext());

        //屏幕适配
        AutoLayoutConifg.getInstance().useDeviceSize();
        //初始化极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        setDatabase();//初始化sql

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
     * 设置greenDao
     */
    private void setDatabase() {

        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        mHelper = new DaoMaster.DevOpenHelper(this, "notes-db", null);
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }
    public DaoSession getDaoSession() {
        return mDaoSession;
    }
    public SQLiteDatabase getDb() {
        return db;
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

    public static String getU_id() {
        return u_id;
    }

    public static void setU_id(String u_id) {
        MyApplication.u_id = u_id;
    }

    public static String getU_phone() {
        return u_phone;
    }

    public static void setU_phone(String u_phone) {
        MyApplication.u_phone = u_phone;
    }

    public static String getU_pwd() {
        return u_pwd;
    }

    public static void setU_pwd(String u_pwd) {
        MyApplication.u_pwd = u_pwd;
    }

    public static String getU_name() {
        return u_name;
    }

    public static void setU_name(String u_name) {
        MyApplication.u_name = u_name;
    }

    public static String getU_headurl() {
        return u_headurl;
    }

    public static void setU_headurl(String u_headurl) {
        MyApplication.u_headurl = u_headurl;
    }

    public static String getU_desc() {
        return u_desc;
    }

    public static void setU_desc(String u_desc) {
        MyApplication.u_desc = u_desc;
    }

    public static String getU_sex() {
        return u_sex;
    }

    public static void setU_sex(String u_sex) {
        MyApplication.u_sex = u_sex;
    }

    public static String getU_time() {
        return u_time;
    }

    public static void setU_time(String u_time) {
        MyApplication.u_time = u_time;
    }

    public static String getU_number() {
        return u_number;
    }

    public static void setU_number(String u_number) {
        MyApplication.u_number = u_number;
    }

    public static String getU_experience() {
        return u_experience;
    }

    public static void setU_experience(String u_experience) {
        MyApplication.u_experience = u_experience;
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(String accessToken) {
        MyApplication.accessToken = accessToken;
    }

    public static boolean isLogin() {
        return isLogin;
    }

    public static void setIsLogin(boolean isLogin) {
        MyApplication.isLogin = isLogin;
    }
}



