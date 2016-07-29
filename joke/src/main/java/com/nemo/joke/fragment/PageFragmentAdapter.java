package com.nemo.joke.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by nemo on 2016/7/24 0024.
 */
public class PageFragmentAdapter extends FragmentPagerAdapter{
    private List<Fragment> fragmentList;
    private FragmentManager fm;
    public PageFragmentAdapter(FragmentManager fm,List<Fragment> fragmentList){
        super(fm);
        this.fragmentList=fragmentList;
        this.fm=fm;
    }
    @Override
    public Fragment getItem(int idx) {
        return fragmentList.get(idx%fragmentList.size());
    }
    @Override
    public int getCount() {
        return fragmentList.size();
    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;  //没有找到child要求重新加载
    }
}
