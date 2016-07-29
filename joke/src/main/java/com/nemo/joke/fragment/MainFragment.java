package com.nemo.joke.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import com.nemo.joke.R;
import com.nemo.joke.bean.Channel;
import com.nemo.joke.bean.ChannelDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nemo on 2016/7/25 0025.
 */
public class MainFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private RadioGroup rgChannel=null;
    private HorizontalScrollView hvChannel;
    private PageFragmentAdapter adapter=null;
    private List<Fragment> fragmentList=new ArrayList<Fragment>();
    private List<Channel> channelList;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView==null){
            rootView = inflater.inflate(R.layout.news_fragment,container,false);
            initView();
            channelList = ChannelDB.getSelectedChannel();
            initTab();
            initViewPager();
            rgChannel.check(0);
        }
        return rootView;
    }

    private void initView(){
        rgChannel=(RadioGroup)rootView.findViewById(R.id.rgChannel);
        viewPager=(ViewPager)rootView.findViewById(R.id.vpNewsList);
        hvChannel=(HorizontalScrollView)rootView.findViewById(R.id.hvChannel);
        rgChannel.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group,
                                                 int checkedId) {
                        viewPager.setCurrentItem(checkedId);
                    }
                });
        viewPager.setOnPageChangeListener(this);
    }
    private void initTab(){
        for(int i=0;i<channelList.size();i++){
            RadioButton rb=(RadioButton) LayoutInflater.from(getActivity().getApplicationContext()).
                    inflate(R.layout.table_rb, null);
            rb.setId(i);
            rb.setText(channelList.get(i).getName());
            RadioGroup.LayoutParams params=new
                    RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT);
            rgChannel.addView(rb,params);
        }
    }
    private void initViewPager(){
        for(int i=0;i<channelList.size();i++){
            NewsFragment frag=new NewsFragment();
            Bundle bundle=new Bundle();
            bundle.putString("name", channelList.get(i).getName());
            bundle.putString("channelId",channelList.get(i).getChannelId());
            frag.setArguments(bundle);	 //向Fragment传入数据
            fragmentList.add(frag);
        }
        adapter=new PageFragmentAdapter(getActivity().getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(adapter);
    }
    /**
     * 滑动ViewPager时调整ScroollView的位置以便显示按钮
     * @param idx
     */
    private void setTab(int idx){
        RadioButton rb=(RadioButton)rgChannel.getChildAt(idx);
        rb.setChecked(true);
        int left=rb.getLeft();
        int width=rb.getMeasuredWidth();
        DisplayMetrics metrics=new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth=metrics.widthPixels;
        int len=left+width/2-screenWidth/2;
        hvChannel.smoothScrollTo(len, 0);//滑动ScroollView
    }
    @Override
    public void onPageScrollStateChanged(int arg0) {
    }
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }
    @Override
    public void onPageSelected(int position) {
        // TODO Auto-generated method stub
        setTab(position);
    }

}
