package com.xy.lifemanage.view.itemview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.xy.lifemanage.R;
import com.xy.lifemanage.app.App;
import com.xy.lifemanage.bean.ORBean;
import com.xy.lifemanage.bean.UserBean;
import com.xy.lifemanage.model.IGetDataListener;
import com.xy.lifemanage.presenter.Presenter;
import com.xy.lifemanage.utils.ToastUtils;
import com.xy.lifemanage.view.TopBar;
import com.xy.lifemanage.view.myview.CommonAdapter;
import com.xy.lifemanage.view.myview.ViewHolder;
import com.xy.lifemanage.view.myview.XListView;
import com.xy.lifemanage.view.proview.OrgDetailView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by nemo on 2016/5/23 0023.
 */
public class MemberOrView extends AppCompatActivity implements XListView.IXListViewListener{
    private XListView xListView;
    private CommonAdapter<ORBean> mAdapter;
    private List<ORBean> items;
    private Presenter presenter;
    private TopBar topBar;
    private App app;
    private String username;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_or_main);
        presenter = new Presenter(getApplicationContext());
        app = (App) getApplication();
        username = getIntent().getStringExtra("username");
        initData();
        topBar = (TopBar) findViewById(R.id.topBarMemberOr);
        topBar.setTopBarOnclickListener(new TopBar.topBarOnclickListener() {
            @Override
            public void leftClick() {

            }

            @Override
            public void rightClick() {
            }
        });
    }


    private void initData() {
        xListView = (XListView) findViewById(R.id.member_or_list);
        xListView.setPullLoadEnable(true);
        xListView.setPullLoadEnable(false);//设置是否可以上拉加载
        xListView.setPullRefreshEnable(true);//设置是否可以下拉刷新
        xListView.setXListViewListener(this);
        if(items==null)
            items = new ArrayList<>();
        BmobQuery<UserBean> query = new BmobQuery<>();
        query.addWhereEqualTo("username",username);
        query.findObjects(getApplicationContext(), new FindListener<UserBean>() {
            @Override
            public void onSuccess(List<UserBean> list) {
                presenter.dataModel.getUserOrganizeData(getApplicationContext(), list.get(0), new IGetDataListener() {
                    @Override
                    public void onSuccess(List var1) {
                        items = var1;
                        initView();
                    }

                    @Override
                    public void onError(String var2) {
                        ToastUtils.toast(getApplicationContext(),"系统异常!"+var2);
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                ToastUtils.toast(getApplicationContext(),"系统异常!"+s);
            }
        });
    }

    private void initView() {
        mAdapter = new CommonAdapter<ORBean>(getApplicationContext(), items,
                R.layout.project_item) {
            @Override
            public void convert(final ViewHolder holder, ORBean t) {
                holder.setText(R.id.or_name, t.getOr_name());
                String url = t.getOr_image().getUrl();
                Picasso.with(getApplicationContext()).load(url).into((ImageView) holder.getView(R.id.or_image));
            }
        };
        xListView.setAdapter(mAdapter);
        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ProItemView.class);
                intent.putExtra("id",items.get(i-1).getObjectId());
                startActivity(intent);
            }
        });
    }

    private void update(){
        if(items==null)
            items = new ArrayList<>();
        else
            items.clear();
        BmobQuery<UserBean> query = new BmobQuery<>();
        query.addWhereEqualTo("username",username);
        query.findObjects(getApplicationContext(), new FindListener<UserBean>() {
            @Override
            public void onSuccess(List<UserBean> list) {
                presenter.dataModel.getUserOrganizeData(getApplicationContext(), list.get(0), new IGetDataListener() {
                    @Override
                    public void onSuccess(List var1) {
                        items = var1;
                        initView();
                    }

                    @Override
                    public void onError(String var2) {
                        ToastUtils.toast(getApplicationContext(),"系统异常!"+var2);
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                ToastUtils.toast(getApplicationContext(),"系统异常!"+s);
            }
        });
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        update();
        onLoad();
    }

    @Override
    public void onLoadMore() {

    }

    private void onLoad() {
        xListView.stopRefresh();
        xListView.stopLoadMore();
        xListView.setRefreshTime("刚刚");
    }

}
