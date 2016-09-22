package com.zmx.youguibao.mvp.view;

import com.zmx.youguibao.mvp.bean.VideoListJson;

import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/8/26 0026 下午 12:31
 *功能模块：更新所有视频的
 */
public interface VideoListView {


    //查询视频列表
    void AllVideoList(List<VideoListJson> lists, String pagenow);

}
