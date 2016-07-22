package com.xy.lifemanage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.xy.lifemanage.presenter.Presenter;
import com.xy.lifemanage.utils.HttpUtils;
import com.xy.lifemanage.view.IUserView;
import com.xy.lifemanage.view.LoginView;
import com.xy.lifemanage.view.RegisterView;

import java.util.List;

import cn.bmob.v3.Bmob;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this, HttpUtils.APP_ID);
        initView();

        String s = "jjda";
    }

    private void initView() {
        Button loginButton = (Button) findViewById(R.id.loginButton);
        Button registerButton = (Button) findViewById(R.id.registerButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginView.class);
                startActivity(intent);
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterView.class);
                startActivity(intent);
            }
        });
    }
}
