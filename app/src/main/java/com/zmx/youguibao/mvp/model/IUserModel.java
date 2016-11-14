package com.zmx.youguibao.mvp.model;

/**
 *作者：胖胖祥
 *时间：2016/9/18 0018 下午 2:02
 *功能模块：处理用户的
 */
public interface IUserModel {

    /**
     * 登录
     * @param name
     * @param pwd
     */
    void ILogin(String tag,String name,String pwd,IDataRequestListener listener);

    /**
     * 查询某个用户的信息
     * @param tag
     * @param uid
     */
    void ISelectUserMessage(String tag,String uid,IDataRequestListener listener);

    /**
     * 查询某个用户的所有视频
     * @param tag
     * @param pagenow
     * @param uid
     * @param listener
     */
    void ISelectUserVideos(String tag,String pagenow,String uid,IDataRequestListener listener);

    /**
     * 查询某个用户的评论未读消息
     * @param tag
     * @param pagenow
     * @param vuid
     * @param listener
     */
    void IQueryMessageComment(String tag,String pagenow,String vuid,IDataRequestListener listener);

    /**
     * 查询某个用户点赞未读消息
     * @param tag
     * @param pagenow
     * @param vuid
     * @param listener
     */
    void IQueryZanComment(String tag,String pagenow,String vuid,IDataRequestListener listener);

    /**
     * 查询某个用户关注未读消息
     * @param tag
     * @param pagenow
     * @param buid
     * @param listener
     */
    void IQueryFollowComment(String tag,String pagenow,String buid,IDataRequestListener listener);

    /**
     *
     * @param tag 查询是否关注某个用户了
     * @param guid
     * @param buid
     * @param listener
     */
    void ISelectFollow(String tag,String guid,String buid,IDataRequestListener listener);

    /**
     *
     * @param tag 添加关注
     * @param guid
     * @param buid
     * @param listener
     */
    void IAddUserFollow(String tag,String guid,String buid,IDataRequestListener listener);

    /**
     * 修改用户资料
     * @param tag
     * @param type
     * @param uid
     * @param parameter
     * @param listener
     */
    void IUpdateUserMessage(String tag,String type,String uid,String parameter,IDataRequestListener listener);



}
