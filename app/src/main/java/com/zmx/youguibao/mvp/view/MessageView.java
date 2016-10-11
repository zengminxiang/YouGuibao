package com.zmx.youguibao.mvp.view;

import com.zmx.youguibao.mvp.bean.VideoCommentJson;

import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/10/10 0010 下午 4:29
 *功能模块：信息的界面更新
 */

public interface MessageView {

    void SelectMessageComment(List<VideoCommentJson> lists, String pagenow);

}
