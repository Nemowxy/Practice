package com.nemo.onejoke.http;

import android.os.Handler;
import android.os.Message;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;
import com.nemo.onejoke.utils.JSONUtils;

/**
 * Created by nemo on 2016/7/24 0024.
 */
public class GetDataThread extends Thread{

    private Handler handler;
    private String url="http://apis.baidu.com/showapi_open_bus/showapi_joke/joke_text";
    private String page="1";
    private String apiKey="da789c4214e788d95f3bc8b0fad639bf";
    private String type="1";

    public GetDataThread(Handler handler,String page,String type){
        this.handler = handler;
        this.page = page;
        this.type = type;
    }

    @Override
    public void run() {
        super.run();
        Parameters para = new Parameters();
        para.put("page", page);

        ApiStoreSDK.execute(url,ApiStoreSDK.GET,para,new ApiCallBack(){
            @Override
            public void onComplete() {
                System.out.println("complete");
            }

            @Override
            public void onError(int i, String s, Exception e) {
                System.out.println("Error:"+s);
            }

            @Override
            public void onSuccess(int i, String s) {
                System.out.println("success:"+s);
                Message message = new Message();
                message.obj = JSONUtils.getJoken(s);
                if(type.equals("1"))
                    message.what = 100;
                else{
                    message.what = 101;
                }
                handler.sendMessage(message);
            }
        });
    }
}
