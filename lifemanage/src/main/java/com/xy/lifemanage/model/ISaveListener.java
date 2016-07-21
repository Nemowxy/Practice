package com.xy.lifemanage.model;


/**
 * Created by nemo on 2016/5/17 0017.
 */
public abstract class ISaveListener {

    public abstract void onSuccess(Object objectId);

    public abstract void onError(String msg);
}
