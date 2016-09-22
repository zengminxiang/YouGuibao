package com.zmx.youguibao.mvp.model;

/**
 *作者：胖胖祥
 *时间：2016/8/24 0024 下午 5:11
 *功能模块：上传视频molder
 */
public interface IUploadVideoModel {


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
    void IUploadview(String tag,String uid,String addre,String content,String videoimgurl,String videourl,IDataRequestListener listener);

    /**
     * 分页查询视频
     * @param tag 标签
     * @param pagenow 页码
     */
    void IQueryAllVideo(String tag,String pagenow,IDataRequestListener listener);

    /**
     * 分页查询视频评论
     * @param tag 标签
     * @param pagenow 页码
     * @param vid 视频id
     * @param listener
     */
    void IQueryComment(String tag,String pagenow,String vid,IDataRequestListener listener);

    /**
     * 评论
     * @param tag 标签
     * @param vid 视频的id
     * @param uid 用户的id
     * @param comment 评论的内容
     * @param listener
     */
    void IAddVideoComment(String tag,String vid,String uid,String comment,IDataRequestListener listener);

    /**
     * 查询某条评论
     * @param tag
     * @param vcid 评论id
     * @param listener
     */
    void IQueryOneComment(String tag,String vcid, IDataRequestListener listener);

    /**
     * 点赞视频
     * @param tag
     * @param vid 视频id
     * @param uid 点赞用户id
     * @param listener
     */
    void IClickALike(String tag,String vid,String uid,IDataRequestListener listener);

    /**
     *
     * @param tag 查询是否点赞
     * @param vid
     * @param uid
     * @param listener
     */
    void IWheterLike(String tag,String vid,String uid,IDataRequestListener listener);
    /**
     *
     * @param tag 取消点赞
     * @param vid
     * @param uid
     * @param listener
     */
    void ICancelLike(String tag,String vid,String uid,IDataRequestListener listener);

    /**
     *
     * @param tag 查询点赞列表
     * @param vid 视频id
     * @param listener
     */
    void ISelectLike(String tag,String vid,String pagenow,IDataRequestListener listener);


}
