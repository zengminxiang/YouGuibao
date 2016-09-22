package com.zmx.youguibao.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.zmx.youguibao.BaseFragment;
import com.zmx.youguibao.MyApplication;
import com.zmx.youguibao.R;
import com.zmx.youguibao.adapter.CommentAdapter;
import com.zmx.youguibao.mvp.bean.NearbyUserJson;
import com.zmx.youguibao.utils.UrlConfig;
import com.zmx.youguibao.utils.baidu.MyOrientationListener;
import com.zmx.youguibao.utils.view.ImageViewUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/8/16 0016 下午 2:23
 *功能模块：附件的人，不用到
 */
public class NearbyFragment extends BaseFragment{

    /**
     * 地图控件
     */
    private MapView mMapView = null;
    /**
     * 地图实例
     */
    private BaiduMap mBaiduMap;
    /**
     * 定位的客户端
     */
    private LocationClient mLocationClient;
    /**
     * 定位的监听器
     */
    public MyLocationListener mMyLocationListener;
    /**
     * 当前定位的模式
     */
    private MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
    /***
     * 是否是第一次定位
     */
    private volatile boolean isFristLocation = true;

    /**
     * 当前的精度
     */
    private float mCurrentAccracy;
    /**
     * 方向传感器的监听器
     */
    private MyOrientationListener myOrientationListener;
    /**
     * 方向传感器X方向的值
     */
    private int mXDirection;
    /**
     * 最新一次的经纬度
     */
    private double mCurrentLantitude;
    private double mCurrentLongitude;

    // 初始化全局 bitmap 信息，不用时及时 recycle
    private BitmapDescriptor mIconMaker;

    //左上角坐标
    private String ulLongitube;
    private String ulLatitude;

    //右上角坐标
    private String lrLongitube;
    private String lrLatitude;


    private List<NearbyUserJson> lists = new ArrayList<>();

