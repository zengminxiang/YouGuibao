package com.zmx.youguibao.mvp.presenter;


import android.content.Context;

import com.google.gson.Gson;
import com.zmx.youguibao.mvp.bean.UserJson;
import com.zmx.youguibao.mvp.model.IDataRequestListener;
import com.zmx.youguibao.mvp.model.UserModel;
import com.zmx.youguibao.mvp.view.LoginView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *作者：胖胖祥
 *时间：2016/9/18 0018 下午 2:08
 *功能模块：处理用户的
 *
 */
public class UserPresenter{

    UserModel model;
    LoginView login;

    public UserPresenter(Context context, LoginView login){

        this.login = login;
        model = new UserModel();

    }

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

}
