package com.zmx.youguibao.mvp.presenter;


import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.zmx.youguibao.mvp.bean.FollowUserPojo;
import com.zmx.youguibao.mvp.bean.PersonalCenterPojo;
import com.zmx.youguibao.mvp.bean.ReplyCommentJson;
import com.zmx.youguibao.mvp.bean.UserJson;
import com.zmx.youguibao.mvp.bean.VideoCommentJson;
import com.zmx.youguibao.mvp.bean.VideoLikeJson;
import com.zmx.youguibao.mvp.bean.VideoListJson;
import com.zmx.youguibao.mvp.model.IDataRequestListener;
import com.zmx.youguibao.mvp.model.UserModel;
import com.zmx.youguibao.mvp.view.LoginView;
import com.zmx.youguibao.mvp.view.MessageFollowsView;
import com.zmx.youguibao.mvp.view.MessageView;
import com.zmx.youguibao.mvp.view.MessageZanView;
import com.zmx.youguibao.mvp.view.PersonalCenterView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/9/18 0018 下午 2:08
 *功能模块：处理用户的
 *
 */
public class UserPresenter{

    UserModel model;
    LoginView login;
    PersonalCenterView pcv;
    MessageView mv;
    MessageZanView mzv;
    MessageFollowsView mfv;

    public UserPresenter(Context context, LoginView login){

        this.login = login;
        model = new UserModel();

    }

    public UserPresenter(Context context,PersonalCenterView pcv){
        this.pcv = pcv;
        model = new UserModel();
    }


    public UserPresenter(Context context, MessageView mv){
        this.mv = mv;
        model = new UserModel();
    }

    public UserPresenter(Context context, MessageZanView mzv){
        this.mzv = mzv;
        model = new UserModel();
    }

    public UserPresenter(Context context, MessageFollowsView mfv){
        this.mfv = mfv;
        model = new UserModel();
    }

    //登录
    public void Login(String tag,String name,String pwd){

        model.Login(tag, name, pwd, new IDataRequestListener() {
            @Override
            public void loadSuccess(Object object) {

                try {
                    String state = new JSONObject(object.toString()).getString("state");

                    if(state.equals("200")){

                        String u = new JSONObject(object.toString()).getString("user");

                        Gson gson = new Gson();
                        UserJson user = gson.fromJson(u,UserJson.class);
                        login.Login(user);

                    }else {

                        login.ErrorLogin("手机号或者密码错误！");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    //查询某个用户的资料
    public void SelectUserMessage(String tag,String uid){

        model.SelectUserMessage(tag, uid, new IDataRequestListener() {
            @Override
            public void loadSuccess(Object object) {

                try {

                    String state = new JSONObject(object.toString()).getString("state");
                    if(state.equals("200")){

                        String user = new JSONObject(object.toString()).getString("user");
                        JSONObject json = new JSONObject(user);
                        PersonalCenterPojo pcp = new PersonalCenterPojo();
                        pcp.setU_name(json.getString("u_name"));
                        pcp.setU_id(json.getString("u_id"));
                        pcp.setU_experience(json.getString("u_experience"));
                        pcp.setU_headurl(json.getString("u_headurl"));
                        pcp.setU_sex(json.getString("u_sex"));
                        pcp.setU_desc(json.getString("u_desc"));
                        pcp.setFans(json.getString("fans"));
                        pcp.setFollows(json.getString("follows"));
                        pcv.SelectUserMessage(pcp);

                        Log.e("粉丝",user+"ss");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    //查询某个用户的视频
    public void SelectUserVideos(String tag, final String pagenow, String uid){

        model.SelectUserVideos(tag, pagenow, uid, new IDataRequestListener() {
            @Override
            public void loadSuccess(Object object) {

                try {

                    String json = new JSONObject(object.toString()).getString("videos");
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

                    pcv.SelectUserVideos(Integer.parseInt(pagenows),lists);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    //查询某个用户所有评论消息
    public void SelectMessageComment(String tag, final String pagenow, String vuid){

        model.QueryMessageComment(tag, pagenow, vuid, new IDataRequestListener() {
            @Override
            public void loadSuccess(Object object) {


                try {

                    String json = new JSONObject(object.toString()).getString("comments");
                    String pagenows = new JSONObject(object.toString()).getString("sum");

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
                        comment.setV_videoimgurl(jsonObject.getString("v_videoimgurl"));
                        comment.setV_content(jsonObject.getString("v_content"));
                        lists.add(comment);

                    }

                    mv.SelectMessageComment(lists,pagenows);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    //查询未读关注消息
    public void SelectZanMessage(String tag, String pagenow, String vuid){

        model.QueryZanComment(tag, pagenow, vuid, new IDataRequestListener() {
            @Override
            public void loadSuccess(Object object) {


                try {

                    String json = new JSONObject(object.toString()).getString("zan");
                    String pagenows = new JSONObject(object.toString()).getString("sum");

                    List<VideoLikeJson> lists = new ArrayList<VideoLikeJson>();
                    JSONArray array = new JSONArray(json);
                    Gson gson = new Gson();

                    for(int i=0;i<array.length();i++) {
                        JSONObject like = array.getJSONObject(i);
                        VideoLikeJson vlj = gson.fromJson(like.toString(),VideoLikeJson.class);
                        lists.add(vlj);
                    }
                    mzv.SelectZanComment(lists,pagenows);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    //查询未读关注消息
    public void SelectFollowsMessage(String tag, String pagenow, String buid){

        model.QueryFollowComment(tag, pagenow, buid, new IDataRequestListener() {
            @Override
            public void loadSuccess(Object object) {

                try {

                    String json = new JSONObject(object.toString()).getString("follows");
                    String pagenows = new JSONObject(object.toString()).getString("sum");

                    List<FollowUserPojo> lists = new ArrayList<>();
                    JSONArray array = new JSONArray(json);
                    Gson gson = new Gson();

                    for(int i=0;i<array.length();i++) {

                        JSONObject like = array.getJSONObject(i);
                        FollowUserPojo fup = gson.fromJson(like.toString(),FollowUserPojo.class);
                        lists.add(fup);

                    }

                   mfv.SelectMessageComment(lists,pagenows);

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
                    pcv.VSelectFollow(state);

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
                    pcv.VAddFollow(state);

                } catch (JSONException e) {

                    e.printStackTrace();

                }
            }
        });
    }

}
