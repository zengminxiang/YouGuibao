package com.zmx.youguibao.mvp.view;

import com.zmx.youguibao.mvp.bean.VideoCommentJson;
import com.zmx.youguibao.mvp.bean.VideoLikeJson;

import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/9/2 0002 上午 10:27
 *功能模块：视频详情界面更新
 */
public interface VideoDetailsView {

    /**
     * 查询视频评论列表
     * @param lists  评论
     * @param pagenow 页码
     */
    void QueryComment(List<VideoCommentJson> lists,String pagenow);

    /**
     * 评论视频
     * @param comment 评论成功
     */
    void AddComment(VideoCommentJson comment);

    /**
     * 点赞视频
     */
    void ClickALike(String state);

    /**
     * 查询是否点赞视频
     *
     */
    void WheterLike(boolean wheterLike);

    /**
     * 取消点赞
     * @param state
     */
    void VCancelLike(String state);


    /**
     * 查询点赞列表
     * @param likeJson
     * @param pagenow
     */
    void VSelectLike(List<VideoLikeJson> likeJson,String pagenow);

    /**
     * 查询是否关注
     * @param state
     */
    void VSelectFollow(String state);

    /**
     * 添加关注
     * @param state
     */
    void VAddFollow(String state);

}
