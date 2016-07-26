package com.nemo.samplenews;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.nemo.samplenews.fragment.LikeFragment;
import com.nemo.samplenews.fragment.MainFragment;
import com.nemo.samplenews.fragment.MyFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private FragmentPagerAdapter mAdapter = null;
    private List<Fragment> mFragments = new ArrayList<Fragment>();

    private long mExitTime;
    private RadioButton rb_main_tab_menu1 = null;
    private RadioButton rb_main_tab_menu2 = null;
    private RadioButton rb_main_tab_menu3 = null;

    private RadioGroup rg_main_menu = null;

    private Fragment mainFragment;
    private Fragment likeFragment;
    private Fragment myFragment;

    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initAdapter();
        initViewPager();
    }

    private void findViews() {
        rb_main_tab_menu1 = (RadioButton) findViewById(R.id.rb_main_tab_menu1);
        rb_main_tab_menu2 = (RadioButton) findViewById(R.id.rb_main_tab_menu2);
        rb_main_tab_menu3 = (RadioButton) findViewById(R.id.rb_main_tab_menu3);
        rg_main_menu = (RadioGroup) findViewById(R.id.rg_main_menu);
        mainFragment = new MainFragment();
        likeFragment = new LikeFragment();
        myFragment = new MyFragment();

        mFragments.add(mainFragment);
        mFragments.add(likeFragment);
        mFragments.add(myFragment);

        // 默认显示第一页
        ft.add(R.id.main_activity, mFragments.get(0));
        ft.commit();
    }


    private void initAdapter() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        mAdapter = new FragmentPagerAdapter(fm) {

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public android.support.v4.app.Fragment getItem(int arg0) {
                return mFragments.get(arg0);
            }

            // 初始化每个页卡选项
            @Override
            public Object instantiateItem(ViewGroup arg0, int arg1) {
                return super.instantiateItem(arg0, arg1);
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                super.destroyItem(container, position, object);
            }
        };
    }

    private void initViewPager() {
        rg_main_menu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup rg, int id) {
                switch (id) {
                    case R.id.rb_main_tab_menu1:
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.main_activity,mFragments.get(0));
                        ft.commit();
                        break;
                    case R.id.rb_main_tab_menu2:
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.main_activity,mFragments.get(1));
                        ft.commit();
                        break;
                    case R.id.rb_main_tab_menu3:
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.main_activity,mFragments.get(2));
                        ft.commit();
                        break;
                    default:
                        break;
                }

            }
        });

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
