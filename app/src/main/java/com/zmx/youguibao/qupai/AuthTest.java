package com.zmx.youguibao.qupai;


import android.content.Context;
import android.util.Log;

import com.duanqu.qupai.auth.AuthService;
import com.duanqu.qupai.auth.QupaiAuthListener;
import com.zmx.youguibao.MyApplication;
import com.zmx.youguibao.SharePreferenceUtil;
import com.zmx.youguibao.qupai.bean.Contant;

/**
 *作者：胖胖祥
 *时间：2016/8/18 0018 上午 10:30
 *功能模块：授权
 */
public class AuthTest {

    private static AuthTest instance;

    public static AuthTest getInstance() {
        if (instance == null) {
            instance = new AuthTest();
        }
        return instance;
    }

    private static final String AUTHTAG = "QupaiAuth";

    /**
     * 鉴权 建议只调用一次,在demo里面为了测试调用了多次 得到accessToken，通常一个用户对应一个token
     * @param context
     * @param appKey    appkey
     * @param appsecret appsecret
     * @param space     space
     */
    public void initAuth(final Context context , String appKey, String appsecret, String space){
        Log.e("Live","accessToken" + Contant.accessToken);
        Log.e("Live","space" + Contant.space);

        AuthService service = AuthService.getInstance();
        service.setQupaiAuthListener(new QupaiAuthListener() {
            @Override
            public void onAuthError(int errorCode, String message) {
                Log.e(AUTHTAG, "ErrorCode" + errorCode + "message" + message);
            }

            @Override
            public void onAuthComplte(int responseCode, String responseMessage) {

                Log.e(AUTHTAG, "onAuthComplte" + responseCode + "message" + responseMessage);
                Contant.accessToken = responseMessage;
                SharePreferenceUtil.getInstance(context).saveKeyObjValue(SharePreferenceUtil.accessToken,responseMessage);
                MyApplication.setAccessToken(responseMessage);

            }
        });
        service.startAuth(context,appKey, appsecret, space);
    }

}
