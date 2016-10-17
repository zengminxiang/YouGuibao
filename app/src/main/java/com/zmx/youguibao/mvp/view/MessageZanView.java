package com.zmx.youguibao.mvp.view;

import com.zmx.youguibao.mvp.bean.VideoLikeJson;

import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/10/17 0017 下午 2:52
 *功能模块：点赞的消息界面
 */
public interface MessageZanView {

    void SelectZanComment(List<VideoLikeJson>lists,String pagenow);

}
