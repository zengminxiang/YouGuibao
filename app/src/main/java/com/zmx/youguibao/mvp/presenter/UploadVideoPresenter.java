package com.zmx.youguibao.mvp.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.zmx.youguibao.SharePreferenceUtil;
import com.zmx.youguibao.mvp.bean.ReplyCommentJson;
import com.zmx.youguibao.mvp.bean.VideoCommentJson;
import com.zmx.youguibao.mvp.bean.VideoLikeJson;
import com.zmx.youguibao.mvp.bean.VideoListJson;
import com.zmx.youguibao.mvp.model.IDataRequestListener;
import com.zmx.youguibao.mvp.model.UploadVideoModel;
import com.zmx.youguibao.mvp.view.ReplyOneCommentView;
import com.zmx.youguibao.mvp.view.UploadVideoView;
import com.zmx.youguibao.mvp.view.VideoDetailsView;
import com.zmx.youguibao.mvp.view.VideoListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/8/25 0025 上午 10:07
 *功能模块：通知model请求服务器和通知view更新
 */
public class UploadVideoPresenter {

    private UploadVideoModel model;
    private UploadVideoView view;
    private VideoListView videolist;
    private VideoDetailsView detailsView;
    private ReplyOneCommentView replyOneCommentView;

    private Context context;

    /**
     * 视频上传
     * @param view
     * @param context
     */
    public UploadVideoPresenter(UploadVideoView view, Context context){

        this.context = context;
        this.view = view;
        model = new UploadVideoModel();

    }

    /**
     * 查询视频列表
     * @param videolist
     * @param context
     */
    public UploadVideoPresenter(VideoListView videolist, Context context){

        this.context = context;
        this.videolist = videolist;
        model = new UploadVideoModel();

    }

    /**
     * 视频详情
     * @param detailsView
     * @param context
     */
    public UploadVideoPresenter(VideoDetailsView detailsView,Context context){

        this.context = context;
        this.detailsView = detailsView;
        model = new UploadVideoModel();

    }

    public UploadVideoPresenter(ReplyOneCommentView replyOneCommentView,Context context){

        this.context = context;
        this.replyOneCommentView = replyOneCommentView;
        model = new UploadVideoModel();

    }

