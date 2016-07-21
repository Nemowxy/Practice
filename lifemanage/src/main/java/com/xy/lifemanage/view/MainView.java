package com.xy.lifemanage.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xy.lifemanage.R;
import com.xy.lifemanage.app.App;
import com.xy.lifemanage.bean.UserBean;
import com.xy.lifemanage.model.ISaveListener;
import com.xy.lifemanage.utils.SHA1;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.bmob.v3.BmobUser;

/**
 * Created by nemo on 2016/5/10 0010.
 */
public class MainView extends AppCompatActivity{

    public final static String TAG = "MainView";
    private long mExitTime;
    /**
     * 主界面的viewpager
     */
    private ViewPager vp_main_tab = null;
    private FragmentPagerAdapter mAdapter = null;
    private List<Fragment> mFragments = new ArrayList<Fragment>();

    /**
     * 底部的四个radiobutton
     */
    private RadioButton rb_main_tab_menu1 = null;
    private RadioButton rb_main_tab_menu2 = null;
    private RadioButton rb_main_tab_menu3 = null;

    private RadioGroup rg_main_menu = null;

    private ProjectFragment projectFragment;
    private OrganizeFragment organizeFragment;
    private MyFragment myFragment;

    private App app;
    private UserBean userBean;
    public static Map<String,Object> data = new HashMap<String, Object>();
    public static boolean flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_main);
        app = (App) getApplication();
        userBean = BmobUser.getCurrentUser(getApplicationContext(),UserBean.class);
        app.setUserBean(userBean);
        getToken(new ISaveListener() {
            @Override
            public void onSuccess(Object objectId) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject((String) objectId);
                    app.setToken(jsonObject.getString("token"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String msg) {

            }
        });
        findViews();
        initAdapter();
        initViewPager();
    }

    private void getToken(final ISaveListener saveListener) {
        final String url = "https://api.cn.ronghub.com/user/getToken.json";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                saveListener.onSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                String appkey = "25wehl3uwnjbw";
                String appsret = "iUAM5gG3MGWZY";
                String nonce = String.valueOf(new Random().nextLong());
                String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
                String signature = SHA1.hex_sha1(appsret + nonce + timestamp);
                map.put("App-Key", appkey);
                map.put("Nonce", nonce);
                map.put("Timestamp", timestamp);
                map.put("Signature", signature);
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                String url = "http://www.rongcloud.cn/docs/assets/img/logo_s.png";
                map.put("userId", userBean.getUsername());
                map.put("name", userBean.getUsername());
                map.put("portraitUri", url);
                return map;
            }
        };
        requestQueue.add(stringRequest);
        requestQueue.start();
    }

    private void findViews() {
        rb_main_tab_menu1 = (RadioButton) findViewById(R.id.rb_main_tab_menu1);
        rb_main_tab_menu2 = (RadioButton) findViewById(R.id.rb_main_tab_menu2);
        rb_main_tab_menu3 = (RadioButton) findViewById(R.id.rb_main_tab_menu3);
        rg_main_menu = (RadioGroup) findViewById(R.id.rg_main_menu);
        vp_main_tab = (ViewPager) findViewById(R.id.vp_main_tab);
        projectFragment = new ProjectFragment();
        organizeFragment = new OrganizeFragment();
        myFragment = new MyFragment();

        mFragments.add(projectFragment);
        mFragments.add(organizeFragment);
        mFragments.add(myFragment);
    }


    private void initAdapter() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        mAdapter = new FragmentPagerAdapter(fm) {

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public android.support.v4.app.Fragment getItem(int arg0) {
                return mFragments.get(arg0);
            }

            // 初始化每个页卡选项
            @Override
            public Object instantiateItem(ViewGroup arg0, int arg1) {
                return super.instantiateItem(arg0, arg1);
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                super.destroyItem(container, position, object);
            }
        };
    }

    private void initViewPager() {
        vp_main_tab.setAdapter(mAdapter);
        vp_main_tab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        rb_main_tab_menu1.setChecked(true);
                        break;
                    case 1:
                        rb_main_tab_menu2.setChecked(true);
                        break;
                    case 2:
                        rb_main_tab_menu3.setChecked(true);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        rg_main_menu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup rg, int id) {
                switch (id) {
                    case R.id.rb_main_tab_menu1:
                        vp_main_tab.setCurrentItem(0);
                        break;
                    case R.id.rb_main_tab_menu2:
                        vp_main_tab.setCurrentItem(1);
                        break;
                    case R.id.rb_main_tab_menu3:
                        vp_main_tab.setCurrentItem(2);
                        break;
                    default:
                        break;
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(flag) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null && bundle.getString("id") != null) {
                if (bundle.getString("id").equals("1")) {
                    vp_main_tab.setCurrentItem(2);
                    flag=false;
                }
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
