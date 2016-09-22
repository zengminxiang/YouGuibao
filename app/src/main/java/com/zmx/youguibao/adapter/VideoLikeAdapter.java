package com.zmx.youguibao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zmx.youguibao.R;
import com.zmx.youguibao.mvp.bean.VideoLikeJson;
import com.zmx.youguibao.utils.UrlConfig;
import com.zmx.youguibao.utils.view.ImageLoadOptions;
import com.zmx.youguibao.utils.view.ImageViewUtil;

import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/9/13 0013 下午 4:32
 *功能模块：视频点赞模块
 */
public class VideoLikeAdapter extends BaseAdapter{

    private Context context;
    private List<VideoLikeJson> lists;

    public VideoLikeAdapter(Context context,List<VideoLikeJson> lists){
        this.context = context;
        this.lists = lists;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {

        ViewHolder holder = null;
        if(v == null){

            holder = new ViewHolder();
            v = LayoutInflater.from(context).inflate(R.layout.video_like_item,null);
            holder.head = (ImageViewUtil) v.findViewById(R.id.head);
            v.setTag(holder);
        }else{
            holder = (ViewHolder) v.getTag();
        }
        ImageLoader.getInstance().displayImage("http://b.hiphotos.baidu.com/image/h%3D200/sign=ff0999f6d4160924c325a51be406359b/86d6277f9e2f070861ccd4a0ed24b899a801f241.jpg", holder.head,
                ImageLoadOptions.getOptions());

        return v;
    }

    public class ViewHolder{

        ImageViewUtil head;

    }

}
