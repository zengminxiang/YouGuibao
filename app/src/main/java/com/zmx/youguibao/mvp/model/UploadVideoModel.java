package com.zmx.youguibao.mvp.model;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zmx.youguibao.MyApplication;
import com.zmx.youguibao.utils.UrlConfig;

import org.json.JSONObject;

/**
 * 作者：胖胖祥
 * 时间：2016/8/24 0024 下午 5:24
 * 功能模块：视频说说的model
 */
public class UploadVideoModel implements IUploadVideoModel {


    //上传视频
    public void UploadView(String tag, String uid, String addre, String content, String videoimgurl, String videourl, IDataRequestListener listener) {
        IUploadview(tag, uid, addre, content, videoimgurl, videourl, listener);

    }
    //分页查询视频
    public void QueryAllVideo(String tag,String pagenow,IDataRequestListener listener){

        IQueryAllVideo(tag,pagenow,listener);

    }

    //分页查询视频的评论
    public void QueryComment(String tag,String pagenow,String vid,IDataRequestListener listener){
        IQueryComment(tag,pagenow,vid,listener);
    }

    //发表评论
    public void AddComment(String tag, String vid,String vuid, String uid, String comment, IDataRequestListener listener){
        IAddVideoComment(tag,vid,vuid,uid,comment,listener);
    }

    //查询某条评论
    public void QueryOneComment(String tag,String vcid,IDataRequestListener listener){

        IQueryOneComment(tag,vcid,listener);

    }

    //点赞视频
    public void ClickALike(String tag,String vid,String uid,IDataRequestListener listener){
        IClickALike(tag,vid,uid,listener);
    }

    //查询是否点赞视频了
    public void WheterLike(String tag, String vid, String uid, IDataRequestListener listener){

        IWheterLike(tag,vid,uid,listener);

    }

    //取消点赞
    public void CancelLike(String tag, String vid, String uid, IDataRequestListener listener){

        ICancelLike(tag,vid,uid,listener);

    }

    //查询点赞列表
    public void SelectLike(String tag, String vid,String pagenow, IDataRequestListener listener){

        ISelectLike(tag,vid,pagenow,listener);

    }

    //查询是否关注用户
    public void SelectFollow(String tag, String guid, String buid, IDataRequestListener listener){

        ISelectFollow(tag,guid,buid,listener);

    }

    //添加关注
    public void AddUserFollow(String tag, String guid, String buid, IDataRequestListener listener){
        IAddUserFollow(tag,guid,buid,listener);
    }

    //回复某条评论
    public void ReplyComment(String tag, String vcid, String huid, String buname, String vrcontent, String vid, IDataRequestListener listener){
        IReplyComment(tag,vcid,huid,buname,vrcontent,vid,listener);
    }



