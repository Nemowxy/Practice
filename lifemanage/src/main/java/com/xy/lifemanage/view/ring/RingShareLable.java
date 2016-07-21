package com.xy.lifemanage.view.ring;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xy.lifemanage.R;
import com.xy.lifemanage.app.App;
import com.xy.lifemanage.utils.ToastUtils;
import com.xy.lifemanage.utils.Utils;
import com.xy.lifemanage.view.TopBar;
import com.xy.lifemanage.view.myview.MyViewGroup;
import com.xy.lifemanage.view.widget.AlertDialog;

import java.util.ArrayList;

/**
 * Created by nemo on 2016/5/15 0015.
 */
public class RingShareLable extends AppCompatActivity{
    private TopBar topBar;
    private MyViewGroup viewGroup;
    private LinearLayout addLinearLayout;
    private App app;
    private ArrayList<String> lables = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ring_share_lable_main);
        initView();
    }

    private void addToAppLable(String str){
        lables.add(str);
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.topBarRingShareLable);
        topBar.setTopBarOnclickListener(new TopBar.topBarOnclickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {
                Intent intent = new Intent();
                intent.putStringArrayListExtra("lable",lables);
                setResult(RingShareLable.RESULT_OK,intent);
                finish();
            }
        });
        viewGroup = (MyViewGroup) findViewById(R.id.ring_share_lable_group);
        addLinearLayout = (LinearLayout) findViewById(R.id.ring_share_lable_addlable);
        addLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialog = new AlertDialog(RingShareLable.this).builder();
                dialog.setEdit("添加标签")
                        .setPositiveButtonForText("确认", new AlertDialog.OnAlertListener() {
                            @Override
                            public void onClick(String str,View v) {
                                addView(str);
                                addToAppLable(str);
                            }
                        }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
            }
        });
    }

    private void delete(View v) {
        viewGroup.removeView(v);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void addView(String str) {
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = Utils.dip2px(RingShareLable.this,10);
        lp.rightMargin = Utils.dip2px(RingShareLable.this,10);
        lp.topMargin = Utils.dip2px(RingShareLable.this,10);
        lp.bottomMargin = Utils.dip2px(RingShareLable.this,10);
        TextView view = new TextView(this);
        view.setText(str);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        view.setTextColor(Color.WHITE);
        view.setBackgroundResource(R.drawable.button_shape_blue);
        view.setPadding(Utils.dip2px(RingShareLable.this,8),Utils.dip2px(RingShareLable.this,8),Utils.dip2px(RingShareLable.this,8),Utils.dip2px(RingShareLable.this,8));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final AlertDialog dialog = new AlertDialog(RingShareLable.this).builder();
                dialog.setTitle("提示")
                        .setMsg("删除后别人的认可会丢失，确定要删除白色区域的标签吗？")
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                delete(view);
                            }
                        }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
            }
        });
        viewGroup.removeView(addLinearLayout);
        viewGroup.addView(view,lp);
        viewGroup.addView(addLinearLayout);
        ToastUtils.toast(getApplication(),"添加成功！");
    }
}
