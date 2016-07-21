package com.xy.lifemanage.view.itemview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xy.lifemanage.R;
import com.xy.lifemanage.app.App;
import com.xy.lifemanage.bean.ORBean;
import com.xy.lifemanage.bean.UserBean;
import com.xy.lifemanage.model.ISaveListener;
import com.xy.lifemanage.presenter.Presenter;
import com.xy.lifemanage.utils.ToastUtils;
import com.xy.lifemanage.utils.Utils;
import com.xy.lifemanage.view.IUserView;
import com.xy.lifemanage.view.TopBar;
import com.xy.lifemanage.view.myview.CircleImageView;
import com.xy.lifemanage.view.myview.MyViewGroup;
import com.xy.lifemanage.view.widget.AlertDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by nemo on 2016/5/11 0011.
 */
public class ProItemView extends Activity implements IUserView{

    private TopBar topBar;
    private String objectId;
    private Button join;
    private CircleImageView image;
    private TextView code;
    private TextView detail;
    private App app;
    private Presenter presenter;
    private ORBean orBean;
    private MyViewGroup viewGroup;
    private LinearLayout addLinearLayout;
    private Map<String,Integer> lables;
    private String ringName;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.or_item_main);
        presenter = new Presenter(getApplicationContext(),this);
        app = (App) getApplication();
        Intent intent = getIntent();
        objectId = intent.getStringExtra("id");
        ringName = intent.getStringExtra("name");
        initView();
        initJoinButton();
        initData();
    }


    private void initData() {
        if(objectId!=null) {
            presenter.dataModel.searchOrForId(getApplicationContext(), objectId, new ISaveListener() {
                @Override
                public void onSuccess(Object bean) {
                    orBean = (ORBean) bean;
                    lables = orBean.getLables();
                    initLables();
                    Picasso.with(getApplicationContext()).load(orBean.getOr_image().getUrl()).into(image);
                    code.setText(String.valueOf(orBean.getOr_code()));
                    topBar.setTitle(orBean.getOr_name());
                    detail.setText(orBean.getDetail());
                }

                @Override
                public void onError(String msg) {
                    ToastUtils.toast(getApplicationContext(), "查询失败!");
                }
            });
        }
        if(ringName!=null){
            presenter.dataModel.searchOrForName(getApplicationContext(), ringName, new ISaveListener() {
                @Override
                public void onSuccess(Object bean) {
                    orBean = (ORBean) bean;
                    lables = orBean.getLables();
                    initLables();
                    Picasso.with(getApplicationContext()).load(orBean.getOr_image().getUrl()).into(image);
                    code.setText(String.valueOf(orBean.getOr_code()));
                    topBar.setTitle(orBean.getOr_name());
                    detail.setText(orBean.getDetail());
                    id = orBean.getObjectId();
                    System.out.println("id="+id);
                }

                @Override
                public void onError(String msg) {
                    ToastUtils.toast(getApplicationContext(), "查询失败!");
                }
            });
        }
    }

    public void initLables(){
        for (String key:lables.keySet()){
            addView(key);
        }
    }

    private void initJoinButton(){
        if(objectId==null){
            presenter.dataModel.inOrg(getApplicationContext(), app.getUserBean().getUsername(), id, new ISaveListener() {
                @Override
                public void onSuccess(Object objectId) {

                    join.setClickable(false);
                    join.setText("已加入");
                }

                @Override
                public void onError(String msg) {
                    join.setClickable(true);
                    join.setText("加入");
                }
            });
        }else {
            presenter.dataModel.inOrg(getApplicationContext(), app.getUserBean().getUsername(), objectId, new ISaveListener() {
                @Override
                public void onSuccess(Object objectId) {

                    join.setClickable(false);
                    join.setText("已加入");
                }

                @Override
                public void onError(String msg) {
                    join.setClickable(true);
                    join.setText("加入");
                }
            });
        }
    }



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void addView(String str) {
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = Utils.dip2px(ProItemView.this,10);
        lp.rightMargin = Utils.dip2px(ProItemView.this,10);
        lp.topMargin = Utils.dip2px(ProItemView.this,10);
        lp.bottomMargin = Utils.dip2px(ProItemView.this,10);
        TextView view = new TextView(this);
        view.setText(str);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        view.setTextColor(Color.WHITE);
        view.setBackground(getDrawable(R.drawable.button_shape_blue));
        view.setPadding(Utils.dip2px(ProItemView.this,8),Utils.dip2px(ProItemView.this,8),Utils.dip2px(ProItemView.this,8),Utils.dip2px(ProItemView.this,8));
        viewGroup.removeView(addLinearLayout);
        viewGroup.addView(view,lp);
        viewGroup.addView(addLinearLayout);
    }

    private void initView() {
        detail = (TextView) findViewById(R.id.or_item_detail);
        code = (TextView) findViewById(R.id.or_item_code);
        image = (CircleImageView) findViewById(R.id.or_item_image);
        join = (Button) findViewById(R.id.or_item_join);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMember();
            }
        });
        topBar = (TopBar) findViewById(R.id.or_item_topBar);
        topBar.setTopBarOnclickListener(new TopBar.topBarOnclickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });
        viewGroup = (MyViewGroup) findViewById(R.id.or_item_group);
        addLinearLayout = (LinearLayout) findViewById(R.id.or_item_linear);
    }

    private void addMember() {
        System.out.println(objectId);
        if(objectId!=null) {
            presenter.dataModel.addMemberToOrg(getApplicationContext(), app.getUserBean().getUsername(), objectId, new SaveListener() {
                @Override
                public void onSuccess() {
                    UserBean userBean = app.getUserBean();
                    List<String> ors;
                    if (null == userBean.getOrganizeId()) {
                        ors = new ArrayList<String>();
                    } else {
                        ors = userBean.getOrganizeId();
                    }
                    ors.add(objectId);
                    userBean.setOrganizeId(ors);
                    presenter.dataModel.updateUser(getApplicationContext(), userBean, new ISaveListener() {
                        @Override
                        public void onSuccess(Object objectId) {
                            ToastUtils.toast(getApplicationContext(), "成功加入！");
                            join.setClickable(false);
                            join.setText("已加入");
                        }

                        @Override
                        public void onError(String s) {
                            ToastUtils.toast(getApplicationContext(), "加入失败！" + s);
                        }
                    });
                }

                @Override
                public void onFailure(int i, String s) {
                    ToastUtils.toast(getApplicationContext(), "加入失败！" + s);
                }
            });
        }else{
            presenter.dataModel.addMemberToOrg(getApplicationContext(), app.getUserBean().getUsername(), id, new SaveListener() {
                @Override
                public void onSuccess() {
                    UserBean userBean = app.getUserBean();
                    List<String> ors;
                    if (null == userBean.getOrganizeId()) {
                        ors = new ArrayList<String>();
                    } else {
                        ors = userBean.getOrganizeId();
                    }
                    ors.add(id);
                    userBean.setOrganizeId(ors);
                    presenter.dataModel.updateUser(getApplicationContext(), userBean, new ISaveListener() {
                        @Override
                        public void onSuccess(Object objectId) {
                            ToastUtils.toast(getApplicationContext(), "成功加入！");
                            join.setClickable(false);
                            join.setText("已加入");
                        }

                        @Override
                        public void onError(String s) {
                            ToastUtils.toast(getApplicationContext(), "加入失败！" + s);
                        }
                    });
                }

                @Override
                public void onFailure(int i, String s) {
                    ToastUtils.toast(getApplicationContext(), "加入失败！" + s);
                }
            });
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
}
