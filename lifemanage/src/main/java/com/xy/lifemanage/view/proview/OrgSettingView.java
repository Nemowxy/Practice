package com.xy.lifemanage.view.proview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xy.lifemanage.R;
import com.xy.lifemanage.app.App;
import com.xy.lifemanage.bean.ORBean;
import com.xy.lifemanage.bean.UserBean;
import com.xy.lifemanage.model.ISaveListener;
import com.xy.lifemanage.presenter.Presenter;
import com.xy.lifemanage.utils.ToastUtils;
import com.xy.lifemanage.view.IUserView;
import com.xy.lifemanage.view.TopBar;
import com.xy.lifemanage.view.widget.AlertDialog;

import java.util.List;

import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by nemo on 2016/5/16 0016.
 */
public class OrgSettingView extends AppCompatActivity implements View.OnClickListener,IUserView {

    private TopBar topBar;
    private RelativeLayout org_setting_detail;
    private RelativeLayout org_setting_member;
    private RelativeLayout org_setting_classify;
    private RelativeLayout org_setting_jianjie;
    private String objectId;
    private ORBean orBean;
    private Presenter presenter;

    private TextView name;
    private TextView code;
    private TextView memberCount;
    private Button exit;

    private App app;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.org_setting_main);
        Intent intent = getIntent();
        objectId = intent.getStringExtra("id");
        presenter = new Presenter(getApplicationContext(),this);
        app = (App) getApplication();
        initView();
        initData();
    }

    private void initData() {
        presenter.dataModel.searchOrForId(getApplicationContext(),objectId,new ISaveListener() {
            @Override
            public void onSuccess(Object var1) {
                orBean = (ORBean) var1;
                topBar.setTitle(orBean.getOr_name());
                name.setText(orBean.getOr_name());
                code.setText(String.valueOf(orBean.getOr_code()));
                memberCount.setText(orBean.getOr_member().size()+"人");
            }
            @Override
            public void onError(String var2) {
                ToastUtils.toast(getApplicationContext(),"error!");
            }
        });
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.topBarOrSetting);
        org_setting_detail = (RelativeLayout) findViewById(R.id.org_setting_detail);
        org_setting_member = (RelativeLayout) findViewById(R.id.org_setting_member);
        org_setting_classify = (RelativeLayout) findViewById(R.id.org_setting_classify);
        org_setting_jianjie = (RelativeLayout) findViewById(R.id.org_setting_jianjie);

        name = (TextView) findViewById(R.id.org_setting_name);
        code = (TextView) findViewById(R.id.org_setting_code);
        memberCount = (TextView) findViewById(R.id.org_setting_orpeoplecount);
        exit = (Button) findViewById(R.id.org_setting_exit);

        topBar.setTopBarOnclickListener(new TopBar.topBarOnclickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });
        exit.setOnClickListener(this);
        org_setting_detail.setOnClickListener(this);
        org_setting_member.setOnClickListener(this);
        org_setting_classify.setOnClickListener(this);
        org_setting_jianjie.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.org_setting_detail:
                intent.setClass(this,OrgSettingNameView.class);
                intent.putExtra("id",objectId);
                startActivity(intent);
                break;
            case R.id.org_setting_member:
                intent.setClass(this,OrgSettingMemberView.class);
                intent.putExtra("id",objectId);
                startActivity(intent);
                break;
            case R.id.org_setting_classify:
                intent.setClass(this,OrgSettingClassifyView.class);
                intent.putExtra("id",objectId);
                startActivity(intent);
                break;
            case R.id.org_setting_jianjie:
                intent.setClass(this,OrgSettingJianJieView.class);
                intent.putExtra("id",objectId);
                startActivity(intent);
                break;
            case R.id.org_setting_exit:
                final AlertDialog dialog = new AlertDialog(OrgSettingView.this).builder();
                dialog.setMsg("确定退出？").setTitle("提示")
                        .setPositiveButtonForText("确认", new AlertDialog.OnAlertListener() {
                            @Override
                            public void onClick(String str,View v) {
                                exitOr();
                            }
                        }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
                break;
        }
    }

    private void exitOr() {
       presenter.dataModel.searchOrForId(getApplicationContext(), objectId, new ISaveListener() {
           @Override
           public void onSuccess(Object or) {
               List<String> members = ((ORBean)or).getOr_member();
               members.remove(app.getUserBean().getUsername());
               ((ORBean)or).update(getApplicationContext(), objectId, new UpdateListener() {
                   @Override
                   public void onSuccess() {
                       UserBean userBean = app.getUserBean();
                       List<String> or = userBean.getOrganizeId();
                       or.remove(objectId);
                       userBean.update(getApplicationContext(), userBean.getObjectId(), new UpdateListener() {
                           @Override
                           public void onSuccess() {
                               ToastUtils.toast(getApplicationContext(),"成功退出！");
                               finish();
                           }

                           @Override
                           public void onFailure(int i, String s) {
                               ToastUtils.toast(getApplicationContext(),"系统异常！"+s);
                           }
                       });
                   }

                   @Override
                   public void onFailure(int i, String s) {
                       ToastUtils.toast(getApplicationContext(),"系统异常！"+s);
                   }
               });
           }
           @Override
           public void onError(String msg) {
               ToastUtils.toast(getApplicationContext(),"系统异常！"+msg);
           }
       });
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
