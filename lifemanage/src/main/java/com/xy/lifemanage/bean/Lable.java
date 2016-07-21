package com.xy.lifemanage.bean;

import java.util.Map;

import cn.bmob.v3.BmobObject;

/**
 * Created by nemo on 2016/5/23 0023.
 */
public class Lable extends BmobObject{
    private Map<String,Integer> lables;

    public Map<String, Integer> getLables() {
        return lables;
    }

    public void setLables(Map<String, Integer> lables) {
        this.lables = lables;
    }
}
