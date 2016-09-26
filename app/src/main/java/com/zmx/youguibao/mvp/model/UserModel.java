package com.zmx.youguibao.mvp.model;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zmx.youguibao.MyApplication;
import com.zmx.youguibao.utils.UrlConfig;

import org.json.JSONObject;

/**
 *作者：胖胖祥
 *时间：2016/9/18 0018 下午 2:03
 *功能模块：用户
 */
public class UserModel implements IUserModel{

    //登录
    public void Login(String tag,String name, String pwd,IDataRequestListener listener){
        ILogin(tag,name,pwd,listener);
    }

    //查询某个用户的资料
    public void SelectUserMessage(String tag, String uid,IDataRequestListener listener){
        ISelectUserMessage(tag,uid,listener);
    }

    //查询某个用户的视频
    public void SelectUserVideos(String tag, String pagenow, String uid, IDataRequestListener listener){
        ISelectUserVideos(tag,pagenow,uid,listener);
    }

    @Override
    public void ILogin(String tag,String name, String pwd,final IDataRequestListener listener) {

        JsonObjectRequest reqA = new JsonObjectRequest(Request.Method.GET, UrlConfig.Login(tag,name,pwd), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                listener.loadSuccess(jsonObject.toString());

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

    @Override
    public void ISelectUserMessage(String tag, String uid,final IDataRequestListener listener) {

        JsonObjectRequest reqB = new JsonObjectRequest(Request.Method.GET, UrlConfig.SelectUserMessage(tag,uid), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                listener.loadSuccess(jsonObject.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        reqB.setTag("reqB");
        MyApplication.getHttpQueues().add(reqB);
        reqB.setShouldCache(true); // 控制是否缓存
    }

    @Override
    public void ISelectUserVideos(String tag, String pagenow, String uid, final IDataRequestListener listener) {

        JsonObjectRequest reqC = new JsonObjectRequest(Request.Method.GET, UrlConfig.SelectUserVideos(tag,pagenow,uid), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                listener.loadSuccess(jsonObject.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        reqC.setTag("reqC");
        MyApplication.getHttpQueues().add(reqC);
        reqC.setShouldCache(true); // 控制是否缓存
    }
}
