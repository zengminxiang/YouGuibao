package com.zmx.youguibao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zmx.youguibao.R;
import com.zmx.youguibao.mvp.bean.FollowUserPojo;
import com.zmx.youguibao.utils.view.ImageViewUtil;

import java.util.List;

/**
 *
 * 开发者：胖胖祥
 * 时间：2016/11/2 17:17
 * 功能模块：未读关注信息
 *
 */
public class MessageFollowAdapter extends BaseAdapter{

    private Context context;
    private List<FollowUserPojo> lists;

    public MessageFollowAdapter(Context context,List<FollowUserPojo> lists){

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
            v = LayoutInflater.from(context).inflate(R.layout.follow_comment_item,null);
            holder.head = (ImageViewUtil) v.findViewById(R.id.user_head);
            holder.name = (TextView) v.findViewById(R.id.user_name);
            holder.time = (TextView) v.findViewById(R.id.time);
            v.setTag(holder);

        }else {

            holder = (ViewHolder) v.getTag();

        }

        holder.name.setText(lists.get(position).getGu_name());
        holder.time.setText(lists.get(position).getF_time());

        return v;
    }

    public class ViewHolder{

        TextView name,time;
        ImageViewUtil head;

    }

}
