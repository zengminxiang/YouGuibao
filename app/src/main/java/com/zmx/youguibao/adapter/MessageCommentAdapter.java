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
import com.zmx.youguibao.mvp.bean.VideoCommentJson;
import com.zmx.youguibao.utils.UrlConfig;
import com.zmx.youguibao.utils.view.ImageLoadOptions;
import com.zmx.youguibao.utils.view.ImageViewUtil;

import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/10/10 0010 下午 5:12
 *功能模块：信息里面的评论适配器
 */
public class MessageCommentAdapter extends BaseAdapter{

    private Context context;
    private List<VideoCommentJson> lists;

    public MessageCommentAdapter(Context context,List<VideoCommentJson> lists){

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

            v = LayoutInflater.from(context).inflate(R.layout.message_comment_item,null);
            holder = new ViewHolder();
            holder.name = (TextView) v.findViewById(R.id.user_name);
            holder.comment = (TextView) v.findViewById(R.id.comment);
            holder.time = (TextView) v.findViewById(R.id.createTime);
            holder.content = (TextView) v.findViewById(R.id.content);
            holder.head = (ImageViewUtil) v.findViewById(R.id.user_head);
            holder.video_img = (ImageView) v.findViewById(R.id.feeds_id);
            v.setTag(holder);

        }else {

            holder = (ViewHolder) v.getTag();

        }

        VideoCommentJson vcj = lists.get(position);

        holder.name.setText(vcj.getU_name());
        holder.comment.setText(vcj.getVc_content());
        holder.time.setText(vcj.getVc_time());
        holder.content.setText(vcj.getV_content());

        ImageLoader.getInstance().displayImage(UrlConfig.HEAD+vcj.getU_head(), holder.head,
                ImageLoadOptions.getOptions());
        ImageLoader.getInstance().displayImage(vcj.getV_videoimgurl(), holder.video_img,
                ImageLoadOptions.getOptions());
        return v;

    }

    public class ViewHolder{

        TextView name,comment,content,time;
        ImageViewUtil head;
        ImageView video_img;

    }


}
