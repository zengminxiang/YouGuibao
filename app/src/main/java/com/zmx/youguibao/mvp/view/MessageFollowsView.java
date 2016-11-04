package com.zmx.youguibao.mvp.view;

import com.zmx.youguibao.mvp.bean.FollowUserPojo;

import java.util.List;

/**
 *
 * 开发者：胖胖祥
 * 时间：2016/11/2 17:10
 * 功能模块：关注未读消息
 *
 */
public interface MessageFollowsView {

    void SelectMessageComment(List<FollowUserPojo> lists, String pagenow);

}
