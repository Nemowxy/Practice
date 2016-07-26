package com.nemo.samplenews.app;

import android.app.Application;

import com.baidu.apistore.sdk.ApiStoreSDK;

/**
 * Created by nemo on 2016/7/25 0025.
 */
public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        ApiStoreSDK.init(this,"da789c4214e788d95f3bc8b0fad639bf");
    }
}
