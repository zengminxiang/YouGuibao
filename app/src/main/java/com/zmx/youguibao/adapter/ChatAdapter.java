package com.zmx.youguibao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zmx.youguibao.R;
import com.zmx.youguibao.SharePreferenceUtil;
import com.zmx.youguibao.mvp.bean.ChatPojo;
import com.zmx.youguibao.utils.UrlConfig;
import com.zmx.youguibao.utils.view.ImageLoadOptions;
import com.zmx.youguibao.utils.view.ImageViewUtil;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 *
 * 开发者：胖胖祥
 * 时间：2016/11/3 21:50
 * 功能模块：聊天界面的适配器
 *
 */
public class ChatAdapter extends BaseAdapter{

    private Context context;
    private List<ChatPojo> lists;
    private LayoutInflater inflater;

    public ChatAdapter(Context context,List<ChatPojo> lists){

        this.context = context;
        this.lists = lists;
        inflater = LayoutInflater.from(context);

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

    // 判断使用那个布局
    @Override
    public int getItemViewType(int position) {

        ChatPojo cp = lists.get(position);

        if (cp.getType() == "0") {
            return 0;
        }
        return 1;
    }

    // 返回多少个布局
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {

        ChatPojo chat = lists.get(position);
        ViewHolder holder = null;

        if (v == null) {

            holder = new ViewHolder();

            if (getItemViewType(position) == 0) {

                v = inflater.inflate(R.layout.chat_user, null);
                holder.time = (TextView) v.findViewById(R.id.u_time);
                holder.mMsg = (TextView) v.findViewById(R.id.u_msg);
                holder.head = (ImageViewUtil) v
                        .findViewById(R.id.u_head);
                v.setTag(holder);

            } else {

                v = inflater.inflate(R.layout.chat_login, null);
                holder.time = (TextView) v.findViewById(R.id.l_time);
                holder.mMsg = (TextView) v.findViewById(R.id.l_msg);
                holder.head = (ImageViewUtil) v
                        .findViewById(R.id.l_head);
                v.setTag(holder);
            }

        } else {

            holder = (ViewHolder) v.getTag();
        }

        // 设置数据
        // 设置时间格式
        holder.time.setText(chat.getTime());
        holder.mMsg.setText(chat.getMsg());
        if (getItemViewType(position) == 0) {

            ImageLoader.getInstance().displayImage(UrlConfig.HEAD+chat.getUser_head(), holder.head,
                    ImageLoadOptions.getOptions());

        } else {

            ImageLoader.getInstance().displayImage(UrlConfig.HEAD+ SharePreferenceUtil.getInstance(context).getString(SharePreferenceUtil.u_headurl,""), holder.head,
                    ImageLoadOptions.getOptions());

        }

        return v;
    }

    private final class ViewHolder {

        TextView time;
        TextView mMsg;
        ImageViewUtil head;

    }

}
