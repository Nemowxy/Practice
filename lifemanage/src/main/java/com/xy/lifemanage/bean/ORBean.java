package com.xy.lifemanage.bean;


import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by nemo on 2016/5/10 0010.
 */
public class ORBean extends BmobObject{
    private BmobFile or_image;
    private String or_name;
    private List<String> or_member;
    private Map<String,Integer> lables;
    private Integer or_code;
    private List<String> or_project;
    private List<String> or_task;
    private String detail;
    private String chatId;

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Map<String, Integer> getLables() {
        return lables;
    }

    public void setLables(Map<String, Integer> lables) {
        this.lables = lables;
    }

    public Integer getOr_code() {
        return or_code;
    }

    public void setOr_code(Integer or_code) {
        this.or_code = or_code;
    }

    public List<String> getOr_member() {
        return or_member;
    }

    public void setOr_member(List<String> or_member) {
        this.or_member = or_member;
    }

    public List<String> getOr_project() {
        return or_project;
    }

    public void setOr_project(List<String> or_project) {
        this.or_project = or_project;
    }

    public List<String> getOr_task() {
        return or_task;
    }

    public void setOr_task(List<String> or_task) {
        this.or_task = or_task;
    }

    public BmobFile getOr_image() {
        return or_image;
    }

    public void setOr_image(BmobFile or_image) {
        this.or_image = or_image;
    }

    public String getOr_name() {
        return or_name;
    }

    public void setOr_name(String or_name) {
        this.or_name = or_name;
    }

    @Override
    public String toString() {
        return "ORBean{" +
                "objectId='" + this.getObjectId() + '\'' +
                "detail='" + detail + '\'' +
                ", or_image=" + or_image +
                ", or_name='" + or_name + '\'' +
                ", or_member=" + or_member +
                ", lables=" + lables +
                ", or_code=" + or_code +
                ", or_project=" + or_project +
                ", or_task=" + or_task +
                '}';
    }
}
