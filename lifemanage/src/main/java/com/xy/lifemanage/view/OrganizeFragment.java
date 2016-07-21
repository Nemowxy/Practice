package com.xy.lifemanage.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.picasso.Picasso;
import com.xy.lifemanage.R;
import com.xy.lifemanage.app.App;
import com.xy.lifemanage.bean.ORBean;
import com.xy.lifemanage.bean.RingBean;
import com.xy.lifemanage.bean.UserBean;
import com.xy.lifemanage.model.IGetDataListener;
import com.xy.lifemanage.model.ISaveListener;
import com.xy.lifemanage.presenter.Presenter;
import com.xy.lifemanage.utils.SHA1;
import com.xy.lifemanage.utils.ToastUtils;
import com.xy.lifemanage.utils.UserData;
import com.xy.lifemanage.utils.Utils;
import com.xy.lifemanage.view.itemview.ProItemView;
import com.xy.lifemanage.view.myview.CircleImageView;
import com.xy.lifemanage.view.myview.CommonAdapter;
import com.xy.lifemanage.view.myview.Dynamic;
import com.xy.lifemanage.view.myview.GridViewInScroll;
import com.xy.lifemanage.view.myview.MyViewGroup;
import com.xy.lifemanage.view.myview.PopMenu;
import com.xy.lifemanage.view.myview.UserMenu;
import com.xy.lifemanage.view.myview.ViewHolder;
import com.xy.lifemanage.view.myview.XListView;
import com.xy.lifemanage.view.proview.CreateTaskView;
import com.xy.lifemanage.view.ring.RingShareMeView;
import com.xy.lifemanage.view.ring.RingShareOrgView;
import com.xy.lifemanage.view.widget.AlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by nemo on 2016/5/10 0010.
 */
