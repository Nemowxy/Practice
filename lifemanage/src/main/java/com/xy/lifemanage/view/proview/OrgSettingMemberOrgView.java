package com.xy.lifemanage.view.proview;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.xy.lifemanage.R;
import com.xy.lifemanage.app.App;
import com.xy.lifemanage.bean.ORBean;
import com.xy.lifemanage.model.IGetDataListener;
import com.xy.lifemanage.presenter.Presenter;
import com.xy.lifemanage.utils.ToastUtils;
import com.xy.lifemanage.view.TopBar;
import com.xy.lifemanage.view.itemview.ProItemView;
import com.xy.lifemanage.view.myview.CommonAdapter;
import com.xy.lifemanage.view.myview.ViewHolder;
import com.xy.lifemanage.view.myview.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nemo on 2016/5/16 0016.
 */
public class OrgSettingMemberOrgView extends AppCompatActivity implements XListView.IXListViewListener{
    private XListView xListView;
    private CommonAdapter<ORBean> mAdapter;
    private Handler mHandler;
    private List<ORBean> items;
    private Presenter presenter;
    private TopBar topBar;
    private App app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.org_setting_member_org);
        app = (App) getApplication();
        presenter = new Presenter(getApplicationContext());
        initData();
        initView();
    }

    private void initData() {
        items = new ArrayList<ORBean>();
        presenter.getORData(app.getUserBean(),new IGetDataListener() {
            @Override
            public void onSuccess(List var1) {
                items = var1;
                initView();
            }
            @Override
            public void onError(String var2) {
                ToastUtils.toast(OrgSettingMemberOrgView.this,var2);
            }
        });
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.topBarOrSettingMemberOr);
        topBar.setTopBarOnclickListener(new TopBar.topBarOnclickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {
            }
        });
        xListView = (XListView) findViewById(R.id.org_setting_member_detail_org_list);
        xListView.setPullLoadEnable(true);
        mAdapter = new CommonAdapter<ORBean>(OrgSettingMemberOrgView.this, items,
                R.layout.project_item) {
            @Override
            public void convert(final ViewHolder holder, ORBean t) {
                holder.setText(R.id.org_setting_member_name, t.getOr_name());
                String url = t.getOr_image().getUrl();
                System.out.println(url);
                Picasso.with(OrgSettingMemberOrgView.this).load(url).into((ImageView) holder.getView(R.id.org_setting_member_image));
            }
        };
        xListView.setAdapter(mAdapter);
        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(OrgSettingMemberOrgView.this, ProItemView.class);
                intent.putExtra("name",items.get(i).getOr_name());
                startActivity(intent);
            }
        });
        xListView.setPullLoadEnable(false);//设置是否可以上拉加载
        xListView.setPullRefreshEnable(false);//设置是否可以下拉刷新
        xListView.setXListViewListener(this);
        mHandler = new Handler();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

}
