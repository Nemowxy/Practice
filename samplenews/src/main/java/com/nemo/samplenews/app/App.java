package com.nemo.samplenews.app;

import android.app.Application;

import com.baidu.apistore.sdk.ApiStoreSDK;

import cn.smssdk.SMSSDK;

/**
 * Created by nemo on 2016/7/25 0025.
 */
public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        ApiStoreSDK.init(this,"da789c4214e788d95f3bc8b0fad639bf");
        SMSSDK.initSDK(this,"1569644d55ed5","749a236ccf987bdabc98fc8ac4064693");
    }
}
