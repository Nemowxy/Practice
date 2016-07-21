package com.xy.lifemanage.view.proview;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xy.lifemanage.R;
import com.xy.lifemanage.app.App;
import com.xy.lifemanage.bean.ORBean;
import com.xy.lifemanage.model.ISaveListener;
import com.xy.lifemanage.presenter.Presenter;
import com.xy.lifemanage.utils.ToastUtils;
import com.xy.lifemanage.view.TopBar;
import com.xy.lifemanage.view.myview.MyViewGroup;

import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by nemo on 2016/5/16 0016.
 */
public class OrgSettingJianJieView extends AppCompatActivity{

    private TopBar topBar;
    private App app;
    private EditText detail;
    private TextView name;
    private String objectId;
    private Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.org_setting_jianjie_main);
        app = (App) getApplication();
        presenter = new Presenter(getApplicationContext());
        objectId = getIntent().getStringExtra("id");
        initView();
        initData();
    }

    private void initView() {
        name = (TextView) findViewById(R.id.org_setting_jianjie_name);
        detail = (EditText) findViewById(R.id.org_setting_jianjie_detail);
        topBar = (TopBar) findViewById(R.id.topBarORSettingJianjie);
        topBar.setTopBarOnclickListener(new TopBar.topBarOnclickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {
                updata();
            }
        });
    }

    private void initData(){
        presenter.dataModel.searchOrForId(getApplicationContext(), objectId, new ISaveListener() {
            @Override
            public void onSuccess(Object objectId) {
                name.setText(((ORBean)objectId).getOr_name());
            }

            @Override
            public void onError(String msg) {
                ToastUtils.toast(getApplicationContext(),"修改失败！"+msg);
            }
        });
    }

    private void updata() {
        String str = detail.getText().toString();
        ORBean orBean = new ORBean();
        orBean.setDetail(str);
        final ProgressDialog progressDialog = new ProgressDialog(OrgSettingJianJieView.this);
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
}
