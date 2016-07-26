package com.nemo.samplenews.http;

import android.os.Handler;
import android.os.Message;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;
import com.nemo.samplenews.utils.JSONUtils;

/**
 * Created by nemo on 2016/7/25 0025.
 */
public class GetNewsForId extends Thread{

    private Handler handler;
    private String channelId;
    private String page="1";

    public GetNewsForId(Handler handler,String channelId){
        this.handler = handler;
        this.channelId = channelId;
    }

    public GetNewsForId(Handler handler,String channelId,String page){
        this.handler = handler;
        this.channelId = channelId;
        this.page = page;
    }

    @Override
    public void run() {
        super.run();

        String url = "http://apis.baidu.com/showapi_open_bus/channel_news/search_news";

        Parameters para = new Parameters();
        para.put("channelId",channelId);
        if(!page.equals("1")){
            para.put("page",page);
        }
        ApiStoreSDK.execute(url,ApiStoreSDK.GET,para,new ApiCallBack(){
            @Override
            public void onComplete() {
            }

            @Override
            public void onError(int i, String s, Exception e) {
            }

            @Override
            public void onSuccess(int i, String s) {
                Message m = new Message();
                if(page.equals("1"))
                    m.what = 100;
                else
                    m.what = 101;
                m.obj = JSONUtils.getTitleList(s);
                handler.sendMessage(m);
            }
        });

    }
}