    private TextView user_name;
    private ImageViewUtil head;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_nearby, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);

    }


    @Override
    protected void initView() {

        // 第一次定位
        isFristLocation = true;
        // 获取地图控件引用
        mMapView = (MapView) findViewById(R.id.id_bmapView);
        // 获得地图的实例
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(20.0f);
        mBaiduMap.setMapStatus(msu);

        // 初始化定位
        initMyLocation();
        // 初始化传感器
        initOritationListener();
        initMarkerClickEvent();
        initMapClickEvent();
        center2myLoc();//我的位置
    }

    /**
     * 初始化定位相关代码
     */
    private void initMyLocation() {
        // 定位初始化
        mLocationClient = new LocationClient(this.getActivity());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        // 设置定位的相关配置
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);

    }

    /**
     * 初始化方向传感器
     */
    private void initOritationListener() {
        myOrientationListener = new MyOrientationListener(
                this.getContext().getApplicationContext());
        myOrientationListener
                .setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
                    @Override
                    public void onOrientationChanged(float x) {
                        mXDirection = (int) x;
                        // 构造定位数据
                        MyLocationData locData = new MyLocationData.Builder()
                                .accuracy(mCurrentAccracy)
                                // 此处设置开发者获取到的方向信息，顺时针0-360
                                .direction(mXDirection)
                                .latitude(mCurrentLantitude)
                                .longitude(mCurrentLongitude).build();
                        // 设置定位数据
                        mBaiduMap.setMyLocationData(locData);
                        // 设置自定义图标
                        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                                .fromResource(R.mipmap.navi_map_gps_locked);
                        MyLocationConfiguration config = new MyLocationConfiguration(
                                mCurrentMode, true, mCurrentMarker);
                        mBaiduMap.setMyLocationConfigeration(config);

                    }
                });
    }


    private void initMarkerClickEvent() {

        // 对Marker的点击
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {

                return true;

            }
        });
    }

    //------百度地图模块---------
    private void initMapClickEvent() {

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {

                                            @Override
                                            public boolean onMapPoiClick(MapPoi arg0) {
                                                return false;
                                            }

                                            @Override
                                            public void onMapClick(LatLng arg0) {

                                                mBaiduMap.hideInfoWindow();

                                            }
                                        }
        );


        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

                Log.e("开始滑动", "1");

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

                Log.e("正在滑动", "2");

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {

                Log.e("滑动停止", "3");

                Point pt = new Point();
                pt.x = 0;
                pt.y = 0;
                LatLng ll = mMapView.getMap().getProjection().fromScreenLocation(pt);
                Log.e("aaaaa", "左上角经纬度 Leftlatitude:" + ll.latitude + " Leftlongitude左下角:" + ll.longitude);

                ulLongitube = ll.longitude + "";
                ulLatitude = ll.latitude + "";

                DisplayMetrics dm = new DisplayMetrics();
                mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);

                Point pty = new Point();
                pty.x = dm.widthPixels;
                pty.y = dm.heightPixels;
                LatLng lly = mMapView.getMap().getProjection().fromScreenLocation(pty);
                Log.e("aaaaa", "右下角经纬度 rightlatitude:" + lly.latitude + " rightlongitude右下角:" + lly.longitude);
                lrLongitube = lly.longitude + "";
                lrLatitude = lly.latitude + "";
                QueryNearbyUser();//获取范围内的数据
            }

        });
    }

    /**
     * 地图移动到我的位置,此处可以重新发定位请求，然后定位； 直接拿最近一次经纬度，如果长时间没有定位成功，可能会显示效果不好
     */
    private void center2myLoc() {

        LatLng ll = new LatLng(mCurrentLantitude, mCurrentLongitude);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
        mBaiduMap.animateMapStatus(u);

    }


    /**
     * 实现实位回调监听
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mXDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mCurrentAccracy = location.getRadius();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            mCurrentLantitude = location.getLatitude();
            mCurrentLongitude = location.getLongitude();

            // 设置自定义图标
            BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                    .fromResource(R.mipmap.navi_map_gps_locked);
            MyLocationConfiguration config = new MyLocationConfiguration(
                    mCurrentMode, true, mCurrentMarker);
            mBaiduMap.setMyLocationConfigeration(config);
            // 第一次定位时，将地图位置移动到当前位置
            if (isFristLocation) {
                isFristLocation = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
            }
        }

    }



    @Override
    public void onStart() {
        // 开启图层定位
        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted()) {

            mLocationClient.start();

        }
        // 开启方向传感器
        myOrientationListener.start();
        super.onStart();
    }



    @Override
    public void onStop() {
        // 关闭图层定位
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();

        // 关闭方向传感器
        myOrientationListener.stop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理

        if (mMapView != null) {

            mMapView.onDestroy();

        }
        if (mIconMaker != null) {

            mIconMaker.recycle();

        }
        mMapView = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }


    public void QueryNearbyUser(){

        Log.e("ssss","进来了");

        JsonObjectRequest reqA = new JsonObjectRequest(Request.Method.GET, UrlConfig.QueryNearbyUser("nearby","0",ulLongitube,lrLongitube,ulLatitude,lrLatitude), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {

                    Log.e("ssss","s"+jsonObject.getString("nearbyUser"));

                    String state = jsonObject.getString("state");

                    if(state.equals("200")){

                        String near = jsonObject.getString("nearbyUser");
                        JSONArray jsonArray = new JSONArray(near);
                        Gson gson = new Gson();

                        for (int i = 0; i<jsonArray.length();i++){

                            JSONObject json = (JSONObject) jsonArray.get(i);
                            NearbyUserJson users = gson.fromJson(json.toString(),NearbyUserJson.class);
                            lists.add(users);

                        }

                        handler.sendEmptyMessage(1);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        reqA.setTag("reqA");
        MyApplication.getHttpQueues().add(reqA);
        reqA.setShouldCache(true); // 控制是否缓存

    }

    /**
     * 初始化图层
     */
    public void addInfosOverlay(List<NearbyUserJson> fiels) {

        mBaiduMap.clear();
        LatLng latLng = null;
        OverlayOptions overlayOptions = null;
        Marker marker = null;

        for (NearbyUserJson nu : fiels) {

            if (nu.getC_latitude() != null && nu.getC_longitude() != null) {


                View point = LayoutInflater.from(mActivity).inflate(R.layout.baidu_nearby_user, null);
                user_name = (TextView) point.findViewById(R.id.user_name);
                user_name.setText("曾");
                mIconMaker = BitmapDescriptorFactory.fromBitmap(getViewBitmap(point));

                // 位置
                latLng = new LatLng(Double.parseDouble(nu.getC_latitude()), Double.parseDouble(nu.getC_longitude()));
                // 图标
                overlayOptions = new MarkerOptions().position(latLng)
                        .icon(mIconMaker).zIndex(9);
                marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
                Bundle bundle = new Bundle();
                bundle.putSerializable("fiels", nu);
                marker.setExtraInfo(bundle);

            }

        }

    }
    /**
     * 将view转换为bitmap
     *
     * @param addViewContent
     * @return
     */
    private Bitmap getViewBitmap(View addViewContent) {

        addViewContent.setDrawingCacheEnabled(true);

        addViewContent.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0,
                addViewContent.getMeasuredWidth(),
                addViewContent.getMeasuredHeight());

        addViewContent.buildDrawingCache();
        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        return bitmap;
    }



    private final int LISTS = 1;//加载附件的广场
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case LISTS:
                    addInfosOverlay(lists);
                    break;

            }

        }
    };

}