public class OrganizeFragment extends Fragment implements IUserView, XListView.IXListViewListener {
    private View rootView;
    private XListView xListView;
    private DisplayImageOptions imageLodeOoptions;
    private List<RingBean> items;
    private Presenter presenter;
    private UserMenu mMenu;
    private TopBar topBar;
    private CommonAdapter<RingBean> adapter;
    private String MyToken="";
    private UserData userData;
    private App app;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.organize_main, container, false);
        presenter = new Presenter(getContext(), OrganizeFragment.this);
        app = (App) getActivity().getApplication();
        userData = new UserData(getContext());
        MyToken = userData.getToken();
        initData();
        initImageLoader();
        initMenu();

        topBar = (TopBar) rootView.findViewById(R.id.topBarOrgan);
        topBar.setTopBarOnclickListener(new TopBar.topBarOnclickListener() {
            @Override
            public void leftClick() {

            }

            @Override
            public void rightClick() {
                mMenu.showAsDropDown(topBar.rightButton);
            }
        });
        return rootView;
    }

    private void initMenu() {

        mMenu = new UserMenu(getContext());
        mMenu.addItem("分享小组", 0);
        mMenu.addItem("分享我", 1);
        mMenu.setOnItemSelectedListener(new PopMenu.OnItemSelectedListener() {
            @Override
            public void selected(View view, PopMenu.Item item, int position) {
                Intent intent = new Intent();
                switch (item.id) {
                    case 0:
                        intent.setClass(getContext(), RingShareOrgView.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent.setClass(getContext(), RingShareMeView.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private void initData() {
        xListView = (XListView) rootView.findViewById(R.id.ring_list);
        xListView.setPullLoadEnable(true);
        xListView.setPullLoadEnable(false);//设置是否可以上拉加载
        xListView.setPullRefreshEnable(true);//设置是否可以下拉刷新
        xListView.setXListViewListener(this);
        if(items==null)
            items = new ArrayList<RingBean>();
        else
            items.clear();
        presenter.getRingData(new IGetDataListener() {
            @Override
            public void onSuccess(List var1) {
                for (int i=0;i<var1.size();i++){
                    System.out.println(((RingBean)var1.get(i)).getCreatedAt());
                    items.add((RingBean) var1.get(var1.size()-1-i));
                }
                initView();
            }

            @Override
            public void onError(String var2) {
                ToastUtils.toast(getContext(), var2);
            }
        });
    }

    private void initView() {
        adapter = new CommonAdapter<RingBean>(getContext(), items,
                R.layout.listview_item_layout) {
            @Override
            public void convert(final ViewHolder holder, final RingBean t) {
//                int width = Utils.dip2px(getContext(),48);
//                ImageSize imageSize = new ImageSize(width, width);// 设置图片大小
//                ImageLoader.getInstance().loadImage(t.getRingImg().getUrl(), imageSize, imageLodeOoptions, new SimpleImageLoadingListener(){
//                    @Override
//                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                        super.onLoadingComplete(imageUri, view, loadedImage);
//                        ((CircleImageView)holder.getView(R.id.imageLogo)).setImageBitmap(loadedImage);
//                    }
//                });
                final ImageButton imageButton = holder.getView(R.id.ring_item_zanButton);
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imageButton.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.zan1));
                        ToastUtils.toast(getContext(),"赞");
                    }
                });
                Picasso.with(getContext()).load(t.getRingImg().getUrl()).into((CircleImageView) holder.getView(R.id.imageLogo));
                holder.setText(R.id.ring_name,t.getRingName());
                holder.setText(R.id.ring_date,t.getCreatedAt());
                holder.setText(R.id.ring_detail,t.getRingDetail());
                GridViewInScroll gridView = (GridViewInScroll)holder.getView(R.id.gridView);
                List<String> data = new ArrayList<>();
                for (int i=0;i<t.getRingList().size();i++){
                    data.add(t.getRingList().get(i).getUrl());
                }
                GridViewAdapter mAdapter = new GridViewAdapter(getContext(), data);
                gridView.setAdapter(mAdapter);
                if(t.getOrg()) {
                    holder.getView(R.id.ring_item_join).setVisibility(View.VISIBLE);
                    holder.getView(R.id.ring_item_joinButton).setVisibility(View.VISIBLE);
                    holder.getView(R.id.ring_item_join).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), ProItemView.class);
                            intent.putExtra("name", t.getRingName());
                            startActivity(intent);
                        }
                    });
                    holder.getView(R.id.ring_item_message).setVisibility(View.INVISIBLE);
                    holder.getView(R.id.ring_item_messageButton).setVisibility(View.INVISIBLE);
                }else {
                    holder.getView(R.id.ring_item_message).setVisibility(View.VISIBLE);
                    holder.getView(R.id.ring_item_messageButton).setVisibility(View.VISIBLE);
                    holder.getView(R.id.ring_item_message).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            initRong(t.getRingName());
                        }
                    });
                    holder.getView(R.id.ring_item_join).setVisibility(View.INVISIBLE);
                    holder.getView(R.id.ring_item_joinButton).setVisibility(View.INVISIBLE);

                }
            }
        };
        xListView.setAdapter(adapter);
    }

    private void initRong(final String username) {
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
                        System.out.println("msg===="+objectId);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject((String) objectId);
                            userData.saveToken(jsonObject.getString("token"));
                            MyToken = jsonObject.getString("token");
                            System.out.println("msg===="+MyToken);
                            initRong(username);
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
            RongIM.getInstance().startPrivateChat(getContext(), username, username);
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


    private void initImageLoader() {
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getContext()));
        imageLodeOoptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.default_background)
                .showImageOnFail(R.drawable.default_background)
                /*.resetViewBeforeLoading(true)*/
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(100))
                .build();
    }

    /**dp转成px*/
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**图片*/
    class GridViewAdapter extends ArrayAdapter<String>{
        private DisplayImageOptions imageLodeOoptions;
        public GridViewAdapter(Context context, List<String> datas) {
            super(context, 0, datas);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.gridview_item_layout, null);
            }
            String url = getItem(position);
            final ImageView imageview = (ImageView)convertView.findViewById(R.id.imageview);
            int width = dip2px(getContext(), getContext().getResources().getDimension(R.dimen.gridview_item_width));
            ImageSize imageSize = new ImageSize(width, width);// 设置图片大小
            ImageLoader.getInstance().displayImage(url, imageview);

          /*  ImageLoader.getInstance().loadImage(url, imageSize, imageLodeOoptions, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    Log.e("TEST", imageUri);
                    imageview.setImageBitmap(loadedImage);
                }
            });*/
            return convertView;
        }
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

    @Override
    public void onRefresh() {
        initData();
        onLoad();
    }

    @Override
    public void onLoadMore() {

    }
    private void onLoad() {
        xListView.stopRefresh();
        xListView.stopLoadMore();
        xListView.setRefreshTime("刚刚");
    }
}
