package com.xy.lifemanage.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
import com.xy.lifemanage.logic.ImgFileListActivity;
import com.xy.lifemanage.model.ISaveListener;
import com.xy.lifemanage.photo.utils.Bimp;
import com.xy.lifemanage.photo.utils.FileUtils;
import com.xy.lifemanage.photo.utils.ImageItem;
import com.xy.lifemanage.photo.utils.PublicWay;
import com.xy.lifemanage.photo.utils.Res;
import com.xy.lifemanage.presenter.Presenter;
import com.xy.lifemanage.utils.SHA1;
import com.xy.lifemanage.utils.ToastUtils;
import com.xy.lifemanage.utils.UserData;
import com.xy.lifemanage.view.my.MyLableView;
import com.xy.lifemanage.view.my.MySettingView;
import com.xy.lifemanage.view.my.MyTaskView;
import com.xy.lifemanage.view.myview.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.bmob.v3.listener.SaveListener;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by nemo on 2016/5/10 0010.
 */
public class MyFragment extends Fragment implements View.OnClickListener,IUserView {

    private static final int TAKE_PICTURE = 0x00004;
    private String MyToken="";
    private View rootView;

    private LinearLayout ll_message;
    private LinearLayout ll_lable;
    private LinearLayout ll_task;
    private LinearLayout ll_setting;
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    public static Bitmap bimap;
    private View parentView;
    private App app;
    private UserData userData;
    private Presenter presenter;

    private CircleImageView imageView;
    private TextView username;
    private TextView email;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.my_main, container, false);
        userData = new UserData(getContext());
        app = (App) getActivity().getApplication();
        MyToken = app.getToken();
        presenter = new Presenter(getContext(),this);
        app = (App) getActivity().getApplication();
        initView();
        bimap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_addpic_unfocused);
        parentView = getActivity().getLayoutInflater().inflate(R.layout.my_main, null);
        init();
        return rootView;
    }

    public void init() {

        pop = new PopupWindow(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.item_popupwindows, null);
        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
        Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                photo();
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),ImgFileListActivity.class);
                intent.putExtra("tag","my");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        username = (TextView) rootView.findViewById(R.id.my_name);
        email = (TextView) rootView.findViewById(R.id.my_email);

        imageView = (CircleImageView) rootView.findViewById(R.id.my_image);
        imageView.setOnClickListener(this);
        if(app.getUserBean().getImg()!=null)
        Picasso.with(getContext()).load(app.getUserBean().getImg().getUrl()).into(imageView);
        username.setText(app.getUserBean().getUsername());
        email.setText(app.getUserBean().getEmail());
    }


    private void initView() {
        ll_lable = (LinearLayout) rootView.findViewById(R.id.my_lable);
        ll_message = (LinearLayout) rootView.findViewById(R.id.my_message);
        ll_task = (LinearLayout) rootView.findViewById(R.id.my_task);
        ll_setting = (LinearLayout) rootView.findViewById(R.id.my_set);
        ll_lable.setOnClickListener(this);
        ll_message.setOnClickListener(this);
        ll_task.setOnClickListener(this);
        ll_setting.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.my_lable:
                intent.setClass(getContext(), MyLableView.class);
                startActivity(intent);
                break;
            case R.id.my_message:
                initRong();
                break;
            case R.id.my_task:
                intent.setClass(getContext(), MyTaskView.class);
                startActivity(intent);
                break;
            case R.id.my_set:
                intent.setClass(getContext(), MySettingView.class);
                startActivity(intent);
                break;
            case R.id.my_image:
                ll_popup.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.activity_translate_in));
                pop.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case TAKE_PICTURE:
                if (Bimp.tempSelectBitmap.size() < 9) {
                    String fileName = String.valueOf(System.currentTimeMillis());
                    Bitmap bm = (Bitmap) data.getExtras().get("data");
                    FileUtils.saveBitmap(bm, fileName);
                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setBitmap(bm);
                    Bimp.tempSelectBitmap.add(takePhoto);
                    imageView.setImageBitmap(bm);
                    upload(bm);
                }
                break;
        }
    }

    public void upload(Bitmap bimap){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("提示");
        progressDialog.setMessage("更新中...");
        progressDialog.show();
        presenter.dataModel.updateUserImg(getContext(), app.getUserBean(), bimap, new ISaveListener() {
            @Override
            public void onSuccess(Object objectId) {
                progressDialog.dismiss();
                ToastUtils.toast(getContext(), "更新成功!");
                Bimp.tempSelectBitmap.clear();
            }
            @Override
            public void onError(String msg) {
                progressDialog.dismiss();
                ToastUtils.toast(getContext(), "更新失败!"+msg);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Bimp.tempSelectBitmap.size()>0){
            imageView.setImageBitmap(Bimp.tempSelectBitmap.get(0).getBitmap());
            upload(Bimp.tempSelectBitmap.get(0).getBitmap());
        }
    }

    public void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);

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
        if(RongIM.getInstance() != null)
            RongIM.getInstance().startConversationList(getActivity());
        //RongIM.getInstance().startPrivateChat(getActivity(), "123", "标题");
    }

    private void getToken(final ISaveListener saveListener){
        final String url = "https://api.cn.ronghub.com/user/getToken.json";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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
                app = (App) getActivity().getApplication();
                UserBean userBean = app.getUserBean();
                map.put("userId",userBean.getUsername());
                map.put("name",userBean.getUsername());
                map.put("portraitUri",url);
                return map;
            }
        };
        requestQueue.add(stringRequest);
        requestQueue.start();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Bimp.tempSelectBitmap.clear();
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
