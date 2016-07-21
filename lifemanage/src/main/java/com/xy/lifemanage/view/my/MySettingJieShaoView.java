package com.xy.lifemanage.view.my;

import android.app.Activity;
import android.os.Bundle;

import com.xy.lifemanage.R;
import com.xy.lifemanage.view.TopBar;

/**
 * Created by nemo on 2016/5/12 0012.
 */
public class MySettingJieShaoView extends Activity{

    private TopBar topBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_set_jieshao);
        initView();
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.topBarMySetJieShao);
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
}
