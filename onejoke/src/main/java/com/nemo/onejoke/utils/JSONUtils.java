package com.nemo.onejoke.utils;

import com.nemo.onejoke.bean.Joken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nemo on 2016/7/24 0024.
 */
public class JSONUtils {

    public static ArrayList<Joken> getJoken(String result){
        ArrayList<Joken> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject jsonObject1 = jsonObject.getJSONObject("showapi_res_body");
            JSONArray jsonArray = jsonObject1.getJSONArray("contentlist");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject object = (JSONObject) jsonArray.get(i);
                Joken joken = new Joken();
                joken.setTitle(object.getString("title"));
                joken.setTime(object.getString("ct"));
                String str = object.getString("text").toString().trim();
                String s1=str.replaceAll("<p>","");
                String s2=s1.replaceAll("</p>","");
                String s3=s2.replaceAll("&nbsp;","");
                System.out.println(s3);
                joken.setText(s3);
                list.add(joken);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
