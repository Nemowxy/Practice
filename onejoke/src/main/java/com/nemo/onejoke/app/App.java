package com.nemo.onejoke.app;

import android.app.Application;

import com.baidu.apistore.sdk.ApiStoreSDK;

/**
 * Created by nemo on 2016/7/24 0024.
 */
public class App extends Application{

    @Override
    public void onCreate() {

        ApiStoreSDK.init(this,"da789c4214e788d95f3bc8b0fad639bf");
        super.onCreate();

    }
}
