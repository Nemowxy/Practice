package com.xy.lifemanage.view.my;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.xy.lifemanage.R;
import com.xy.lifemanage.view.TopBar;

import io.rong.imkit.RongIM;

/**
 * Created by nemo on 2016/5/15 0015.
 */
public class ConversationListActivity extends FragmentActivity{

    private TopBar topBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversationlist);
        initView();
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.topBarMyMessageList);
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
