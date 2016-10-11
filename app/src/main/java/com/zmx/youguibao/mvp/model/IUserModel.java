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
     * 查询某个用户的评论
     * @param tag
     * @param pagenow
     * @param vuid
     * @param listener
     */
    void IQueryMessageComment(String tag,String pagenow,String vuid,IDataRequestListener listener);

}
