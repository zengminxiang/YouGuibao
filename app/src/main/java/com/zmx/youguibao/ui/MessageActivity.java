package com.zmx.youguibao.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zmx.youguibao.BaseActivity;
import com.zmx.youguibao.R;
import com.zmx.youguibao.fragment.CommentMessageFragment;
import com.zmx.youguibao.fragment.ZanMessageFragment;

import java.util.ArrayList;
import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/10/10 0010 下午 3:26
 *功能模块：用户消息（评论通知和点赞通知）
 */
public class MessageActivity extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private List<Fragment> list_fragment;
    private CommentMessageFragment cmf;
    private ZanMessageFragment zmf;
    private String[] mTitles = new String[]{"评论","点赞"};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    protected void initViews() {

        setTitle("消息");
        tabLayout = (TabLayout) findViewById(R.id.message_tabs);
        mViewPager = (ViewPager) findViewById(R.id.message_viewpager);
        cmf = new CommentMessageFragment();
        zmf = new ZanMessageFragment();
        list_fragment = new ArrayList<>();
        list_fragment.add(cmf);
        list_fragment.add(zmf);

          /*viewPager通过适配器与fragment关联*/
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return list_fragment.get(position);
            }

            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }
        });
        //TabLayout和ViewPager的关联
        tabLayout.setupWithViewPager(mViewPager);

    }
}
