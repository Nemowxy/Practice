package com.nemo.samplenews.utils;

import com.nemo.samplenews.bean.Channel;
import com.nemo.samplenews.bean.TitleBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nemo on 2016/7/25 0025.
 */
public class JSONUtils {
    public static List<Channel> getChannelList(String result){
        List<Channel> list = new ArrayList<>();

        try {
            JSONObject object = new JSONObject(result);
            JSONObject o1 = object.getJSONObject("showapi_res_body");
            JSONArray array = o1.getJSONArray("channelList");

            for (int i=0;i<array.length();i++){
                JSONObject o2 = array.getJSONObject(i);

                Channel c = new Channel();
                c.setName(o2.getString("name"));
                c.setChannelId(o2.getString("channelId"));
                list.add(c);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<TitleBean> getTitleList(String result){
        List<TitleBean> list = new ArrayList<>();

        try {
            JSONObject object = new JSONObject(result);
            JSONObject o1 = object.getJSONObject("showapi_res_body");
            JSONObject o2 = o1.getJSONObject("pagebean");
            JSONArray array = o2.getJSONArray("contentlist");
            for (int i=0;i<array.length();i++){
                JSONObject o3 = array.getJSONObject(i);
                TitleBean t = new TitleBean();
                t.setTitle(o3.getString("title"));
                t.setPubDate(o3.getString("pubDate"));
                if(o3.getJSONArray("imageurls")!=null){
                    JSONArray a2 = o3.getJSONArray("imageurls");
                    List<String> urls = new ArrayList<>();
                    for(int j=0;j<a2.length();j++){
                        JSONObject o4 = a2.getJSONObject(j);
                        urls.add(o4.getString("url"));
                    }
                    t.setImageurls(urls);
                }
                list.add(t);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return list;
    }
}