    /**
     * 发表视频
     * @param tag  标签
     * @param uid  用户id
     * @param addre 地址
     * @param content  内容
     * @param videoimgurl 视频图片路径
     * @param videourl 视频路径
     * @param listener 请求后台数据服务器响应后的回调监听
     */
    @Override
    public void IUploadview(String tag, String uid, String addre, String content, String videoimgurl, String videourl,final IDataRequestListener listener) {

        JsonObjectRequest reqA = new JsonObjectRequest(Request.Method.GET, UrlConfig.UploadVideo(tag,uid,addre,content,videoimgurl,videourl), new Response.Listener<JSONObject>() {
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

    /**
     * 分页查询视频
     * @param tag 标签
     * @param pagenow 页码
     * @param listener
     */
    @Override
    public void IQueryAllVideo(String tag, String pagenow,final IDataRequestListener listener) {

        JsonObjectRequest reqB = new JsonObjectRequest(Request.Method.GET, UrlConfig.QueryAllVideo(tag,pagenow), new Response.Listener<JSONObject>() {
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

    /**
     * 分页查询视频的评论
     * @param tag 标签
     * @param pagenow 页码
     * @param vid 视频id
     * @param listener
     */
    @Override
    public void IQueryComment(String tag, String pagenow, String vid,final IDataRequestListener listener) {

        JsonObjectRequest reqC = new JsonObjectRequest(Request.Method.GET, UrlConfig.QueryComment(tag,pagenow,vid), new Response.Listener<JSONObject>() {
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

    /**
     * 发表评论
     * @param tag 标签
     * @param vid 视频的id
     * @param uid 用户的id
     * @param comment 评论的内容
     * @param listener
     */
    @Override
    public void IAddVideoComment(String tag, String vid,String vuid, String uid, String comment,final IDataRequestListener listener) {
        JsonObjectRequest reqD = new JsonObjectRequest(Request.Method.GET, UrlConfig.AddVideoComment(tag,vid,vuid,uid,comment), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                listener.loadSuccess(jsonObject.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        reqD.setTag("reqD");
        MyApplication.getHttpQueues().add(reqD);
        reqD.setShouldCache(true); // 控制是否缓存
    }

    /**
     * 查询某条评论
     * @param tag
     * @param vcid 评论id
     * @param listener
     */
    @Override
    public void IQueryOneComment(String tag, String vcid,final IDataRequestListener listener) {

        JsonObjectRequest reqE = new JsonObjectRequest(Request.Method.GET, UrlConfig.QueryOneComment(tag,vcid), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                listener.loadSuccess(jsonObject.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        reqE.setTag("reqE");
        MyApplication.getHttpQueues().add(reqE);
        reqE.setShouldCache(true); // 控制是否缓存


    }

    @Override
    public void IReplyComment(String tag, String vcid, String huid, String buname, String vrcontent, String vid, final IDataRequestListener listener) {
        JsonObjectRequest reqE = new JsonObjectRequest(Request.Method.GET, UrlConfig.ReplyComment(tag,vcid,huid,buname,vrcontent,vid), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                listener.loadSuccess(jsonObject.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        reqE.setTag("reqE");
        MyApplication.getHttpQueues().add(reqE);
        reqE.setShouldCache(true); // 控制是否缓存
    }

    /**
     * 点赞视频
     * @param tag
     * @param vid 视频id
     * @param uid 点赞用户id
     * @param listener
     */
    @Override
    public void IClickALike(String tag, String vid, String uid, final IDataRequestListener listener) {

        JsonObjectRequest reqF = new JsonObjectRequest(Request.Method.GET, UrlConfig.ClickLike(tag,vid,uid), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                listener.loadSuccess(jsonObject.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        reqF.setTag("reqF");
        MyApplication.getHttpQueues().add(reqF);
        reqF.setShouldCache(true); // 控制是否缓存
    }

    /**
     *
     * @param tag 查询是否点赞
     * @param vid
     * @param uid
     * @param listener
     */
    @Override
    public void IWheterLike(String tag, String vid, String uid, final IDataRequestListener listener) {

        JsonObjectRequest reqJ = new JsonObjectRequest(Request.Method.GET, UrlConfig.WheterLike(tag,vid,uid), new Response.Listener<JSONObject>() {
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
     * @param tag 取消点赞
     * @param vid
     * @param uid
     * @param listener
     */
    @Override
    public void ICancelLike(String tag, String vid, String uid, final IDataRequestListener listener) {

        JsonObjectRequest reqH = new JsonObjectRequest(Request.Method.GET, UrlConfig.CancelLike(tag,vid,uid), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                listener.loadSuccess(jsonObject.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        reqH.setTag("reqH");
        MyApplication.getHttpQueues().add(reqH);
        reqH.setShouldCache(true); // 控制是否缓存

    }

    /**
     *
     * @param tag 查询点赞列表
     * @param vid 视频id
     * @param listener
     */
    @Override
    public void ISelectLike(String tag, String vid,String pagenow, final IDataRequestListener listener) {


        JsonObjectRequest reqI = new JsonObjectRequest(Request.Method.GET, UrlConfig.SelectLike(tag,vid,pagenow), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                listener.loadSuccess(jsonObject.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        reqI.setTag("reqI");
        MyApplication.getHttpQueues().add(reqI);
        reqI.setShouldCache(true); // 控制是否缓存

    }

    /**
     *
     * @param tag 查询是否关注某个用户了
     * @param guid
     * @param buid
     * @param listener
     */
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

}
