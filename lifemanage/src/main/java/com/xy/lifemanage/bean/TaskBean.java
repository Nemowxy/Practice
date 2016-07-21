package com.xy.lifemanage.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by nemo on 2016/5/17 0017.
 */
public class TaskBean extends BmobObject{
    private String title;
    private String detail;
    private List<BmobFile> images;
    private String date;
    private String sendName;
    private String receiverName;
    private Boolean hasFujian;
    private Boolean hasFinish;

    public List<BmobFile> getImages() {
        return images;
    }

    public void setImages(List<BmobFile> images) {
        this.images = images;
    }

    public Boolean getHasFinish() {
        return hasFinish;
    }

    public void setHasFinish(Boolean hasFinish) {
        this.hasFinish = hasFinish;
    }

    public Boolean getHasFujian() {
        return hasFujian;
    }

    public void setHasFujian(Boolean hasFujian) {
        this.hasFujian = hasFujian;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<BmobFile> getImage() {
        return images;
    }

    public void setImage(List<BmobFile> image) {
        this.images = image;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
