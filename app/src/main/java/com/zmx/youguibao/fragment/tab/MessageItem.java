package com.zmx.youguibao.fragment.tab;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jauker.widget.BadgeView;
import com.zmx.youguibao.MyApplication;
import com.zmx.youguibao.R;
import com.zmx.youguibao.mvp.bean.MessageCountPojo;

import java.util.List;

import greenDao.MessageCountPojoDao;

/**
 *作者：胖胖祥
 *时间：2016/10/14 0014 下午 3:42
 *功能模块：消息滑动栏的标题
 */
public class MessageItem {


    private BadgeView badgeView;//消息提示红点

    public View getTabView(int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.tab_item, null);
        mTvTitle = (TextView) view.findViewById(R.id.textView);

        badgeView = new BadgeView(context);
        badgeView.setTargetView(mTvTitle);
        //设置相对位置
        badgeView.setBadgeMargin(0, 5, 10, 0);

            if (i == 0) {

                mTvTitle.setText("评论");
                badgeView.setBadgeCount(SelectMessageCount(2));

            } else if (i == 1) {

                mTvTitle.setText("点赞");
                badgeView.setBadgeCount(SelectMessageCount(3));

            }else if(i == 2){

                mTvTitle.setText("关注");
                badgeView.setBadgeCount(SelectMessageCount(4));

            }

        return view;
    }

    public MessageItem(Context context) {
        this.context = context;
    }

    public void setTabTitle(CharSequence title) {
        mTvTitle.setText(title);
    }

    private Context context;

    TextView mTvTitle;

    /**
     * 查询本地缓存了多少条未读消息
     * @param type  消息的类型
     */
    public int SelectMessageCount(int type){

        MessageCountPojo count = null;
        List<MessageCountPojo> lmcps = MyApplication.getInstance().getDaoSession().getMessageCountPojoDao().queryBuilder()
                .where(MessageCountPojoDao.Properties.Type.eq(type))
                .orderAsc(MessageCountPojoDao.Properties.Type)
                .list();

        for(MessageCountPojo m:lmcps){

            Log.e("未读消息","： "+m.getCount());
            count = m;

        }

        return count.getCount();

    }

}
