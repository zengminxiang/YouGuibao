package com.zmx.youguibao.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 *作者：胖胖祥
 *时间：2016/8/25 0025 上午 9:50
 *功能模块：api
 */
public class UrlConfig {


    public static Map<String,String> params;

    private static String URL = "http://192.168.99.221:8080/Guiyoubao/";
    public static String HEAD = URL+"head/";

    //处理视频说说模块
    private static String UPLOADVIDEO = URL+"ViedoServlet";

    private static String USER = URL + "UserServlet";

//    http://localhost:8080/Guiyoubao/UserServlet?tag=login&name=13751729147&pwd=123456
    public static String Login(String tag,String name,String pwd){

        params = new HashMap<>();
        params.put("tag",tag);
        params.put("name", name);
        params.put("pwd",pwd);
        return CombinationUrl(USER,params);

    }

//    http://localhost:8080/Guiyoubao/ViedoServlet?tag=publish&uid=1&addre=lallala&content=lalallalall&videoimgurl=baidu.com&videourl=fdafdasfdsa
    /**
     * 发表视频说说
     * @param uid  用户id
     * @param addre 发表的地址
     * @param content 内容
     * @param videoimgurl 视频第一帧图片路径
     * @param videourl 视频路径
     * @return
     */
    public static String UploadVideo(String tag,String uid,String addre,String content,String videoimgurl,String videourl){

        params = new HashMap<>();
        params.put("tag",tag);
        params.put("uid", uid);
        params.put("addre",addre);
        params.put("content",content);
        params.put("videoimgurl",videoimgurl);
        params.put("videourl",videourl);
        return CombinationUrl(UPLOADVIDEO,params);

    }

    /**
     * 分页查询视频
     * @param tag
     * @param pagenow
     * @return
     */
    //    http://localhost:8080/Guiyoubao/ViedoServlet?tag=queryAll&pagenow=1
    public static String QueryAllVideo(String tag,String pagenow){
        params = new HashMap<>();
        params.put("tag",tag);
        params.put("pagenow", pagenow);
        return CombinationUrl(UPLOADVIDEO,params);
    }


    /**
     * 评论
     * @param tag
     * @param vid 视频id
     * @param uid  用户id
     * @param comment 内容
     * @return
     */
//    http://localhost:8080/Guiyoubao/ViedoServlet?tag=AddComment&vid=4&uid=1&comment=%E5%A4%A7%E5%AE%B6%E5%A5%BD%E5%95%8A   添加评论

    public static String  AddVideoComment(String tag,String vid,String vuid,String uid,String comment){

        params = new HashMap<>();
        params.put("tag",tag);
        params.put("vid", vid);
        params.put("vuid", vuid);
        params.put("uid", uid);
        params.put("comment", comment);
        return CombinationUrl(UPLOADVIDEO,params);

    }

  //  http://localhost:8080/Guiyoubao/ViedoServlet?tag=ReplyComment&vcid=4&huid=2&vrcontent=%E5%A4%A7%E5%AE%B6%E5%A5%BD%E5%95%8A？  回复评论

    /**
     * 查询评论
     * @param tag
     * @param pagenow  页码
     * @param vid  视频id
     * @return
     * //    http://localhost:8080/Guiyoubao/ViedoServlet?tag=QueryComment&vid=5,pagenow=1  查询评论
     */
    public static String QueryComment(String tag,String pagenow,String vid){

        params = new HashMap<>();
        params.put("tag",tag);
        params.put("vid", vid);
        params.put("pagenow", pagenow);
        return CombinationUrl(UPLOADVIDEO,params);

    }

    /**
     *  查询某个用户的评论信息（消息用到 ）
     * @param tag  http://localhost:8080/Guiyoubao/ViedoServlet?tag=QueryCommentMessage&vuid=1&pagenow=1
     * @param pagenow
     * @param vuid
     * @return
     */
    public static String QueryMessageComment(String tag,String pagenow,String vuid){

        params = new HashMap<>();
        params.put("tag",tag);
        params.put("vuid", vuid);
        params.put("pagenow", pagenow);
        return CombinationUrl(UPLOADVIDEO,params);

    }

    /**
     * 查询未读点赞消息
     * @param tag
     * @param pagenow
     * @param vuid
     * @return
     */
    public static String QueryZanComment(String tag,String pagenow,String vuid){

        params = new HashMap<>();
        params.put("tag",tag);
        params.put("vuid", vuid);
        params.put("pagenow", pagenow);
        return CombinationUrl(UPLOADVIDEO,params);

    }

    /**
     * 查询某条评论
     * @param tag
     * @param vcid  评论id
     * @returnhttp://localhost:8080/Guiyoubao/ViedoServlet?tag=QueryOneComment&vcid=8
     */
    public static String QueryOneComment(String tag,String vcid){

        params = new HashMap<>();
        params.put("tag",tag);
        params.put("vcid", vcid);
        return CombinationUrl(UPLOADVIDEO,params);

    }

