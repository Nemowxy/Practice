package com.xy.lifemanage.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by nemo on 2016/5/17 0017.
 */
public class MessageBean extends BmobObject{
    //私信
    private List<String> messages;
    //任务
    private List<String> tasks;
    //邀请
    private List<String> invites;
    //申请
    private List<String> applys;

    public List<String> getApplys() {
        return applys;
    }

    public void setApplys(List<String> applys) {
        this.applys = applys;
    }

    public List<String> getInvites() {
        return invites;
    }

    public void setInvites(List<String> invites) {
        this.invites = invites;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public List<String> getTasks() {
        return tasks;
    }

    public void setTasks(List<String> tasks) {
        this.tasks = tasks;
    }
}
