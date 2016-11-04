package com.zmx.youguibao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zmx.youguibao.R;
import com.zmx.youguibao.mvp.bean.ChatMessagePojo;
import com.zmx.youguibao.utils.UrlConfig;
import com.zmx.youguibao.utils.view.ImageLoadOptions;
import com.zmx.youguibao.utils.view.ImageViewUtil;

import java.util.List;

/**
 *
 * 开发者：胖胖祥
 * 时间：2016/11/3 10:02
 * 功能模块：聊天列表适配器
 *
 */
public class ChatListAdapter extends BaseAdapter{

    private Context context;
    private List<ChatMessagePojo>lists;

    public ChatListAdapter(Context context, List<ChatMessagePojo>lists){

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
            v = LayoutInflater.from(context).inflate(R.layout.caht_list_item,null);
            holder.content = (TextView) v.findViewById(R.id.content);
            holder.name = (TextView) v.findViewById(R.id.user_name);
            holder.time = (TextView) v.findViewById(R.id.time);
            holder.head = (ImageViewUtil) v.findViewById(R.id.user_head);
            v.setTag(v);

        }else{

            holder = (ViewHolder) v.getTag();

        }

        holder.name.setText(lists.get(position).getUser_name());

        ImageLoader.getInstance().displayImage(UrlConfig.HEAD+lists.get(position).getUser_head(), holder.head,
                ImageLoadOptions.getOptions());


        return v;
    }

    private class ViewHolder{

        TextView name,content,time;
        ImageViewUtil head;

    }

}
