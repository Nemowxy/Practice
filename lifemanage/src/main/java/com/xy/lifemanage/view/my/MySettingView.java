package com.xy.lifemanage.view.my;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xy.lifemanage.MainActivity;
import com.xy.lifemanage.R;
import com.xy.lifemanage.bean.UserBean;
import com.xy.lifemanage.utils.ToastUtils;
import com.xy.lifemanage.view.TopBar;
import com.xy.lifemanage.view.widget.AlertDialog;

/**
 * Created by nemo on 2016/5/12 0012.
 */
public class MySettingView extends Activity implements View.OnClickListener {

    private LinearLayout ll_jieshao;
    private LinearLayout ll_about;
    private TopBar topBar;
    private Button exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_setting_main);
        initView();
    }

    private void initView() {
        ll_about = (LinearLayout) findViewById(R.id.my_set_about);
        ll_jieshao = (LinearLayout) findViewById(R.id.my_set_jieshao);
        ll_jieshao.setOnClickListener(this);
        ll_about.setOnClickListener(this);
        topBar = (TopBar) findViewById(R.id.topBarMySet);
        exitButton = (Button) findViewById(R.id.my_set_exit);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialog = new AlertDialog(MySettingView.this).builder();
                dialog.setTitle("退出当前账号")
                        .setMsg("确定退出？")
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                UserBean.logOut(getApplication());
                                Intent intent = new Intent(MySettingView.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();

            }
        });
        topBar.setTopBarOnclickListener(new TopBar.topBarOnclickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.my_set_about:
                ToastUtils.toast(getApplicationContext(),"xy");
                break;
            case R.id.my_set_jieshao:
                intent.setClass(MySettingView.this, MySettingJieShaoView.class);
                startActivity(intent);
                break;
        }
    }
}
