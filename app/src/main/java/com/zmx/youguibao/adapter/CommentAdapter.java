package com.zmx.youguibao.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zmx.youguibao.R;
import com.zmx.youguibao.mvp.bean.ReplyCommentJson;
import com.zmx.youguibao.mvp.bean.VideoCommentJson;
import com.zmx.youguibao.ui.ReplyCommentActivity;
import com.zmx.youguibao.utils.UrlConfig;
import com.zmx.youguibao.utils.view.ImageLoadOptions;
import com.zmx.youguibao.utils.view.ImageViewUtil;

import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/8/31 0031 上午 10:25
 *功能模块：评论的适配器
 */
public class CommentAdapter extends BaseAdapter{

    private Context context;
    private List<VideoCommentJson> lists;

    public CommentAdapter(Context context,List<VideoCommentJson> lists){

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
    public View getView(final int position, View v, ViewGroup parent) {

        ViewHolder holder = null;
        if(v == null){

            holder = new ViewHolder();
            v = LayoutInflater.from(context).inflate(R.layout.video_comment_list,null);
            holder.name = (TextView) v.findViewById(R.id.video_comment_item_name);
            holder.comtext = (TextView) v.findViewById(R.id.comtext);
            holder.time = (TextView) v.findViewById(R.id.time);
            holder.head = (ImageViewUtil) v.findViewById(R.id.head);
            holder.comment_one = (TextView) v.findViewById(R.id.comment_one);
            holder.comment_two = (TextView) v.findViewById(R.id.comment_two);
            holder.comment_more = (TextView) v.findViewById(R.id.comment_more);
            holder.comment_button = (ImageView) v.findViewById(R.id.comment_button);
            v.setTag(holder);

        }else{

            holder = (ViewHolder) v.getTag();

        }

        holder.name.setText(lists.get(position).getU_name());
        holder.comtext.setText(lists.get(position).getVc_content());
        holder.time.setText(lists.get(position).getVc_time());

        //处理刷新数据后闪屏问题
        holder.head.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if(!lists.get(position).getU_head().equals(holder.head.getTag())) {

            holder.head.setTag(lists.get(position).getU_head());

            ImageLoader.getInstance().displayImage(UrlConfig.HEAD+lists.get(position).getU_head(), holder.head,
                    ImageLoadOptions.getOptions());
        }

        //点击回复评论
        holder.comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ReplyCommentActivity.class);
                intent.putExtra("vcid",lists.get(position).getVc_id());
                intent.putExtra("vid",lists.get(position).getV_id());
                context.startActivity(intent);

            }
        });

        //点击更多评论按钮
        holder.comment_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ReplyCommentActivity.class);
                intent.putExtra("vcid",lists.get(position).getVc_id());
                intent.putExtra("vid",lists.get(position).getV_id());
                context.startActivity(intent);

            }
        });
        //回复评论区
        holder.replys = lists.get(position).getReplylist();
        if(holder.replys.size()>0){

            if(holder.replys.size()>2){
                holder.comment_more.setVisibility(View.VISIBLE);
                holder.comment_more.setText("更多"+(holder.replys.size()-2)+"条回复");
            }

            for (int i = 0; i<holder.replys.size(); i++){

                String uid = holder.replys.get(i).getHu_id();
                String name = holder.replys.get(i).getHu_name();//回复评论的用户
                String bname = holder.replys.get(i).getBu_name();//被回复的用户
                String comment = holder.replys.get(i).getVr_content();//回复的内容

                if (i == 0){

                    holder.comment_one.setVisibility(View.VISIBLE);
                    holder.comment_one.setText(UserNameOnclik(holder.comment_one,name,uid,bname,comment));

                }

                if(i == 1){

                    holder.comment_two.setVisibility(View.VISIBLE);
                    holder.comment_two.setText(UserNameOnclik(holder.comment_two,name,uid,bname,comment));

                }

            }

        }else{

            holder.comment_one.setVisibility(View.GONE);
            holder.comment_two.setVisibility(View.GONE);
            holder.comment_more.setVisibility(View.GONE);

        }

        return v;
    }

    public class ViewHolder{
        TextView name,time,comtext,comment_one,comment_two,comment_more;
        ImageViewUtil head;
        ImageView comment_button;
        List<ReplyCommentJson> replys;

    }

    public SpannableStringBuilder UserNameOnclik(TextView comment_view,String name,String uid,String bname,String comments){

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
