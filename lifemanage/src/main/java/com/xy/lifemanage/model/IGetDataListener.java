package com.xy.lifemanage.model;

import com.xy.lifemanage.bean.ORBean;

import java.util.List;


/**
 * Created by nemo on 2016/5/10 0010.
 */
public abstract class IGetDataListener<T>{
    public IGetDataListener() {
    }
    public abstract void onSuccess(List<T> var1);

    public abstract void onError(String var2);
}
