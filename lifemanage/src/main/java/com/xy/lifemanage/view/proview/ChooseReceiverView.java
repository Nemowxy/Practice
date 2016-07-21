package com.xy.lifemanage.view.proview;

/**
 * Created by nemo on 2016/5/22 0022.
 */

import android.content.Intent;
import android.graphics.Bitmap;
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
import com.xy.lifemanage.bean.UserBean;
import com.xy.lifemanage.model.IGetDataListener;
import com.xy.lifemanage.presenter.Presenter;
import com.xy.lifemanage.utils.ToastUtils;
import com.xy.lifemanage.view.IUserView;
import com.xy.lifemanage.view.TopBar;
import com.xy.lifemanage.view.myview.CircleImageView;
import com.xy.lifemanage.view.myview.CommonAdapter;
import com.xy.lifemanage.view.myview.ViewHolder;
import com.xy.lifemanage.view.myview.XListView;
import com.xy.lifemanage.view.ring.RingShareOrgView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by nemo on 2016/5/15 0015.
 */
public class ChooseReceiverView extends AppCompatActivity implements XListView.IXListViewListener,IUserView {

    private XListView xListView;
    private CommonAdapter<UserBean> mAdapter;
    private Handler mHandler;
    private List<UserBean> items;
    private Presenter presenter;
    private TopBar topBar;
    private String objectId;
    private App app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_receiver);
        presenter = new Presenter(getApplication(), this);
        app = (App) getApplication();
        Intent intent = getIntent();
        objectId = intent.getStringExtra("id");
        initData();
        initView();
    }

    private void initData() {
        topBar = (TopBar) findViewById(R.id.topBarChooseReceiver);
        topBar.setTopBarOnclickListener(new TopBar.topBarOnclickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {
            }
        });
        items = new ArrayList<UserBean>();
        presenter.dataModel.getMemberForOrId(getApplicationContext(), objectId, new IGetDataListener<UserBean>() {
            @Override
            public void onSuccess(List<UserBean> var1) {
                items = var1;
                initView();
            }

            @Override
            public void onError(String var2) {
                ToastUtils.toast(getApplicationContext(), var2);
            }
        });
    }

    private void initView() {

        xListView = (XListView) findViewById(R.id.choose_receiver_list);
        xListView.setPullLoadEnable(true);
        mAdapter = new CommonAdapter<UserBean>(ChooseReceiverView.this, items,
                R.layout.org_setting_member_item) {
            @Override
            public void convert(final ViewHolder holder, UserBean t) {
                holder.setText(R.id.org_setting_member_name, t.getUsername());
                String url = t.getImg().getUrl();
                Picasso.with(ChooseReceiverView.this).load(url).into((CircleImageView) holder.getView(R.id.org_setting_member_image));
            }
        };
        xListView.setAdapter(mAdapter);
        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), MemberView.class);
                intent.putExtra("username", items.get(i-1).getUsername());
                setResult(RESULT_OK,intent);
                finish();
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

    @Override
    public String getEditText(int id) {
        return null;
    }

    @Override
    public Bitmap getImageView(int id) {
        return null;
    }

    @Override
    public void setEditText(int id, String str) {

    }

    @Override
    public void setImageView(int id, Bitmap bitmap) {

    }

    @Override
    public void setListView(int id, List items) {

    }
}