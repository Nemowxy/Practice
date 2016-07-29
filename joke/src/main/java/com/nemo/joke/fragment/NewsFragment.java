package com.nemo.joke.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.nemo.joke.R;
import com.nemo.joke.bean.TitleBean;
import com.nemo.joke.view.CommonAdapter;
import com.nemo.joke.view.ViewHolder;
import com.nemo.joke.view.XListView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by nemo on 2016/7/24 0024.
 */
public class NewsFragment extends Fragment implements XListView.IXListViewListener {
    private String weburl;
    private String channelName;
    private String channelId;
    private XListView xListView;
    private CommonAdapter<TitleBean> adapter;
    private List<TitleBean> items;
    private static String page="1";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    items = (List<TitleBean>) msg.obj;
                    initAdapter();
                    onLoad();
                    break;
                case 101:
                    items.addAll((List<TitleBean>) msg.obj);
                    adapter.notifyDataSetChanged();
                    onLoad();
                    break;
            }
        }
    };


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {//优化View减少View的创建次数
            view = inflater.inflate(R.layout.fragment_main, container, false);
            initListView();
        }
        return view;
    }

    private void initListView() {
        xListView = (XListView) view.findViewById(R.id.list);
        xListView.setPullLoadEnable(true);
        xListView.setPullLoadEnable(true);//设置是否可以上拉加载
        xListView.setPullRefreshEnable(true);//设置是否可以下拉刷新
        xListView.setXListViewListener(this);
    }

    private void initAdapter() {
        adapter = new CommonAdapter<TitleBean>(getContext(), items, R.layout.list_item) {
            @Override
            public void convert(ViewHolder holder, final TitleBean t) {
                if (!t.getTitle().equals(""))
                    holder.setText(R.id.title, t.getTitle());
                else
                    holder.setText(R.id.title,t.getDesc());
                holder.setText(R.id.date, t.getPubDate());
                final ImageView iv = holder.getView(R.id.titleImg);
                if (t.getImageurls() != null && t.getImageurls().size() > 0) {
                    String s = t.getImageurls().get(0).replace("\\", "");
                    iv.setVisibility(View.VISIBLE);
                    Picasso.with(getContext()).load(s).resize(500, 400).centerCrop().into(iv);
                } else {
                    iv.setVisibility(View.GONE);
                }
                LinearLayout ll = holder.getView(R.id.item_layout);
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), t.getTitle(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        xListView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        page = String.valueOf(Integer.parseInt(page)+1);
        System.out.println("page:"+page);
    }

    private void onLoad() {
        xListView.stopRefresh();
        xListView.stopLoadMore();
        xListView.setRefreshTime("刚刚");
    }

    @Override
    public void setArguments(Bundle bundle) {//接收传入的数据
        channelName = bundle.getString("name");
        channelId = bundle.getString("channelId");
    }
}