package com.zmx.youguibao.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
 * 作者：胖胖祥
 * 时间：2016/8/25 0025 下午 4:07
 * 功能模块：视频的适配器
 */
public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<VideoListJson> lists;

    public VideoAdapter(Context context, List<VideoListJson> lists) {
        mContext = context;
        this.lists = lists;
        mLayoutInflater = LayoutInflater.from(context);
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class NormalViewHolder extends RecyclerView.ViewHolder {
        TextView name,context,time,like_tv,comment_tv;
        ImageViewUtil head;
        ImageView video_image;

        public NormalViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.user_name);
            context = (TextView) itemView.findViewById(R.id.user_context);
            time = (TextView) itemView.findViewById(R.id.user_time);
            head = (ImageViewUtil) itemView.findViewById(R.id.user_head);
            video_image = (ImageView) itemView.findViewById(R.id.video_image);
            like_tv = (TextView) itemView.findViewById(R.id.like_tv);
            comment_tv = (TextView) itemView.findViewById(R.id.comment_tv);

        }
    }

    //在该方法中我们创建一个ViewHolder并返回，ViewHolder必须有一个带有View的构造函数，
    // 这个View就是我们Item的根布局，在这里我们使用自定义Item的布局；
    @Override
    public NormalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalViewHolder(mLayoutInflater.inflate(R.layout.video_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NormalViewHolder viewholder = (NormalViewHolder) holder;
        viewholder.name.setText(lists.get(position).getU_name());
        viewholder.context.setText(lists.get(position).getV_content());
        viewholder.time.setText(lists.get(position).getV_time());
        viewholder.comment_tv.setText(lists.get(position).getCount_comment());
        viewholder.like_tv.setText(lists.get(position).getCount_like());

        //处理刷新数据后闪屏问题
        viewholder.video_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if(!lists.get(position).getV_videoimgurl().equals(viewholder.video_image.getTag())) {

            viewholder.video_image.setTag(lists.get(position).getV_videoimgurl());
            ImageLoader.getInstance().displayImage(lists.get(position).getV_videoimgurl(), viewholder.video_image,
                    ImageLoadOptions.getOptions());

        }
        ImageLoader.getInstance().displayImage(UrlConfig.HEAD+lists.get(position).getU_headurl(), viewholder.head,
                ImageLoadOptions.getOptions());

    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return lists == null ? 0 : lists.size();
    }
}