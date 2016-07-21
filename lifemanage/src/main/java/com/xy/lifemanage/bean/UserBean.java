package com.xy.lifemanage.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by nemo on 2016/5/8 0008.
 */
public class UserBean extends BmobUser{
    private BmobFile img;
    private Map<String,Integer> lables;
    private String messageId;
    private List<String> organizeId;
    private List<String> ringId;
    private List<String> tasks;

    public List<String> getTasks() {
        return tasks;
    }

    public void setTasks(List<String> tasks) {
        this.tasks = tasks;
    }

    public List<String> getRingId() {
        return ringId;
    }

    public void setRingId(List<String> ringId) {
        this.ringId = ringId;
    }

    public List<String> getOrId() {
        return organizeId;
    }

    public void setOrId(List<String> orId) {
        this.organizeId = orId;
    }

    public Map<String, Integer> getLables() {
        return lables;
    }

    public void setLables(Map<String, Integer> lables) {
        this.lables = lables;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public BmobFile getImg() {
        return img;
    }

    public void setImg(BmobFile img) {
        this.img = img;
    }

    public List<String> getOrganizeId() {
        return organizeId;
    }

    public void setOrganizeId(List<String> organizeId) {
        this.organizeId = organizeId;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "username=" + getUsername() +
                ", lables=" + lables +
                ", organizeId=" + organizeId +
                ", ringId=" + ringId +
                '}';
    }
}
