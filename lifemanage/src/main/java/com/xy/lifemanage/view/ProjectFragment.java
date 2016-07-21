package com.xy.lifemanage.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.xy.lifemanage.R;
import com.xy.lifemanage.app.App;
import com.xy.lifemanage.bean.ORBean;
import com.xy.lifemanage.model.IGetDataListener;
import com.xy.lifemanage.presenter.Presenter;
import com.xy.lifemanage.utils.ToastUtils;
import com.xy.lifemanage.view.myview.CommonAdapter;
import com.xy.lifemanage.view.myview.PopMenu;
import com.xy.lifemanage.view.myview.UserMenu;
import com.xy.lifemanage.view.myview.ViewHolder;
import com.xy.lifemanage.view.myview.XListView;
import com.xy.lifemanage.view.proview.CreateOrgView;
import com.xy.lifemanage.view.proview.OrgDetailView;
import com.xy.lifemanage.view.proview.SearchOrgView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nemo on 2016/5/10 0010.
 */
public class ProjectFragment extends Fragment implements XListView.IXListViewListener,IUserView{
    private View rootView;
    private XListView xListView;
    private CommonAdapter<ORBean> mAdapter;
    private Handler mHandler;
    private List<ORBean> items;
    private Presenter presenter;
    private UserMenu mMenu;
    private TopBar topBar;
    private App app;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.project_main,container,false);
        presenter = new Presenter(getContext(),this);
        app = (App) getActivity().getApplication();
        initData();
        initMenu();
        topBar = (TopBar) rootView.findViewById(R.id.topBarPro);
        topBar.setTopBarOnclickListener(new TopBar.topBarOnclickListener() {
            @Override
            public void leftClick() {

            }

            @Override
            public void rightClick() {
                mMenu.showAsDropDown(topBar.rightButton);
            }
        });
        return rootView;
    }


    private void initMenu() {
        mMenu = new UserMenu(getContext());
        mMenu.addItem("加入小组", 0);
        mMenu.addItem("创建小组", 1);
        mMenu.setOnItemSelectedListener(new PopMenu.OnItemSelectedListener() {
            @Override
            public void selected(View view, PopMenu.Item item, int position) {
                Intent intent = new Intent();
                switch (item.id) {
                    case 0:
                        intent.setClass(getContext(), SearchOrgView.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent.setClass(getContext(), CreateOrgView.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private void initData() {
        xListView = (XListView) rootView.findViewById(R.id.or_list);
        xListView.setPullLoadEnable(true);
        xListView.setPullLoadEnable(false);//设置是否可以上拉加载
        xListView.setPullRefreshEnable(true);//设置是否可以下拉刷新
        xListView.setXListViewListener(this);
        if(items==null)
            items = new ArrayList<ORBean>();
        presenter.getORData(app.getUserBean(),new IGetDataListener() {
            @Override
            public void onSuccess(List var1) {
                items = var1;
                initView();
            }
            @Override
            public void onError(String var2) {
                ToastUtils.toast(getContext(),var2);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    private void update(){
        if(items==null)
            items = new ArrayList<ORBean>();
        presenter.getORData(app.getUserBean(),new IGetDataListener() {
            @Override
            public void onSuccess(List var1) {
                items = var1;
                if(mAdapter!=null){
                    mAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onError(String var2) {
                ToastUtils.toast(getContext(),var2);
            }
        });
    }

    private void initView() {
        mAdapter = new CommonAdapter<ORBean>(getContext(), items,
                R.layout.project_item) {
            @Override
            public void convert(final ViewHolder holder, ORBean t) {
                holder.setText(R.id.or_name, t.getOr_name());
                String url = t.getOr_image().getUrl();
                Picasso.with(getContext()).load(url).into((ImageView) holder.getView(R.id.or_image));
            }
        };
        xListView.setAdapter(mAdapter);
        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), OrgDetailView.class);
                intent.putExtra("id",items.get(i-1).getObjectId());
                startActivity(intent);
            }
        });

        mHandler = new Handler();
    }

    @Override
    public void onRefresh() {
        initData();
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
