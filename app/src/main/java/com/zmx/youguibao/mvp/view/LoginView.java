package com.zmx.youguibao.mvp.view;

import com.zmx.youguibao.mvp.bean.UserJson;

/**
 *作者：胖胖祥
 *时间：2016/9/18 0018 下午 2:07
 *功能模块：登录的界面
 */
public interface LoginView {

    /**
     * 登录成功
     * @param user
     */
    void Login(UserJson user);

    /**
     * 登录失败
     * @param message
     */
    void ErrorLogin(String message);

}
