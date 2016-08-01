package com.nemo.joke.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nemo on 2016/7/24 0024.
 */
public class ChannelDB {

    private static List<Channel> selectedChannel=new ArrayList<Channel>();
    static{
        selectedChannel.add(new Channel("Android","1"));
        selectedChannel.add(new Channel("笑话","2"));
        selectedChannel.add(new Channel("Gif","3"));
        selectedChannel.add(new Channel("图片","4"));
        selectedChannel.add(new Channel("视频","5"));
        selectedChannel.add(new Channel("其他","6"));
    }
    public static  List<Channel> getSelectedChannel(){
        return selectedChannel;
    }
}