    /**
     * 上传视频
     * @param tag
     * @param uid
     * @param addre
     * @param content
     * @param videoimgurl
     * @param videourl
     */
    public void Upload(String tag,String uid,String addre,String content,String videoimgurl,String videourl){

        model.UploadView(tag, uid, addre, content, videoimgurl, videourl, new IDataRequestListener() {
            @Override
            public void loadSuccess(Object object) {

                try {

                    String state = new JSONObject(object.toString()).getString("state");
                    view.UploadFinishMessage(state);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    /**
     * 分页查询视频
     * @param tag
     * @param pagenow
     */
    public void QueryAllVideo(String tag,String pagenow){

        model.QueryAllVideo(tag, pagenow, new IDataRequestListener() {
            @Override
            public void loadSuccess(Object object) {

                try {

                    String json = new JSONObject(object.toString()).getString("video");
                    String pagenows = new JSONObject(object.toString()).getString("sum");

                    List<VideoListJson> lists = new ArrayList<VideoListJson>();
                    VideoListJson video;
                    Gson gson;

                    JSONArray array = new JSONArray(json);
                    for(int i=0;i<array.length();i++){

                        JSONObject jsonObject = array.getJSONObject(i);
                        gson = new Gson();
                        video = gson.fromJson(jsonObject.toString(),VideoListJson.class);
                        lists.add(video);

                    }

                    videolist.AllVideoList(lists,pagenows);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    /**
     * 查询评论
     * @param tag
     * @param pagenow 页码
     * @param vid 视频id
     */
    public void QueryComment(String tag, final String pagenow, String vid){
        model.QueryComment(tag, pagenow, vid, new IDataRequestListener() {
            @Override
            public void loadSuccess(Object object) {

                try {

                    String json = new JSONObject(object.toString()).getString("comments");
                    String pagenows = new JSONObject(object.toString()).getString("sum");

                    Log.e("lists","lists"+json);
                    List<VideoCommentJson> lists = new ArrayList<VideoCommentJson>();

                    JSONArray array = new JSONArray(json);
                    for(int i=0;i<array.length();i++){

                        JSONObject jsonObject = array.getJSONObject(i);
                        VideoCommentJson comment = new VideoCommentJson();
                        comment.setVc_id(jsonObject.getString("vc_id"));
                        comment.setU_id(jsonObject.getString("u_id"));
                        comment.setU_head(jsonObject.getString("u_head"));
                        comment.setU_name(jsonObject.getString("u_name"));
                        comment.setU_experience(jsonObject.getString("u_experience"));
                        comment.setVc_content(jsonObject.getString("vc_content"));
                        comment.setVc_time(jsonObject.getString("vc_time"));
                        comment.setV_id(jsonObject.getString("v_id"));

                        //获取回复的评论
                        List<ReplyCommentJson> replylists = new ArrayList<ReplyCommentJson>();
                        JSONArray replys = new JSONArray(jsonObject.getString("replys"));
                        for(int j = 0 ;j<replys.length();j++){

                            ReplyCommentJson reply = new ReplyCommentJson();
                            JSONObject rep = replys.getJSONObject(j);
                            reply.setVc_id(rep.getString("vc_id"));
                            reply.setHu_id(rep.getString("hu_id"));
                            reply.setHu_name(rep.getString("hu_name"));
                            reply.setVr_id(rep.getString("vr_id"));
                            reply.setVr_content(rep.getString("vr_content"));
                            reply.setVr_time(rep.getString("vr_time"));
                            reply.setBu_name(rep.getString("bu_name"));
                            replylists.add(reply);

                        }

                        comment.setReplylist(replylists);

                        lists.add(comment);

                    }

                    detailsView.QueryComment(lists,pagenows);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    /**
     * 评论
     * @param tag
     * @param vid 视频id
     * @param uid 评论用户id
     * @param comment 评论内容
     */
    public void AddComment(String tag, String vid, String uid, String comment){

        model.AddComment(tag, vid, uid, comment, new IDataRequestListener() {
            @Override
            public void loadSuccess(Object object) {

                try {

                    String state = new JSONObject(object.toString()).getString("state");
                    if(state.equals("200")){

                        String commentjson = new JSONObject(object.toString()).getString("comment");

                        JSONObject jsonComment = new JSONObject(commentjson);
                        VideoCommentJson comment = new VideoCommentJson();
                        comment.setVc_id(jsonComment.getString("vc_id"));
                        comment.setU_id(jsonComment.getString("u_id"));
                        comment.setU_head(jsonComment.getString("u_head"));
                        comment.setU_name(jsonComment.getString("u_name"));
                        comment.setU_experience(jsonComment.getString("u_experience"));
                        comment.setVc_content(jsonComment.getString("vc_content"));
                        comment.setVc_time(jsonComment.getString("vc_time"));

                        //获取回复的评论
                        List<ReplyCommentJson> replylists = new ArrayList<ReplyCommentJson>();
                        JSONArray replys = new JSONArray(jsonComment.getString("replys"));
                        for(int j = 0 ;j<replys.length();j++) {

                            ReplyCommentJson reply = new ReplyCommentJson();
                            JSONObject rep = replys.getJSONObject(j);
                            reply.setVc_id(rep.getString("vc_id"));
                            reply.setHu_id(rep.getString("hu_id"));
                            reply.setHu_name(rep.getString("hu_name"));
                            reply.setVr_id(rep.getString("vr_id"));
                            reply.setVr_content(rep.getString("vr_content"));
                            reply.setVr_time(rep.getString("vr_time"));
                            reply.setBu_name(rep.getString("bu_name"));
                            replylists.add(reply);

                        }
                        comment.setReplylist(replylists);
                        detailsView.AddComment(comment);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    /**
     * 查询某条评论
     * @param tag
     * @param vcid
     */
    public void QueryOneComment(String tag,String vcid){

        model.QueryOneComment(tag, vcid, new IDataRequestListener() {
            @Override
            public void loadSuccess(Object object) {


                try {

                    String state = new JSONObject(object.toString()).getString("state");
                    if(state.equals("200")){

                        String commentjson = new JSONObject(object.toString()).getString("comment");

                        JSONObject jsonComment = new JSONObject(commentjson);
                        VideoCommentJson comment = new VideoCommentJson();
                        comment.setVc_id(jsonComment.getString("vc_id"));
                        comment.setU_id(jsonComment.getString("u_id"));
                        comment.setU_head(jsonComment.getString("u_head"));
                        comment.setU_name(jsonComment.getString("u_name"));
                        comment.setU_experience(jsonComment.getString("u_experience"));
                        comment.setVc_content(jsonComment.getString("vc_content"));
                        comment.setVc_time(jsonComment.getString("vc_time"));
                        comment.setV_id(jsonComment.getString("v_id"));

                        //获取回复的评论
                        List<ReplyCommentJson> replylists = new ArrayList<ReplyCommentJson>();
                        JSONArray replys = new JSONArray(jsonComment.getString("replys"));
                        for(int j = 0 ;j<replys.length();j++) {

                            ReplyCommentJson reply = new ReplyCommentJson();
                            JSONObject rep = replys.getJSONObject(j);
                            reply.setVc_id(rep.getString("vc_id"));
                            reply.setHu_id(rep.getString("hu_id"));
                            reply.setHu_name(rep.getString("hu_name"));
                            reply.setVr_id(rep.getString("vr_id"));
                            reply.setVr_content(rep.getString("vr_content"));
                            reply.setVr_time(rep.getString("vr_time"));
                            reply.setBu_name(rep.getString("bu_name"));
                            reply.setV_id(rep.getString("v_id"));
                            replylists.add(reply);

                        }
                        comment.setReplylist(replylists);
                        replyOneCommentView.QureyOneComment(comment);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    /**
     * 回复某条评论
     * @param tag
     * @param vcid
     * @param huid
     * @param buname
     * @param vrcontent
     * @param vid
     */
    public void ReplyComment(final String tag,final String vcid,final String huid,final String buname,final String vrcontent, final String vid){

        model.ReplyComment(tag, vcid, huid, buname, vrcontent, vid, new IDataRequestListener() {
            @Override
            public void loadSuccess(Object object) {
                try {

                    String state = new JSONObject(object.toString()).getString("state");
                    ReplyCommentJson json = new ReplyCommentJson();
                    json.setVc_id(vcid);
                    json.setV_id(vid);
                    json.setBu_name(buname);
                    json.setHu_name(SharePreferenceUtil.getInstance(context).getString(SharePreferenceUtil.u_name,""));
                    json.setVr_content(vrcontent);
                    replyOneCommentView.VReplyComment(json);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    /**
     * 点赞
     * @param tag
     * @param vid
     * @param uid
     */
    public void AddClickALike(String tag,String vid,String uid){

        model.ClickALike(tag, vid, uid, new IDataRequestListener() {
            @Override
            public void loadSuccess(Object object) {

                try {

                    String state = new JSONObject(object.toString()).getString("state");
                    detailsView.ClickALike(state);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    /**
     * 查询是否点赞
     * @param tag
     * @param vid
     * @param uid
     */
    public void QueryWheterLike(String tag,String vid,String uid){

        model.WheterLike(tag, vid, uid, new IDataRequestListener() {
            @Override
            public void loadSuccess(Object object) {
                try {

                    boolean value = new JSONObject(object.toString()).getBoolean("value");
                    detailsView.WheterLike(value);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 取消点赞
     * @param tag
     * @param vid
     * @param uid
     */
    public void CancelLike(String tag,String vid,String uid){

        model.CancelLike(tag, vid, uid, new IDataRequestListener() {
            @Override
            public void loadSuccess(Object object) {
                try {

                    String state = new JSONObject(object.toString()).getString("state");
                    detailsView.VCancelLike(state);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 查询点赞列表
     * @param tag
     * @param vid
     * @param pagenow
     */
    public void SelectLike(String tag,String vid,String pagenow){

        model.SelectLike(tag, vid, pagenow, new IDataRequestListener() {
            @Override
            public void loadSuccess(Object object) {

                try {
                    String pagenow = new JSONObject(object.toString()).getString("sum");
                    String likes = new JSONObject(object.toString()).getString("likes");

                    JSONArray array = new JSONArray(likes);
                    List<VideoLikeJson> lists = new ArrayList<VideoLikeJson>();
                    for (int i = 0;i<array.length();i++){

                        Gson gson = new Gson();
                        JSONObject like = array.getJSONObject(i);
                        VideoLikeJson json = gson.fromJson(like.toString(),VideoLikeJson.class);
                        lists.add(json);
                    }

                    detailsView.VSelectLike(lists,pagenow);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    //查询是否关注某个用户了
    public void SelectFollows(String tag,String guid,String buid){

        model.SelectFollow(tag, guid, buid, new IDataRequestListener() {
            @Override
            public void loadSuccess(Object object) {

                try {

                    String state = new JSONObject(object.toString()).getString("state");
                    detailsView.VSelectFollow(state);

                } catch (JSONException e) {

                    e.printStackTrace();

                }

            }
        });
    }

    /**
     * 添加关注
     * @param tag
     * @param guid
     * @param buid
     */
    public void AddFollows(String tag,String guid,String buid){
        model.AddUserFollow(tag, guid, buid, new IDataRequestListener() {
            @Override
            public void loadSuccess(Object object) {
                try {

                    String state = new JSONObject(object.toString()).getString("state");
                    detailsView.VAddFollow(state);

                } catch (JSONException e) {

                    e.printStackTrace();

                }
            }
        });
    }

}
