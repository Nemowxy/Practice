package com.xy.lifemanage.utils;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserData {
    private Context context;

    public UserData(Context context) {
        this.context = context;
    }


    /**
     * 保存用户名和密码在本地
     */
    public void saveUserInfo(Map<String, Object> map) {
        //获得SharedPreferences对象
        SharedPreferences preferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString("userName", map.get("userName").toString());
        editor.putString("phone", map.get("phone").toString());
        editor.putString("password", map.get("password").toString());
        editor.putString("email", map.get("email").toString());
        editor.putInt("userId", Integer.parseInt(map.get("userId").toString()));
        editor.putString("userImg", map.get("userImg").toString());
        editor.putString("certificate", map.get("certificate").toString());
        editor.commit();
    }

    /**
     * 获取存在本地的token
     * @return
     */
    public String getToken() {
        if(!context.getSharedPreferences("userdata", Context.MODE_PRIVATE).contains("token")){
            return null;
        }
        SharedPreferences preferences = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        return preferences.getString("token", "");
    }

    /**
     * 保存token在本地
     */
    public void saveToken(String token) {
        //获得SharedPreferences对象
        SharedPreferences preferences = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString("token", token);
        editor.commit();
    }

    /**
     * 获取存在本地的用户名和密码
     * @return
     */
    public Map<String, Object> getUserInfo() {
        Map<String, Object> params = new HashMap<String, Object>();
        if(!context.getSharedPreferences("userinfo", Context.MODE_PRIVATE).contains("userName")){
            return null;
        }
        SharedPreferences preferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        params.put("userName", preferences.getString("userName", ""));
        params.put("password", preferences.getString("password", ""));
        params.put("phone", preferences.getString("phone", ""));
        params.put("email", preferences.getString("email", ""));
        params.put("userId", preferences.getInt("userId", 0));
        params.put("userImg", preferences.getString("userImg", ""));
        params.put("certificate", preferences.getString("certificate", ""));
        return params;
    }
}
