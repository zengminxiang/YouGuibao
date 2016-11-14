package com.zmx.youguibao.mvp.model;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
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

    //查询某个用户的所有评论
    public void QueryMessageComment(String tag, String pagenow, String vuid,IDataRequestListener listener){
        IQueryMessageComment(tag,pagenow,vuid,listener);
    }

    //查询某个用户的所所有点赞信息
    public void QueryZanComment(String tag, String pagenow, String vuid,IDataRequestListener listener){
        IQueryZanComment(tag,pagenow,vuid,listener);
    }

    //查询某个用户所有关注消息
    public void QueryFollowComment(String tag, String pagenow, String buid, final IDataRequestListener listener){

        IQueryFollowComment(tag,pagenow,buid,listener);

    }

    //查询是否关注用户
    public void SelectFollow(String tag, String guid, String buid, IDataRequestListener listener){

        ISelectFollow(tag,guid,buid,listener);

    }

    //添加关注
    public void AddUserFollow(String tag, String guid, String buid, IDataRequestListener listener){
        IAddUserFollow(tag,guid,buid,listener);
    }

    //修改昵称
    public void UpdateUserMessage(String tag,String type,String uid,String parameter, final IDataRequestListener listener){

        IUpdateUserMessage(tag,type,uid,parameter,listener);

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

        reqC.setTag("reqC");// 设置重连策略
        reqC.setRetryPolicy(new DefaultRetryPolicy(10*1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getHttpQueues().add(reqC);
        reqC.setShouldCache(true); // 控制是否缓存
    }

    /**
     * 查询未读评论消息
     * @param tag
     * @param pagenow
     * @param vuid
     * @param listener
     */
    @Override
    public void IQueryMessageComment(String tag, String pagenow, String vuid,final IDataRequestListener listener) {


        JsonObjectRequest reqD = new JsonObjectRequest(Request.Method.GET, UrlConfig.QueryMessageComment(tag,pagenow,vuid), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                listener.loadSuccess(jsonObject.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        reqD.setTag("reqD");// 设置重连策略
        reqD.setRetryPolicy(new DefaultRetryPolicy(10*1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getHttpQueues().add(reqD);
        reqD.setShouldCache(true); // 控制是否缓存

    }

    /**
     * 查询未读点赞消息
     * @param tag
     * @param pagenow
     * @param vuid
     * @param listener
     */
    @Override
    public void IQueryZanComment(String tag, String pagenow, String vuid,final IDataRequestListener listener) {

        JsonObjectRequest reqE = new JsonObjectRequest(Request.Method.GET, UrlConfig.QueryZanComment(tag,pagenow,vuid), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                listener.loadSuccess(jsonObject.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        reqE.setTag("reqE");// 设置重连策略
        reqE.setRetryPolicy(new DefaultRetryPolicy(10*1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getHttpQueues().add(reqE);
        reqE.setShouldCache(true); // 控制是否缓存

    }

    /**
     * 查询未读关注消息
     * @param tag
     * @param pagenow
     * @param buid
     * @param listener
     */
    @Override
    public void IQueryFollowComment(String tag, String pagenow, String buid, final IDataRequestListener listener) {

        JsonObjectRequest reqF = new JsonObjectRequest(Request.Method.GET, UrlConfig.QueryFollowComment(tag,pagenow,buid), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                listener.loadSuccess(jsonObject.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        reqF.setTag("reqF");// 设置重连策略
        reqF.setRetryPolicy(new DefaultRetryPolicy(10*1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getHttpQueues().add(reqF);
        reqF.setShouldCache(true); // 控制是否缓存

    }

    @Override
    public void ISelectFollow(String tag, String guid, String buid, final IDataRequestListener listener) {

        JsonObjectRequest reqJ = new JsonObjectRequest(Request.Method.GET, UrlConfig.QueryUserFollow(tag,guid,buid), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                listener.loadSuccess(jsonObject.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        reqJ.setTag("reqJ");
        MyApplication.getHttpQueues().add(reqJ);
        reqJ.setShouldCache(true); // 控制是否缓存

    }

    /**
     *
     * @param tag 添加关注
     * @param guid
     * @param buid
     * @param listener
     */
    @Override
    public void IAddUserFollow(String tag, String guid, String buid, final IDataRequestListener listener) {

        JsonObjectRequest reqK = new JsonObjectRequest(Request.Method.GET, UrlConfig.AddFollow(tag,guid,buid), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                listener.loadSuccess(jsonObject.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        reqK.setTag("reqK");
        MyApplication.getHttpQueues().add(reqK);
        reqK.setShouldCache(true); // 控制是否缓存
    }

    @Override
    public void IUpdateUserMessage(String tag,String type,String uid,String parameter,final IDataRequestListener listener) {

        JsonObjectRequest reqL = new JsonObjectRequest(Request.Method.GET, UrlConfig.UpdateUserMessage(tag,type,uid,parameter), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                listener.loadSuccess(jsonObject.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        reqL.setTag("reqL");
        MyApplication.getHttpQueues().add(reqL);
        reqL.setShouldCache(true); // 控制是否缓存

    }

}
