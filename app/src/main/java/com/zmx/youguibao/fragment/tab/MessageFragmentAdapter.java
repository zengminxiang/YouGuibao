package com.zmx.youguibao.fragment.tab;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zmx.youguibao.fragment.CommentMessageFragment;
import com.zmx.youguibao.fragment.FollowMessageFragment;
import com.zmx.youguibao.fragment.ZanMessageFragment;

/**
 *作者：胖胖祥
 *时间：2016/10/14 0014 下午 3:45
 *功能模块：
 */
public class MessageFragmentAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    private CommentMessageFragment cmf;
    private ZanMessageFragment zmf;
    private FollowMessageFragment fmf;

    public MessageFragmentAdapter(FragmentManager fm) {
        super(fm);

        cmf = new CommentMessageFragment();
        zmf = new ZanMessageFragment();
        fmf = new FollowMessageFragment();

    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){

            return cmf;

        }else if(position == 1){

            return zmf;

        }else{

            return fmf;

        }

    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return null;

    }

}
