package com.nemo.joke.http;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.nemo.joke.app.App;

import org.json.JSONObject;

import java.util.Objects;

/**
 * Created by nemo on 2016/8/1 0001.
 */

public class GetInfoUtils{

    //apistore
    public static final String URL_JOKE_INFO="http://apis.baidu.com/showapi_open_bus/showapi_joke/joke_text";
    public static final String URL_PHOTO_INFO="http://apis.baidu.com/txapi/mvtp/meinv";

    //gank网
    public static final String URL_ANDROID_INFO="http://gank.io/api/search/query/listview/category/Android";
    public static final String URL_GRIL_INFO="http://gank.io/api/search/query/listview/category/福利";
    public static final String URL_GIF_INFO="http://gank.io/api/search/query/listview/category/福利";
    public static final String URL_VIDEO_INFO="http://gank.io/api/search/query/listview/category/休息视频";
    public static final String URL_OTHER_INFO="http://gank.io/api/day/2015/08/06";

    private String mUrl = "";
    private RequestQueue mQueue;

    public GetInfoUtils(String url){
        this.mUrl = url;
        if (mQueue == null) {
            mQueue = App.getQueueInstance();
        }
    }

    public void request(IGetInfoListener listener){
        if(mUrl.equals(URL_JOKE_INFO)||mUrl.equals(URL_PHOTO_INFO)){
            requestFromApiStore(listener);
        }else{
            requestFromGank(listener);
        }
    }

    public void requestFromGank(final IGetInfoListener listener){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, mUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                listener.success(jsonObject.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                listener.error(volleyError.getMessage());
            }
        });
    }

    public void requestFromApiStore(IGetInfoListener listener){

    }

    public interface IGetInfoListener{
        public void success(String response);
        public void error(String error);
    }
}
