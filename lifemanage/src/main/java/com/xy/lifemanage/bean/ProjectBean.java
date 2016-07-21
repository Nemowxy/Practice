package com.xy.lifemanage.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by nemo on 2016/5/17 0017.
 */
public class ProjectBean extends BmobObject{
    private String title;
    private String detail;
    private List<BmobFile> images;
        private String date;
    private String author;
    private Boolean hasFujian;

    public Boolean getHasFujian() {
        return hasFujian;
    }

    public void setHasFujian(Boolean hasFujian) {
        this.hasFujian = hasFujian;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public List<BmobFile> getImages() {
        return images;
    }

    public void setImages(List<BmobFile> images) {
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
