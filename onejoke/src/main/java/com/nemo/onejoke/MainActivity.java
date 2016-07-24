package com.nemo.onejoke;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nemo.onejoke.bean.Joken;
import com.nemo.onejoke.http.GetDataThread;
import com.nemo.onejoke.view.CommonAdapter;
import com.nemo.onejoke.view.ViewHolder;
import com.nemo.onejoke.view.XListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements XListView.IXListViewListener{
    
    private XListView xListView;
    private CommonAdapter<Joken> adapter;
    private ArrayList<Joken> items = new ArrayList<>();
    public static String page="1";
    private boolean flag = false;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 100:
                    items.addAll((ArrayList<Joken>) msg.obj);
                    System.out.println("item.size:"+items.size());
                    if(!flag)
                        initView();
                    else
                        adapter.notifyDataSetChanged();
                    break;
                case 101:
                    items = (ArrayList<Joken>) msg.obj;
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
    }

    private void initData() {
        xListView = (XListView) findViewById(R.id.list);
        xListView.setPullLoadEnable(true);
        xListView.setPullLoadEnable(true);//设置是否可以上拉加载
        xListView.setPullRefreshEnable(true);//设置是否可以下拉刷新
        xListView.setXListViewListener(this);
        new GetDataThread(handler,page,"1").start();
    }

    private void initView() {
        adapter = new CommonAdapter<Joken>(getApplicationContext(),items,R.layout.list_item) {
            @Override
            public void convert(ViewHolder holder, Joken joken) {
                holder.setText(R.id.title,joken.getTitle());
                holder.setText(R.id.time,joken.getTime());
                holder.setText(R.id.text,joken.getText());
            }
        };
        xListView.setAdapter(adapter);
        flag=true;
    }

    @Override
    public void onRefresh() {
        new GetDataThread(handler,"1","0").start();
        onLoad();

    }

    @Override
    public void onLoadMore() {
        page = String.valueOf(Integer.parseInt(page)+1);
        new GetDataThread(handler,page,"1").start();
        onLoad();
    }

    private void onLoad() {
        xListView.stopRefresh();
        xListView.stopLoadMore();
        xListView.setRefreshTime("刚刚");
    }
}
