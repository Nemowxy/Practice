package com.nemo.samplenews.bean;

import java.util.List;

/**
 * Created by nemo on 2016/7/25 0025.
 */
public class TitleBean {
    private String title;
    private String channelId;
    private String link;
    private String pubDate;
    private String nid;
    private List<String> imageurls;
    private String desc;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getImageurls() {
        return imageurls;
    }

    public void setImageurls(List<String> imageurls) {
        this.imageurls = imageurls;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "TitleBean{" +
                "channelId='" + channelId + '\'' +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", nid='" + nid + '\'' +
                ", imageurls=" + imageurls +
                ", desc='" + desc + '\'' +
                '}';
    }
}
