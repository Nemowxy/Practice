package com.nemo.joke.bean;

/**
 * Created by nemo on 2016/7/24 0024.
 */
public class Channel {
    private String name;
    private String channelId;
    public Channel(String s,String id){
        name = s;
        channelId = id;
    }

    public Channel(){

    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
