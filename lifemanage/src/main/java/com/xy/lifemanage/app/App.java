package com.xy.lifemanage.app;

import android.app.Application;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xy.lifemanage.bean.Lable;
import com.xy.lifemanage.bean.UserBean;
import com.xy.lifemanage.model.ISaveListener;
import com.xy.lifemanage.utils.SHA1;
import com.xy.lifemanage.utils.UserData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import io.rong.imkit.RongIM;

/**
 * Created by nemo on 2016/5/10 0010.
 */
public class App extends Application {

    private UserBean userBean;
    private String token;

    @Override
    public void onCreate() {
        super.onCreate();
        RongIM.init(this);
        //设置BmobConfig
        BmobConfig config = new BmobConfig.Builder()
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                //文件分片上传时每片的大小（单位字节），默认512*1024
                .setBlockSize(500 * 1024)
                .build();
        Bmob.getInstance().initConfig(config);
       // test();
        if(BmobUser.getCurrentUser(getApplicationContext(),UserBean.class)!=null)
            userBean = BmobUser.getCurrentUser(getApplicationContext(), UserBean.class);
    }

    private void test(){
        final Lable lable = new Lable();
        Map<String,Integer> map = new HashMap<>();
        map.put("hellp",1);
        lable.setLables(map);
        lable.save(getApplicationContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                BmobQuery<Lable> query = new BmobQuery<Lable>();
                query.findObjects(getApplicationContext(), new FindListener<Lable>() {
                    @Override
                    public void onSuccess(List<Lable> list) {
                        System.out.println(list.get(0).getLables());
                    }
                    @Override
                    public void onError(int i, String s) {

                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

}
