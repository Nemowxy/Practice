package com.xy.lifemanage.view.ring;

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
import com.xy.lifemanage.model.IGetDataListener;
import com.xy.lifemanage.presenter.Presenter;
import com.xy.lifemanage.utils.ToastUtils;
import com.xy.lifemanage.view.IUserView;
import com.xy.lifemanage.view.TopBar;
import com.xy.lifemanage.view.itemview.ProItemView;
import com.xy.lifemanage.view.myview.CommonAdapter;
import com.xy.lifemanage.view.myview.UserMenu;
import com.xy.lifemanage.view.myview.ViewHolder;
import com.xy.lifemanage.view.myview.XListView;
import com.xy.lifemanage.view.proview.CreateTaskView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nemo on 2016/5/15 0015.
 */
public class RingChooseView extends AppCompatActivity implements XListView.IXListViewListener,IUserView{

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
        setContentView(R.layout.ring_choose_main);
        presenter = new Presenter(getApplicationContext(),this);
        app = (App) getApplication();
        topBar = (TopBar) findViewById(R.id.topBarRingChoose);
        topBar.setTopBarOnclickListener(new TopBar.topBarOnclickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {
            }
        });
        initData();
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
                ToastUtils.toast(RingChooseView.this,var2);
            }
        });
    }

    private void initView() {

        xListView = (XListView) findViewById(R.id.ring_choose_list);
        xListView.setPullLoadEnable(true);
        mAdapter = new CommonAdapter<ORBean>(RingChooseView.this, items,
                R.layout.project_item) {
            @Override
            public void convert(final ViewHolder holder, ORBean t) {
                holder.setText(R.id.or_name, t.getOr_name());
                String url = t.getOr_image().getUrl();
                Picasso.with(RingChooseView.this).load(url).into((ImageView) holder.getView(R.id.or_image));
            }
        };
        xListView.setAdapter(mAdapter);
        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplication(),RingShareOrgView.class);
                intent.putExtra("name",items.get(i-1).getOr_name()+","+items.get(i-1).getObjectId());
                System.out.println(items.get(i-1).getOr_name()+","+items.get(i-1).getObjectId());
                setResult(RingShareOrgView.RESULT_OK,intent);

//                Intent intent1 = new Intent(getApplication(),CreateTaskView.class);
//                intent1.putExtra("name",items.get(i-1).getOr_name());
//                setResult(CreateTaskView.RESULT_OK,intent1);
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
