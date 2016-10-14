package com.zmx.youguibao.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jauker.widget.BadgeView;
import com.zmx.youguibao.BaseActivity;
import com.zmx.youguibao.R;
import com.zmx.youguibao.fragment.CommentMessageFragment;
import com.zmx.youguibao.fragment.ZanMessageFragment;
import com.zmx.youguibao.fragment.tab.MessageFragmentAdapter;
import com.zmx.youguibao.fragment.tab.MessageItem;

import java.util.ArrayList;
import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/10/10 0010 下午 3:26
 *功能模块：用户消息（评论通知和点赞通知）
 */
public class MessageActivity extends BaseActivity {

//    private TabLayout tabLayout;
//    private ViewPager mViewPager;
//    private List<Fragment> list_fragment;
//    private CommentMessageFragment cmf;
//    private ZanMessageFragment zmf;
//    private String[] mTitles = new String[]{"评论","点赞"};

    private MessageFragmentAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private MessageItem[] items;
    private static final String POSITION = "position";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    protected void initViews() {

        setTitle("消息");
//        tabLayout = (TabLayout) findViewById(R.id.message_tabs);
//        mViewPager = (ViewPager) findViewById(R.id.message_viewpager);
//        cmf = new CommentMessageFragment();
//        zmf = new ZanMessageFragment();
//        list_fragment = new ArrayList<>();
//        list_fragment.add(cmf);
//        list_fragment.add(zmf);
//
//          /*viewPager通过适配器与fragment关联*/
//        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
//            @Override
//            public Fragment getItem(int position) {
//                return list_fragment.get(position);
//            }
//
//            @Override
//            public int getCount() {
//                return mTitles.length;
//            }
//
//            @Override
//            public CharSequence getPageTitle(int position) {
//                return mTitles[position];
//            }
//        });
//        //TabLayout和ViewPager的关联
//        tabLayout.setupWithViewPager(mViewPager);

        //左右滑动
        viewPager = (ViewPager) findViewById(R.id.message_viewpager);
        adapter = new MessageFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        //设置ViewPager预加载3个页面，解决第3个页面初始点击崩溃的Bug
        viewPager.setOffscreenPageLimit(3);
        tabLayout = (TabLayout) findViewById(R.id.message_tabs);
        tabLayout.setupWithViewPager(viewPager);
        items = new MessageItem[tabLayout.getTabCount()];

        for (int i = 0; i < tabLayout.getTabCount(); i++) {

            TabLayout.Tab tab = tabLayout.getTabAt(i);
            items[i] = new MessageItem(this);
            tab.setCustomView(items[i].getTabView(i));

        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //滑动end

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, viewPager.getCurrentItem());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        viewPager.setCurrentItem(savedInstanceState.getInt(POSITION));
    }

}
