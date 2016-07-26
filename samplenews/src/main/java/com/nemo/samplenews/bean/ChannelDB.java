package com.nemo.samplenews.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nemo on 2016/7/24 0024.
 */
public class ChannelDB {

    private static List<Channel> selectedChannel=new ArrayList<Channel>();
    static{
        selectedChannel.add(new Channel("头条"));
        selectedChannel.add(new Channel("娱乐"));
        selectedChannel.add(new Channel("体育"));
        selectedChannel.add(new Channel("财经"));
        selectedChannel.add(new Channel("热点"));
        selectedChannel.add(new Channel("科技"));
        selectedChannel.add(new Channel("图片"));
        selectedChannel.add(new Channel("汽车"));
        selectedChannel.add(new Channel("时尚"));
    }
    public static  List<Channel> getSelectedChannel(){
        return selectedChannel;
    }
}
