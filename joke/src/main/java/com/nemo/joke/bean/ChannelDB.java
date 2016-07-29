package com.nemo.joke.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nemo on 2016/7/24 0024.
 */
public class ChannelDB {

    private static List<Channel> selectedChannel=new ArrayList<Channel>();
    static{
        selectedChannel.add(new Channel("Android"));
        selectedChannel.add(new Channel("笑话"));
        selectedChannel.add(new Channel("Gif"));
        selectedChannel.add(new Channel("图片"));
        selectedChannel.add(new Channel("视频"));
        selectedChannel.add(new Channel("其他"));
    }
    public static  List<Channel> getSelectedChannel(){
        return selectedChannel;
    }
}
