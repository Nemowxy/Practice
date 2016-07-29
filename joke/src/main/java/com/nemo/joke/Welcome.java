package com.nemo.joke;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


import java.util.HashMap;


/**
 * Created by nemo on 2016/7/26 0026.
 */
public class Welcome extends AppCompatActivity implements View.OnClickListener {

    private Button tiyan;
    private Button login;
    private Button register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcom_main);
        this.register = (Button) findViewById(R.id.register);
        this.login = (Button) findViewById(R.id.login);
        this.tiyan = (Button) findViewById(R.id.tiyan);

        register.setOnClickListener(this);
        login.setOnClickListener(this);
        tiyan.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.login:
                break;
            case R.id.register:
                break;
            case R.id.tiyan:
                intent.setClass(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                break;
        }
        finish();
    }
}
