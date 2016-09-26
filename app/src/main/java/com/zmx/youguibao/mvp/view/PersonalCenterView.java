package com.zmx.youguibao.mvp.view;

import com.zmx.youguibao.mvp.bean.PersonalCenterPojo;
import com.zmx.youguibao.mvp.bean.VideoListJson;

import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/9/26 0026 上午 11:04
 *功能模块：个人中心
 */
public interface PersonalCenterView {

    //用户资料
    void SelectUserMessage(PersonalCenterPojo pcp);

    //用户发表的视频
    void SelectUserVideos(int sun,List<VideoListJson> lists);

}
