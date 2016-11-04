package com.zmx.youguibao.mvp.view;

import com.zmx.youguibao.mvp.bean.VideoListJson;

import java.util.List;

/**
 *
 * 开发者：胖胖祥
 * 时间：2016/11/1 16:09
 * 功能模块：关注的视频
 *
 */
public interface FollowFragmentView {

    //查询视频列表
    void AllFollowVideo(List<VideoListJson> lists, String pagenow);

}
