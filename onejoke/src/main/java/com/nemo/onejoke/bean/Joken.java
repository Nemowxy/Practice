package com.nemo.onejoke.bean;

/**
 * Created by nemo on 2016/7/24 0024.
 */
public class Joken {
    private String title;
    private String time;
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
