package com.xy.lifemanage.view.proview;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

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
import com.xy.lifemanage.bean.Bean;
import com.xy.lifemanage.bean.ORBean;
import com.xy.lifemanage.bean.UserBean;
import com.xy.lifemanage.logic.ImgFileListActivity;
import com.xy.lifemanage.model.IGetDataListener;
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
import com.xy.lifemanage.utils.Utils;
import com.xy.lifemanage.view.IUserView;
import com.xy.lifemanage.view.TopBar;
import com.xy.lifemanage.view.myview.CircleImageView;
import com.xy.lifemanage.view.myview.CommonAdapter;
import com.xy.lifemanage.view.myview.ViewHolder;
import com.xy.lifemanage.view.myview.XListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * Created by nemo on 2016/5/16 0016.
 */
public class OrgDetailView extends AppCompatActivity implements View.OnClickListener ,IUserView,XListView.IXListViewListener{

    private static final int TAKE_PICTURE = 0x00004;
    private ImageView chat;
    private ImageView task;
    private ImageView org;
    private TopBar topBar;
    private App app;
    UserData userData;
    private String MyToken="";
    private CircleImageView or_detail_image;
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    public static Bitmap bimap;
    private View parentView;
    private Presenter presenter;
    private String objectId;
    private ORBean thisOrBean;
    private XListView xListView;
    private List<Bean> items;
    private CommonAdapter<Bean> mAdapter;
    private List<String> members;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.or_detail_main);
        app = (App) getApplication();
        Intent intent = getIntent();
        objectId = intent.getStringExtra("id");
        presenter = new Presenter(getApplicationContext(),this);
        initView();
        initData();
        bimap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_addpic_unfocused);
        parentView = OrgDetailView.this.getLayoutInflater().inflate(R.layout.my_main, null);
        init();
        initListViewData();
    }
    private void initData() {
        presenter.dataModel.searchOrForId(getApplicationContext(),objectId,new ISaveListener() {
            @Override
            public void onSuccess(Object var1) {
                thisOrBean = (ORBean) var1;
                members = ((ORBean) var1).getOr_member();
                String url = thisOrBean.getOr_image().getUrl();
                topBar.setTitle(thisOrBean.getOr_name());
                Picasso.with(getApplicationContext()).load(url).into(or_detail_image);
            }
            @Override
            public void onError(String var2) {
                ToastUtils.toast(getApplicationContext(),"error!");
            }
        });
    }

    private void initListViewData(){
        if(items==null)
            items = new ArrayList<Bean>();
        presenter.dataModel.searchOrTask(getApplicationContext(), objectId, new IGetDataListener() {
            @Override
            public void onSuccess(final List tasks) {
                presenter.dataModel.searchOrProject(getApplicationContext(), objectId, new IGetDataListener() {
                    @Override
                    public void onSuccess(List projects) {
                        presenter.dataModel.getOrAllData(getApplicationContext(), tasks, projects, items, new IGetDataListener() {
                            @Override
                            public void onSuccess(List var1) {
                                items = var1;
                                initListView();
                            }

                            @Override
                            public void onError(String var2) {
                                System.out.println("error:"+var2);
                                ToastUtils.toast(getApplicationContext(),var2);
                            }
                        });
                    }
                    @Override
                    public void onError(String var2) {
                        System.out.println("error:"+var2);
                        ToastUtils.toast(getApplicationContext(),var2);
                    }
                });
            }
            @Override
            public void onError(String var2) {
                System.out.println("error:"+var2);
                ToastUtils.toast(getApplicationContext(),var2);
            }
        });
    }

    private void initView() {
        chat = (ImageView) findViewById(R.id.or_detail_chat);
        task = (ImageView) findViewById(R.id.or_detail_task);
        org = (ImageView) findViewById(R.id.or_detail_org);
        topBar = (TopBar) findViewById(R.id.topBarOrDetail);
        or_detail_image = (CircleImageView) findViewById(R.id.or_detail_image);
        or_detail_image.setOnClickListener(this);
        chat.setOnClickListener(this);
        task.setOnClickListener(this);
        org.setOnClickListener(this);
        topBar.setTopBarOnclickListener(new TopBar.topBarOnclickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {
                //设置
                Intent intent = new Intent(getApplicationContext(),OrgSettingView.class);
                intent.putExtra("id",objectId);
                startActivity(intent);
            }
        });

    }

    private void updateList(){
        if(items==null)
           items = new ArrayList<Bean>();
        else
            items.clear();
        presenter.dataModel.searchOrTask(getApplicationContext(), objectId, new IGetDataListener() {
            @Override
            public void onSuccess(final List tasks) {
                presenter.dataModel.searchOrProject(getApplicationContext(), objectId, new IGetDataListener() {
                    @Override
                    public void onSuccess(List projects) {
                        presenter.dataModel.getOrAllData(getApplicationContext(), tasks, projects, items, new IGetDataListener() {
                            @Override
                            public void onSuccess(List var1) {
                                if(items.size()==var1.size()){

                                }else {
                                    items = var1;
                                    mAdapter.notifyDataSetChanged();
                                    xListView.setAdapter(mAdapter);
                                }
                            }

                            @Override
                            public void onError(String var2) {
                                System.out.println("error:"+var2);
                                ToastUtils.toast(getApplicationContext(),var2);
                            }
                        });
                    }
                    @Override
                    public void onError(String var2) {
                        System.out.println("error:"+var2);
                        ToastUtils.toast(getApplicationContext(),var2);
                    }
                });
            }
            @Override
            public void onError(String var2) {
                System.out.println("error:"+var2);
                ToastUtils.toast(getApplicationContext(),var2);
            }
        });
    }

    private void initListView() {
        xListView = (XListView) findViewById(R.id.or_detail_list);
        xListView.setPullLoadEnable(true);
        mAdapter = new CommonAdapter<Bean>(getApplicationContext(), items,
                R.layout.childitem) {
            @Override
            public void convert(final ViewHolder holder, Bean t) {
                holder.setText(R.id.my_task_list_item_date, t.getDate());
                holder.setText(R.id.my_task_list_item_title, t.getTitle());
                holder.setText(R.id.my_task_list_item_detail, t.getDetail());
                holder.getView(R.id.my_task_list_item_check).setVisibility(View.GONE);
                holder.getView(R.id.my_task_list_item_fujian).setVisibility(View.GONE);
            }
        };
        xListView.setAdapter(mAdapter);
        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(getContext(), OrgDetailView.class);
//                intent.putExtra("id",items.get(i-1).getObjectId());
//                startActivity(intent);
            }
        });
        xListView.setPullLoadEnable(false);//设置是否可以上拉加载
        xListView.setPullRefreshEnable(true);//设置是否可以下拉刷新
        xListView.setXListViewListener(this);
    }

    @Override
    public void onRefresh() {
        updateList();
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

    @Override
    protected void onRestart() {
        if(mAdapter!=null)
             mAdapter.notifyDataSetChanged();
        super.onRestart();
    }


    public void init() {

        pop = new PopupWindow(OrgDetailView.this);
        View view = OrgDetailView.this.getLayoutInflater().inflate(R.layout.item_popupwindows, null);
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
                intent.setClass(OrgDetailView.this,ImgFileListActivity.class);
                intent.putExtra("tag","ordetail");
                startActivity(intent);
                OrgDetailView.this.overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
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
                    or_detail_image.setImageBitmap(bm);
                    ORBean orBean = new ORBean();
                    orBean.setOr_image(new BmobFile(new File(Utils.toFile(bm).getAbsolutePath())));
                    final ProgressDialog progressDialog = new ProgressDialog(OrgDetailView.this);
                    progressDialog.setTitle("提示");
                    progressDialog.setMessage("更新中...");
                    progressDialog.show();
                    presenter.dataModel.updateOrganizeData(getApplicationContext(), orBean, objectId, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            progressDialog.dismiss();
                            ToastUtils.toast(getApplicationContext(),"更新成功!");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            progressDialog.dismiss();
                            ToastUtils.toast(getApplicationContext(),"更新失败!"+s);
                        }
                    });
                }
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if(Bimp.tempSelectBitmap.size()>0){
            or_detail_image.setImageBitmap(Bimp.tempSelectBitmap.get(0).getBitmap());
            ORBean orBean = new ORBean();
            orBean.setOr_image(new BmobFile(new File(Utils.toFile(Bimp.tempSelectBitmap.get(0).getBitmap()).getAbsolutePath())));
            presenter.dataModel.updateOrganizeData(getApplicationContext(), orBean, objectId, new SaveListener() {
                @Override
                public void onSuccess() {
                    ToastUtils.toast(getApplicationContext(),"修改成功!");
                }

                @Override
                public void onFailure(int i, String s) {
                    ToastUtils.toast(getApplicationContext(),"修改失败!"+s);
                }
            });

        }
    }

    public void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    @Override
    public void finish() {
        Bimp.tempSelectBitmap.clear();
        super.finish();
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.or_detail_org:
                intent.setClass(this,CreateProView.class);
                intent.putExtra("name",thisOrBean.getOr_name());
                intent.putExtra("id",objectId);
                startActivity(intent);
                break;
            case R.id.or_detail_task:
                intent.setClass(this,CreateTaskView.class);
                intent.putExtra("id",objectId);
                startActivity(intent);
                break;
            case R.id.or_detail_chat:
                initRong();
                break;
            case R.id.or_detail_image:
                ll_popup.startAnimation(AnimationUtils.loadAnimation(this, R.anim.activity_translate_in));
                pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                break;
        }
    }
    private void initRong() {
        RongIM.connect(app.getToken(), new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                getToken(new ISaveListener() {
                    @Override
                    public void onSuccess(Object objectId) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject((String)objectId);
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
                if(RongIM.getInstance() != null)
                {
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
                    /**
                     * 创建讨论组会话并进入会话界面。
                     *
                     * @param context       应用上下文。
                     * @param targetUserIds 要与之聊天的讨论组用户 Id 列表。
                     * @param title         聊天的标题，如果传入空值，则默认显示与之聊天的用户名称。
                     */
                    RongIM.getInstance().createDiscussionChat(OrgDetailView.this, members, thisOrBean.getOr_name());
                }
            }
            private UserInfo findUserById(String userId) {
                UserInfo userInfo = new UserInfo(app.getUserBean().getUsername(),app.getUserBean().getUsername(),Uri.parse(app.getUserBean().getImg().getUrl()));
                return userInfo;
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                System.out.println("s——onError—-" + errorCode);
            }
        });

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
                app = (App) getApplication();
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

    public String getObjectId(){
        return objectId;
    }
}
