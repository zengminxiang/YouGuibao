package com.zmx.youguibao.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zmx.youguibao.R;
import com.zmx.youguibao.mvp.bean.ReplyCommentJson;
import com.zmx.youguibao.mvp.bean.VideoCommentJson;

import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/9/6 0006 上午 9:54
 *功能模块：回复评论的列表
 */
public class ReplyCommentAdapter extends BaseAdapter{

    private Context context;
    private List<ReplyCommentJson> lists;

    public ReplyCommentAdapter(Context context,List<ReplyCommentJson> lists){

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

            v = LayoutInflater.from(context).inflate(R.layout.reply_comment_item,null);
            holder = new ViewHolder();
            holder.content = (TextView) v.findViewById(R.id.content);
            v.setTag(holder);

        }else{
            holder = (ViewHolder) v.getTag();
        }


        String uid = lists.get(position).getHu_id();
        String name = lists.get(position).getHu_name();//回复评论的用户
        String bname = lists.get(position).getBu_name();//被回复的用户
        String comment = lists.get(position).getVr_content();//回复的内容

        Log.e("数据","uid "+uid+"name "+name+"bname "+bname+"comment "+comment);


        holder.content.setText(UserNameOnclik(holder.content,name,uid,bname,comment));

        return v;
    }

    public class ViewHolder{
        TextView content;
    }

    public SpannableStringBuilder UserNameOnclik(TextView comment_view, String name, String uid, String bname, String comments){

        StringBuilder actionText = new StringBuilder();
        actionText
                .append("<a style=\"text-decoration:none;\" href='"+uid+"'>"
                        + name + " </a>");

        if(!bname.equals("")){

            actionText
                    .append(" 回复 "+bname);

        }

        actionText.append(" : "+comments);
        comment_view.setText(Html.fromHtml(actionText.toString()));
        comment_view.setMovementMethod(LinkMovementMethod
                .getInstance());
        CharSequence text = comment_view.getText();
        int ends = text.length();
        Spannable spannable = (Spannable) comment_view.getText();
        URLSpan[] urlspan = spannable.getSpans(0, ends, URLSpan.class);
        SpannableStringBuilder stylesBuilder = new SpannableStringBuilder(text);
        stylesBuilder.clearSpans();
        for (URLSpan url : urlspan) {
            TextViewURLSpan myURLSpan = new TextViewURLSpan(url.getURL(),uid);
            stylesBuilder.setSpan(myURLSpan, spannable.getSpanStart(url),
                    spannable.getSpanEnd(url), spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return stylesBuilder;
    }


    /**
     * 对加色的字符串监听
     */
    private class TextViewURLSpan extends ClickableSpan {
        private String clickString;
        private String name;

        public TextViewURLSpan(String clickString,String name) {
            this.clickString = clickString;
            this.name = name;

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(context.getResources().getColor(R.color.text_color));
            ds.setUnderlineText(false); //去掉下划线
        }

        @Override
        public void onClick(View widget) {
            if (clickString.equals(name)) {
                Toast.makeText(context, clickString, Toast.LENGTH_LONG)
                        .show();
            }
        }
    }


}
