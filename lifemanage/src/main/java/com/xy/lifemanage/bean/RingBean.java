package com.xy.lifemanage.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by nemo on 2016/5/12 0012.
 */
public class RingBean extends BmobObject{
    private String ringName;
    private List<BmobFile> ringList;
    private Integer zanCount;
    private String ringDetail;
    private Map<String,Integer> ringLables;
    private BmobFile ringImg;
    private Boolean isOrg;

    public Boolean getOrg() {
        return isOrg;
    }

    public void setOrg(Boolean org) {
        isOrg = org;
    }

    public BmobFile getRingImg() {
        return ringImg;
    }

    public void setRingImg(BmobFile ringImg) {
        this.ringImg = ringImg;
    }

    public Map<String, Integer> getRingLables() {
        return ringLables;
    }

    public void setRingLables(Map<String, Integer> ringLables) {
        this.ringLables = ringLables;
    }

    public String getRingDetail() {
        return ringDetail;
    }

    public void setRingDetail(String ringDetail) {
        this.ringDetail = ringDetail;
    }

    public List<BmobFile> getRingList() {
        return ringList;
    }

    public void setRingList(List<BmobFile> ringList) {
        this.ringList = ringList;
    }

    public String getRingName() {
        return ringName;
    }

    public void setRingName(String ringName) {
        this.ringName = ringName;
    }


    public Integer getZanCount() {
        return zanCount;
    }

    public void setZanCount(Integer zanCount) {
        this.zanCount = zanCount;
    }
}
