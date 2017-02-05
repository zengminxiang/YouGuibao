package com.zmx.youguibao.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zmx.youguibao.R;
import com.zmx.youguibao.emoticon.utils.SpanStringUtils;
import com.zmx.youguibao.mvp.bean.ChatMessagePojo;
import com.zmx.youguibao.ui.ChatActivity;
import com.zmx.youguibao.utils.UrlConfig;
import com.zmx.youguibao.utils.Utils;
import com.zmx.youguibao.utils.view.ImageLoadOptions;
import com.zmx.youguibao.utils.view.ImageViewUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private ListView mListView;

//    private int update_position = 0;//要修改的那个item

    public ChatListAdapter(Context context, List<ChatMessagePojo>lists){

        this.context = context;
        this.lists = lists;

    }

    /**
     * 设置listview对象
     *
     * @param lisv
     */
    public void setListView(ListView lisv)
    {
        this.mListView = lisv;
    }


    /**
     * update listview 单条数据
     *
     * @param
     */
    public void updateItemData(ChatMessagePojo chat)
    {
        Message msg = Message.obtain();
        int ids = -1;
        // 进行数据对比获取对应数据在list中的位置
        for (int i = 0; i < lists.size(); i++)
        {
            if(lists.get(i).getUser_id().equals(chat.getUser_id())){

                ids = i;

            }
        }

        if(ids == -1){

            lists.add(chat);
            notifyDataSetChanged();


        }else {

            msg.arg1 = ids;
            // 更新mDataList对应位置的数据
            lists.set(ids, chat);
            // handle刷新界面
            han.sendMessage(msg);

        }


    }

    private Handler han = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            updateItem(msg.arg1);
        };
    };

    /**
     * 刷新指定item
     *
     * @param index item在listview中的位置
     */
    private void updateItem(int index)
    {
        if (mListView == null)
        {
            return;
        }

        // 获取当前可以看到的item位置
        int visiblePosition = mListView.getFirstVisiblePosition();
        // 如添加headerview后 firstview就是hearderview
        // 所有索引+1 取第一个view
        // View view = listview.getChildAt(index - visiblePosition + 1);
        // 获取点击的view
        View view = mListView.getChildAt(index - visiblePosition);
        // 从view中取得holder
        ViewHolder holder = (ViewHolder) view.getTag();
        ChatMessagePojo chat = (ChatMessagePojo) getItem(index);
        holder.content.setText(SpanStringUtils.getEmotionContent(0x0001,
                context, holder.content, chat.getContent()));
        holder.time.setText(Utils.dateDiff(chat.getTime(),new Date(),"yyyy-MM-dd HH:mm:ss"));

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
    public View getView(final int position, View v, ViewGroup parent) {

        ViewHolder holder = null;

        if(v == null){

            holder = new ViewHolder();
            v = LayoutInflater.from(context).inflate(R.layout.caht_list_item,null);
            holder.content = (TextView) v.findViewById(R.id.content);
            holder.name = (TextView) v.findViewById(R.id.user_name);
            holder.time = (TextView) v.findViewById(R.id.time);
            holder.head = (ImageViewUtil) v.findViewById(R.id.user_head);
            v.setTag(holder);

        }else{

            holder = (ViewHolder) v.getTag();

        }

        holder.name.setText(lists.get(position).getUser_name());
        holder.content.setText(SpanStringUtils.getEmotionContent(0x0001,
                context, holder.content, lists.get(position).getContent()));
        holder.time.setText(Utils.dateDiff(lists.get(position).getTime(),new Date(),"yyyy-MM-dd HH:mm:ss"));

        ImageLoader.getInstance().displayImage(UrlConfig.HEAD+lists.get(position).getUser_head(), holder.head,
                ImageLoadOptions.getOptions());

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("user_name", lists.get(position).getUser_name());
                bundle.putString("user_id", lists.get(position).getUser_id());
                bundle.putString("user_head", lists.get(position).getUser_head());
                Intent intent = new Intent(context,ChatActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);

            }
        });


        return v;
    }

    private class ViewHolder{

        TextView name,content,time;
        ImageViewUtil head;

    }



}
