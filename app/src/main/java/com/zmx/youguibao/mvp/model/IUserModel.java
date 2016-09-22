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

}
