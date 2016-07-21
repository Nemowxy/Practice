package com.xy.lifemanage.view.my;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by nemo on 2016/5/12 0012.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xy.lifemanage.R;
import com.xy.lifemanage.app.App;
import com.xy.lifemanage.bean.ORBean;
import com.xy.lifemanage.bean.TaskBean;
import com.xy.lifemanage.model.IGetDataListener;
import com.xy.lifemanage.presenter.Presenter;
import com.xy.lifemanage.utils.ToastUtils;
import com.xy.lifemanage.view.TopBar;
import com.xy.lifemanage.view.myview.CircleImageView;

public class MyTaskView extends Activity implements
        OnScrollListener {
    // 存放父列表数据
    List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
    // 放子列表列表数据
    List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
    List<String> imgs = new ArrayList<>();
    ExpandableAdapter expandAdapter;
    ExpandableListView expandableList;
    private int indicatorGroupHeight;
    private int the_group_expand_position = -1;
    private int count_expand = 0;
    private Map<Integer, Integer> ids = new HashMap<Integer, Integer>();
    private LinearLayout view_flotage = null;
    private TextView group_content = null;
    private ImageView tubiao;
    private List<ORBean> items;
    private Presenter presenter;
    private App app;
    private TopBar topBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_task_main);
        presenter = new Presenter(getApplicationContext());
        app = (App) getApplication();
        initData();
        // mInflater = (LayoutInflater)
        // getSystemService(LAYOUT_INFLATER_SERVICE);
        expandAdapter = new ExpandableAdapter(MyTaskView.this);
        expandableList = (ExpandableListView) findViewById(R.id.my_task_list);
        View v = new View(this);
        expandableList.addHeaderView(v);
        // indicatorGroup = (LinearLayout) findViewById(R.id.topGroup);
        expandableList.setAdapter(expandAdapter);
        expandableList.setGroupIndicator(null);
        initView();
    }

    private void initData() {
        if (items == null)
            items = new ArrayList<ORBean>();
        presenter.getORData(app.getUserBean(), new IGetDataListener() {
            @Override
            public void onSuccess(final List var1) {
                items = var1;
                presenter.dataModel.getAllTask(getApplication(), new IGetDataListener() {
                    @Override
                    public void onSuccess(List tasks) {
                        for (int i = 0; i < var1.size(); i++) {
                            ORBean orBean = (ORBean) var1.get(i);
                            imgs.add(orBean.getOr_image().getUrl());
                            Map<String, String> curGroupMap = new HashMap<String, String>();
                            groupData.add(curGroupMap);
                            curGroupMap.put("group_text", orBean.getOr_name());
                            List<Map<String, String>> children = new ArrayList<Map<String, String>>();
                            for (int j = 0; j < tasks.size(); j++) {
                                TaskBean taskBean = (TaskBean) tasks.get(j);
                                Map<String, String> curChildMap = new HashMap<String, String>();
                                if (taskBean.getTitle().equals(orBean.getOr_name()) &&
                                        taskBean.getReceiverName().equals(app.getUserBean().getUsername())
                                        ) {
                                    children.add(curChildMap);
                                    curChildMap.put("child_text1", taskBean.getTitle());
                                    curChildMap.put("child_text2", taskBean.getDetail());
                                }
                            }
                            childData.add(children);

                        }
                        System.out.println(groupData);
                        System.out.println(childData);
                        expandAdapter = new ExpandableAdapter(MyTaskView.this);
                        expandableList = (ExpandableListView) findViewById(R.id.my_task_list);
                        View v = new View(MyTaskView.this);
                        expandableList.addHeaderView(v);
                        // indicatorGroup = (LinearLayout) findViewById(R.id.topGroup);
                        expandableList.setAdapter(expandAdapter);
                        expandableList.setGroupIndicator(null);
                        initView();
                    }

                    @Override
                    public void onError(String var2) {

                    }
                });
            }

            @Override
            public void onError(String var2) {
                ToastUtils.toast(getApplicationContext(), var2);
            }
        });

    }

    public void initView() {
        topBar = (TopBar) findViewById(R.id.topBarMyTask);
        topBar.setTopBarOnclickListener(new TopBar.topBarOnclickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });
        /**
         * 监听父节点打开的事件
         */
        expandableList.setOnGroupExpandListener(new OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                the_group_expand_position = groupPosition;
                ids.put(groupPosition, groupPosition);
                count_expand = ids.size();
            }
        });
        /**
         * 监听父节点关闭的事件
         */
        expandableList
                .setOnGroupCollapseListener(new OnGroupCollapseListener() {
                    @Override
                    public void onGroupCollapse(int groupPosition) {
                        ids.remove(groupPosition);
                        expandableList.setSelectedGroup(groupPosition);
                        count_expand = ids.size();
                    }
                });
        view_flotage = (LinearLayout) findViewById(R.id.topGroup);
        view_flotage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_flotage.setVisibility(View.GONE);
                expandableList.collapseGroup(the_group_expand_position);
                expandableList.setSelectedGroup(the_group_expand_position);
            }
        });
        group_content = (TextView) findViewById(R.id.content_001);
        tubiao = (ImageView) findViewById(R.id.tubiao);
        tubiao.setBackgroundResource(R.mipmap.btn_browser2);
        //设置滚动事件
        expandableList.setOnScrollListener(this);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        //防止三星,魅族等手机第一个条目可以一直往下拉,父条目和悬浮同时出现的问题
        if (firstVisibleItem == 0) {
            view_flotage.setVisibility(View.GONE);
        }
        // 控制滑动时TextView的显示与隐藏
        int npos = view.pointToPosition(0, 0);
        if (npos != AdapterView.INVALID_POSITION) {
            long pos = expandableList.getExpandableListPosition(npos);
            int childPos = ExpandableListView.getPackedPositionChild(pos);
            final int groupPos = ExpandableListView.getPackedPositionGroup(pos);
            if (childPos == AdapterView.INVALID_POSITION) {
                View groupView = expandableList.getChildAt(npos
                        - expandableList.getFirstVisiblePosition());
                indicatorGroupHeight = groupView.getHeight();
            }

            if (indicatorGroupHeight == 0) {
                return;
            }
            // if (isExpanded) {
            if (count_expand > 0) {
                the_group_expand_position = groupPos;
                group_content.setText(groupData.get(the_group_expand_position)
                        .get("group_text"));
                if (the_group_expand_position != groupPos || !expandableList.isGroupExpanded(groupPos)) {
                    view_flotage.setVisibility(View.GONE);
                } else {
                    view_flotage.setVisibility(View.VISIBLE);
                }
            }
            if (count_expand == 0) {
                view_flotage.setVisibility(View.GONE);
            }
        }

        if (the_group_expand_position == -1) {
            return;
        }
        /**
         * calculate point (0,indicatorGroupHeight)
         */
        int showHeight = getHeight();
        MarginLayoutParams layoutParams = (MarginLayoutParams) view_flotage
                .getLayoutParams();
        // 得到悬浮的条滑出屏幕多少
        layoutParams.topMargin = -(indicatorGroupHeight - showHeight);
        view_flotage.setLayoutParams(layoutParams);
    }

    class ExpandableAdapter extends BaseExpandableListAdapter {
        MyTaskView exlistview;
        @SuppressWarnings("unused")
        private int mHideGroupPos = -1;

        public ExpandableAdapter(MyTaskView elv) {
            super();
            exlistview = elv;
        }

        // **************************************
        public Object getChild(int groupPosition, int childPosition) {
            return childData.get(groupPosition).get(childPosition)
                    .get("child_text1").toString();
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        public int getChildrenCount(int groupPosition) {
            return childData.get(groupPosition).size();
        }

        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.childitem, null);
            }
            final TextView title = (TextView) view
                    .findViewById(R.id.my_task_list_item_title);
            title.setText(childData.get(groupPosition).get(childPosition)
                    .get("child_text1").toString());
            final TextView title2 = (TextView) view
                    .findViewById(R.id.my_task_list_item_detail);
            title2.setText(childData.get(groupPosition).get(childPosition)
                    .get("child_text2").toString());
            return view;
        }

        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflaterGroup = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflaterGroup.inflate(R.layout.groupitem, null);
            }
            TextView title = (TextView) view.findViewById(R.id.my_task_list_group_name);
            title.setText(getGroup(groupPosition).toString());
            ImageView image = (ImageView) view.findViewById(R.id.tubiao);
            CircleImageView image1 = (CircleImageView) view.findViewById(R.id.my_task_list_group_image);
            Picasso.with(getApplicationContext()).load(imgs.get(groupPosition)).into(image1);
            if (isExpanded) {
                image.setBackgroundResource(R.mipmap.down);
            } else {
                image.setBackgroundResource(R.mipmap.right);
            }
            return view;
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        public Object getGroup(int groupPosition) {
            return groupData.get(groupPosition).get("group_text").toString();
        }

        public int getGroupCount() {
            return groupData.size();

        }

        public boolean hasStableIds() {
            return true;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

        public void hideGroup(int groupPos) {
            mHideGroupPos = groupPos;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    private int getHeight() {
        int showHeight = indicatorGroupHeight;
        int nEndPos = expandableList.pointToPosition(0, indicatorGroupHeight);
        if (nEndPos != AdapterView.INVALID_POSITION) {
            long pos = expandableList.getExpandableListPosition(nEndPos);
            int groupPos = ExpandableListView.getPackedPositionGroup(pos);
            if (groupPos != the_group_expand_position) {
                View viewNext = expandableList.getChildAt(nEndPos
                        - expandableList.getFirstVisiblePosition());
                showHeight = viewNext.getTop();
            }
        }
        return showHeight;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }
}

