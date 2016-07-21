package com.xy.lifemanage.bean;

/**
 * Created by nemo on 2016/5/10 0010.
 */
public class Bean {
    private String title;
    private String detail;
    private String date;
    private boolean hasFujian;
    private boolean hasFinish;

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

    public boolean isHasFinish() {
        return hasFinish;
    }

    public void setHasFinish(boolean hasFinish) {
        this.hasFinish = hasFinish;
    }

    public boolean isHasFujian() {
        return hasFujian;
    }

    public void setHasFujian(boolean hasFujian) {
        this.hasFujian = hasFujian;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
