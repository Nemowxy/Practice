package com.xy.lifemanage.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.xy.lifemanage.model.DialogListener;
import com.xy.lifemanage.view.LoginView;

/**
 * Created by nemo on 2016/5/9 0009.
 */
public class ToastUtils {
    public static void toast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static void progress(ProgressDialog pd, Context context, String str) {
        pd.setTitle("提示");
        pd.setMessage(str);
        pd.show();
    }

    public static void dialog(Context context, String title, String msg, final DialogListener listener){
        final com.xy.lifemanage.view.widget.AlertDialog dialog = new com.xy.lifemanage.view.widget.AlertDialog(context).builder();
        dialog.setTitle(title)
                .setMsg(msg)
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.clickOK();
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
    listener.clickCancle();
            }
        }).show();
    }
}
