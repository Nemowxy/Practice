package com.nemo.samplenews.fragment;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.nemo.samplenews.R;
import com.nemo.samplenews.bean.Channel;
import com.nemo.samplenews.http.GetChannelList;
import com.nemo.samplenews.utils.Utils;
import com.nemo.samplenews.view.MyViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nemo on 2016/7/25 0025.
 */
public class LikeFragment extends Fragment{

    private View rootView;

    private GridView groupLike;
    private GridView groupNoLike;
    private List<Channel> channelList;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 100:
                    channelList = (List<Channel>) msg.obj;
                    initData();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(rootView == null){
            rootView = inflater.inflate(R.layout.like_fragment,container,false);
            initView();
            new GetChannelList(handler).start();
        }

        return rootView;
    }

    private void initView() {
        groupLike = (GridView) rootView.findViewById(R.id.like_grid);
        groupNoLike = (GridView) rootView.findViewById(R.id.nolike_grid);
    }

    private void initData(){
        List<Map<String,String>> data = new ArrayList<>();
        for (int i=0;i<channelList.size();i++){
            if(i<20) {
                Map<String, String> map = new HashMap<>();
                map.put("text", channelList.get(i).getName());
                data.add(map);
            }else{
                Map<String, String> map = new HashMap<>();
                map.put("text", "......");
                data.add(map);
                break;
            }
        }
        SimpleAdapter adapter = new SimpleAdapter(getContext(),data,R.layout.grid_item,new String[]{"text"},new int[]{R.id.grid_text});
        groupLike.setAdapter(adapter);
        groupNoLike.setAdapter(adapter);
    }

    private void initListenr(){
        groupLike.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        groupNoLike.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }
}