    /**
     * 回复评论
     * @param tag
     * @param vcid  主评论id
     * @param huid  回复用户的id
     * @param buname  被回复用户名
     * @param vrcontent 回复的内容
     * @param vid  回复的视频id
     * @return
     */
    public static String ReplyComment(String tag,String vcid,String huid,String buname,String vrcontent,String vid){
        params = new HashMap<>();
        params.put("tag",tag);
        params.put("vcid", vcid);
        params.put("huid", huid);
        params.put("buname", buname);
        params.put("vrcontent", vrcontent);
        params.put("vid", vid);
        return CombinationUrl(UPLOADVIDEO,params);
    }

    /**
     * 点赞视频
     * @param tag
     * @param vid 视频id
     * @param uid 点赞用户id
     * @return
     */
    public static String ClickLike(String tag,String vuid,String vid,String uid){

        params = new HashMap<>();
        params.put("tag",tag);
        params.put("vuid", vuid);
        params.put("vid", vid);
        params.put("uid", uid);
        return CombinationUrl(UPLOADVIDEO,params);

    }

//    http://localhost:8080/Guiyoubao/ViedoServlet?tag=WhetherLike&vid=4&uid=1
    /**
     * 查询是否点赞
     * @param tag
     * @param vid
     * @param uid
     * @return
     */
    public static String WheterLike(String tag,String vid,String uid){

        params = new HashMap<>();
        params.put("tag",tag);
        params.put("vid", vid);
        params.put("uid", uid);
        return CombinationUrl(UPLOADVIDEO,params);
    }

//    http://localhost:8080/Guiyoubao/ViedoServlet?tag=CancelLike&vid=7&uid=1
    /**
     *
     * @param tag 取消点赞
     * @param vid
     * @param uid
     * @return
     */
    public static String CancelLike(String tag,String vid,String uid){
        params = new HashMap<>();
        params.put("tag",tag);
        params.put("vid", vid);
        params.put("uid", uid);
        return CombinationUrl(UPLOADVIDEO,params);

    }

//    http://localhost:8080/Guiyoubao/ViedoServlet?tag=SelectLike&vid=9&pagenow=1
    /**
     * 分页查询点赞列表
     * @param tag
     * @param vid 视频id
     * @param pagenow 页码
     * @return
     */
    public static String SelectLike(String tag,String vid,String pagenow){

        params = new HashMap<>();
        params.put("tag",tag);
        params.put("vid", vid);
        params.put("pagenow", pagenow);
        return CombinationUrl(UPLOADVIDEO,params);
    }

//    http://localhost:8080/Guiyoubao/UserServlet?tag=nearby&ulLongitube=113.32738445906347
//    // &lrLongitube=113.33400497066981&ulLatitude=23.1603335974894&lrLatitude=23.149451414335466
    /**
     * 查询附近的用户
     * @param tag
     * @param uid
     * @param ulLongitube
     * @param lrLongitube
     * @param ulLatitude
     * @param lrLatitude
     * @return
     */
    public static String QueryNearbyUser(String tag,String uid,String ulLongitube,String lrLongitube,String ulLatitude,String lrLatitude){
        params = new HashMap<>();
        params.put("tag",tag);
        params.put("uid", uid);
        params.put("ulLongitube", ulLongitube);
        params.put("lrLongitube", lrLongitube);
        params.put("ulLatitude", ulLatitude);
        params.put("lrLatitude", lrLatitude);
        return CombinationUrl(USER,params);

    }

    /**
     * 查询是否关注了
     * @param tag
     * @param guid  关注的人
     * @param buid  被关注的人
     * @return
     */
    public static String QueryUserFollow(String tag,String guid,String buid){
        params = new HashMap<>();
        params.put("tag",tag);
        params.put("guid", guid);
        params.put("buid", buid);
        return CombinationUrl(USER,params);
    }

    /**
     * 添加关注
     * @param tag
     * @param guid
     * @param buid
     * @return
     */
    public static String AddFollow(String tag,String guid,String buid){

        params = new HashMap<>();
        params.put("tag",tag);
        params.put("guid", guid);
        params.put("buid", buid);
        return CombinationUrl(USER,params);

    }

//    http://localhost:8080/Guiyoubao/UserServlet?tag=UserMessage&uid=1
    /**
     * 查询某个用户的资料
     * @param tag
     * @param uid
     * @return
     */
    public static String SelectUserMessage(String tag,String uid){
        params = new HashMap<>();
        params.put("tag",tag);
        params.put("uid", uid);
        return CombinationUrl(USER,params);

    }

//    http://localhost:8080/Guiyoubao/UserServlet?tag=UserVideos&pagenow=1&uid=2

    /**
     * 查询某个用户发的视频
     * @param tag
     * @param pagenow
     * @param uid
     * @return
     */
    public static String SelectUserVideos(String tag,String pagenow,String uid){

        params = new HashMap<>();
        params.put("tag",tag);
        params.put("uid", uid);
        params.put("pagenow",pagenow);
        return CombinationUrl(USER,params);

    }

    //组合url
    public static String CombinationUrl(String part, Map<String, String> params){

        // 处理乱码
        StringBuilder sb = new StringBuilder(part);

        if (params != null) {

            sb.append('?');
            for (Map.Entry<String, String> entry : params.entrySet()) {

                try {
                    sb.append(entry.getKey()).append('=')
                            .append(URLEncoder.encode(entry.getValue(), "UTF-8"))
                            .append('&');
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            sb.deleteCharAt(sb.length() - 1);

        }

        return sb.toString();

    }


}
