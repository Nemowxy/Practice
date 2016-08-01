package com.nemo.joke.app;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.apistore.sdk.ApiStoreSDK;

import cn.smssdk.SMSSDK;

/**
 * Created by nemo on 2016/7/25 0025.
 */
public class App extends Application{

    private static RequestQueue queue;

    @Override
    public void onCreate() {
        super.onCreate();
        ApiStoreSDK.init(this,"da789c4214e788d95f3bc8b0fad639bf");
        SMSSDK.initSDK(this,"15a15f31f1b24","6f12fef34ddeec64a7c0c929c5c0f2b8");
        queue = Volley.newRequestQueue(this);
    }

    public static RequestQueue getQueueInstance(){
        return queue;
    }

}
