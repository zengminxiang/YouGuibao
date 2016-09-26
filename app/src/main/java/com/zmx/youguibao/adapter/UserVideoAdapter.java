package com.zmx.youguibao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zmx.youguibao.R;
import com.zmx.youguibao.mvp.bean.VideoListJson;
import com.zmx.youguibao.utils.UrlConfig;
import com.zmx.youguibao.utils.view.ImageLoadOptions;
import com.zmx.youguibao.utils.view.ImageViewUtil;

import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/9/26 0026 下午 4:23
 *功能模块：用户资料中的视频adapter
 */
public class UserVideoAdapter extends BaseAdapter{

    private Context context;
    private List<VideoListJson> lists;

    public UserVideoAdapter(Context context,List<VideoListJson> lists){

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
    public View getView(int position, View itemView, ViewGroup parent) {

        ViewHolder holder=null;

        if(itemView == null){

            itemView = LayoutInflater.from(context).inflate(R.layout.user_video_item,null);
            holder = new ViewHolder();
            holder.context = (TextView) itemView.findViewById(R.id.user_context);
            holder.video_image = (ImageView) itemView.findViewById(R.id.video_image);
            holder.like_tv = (TextView) itemView.findViewById(R.id.like_tv);
            holder.comment_tv = (TextView) itemView.findViewById(R.id.comment_tv);
            itemView.setTag(holder);

        }else {

            holder = (ViewHolder) itemView.getTag();

        }

        holder.context.setText(lists.get(position).getV_content());
        holder.comment_tv.setText(lists.get(position).getCount_comment());
        holder.like_tv.setText(lists.get(position).getCount_like());

        //处理刷新数据后闪屏问题
        holder.video_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if(!lists.get(position).getV_videoimgurl().equals(holder.video_image.getTag())) {

            holder.video_image.setTag(lists.get(position).getV_videoimgurl());
            ImageLoader.getInstance().displayImage(lists.get(position).getV_videoimgurl(), holder.video_image,
                    ImageLoadOptions.getOptions());

        }

        return itemView;
    }

    public class ViewHolder{

        TextView context,like_tv,comment_tv;
        ImageView video_image;

    }

}
