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
import com.zmx.youguibao.mvp.bean.VideoLikeJson;
import com.zmx.youguibao.utils.UrlConfig;
import com.zmx.youguibao.utils.view.ImageLoadOptions;
import com.zmx.youguibao.utils.view.ImageViewUtil;

import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/10/17 0017 上午 10:17
 *功能模块：点赞的消息通知
 */
public class ZanMessageAdapter extends BaseAdapter{

    private Context context;
    private List<VideoLikeJson> lists;

    public ZanMessageAdapter(Context context,List<VideoLikeJson> lists){

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
            v = LayoutInflater.from(context).inflate(R.layout.message_zan_item,null);
            holder.name = (TextView) v.findViewById(R.id.user_name);
            holder.time = (TextView) v.findViewById(R.id.createTime);
            holder.content = (TextView) v.findViewById(R.id.content);
            holder.head = (ImageViewUtil) v.findViewById(R.id.user_head);
            holder.video_img = (ImageView) v.findViewById(R.id.feeds_id);
            v.setTag(holder);

        }else {

            holder = (ViewHolder) v.getTag();

        }
        VideoLikeJson vlj = lists.get(position);

        holder.name.setText(vlj.getU_name());
        holder.time.setText(vlj.getVl_time());
        holder.content.setText(vlj.getV_content());

        //处理刷新数据后闪屏问题
        holder.video_img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if(!vlj.getV_videoimgurl().equals(holder.video_img.getTag())) {

            holder.video_img.setTag(vlj.getV_videoimgurl());
            ImageLoader.getInstance().displayImage(vlj.getV_videoimgurl(), holder.video_img,
                    ImageLoadOptions.getOptions());

        }
        ImageLoader.getInstance().displayImage(UrlConfig.HEAD+vlj.getU_headurl(), holder.head,
                ImageLoadOptions.getOptions());

        return v;
    }

    public class ViewHolder{

        TextView name,content,time;
        ImageViewUtil head;
        ImageView video_img;

    }



}
