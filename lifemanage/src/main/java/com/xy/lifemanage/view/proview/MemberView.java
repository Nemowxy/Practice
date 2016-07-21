package com.xy.lifemanage.view.proview;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.xy.lifemanage.R;
import com.xy.lifemanage.app.App;
import com.xy.lifemanage.bean.UserBean;
import com.xy.lifemanage.model.ISaveListener;
import com.xy.lifemanage.presenter.Presenter;
import com.xy.lifemanage.utils.SHA1;
import com.xy.lifemanage.utils.ToastUtils;
import com.xy.lifemanage.utils.Utils;
import com.xy.lifemanage.view.IUserView;
import com.xy.lifemanage.view.TopBar;
import com.xy.lifemanage.view.itemview.MemberOrView;
import com.xy.lifemanage.view.myview.MyViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.bmob.v3.listener.UpdateListener;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * Created by nemo on 2016/5/19 0019.
 */
public class MemberView extends AppCompatActivity implements IUserView{

    private String MyToken="";
    private App app;
    private Presenter presenter;
    private TopBar topBar;
    private Button chat;
    private TextView name;
    private TextView email;
    private ImageView image;
    private TextView power;

    private MyViewGroup viewGroup;
    private LinearLayout addLinearLayout;
    private Map<String,Integer> lables;
    private String username;
    private UserBean userBean;
    private LinearLayout member_or;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_main);
        username = getIntent().getStringExtra("username");
        app = (App) getApplication();
        MyToken = app.getToken();
        presenter = new Presenter(getApplicationContext(),this);
        initView();
        initData();
    }

    private void initData(){
        presenter.dataModel.searchUserForName(getApplicationContext(), username, new ISaveListener() {
            @Override
            public void onSuccess(Object objectId) {
                userBean = (UserBean) objectId;
                name.setText(userBean.getUsername());
                email.setText(userBean.getEmail());
                if(userBean.getLables()!=null) {
                    lables = userBean.getLables();
                    initLables();
                }
                Picasso.with(getApplicationContext()).load(userBean.getImg().getUrl()).into(image);
            }

            @Override
            public void onError(String msg) {

            }
        });
    }
    private void updata() {
        userBean.setLables(lables);
        userBean.update(getApplicationContext(), app.getUserBean().getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                ToastUtils.toast(getApplicationContext(),"+1");
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtils.toast(getApplicationContext(),s);
            }
        });
    }

    private void initView() {
        chat = (Button) findViewById(R.id.member_chat);
        name = (TextView) findViewById(R.id.member_name);
        email = (TextView) findViewById(R.id.member_email);
        image = (ImageView) findViewById(R.id.member_image);
        topBar = (TopBar) findViewById(R.id.topBarMember);
        topBar.setTopBarOnclickListener(new TopBar.topBarOnclickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });
        viewGroup = (MyViewGroup) findViewById(R.id.member_group);
        addLinearLayout = (LinearLayout) findViewById(R.id.member_lable);
        member_or = (LinearLayout) findViewById(R.id.member_or);
        member_or.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MemberView.this, MemberOrView.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initRong();
            }
        });
    }
    private void initRong() {
        /**
         * IMKit SDK调用第二步
         *
         * 建立与服务器的连接
         *
         */
        RongIM.connect(MyToken, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                getToken(new ISaveListener() {
                    @Override
                    public void onSuccess(Object objectId) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject((String) objectId);
                            app.setToken(jsonObject.getString("token"));
                            initRong();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(String msg) {
                        System.out.println("msg===="+msg);
                    }
                });

            }
            @Override
            public void onSuccess(String userId) {
                System.out.println("s——onSuccess—-" + userId);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                System.out.println("s——onError—-" + errorCode);
            }
        });
        if(RongIM.getInstance() != null) {
            /**
             * 设置用户信息的提供者，供 RongIM 调用获取用户名称和头像信息。
             *
             * @param userInfoProvider 用户信息提供者。
             * @param isCacheUserInfo  设置是否由 IMKit 来缓存用户信息。<br>
             *                         如果 App 提供的 UserInfoProvider。
             *                         每次都需要通过网络请求用户数据，而不是将用户数据缓存到本地内存，会影响用户信息的加载速度；<br>
             *                         此时最好将本参数设置为 true，由 IMKit 将用户信息缓存到本地内存中。
             * @see UserInfoProvider
             */
            RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

                @Override
                public UserInfo getUserInfo(String userId) {
                    return findUserById(userId);//根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
                }

            }, true);
            RongIM.getInstance().startPrivateChat(this, username, username);
        }

    }

    private UserInfo findUserById(String userId) {
        String name = app.getUserBean().getUsername();
        String id = app.getUserBean().getUsername();
        Uri uri = Uri.parse(app.getUserBean().getImg().getUrl());
        UserInfo userInfo = new UserInfo(name,id,uri);
        return userInfo;
    }

    private void getToken(final ISaveListener saveListener){
        final String url = "https://api.cn.ronghub.com/user/getToken.json";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                saveListener.onSuccess(s);
                System.out.println("get=="+s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                String appkey = "25wehl3uwnjbw";
                String appsret="iUAM5gG3MGWZY";
                String nonce = String.valueOf(new Random().nextLong());
                String timestamp  = String.valueOf(System.currentTimeMillis()/1000);
                String signature  = SHA1.hex_sha1(appsret+nonce+timestamp);
                map.put("App-Key",appkey);
                map.put("Nonce",nonce);
                map.put("Timestamp",timestamp);
                map.put("Signature",signature);
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                String url = "http://www.rongcloud.cn/docs/assets/img/logo_s.png";
                app = (App) getApplicationContext();
                UserBean userBean = app.getUserBean();
                map.put("userId",userBean.getUsername());
                map.put("name",userBean.getUsername());
                System.out.println(username+"---"+userBean.getUsername());
                map.put("portraitUri",url);
                return map;
            }
        };
        requestQueue.add(stringRequest);
        requestQueue.start();
    }


    public void initLables(){
        for (String key:lables.keySet()){
            addView(key,lables.get(key));
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void addView(final String str, final Integer count) {
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = Utils.dip2px(MemberView.this,20);
        lp.rightMargin = Utils.dip2px(MemberView.this,10);
        lp.topMargin = Utils.dip2px(MemberView.this,10);
        lp.bottomMargin = Utils.dip2px(MemberView.this,10);
        final TextView view = new TextView(this);
        view.setText(str+"  "+count);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        view.setTextColor(Color.WHITE);
        view.setBackground(getDrawable(R.drawable.button_shape_blue));
        view.setPadding(Utils.dip2px(MemberView.this,8),Utils.dip2px(MemberView.this,8),Utils.dip2px(MemberView.this,8),Utils.dip2px(MemberView.this,8));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setText(str+"  "+(count+1));
                view.setClickable(false);
                view.setBackground(getDrawable(R.drawable.button_shape_bluesky));
                view.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                view.setTextColor(Color.WHITE);
                view.setPadding(Utils.dip2px(MemberView.this,8),Utils.dip2px(MemberView.this,8),Utils.dip2px(MemberView.this,8),Utils.dip2px(MemberView.this,8));
                lables.remove(str);
                lables.put(str,count+1);
                updata();
            }
        });
        viewGroup.removeView(addLinearLayout);
        viewGroup.addView(view,lp);
        viewGroup.addView(addLinearLayout);
    }




    @Override
    public String getEditText(int id) {
        return null;
    }

    @Override
    public Bitmap getImageView(int id) {
        return null;
    }

    @Override
    public void setEditText(int id, String str) {

    }

    @Override
    public void setImageView(int id, Bitmap bitmap) {

    }

    @Override
    public void setListView(int id, List items) {

    }
}
