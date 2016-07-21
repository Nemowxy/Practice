package com.xy.lifemanage.view.my;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xy.lifemanage.R;
import com.xy.lifemanage.app.App;
import com.xy.lifemanage.bean.UserBean;
import com.xy.lifemanage.utils.ToastUtils;
import com.xy.lifemanage.utils.Utils;
import com.xy.lifemanage.view.TopBar;
import com.xy.lifemanage.view.myview.MyViewGroup;
import com.xy.lifemanage.view.widget.AlertDialog;

import java.util.HashMap;
import java.util.Map;

import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by nemo on 2016/5/12 0012.
 */
public class MyLableView extends Activity{

    private TopBar topBar;
    private MyViewGroup viewGroup;
    private LinearLayout addLinearLayout;
    private App app;
    private Map<String,Integer> lables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_lable_main);
        initView();
        initLables();
    }

    private void addToAppLable(String str){
        lables.put(str,0);
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.topBarMyLable);
        topBar.setTopBarOnclickListener(new TopBar.topBarOnclickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {
                upload();
            }
        });
        viewGroup = (MyViewGroup) findViewById(R.id.my_lable_group);
        addLinearLayout = (LinearLayout) findViewById(R.id.my_lable_addlable);
        addLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialog = new AlertDialog(MyLableView.this).builder();
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

    private void upload() {
        final ProgressDialog progressDialog = new ProgressDialog(MyLableView.this);
        progressDialog.setTitle("提示");
        progressDialog.setMessage("更新中...");
        progressDialog.show();
        final UserBean userBean = new UserBean();
        userBean.setLables(lables);
        userBean.update(getApplicationContext(), app.getUserBean().getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                app.setUserBean(userBean);
                ToastUtils.toast(getApplicationContext(),"更新成功！");
                finish();
            }
            @Override
            public void onFailure(int i, String s) {
                progressDialog.dismiss();
                ToastUtils.toast(getApplicationContext(),"更新失败！"+s);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void initLables(){
        app = (App) getApplication();
        if(app.getUserBean().getLables()==null){
            lables = new HashMap<>();
        }else{
            lables = app.getUserBean().getLables();
        }
        for (String key:lables.keySet()){
            addView(key);
        }
    }

    private void delete(View v) {
        viewGroup.removeView(v);
        lables.remove(((TextView)v).getText().toString());
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void addView(String str) {
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = Utils.dip2px(MyLableView.this,10);
        lp.rightMargin = Utils.dip2px(MyLableView.this,10);
        lp.topMargin = Utils.dip2px(MyLableView.this,10);
        lp.bottomMargin = Utils.dip2px(MyLableView.this,10);
        TextView view = new TextView(this);
        view.setText(str);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        view.setTextColor(Color.WHITE);
        view.setBackground(getDrawable(R.drawable.button_shape_blue));
        view.setPadding(Utils.dip2px(MyLableView.this,8),Utils.dip2px(MyLableView.this,8),Utils.dip2px(MyLableView.this,8),Utils.dip2px(MyLableView.this,8));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final AlertDialog dialog = new AlertDialog(MyLableView.this).builder();
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
