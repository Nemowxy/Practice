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
import android.widget.LinearLayout;
import android.widget.TextView;

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

import java.util.Map;

import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by nemo on 2016/5/16 0016.
 */
public class OrgSettingClassifyView extends AppCompatActivity{

    private TopBar topBar;
    private MyViewGroup viewGroup;
    private LinearLayout addLinearLayout;
    private App app;
    private Presenter presenter;
    private String objectId;
    private Map<String,Integer> lables;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.org_setting_classify_main);
        presenter = new Presenter(getApplicationContext());
        objectId = getIntent().getStringExtra("id");
        initView();
        initLables();
    }

    private void addToAppLable(String str){
        lables.put(str,0);
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.topBarOrSettingClassify);
        topBar.setTopBarOnclickListener(new TopBar.topBarOnclickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {
                update();
            }
        });
        viewGroup = (MyViewGroup) findViewById(R.id.org_setting_classify_group);
        addLinearLayout = (LinearLayout) findViewById(R.id.org_setting_classify_addlable);
        addLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialog = new AlertDialog(OrgSettingClassifyView.this).builder();
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

    private void update() {
        ORBean orBean = new ORBean();
        orBean.setLables(lables);
        final ProgressDialog progressDialog = new ProgressDialog(OrgSettingClassifyView.this);
        progressDialog.setTitle("提示");
        progressDialog.setMessage("更新中...");
        progressDialog.show();
        orBean.update(getApplicationContext(), objectId, new UpdateListener() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                ToastUtils.toast(getApplicationContext(),"更新成功!");
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                progressDialog.dismiss();
                ToastUtils.toast(getApplicationContext(),"更新失败!"+s);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void initLables(){
        presenter.dataModel.searchOrForId(getApplicationContext(), objectId, new ISaveListener() {
            @Override
            public void onSuccess(Object objectId) {
                lables = ((ORBean)objectId).getLables();
                for (String key:lables.keySet()){
                    addView(key);
                }
            }
            @Override
            public void onError(String msg) {
                ToastUtils.toast(getApplicationContext(),"Error:"+msg);
            }
        });

    }

    private void delete(View v) {
        viewGroup.removeView(v);
        lables.remove(((TextView)v).getText().toString());
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void addView(String str) {
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = Utils.dip2px(OrgSettingClassifyView.this,10);
        lp.rightMargin = Utils.dip2px(OrgSettingClassifyView.this,10);
        lp.topMargin = Utils.dip2px(OrgSettingClassifyView.this,10);
        lp.bottomMargin = Utils.dip2px(OrgSettingClassifyView.this,10);
        TextView view = new TextView(this);
        view.setText(str);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        view.setTextColor(Color.WHITE);
        view.setBackground(getDrawable(R.drawable.button_shape_blue));
        view.setPadding(Utils.dip2px(OrgSettingClassifyView.this,8),Utils.dip2px(OrgSettingClassifyView.this,8),Utils.dip2px(OrgSettingClassifyView.this,8),Utils.dip2px(OrgSettingClassifyView.this,8));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final AlertDialog dialog = new AlertDialog(OrgSettingClassifyView.this).builder();
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
