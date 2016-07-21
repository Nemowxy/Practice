package com.xy.lifemanage.view.proview;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.utils.L;
import com.xy.lifemanage.R;
import com.xy.lifemanage.app.App;
import com.xy.lifemanage.bean.ORBean;
import com.xy.lifemanage.model.ISaveListener;
import com.xy.lifemanage.presenter.Presenter;
import com.xy.lifemanage.utils.ToastUtils;
import com.xy.lifemanage.utils.Utils;
import com.xy.lifemanage.view.TopBar;
import com.xy.lifemanage.view.myview.MyViewGroup;
import com.xy.lifemanage.view.widget.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by nemo on 2016/5/16 0016.
 */
public class OrgSettingNameView extends AppCompatActivity{

    private TopBar topBar;
    private MyViewGroup viewGroup;
    private LinearLayout addLinearLayout;
    private App app;
    private EditText name;
    private String objectId;
    private Presenter presenter;
    private List<String> lables;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.org_setting_detail_main);
        app = (App) getApplication();
        presenter = new Presenter(getApplicationContext());
        objectId = getIntent().getStringExtra("id");
        initView();
    }

    private void addToAppLable(String str){
        lables.add(str);
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.topBarOrSettingDetail);
        topBar.setTopBarOnclickListener(new TopBar.topBarOnclickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {
                uploadData();
            }
        });
        name = (EditText) findViewById(R.id.org_setting_detail_name);
        viewGroup = (MyViewGroup) findViewById(R.id.org_setting_detail_group);
        addLinearLayout = (LinearLayout) findViewById(R.id.org_setting_detail_add);
        addLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialog = new AlertDialog(OrgSettingNameView.this).builder();
                dialog.setEdit("添加标签")
                        .setPositiveButtonForText("确认", new AlertDialog.OnAlertListener() {
                            @Override
                            public void onClick(String str,View v) {
                                addView(str);
                                addToAppLable(str);
                            }
                        }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
            }
        });
    }

    private void uploadData() {
        ORBean orBean = new ORBean();
        orBean.setOr_name(name.getText().toString());
        final ProgressDialog progressDialog = new ProgressDialog(OrgSettingNameView.this);
        progressDialog.setTitle("提示");
        progressDialog.setMessage("更新中...");
        progressDialog.show();
        orBean.update(getApplicationContext(), objectId, new UpdateListener() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                ToastUtils.toast(getApplicationContext(),"修改成功！");
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                progressDialog.dismiss();
                ToastUtils.toast(getApplicationContext(),"修改失败！"+s);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void initLables(){
        app = (App) getApplication();
        for(int i=0;i<lables.size();i++){
            addView(lables.get(i));
        }
    }

    private void delete(View v) {
        viewGroup.removeView(v);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void addView(String str) {
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = Utils.dip2px(OrgSettingNameView.this,10);
        lp.rightMargin = Utils.dip2px(OrgSettingNameView.this,10);
        lp.topMargin = Utils.dip2px(OrgSettingNameView.this,10);
        lp.bottomMargin = Utils.dip2px(OrgSettingNameView.this,10);
        TextView view = new TextView(this);
        view.setText(str);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        view.setTextColor(Color.WHITE);
        view.setBackground(getDrawable(R.drawable.button_shape_blue));
        view.setPadding(Utils.dip2px(OrgSettingNameView.this,8),Utils.dip2px(OrgSettingNameView.this,8),Utils.dip2px(OrgSettingNameView.this,8),Utils.dip2px(OrgSettingNameView.this,8));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final AlertDialog dialog = new AlertDialog(OrgSettingNameView.this).builder();
                dialog.setTitle("提示")
                        .setMsg("删除后别人的认可会丢失，确定要删除白色区域的标签吗？")
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                delete(view);
                            }
                        }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
            }
        });
        viewGroup.removeView(addLinearLayout);
        viewGroup.addView(view,lp);
        viewGroup.addView(addLinearLayout);
    }
}
