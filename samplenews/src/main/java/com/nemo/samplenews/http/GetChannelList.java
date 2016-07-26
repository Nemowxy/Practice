package com.nemo.samplenews.http;

import android.os.Handler;
import android.os.Message;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.nemo.samplenews.utils.JSONUtils;

/**
 * Created by nemo on 2016/7/25 0025.
 */
public class GetChannelList extends Thread{

    private Handler handler;

    public GetChannelList(Handler handler){
        this.handler = handler;
    }

    @Override
    public void run() {
        super.run();

        String url="http://apis.baidu.com/showapi_open_bus/channel_news/channel_news";

        ApiStoreSDK.execute(url,ApiStoreSDK.GET,null,new ApiCallBack(){
            @Override
            public void onComplete() {
            }

            @Override
            public void onSuccess(int i, String s) {
                Message m = new Message();
                m.obj = JSONUtils.getChannelList(s);
                m.what = 100;
                handler.sendMessage(m);
            }

            @Override
            public void onError(int i, String s, Exception e) {
                System.out.println("Error:"+s);
            }
        });

    }
}
