package com.xy.lifemanage.view.my;

/**
 * Created by nemo on 2016/5/14 0014.
 */
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.xy.lifemanage.R;
import com.xy.lifemanage.view.TopBar;

/**
 * Created by Bob on 2015/4/16.
 */
public class ConversationActivity extends AppCompatActivity {

    private static final String TAG = ConversationActivity.class.getSimpleName();

    private TopBar topBar;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //唯一有用的代码，加载一个 layout
        setContentView(R.layout.conversation);
        //继承的是ActionBarActivity，直接调用 自带的 Actionbar，下面是Actionbar 的配置，如果不用可忽略…
        initView();
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.topBarMyMessageConversation);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
