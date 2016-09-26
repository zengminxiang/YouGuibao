package com.zmx.youguibao.mvp.presenter;


import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.zmx.youguibao.mvp.bean.PersonalCenterPojo;
import com.zmx.youguibao.mvp.bean.UserJson;
import com.zmx.youguibao.mvp.bean.VideoListJson;
import com.zmx.youguibao.mvp.model.IDataRequestListener;
import com.zmx.youguibao.mvp.model.UserModel;
import com.zmx.youguibao.mvp.view.LoginView;
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

    public UserPresenter(Context context, LoginView login){

        this.login = login;
        model = new UserModel();

    }

    public UserPresenter(Context context,PersonalCenterView pcv){
        this.pcv = pcv;
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

}
